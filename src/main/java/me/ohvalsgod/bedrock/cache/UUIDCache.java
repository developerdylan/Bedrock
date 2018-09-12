package me.ohvalsgod.bedrock.cache;

import me.ohvalsgod.bedrock.Bedrock;
import me.ohvalsgod.bedrock.util.AtomicString;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UUIDCache {

    private static Map<String, UUID> nameToUuid;
    private static Map<UUID, String> uuidToName;

    private Bedrock nucleus;

    public UUIDCache(Bedrock bedrock) {
        this.nucleus = bedrock;
        nameToUuid = new HashMap<>();
        uuidToName = new HashMap<>();
    }
    public String getName(UUID uuid) {
        if (uuidToName.containsKey(uuid)) {
            return uuidToName.get(uuid);
        }

        AtomicString atomic = new AtomicString();

        nucleus.getJedis().runCommand(redis -> {
            atomic.setString(redis.hget("uuid-to-name", String.valueOf(uuid)));
            return null;
        });

        if (atomic.getString() == null) {
            return null;
        } else {
            return atomic.getString();
        }
    }

    public UUID getUUID(String name) {
        if (nameToUuid.containsKey(name.toLowerCase())) {
            return nameToUuid.get(name.toLowerCase());
        }

        AtomicString atomic = new AtomicString();

        nucleus.getJedis().runCommand(redis -> {
            atomic.setString(redis.hget("name-to-uuid", name.toLowerCase()));
            return null;
        });

        if (atomic.getString() == null) {
            return null;
        } else {
            return UUID.fromString(atomic.getString());
        }
    }

    public void fetch() {
        nucleus.getJedis().runCommand((redis) -> {
            Map<String, String> cached = redis.hgetAll("name-to-uuid");

            if (cached == null || cached.isEmpty()) {
                return null;
            }

            Map<String, UUID> ntu = new HashMap<>();
            Map<UUID, String> utn = new HashMap<>();

            for (Map.Entry<String, String> entry : cached.entrySet()) {
                ntu.put(entry.getKey(), UUID.fromString(entry.getValue()));
                utn.put(UUID.fromString(entry.getValue()), entry.getKey());
            }

            nameToUuid = ntu;
            uuidToName = utn;

            return null;
        });
    }

    public void update(String name, UUID uuid) {
        nameToUuid.put(name.toLowerCase(), uuid);
        uuidToName.put(uuid, name);

        nucleus.getJedis().runCommand((redis) -> {
            redis.hset("name-to-uuid", name.toLowerCase(), uuid.toString());
            redis.hset("uuid-to-name", uuid.toString(), name);
            return null;
        });
    }

}

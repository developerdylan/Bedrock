package me.ohvalsgod.bedrock.profiling.provider;

import com.google.common.collect.Maps;
import lombok.Getter;
import me.ohvalsgod.bedrock.Bedrock;
import me.ohvalsgod.bedrock.player.BPlayer;
import me.ohvalsgod.bedrock.profiling.callback.result.BPlayerCallbackResult;
import me.ohvalsgod.bedrock.profiling.storage.ObjectStorage;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @param <T> The profile implementation we want to use.
 */

@Getter
public abstract class AbstractBPlayerProvider<T extends BPlayer> {

    private final Bedrock bedrock;
    private Map<UUID, T> bplayers = new HashMap<>();

    public AbstractBPlayerProvider(Bedrock bedrock) {
        this.bedrock = bedrock;
    }


    public T getByUniqueId(UUID uniqueId) {
        return bplayers.get(uniqueId);
    }

    public T getByPlayer(Player player) {
        return getByUniqueId(player.getUniqueId());
    }

    public void save(T bplayer) {

        //TODO: Save.
        getStorage().save(bplayer, (callbackProfile, result) -> {

            //Honestly, can't be fucking asked just do what you want here you get the gist.

        });

    }

    public void load(UUID uniqueId) {

        //TODO: Load.
        getStorage().load(uniqueId, (profile, result) -> {

            //go ham here.
            if(result == BPlayerCallbackResult.BPLAYER_DOESNT_EXIST) return; //oh no, what am I gonna do(instantiate new profile object)

        });

    }

    public Map<String, Object> serializeDefaultBPlayer(T bplayer) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("uniqueId", bplayer.getUniqueId());

        map.put("ignoredPlayers", bplayer.getIgnoredPlayers());
        map.put("friendsList", bplayer.getFriendsList());

        map.put("seeGlobalChat", bplayer.isSeeGlobalChat());
        map.put("hearPingOnMessage", bplayer.isHearPingOnMessage());
        map.put("seePrivateMessages", bplayer.isSeePrivateMessages());

        map.put("firstLogin", bplayer.getFirstLogin());
        map.put("lastLogout", bplayer.getLastLogout());
        map.put("currentAddress", bplayer.getCurrentAddress());
        map.put("lastAddress", bplayer.getLastAddress());
        map.put("addresses", bplayer.getAddresses());
        map.put("alternateAccounts", bplayer.getAlternateAccounts());

        if(serializeBPlayerImplementation(bplayer) != null && !serializeBPlayerImplementation(bplayer).isEmpty())
            map.putAll(serializeBPlayerImplementation(bplayer));

        return map;
    }

    public T deserializeDefaultBPlayer(Map<String, Object> map) {

        UUID uuid = (UUID) map.get("uniqueId");
        T bplayer;
        if(retrieveBPlayerImplementationInstance(uuid) == null) {
            bplayer = (T) new BPlayer(uuid);
        } else
            bplayer = retrieveBPlayerImplementationInstance(uuid);

        if(deserializeBPlayerImplementation(map) == null) {
            bplayer = deserializeBPlayerImplementation(map);
        }

        bplayer.setIgnoredPlayers((Set<UUID>) map.get("ignoredPlayers"));
        bplayer.setFriendsList((Set<UUID>) map.get("friendsList"));

        bplayer.setSeeGlobalChat((Boolean) map.get("seeGlobalChat"));
        bplayer.setHearPingOnMessage((Boolean) map.get("hearPingOnMessage"));
        bplayer.setSeePrivateMessages((Boolean) map.get("seePrivateMessages"));

        bplayer.setFirstLogin((long) map.get("firstLogin"));
        bplayer.setLastLogout((long) map.get("lastLogout"));
        bplayer.setCurrentAddress((String) map.get("currentAddress"));
        bplayer.setLastAddress((String) map.get("lastAddress"));
        bplayer.setAddresses((List<String>) map.get("addresses"));
        bplayer.setAlternateAccounts((Set<UUID>) map.get("alternateAccounts"));
        return bplayer;
    }


    public abstract ObjectStorage<UUID, T, BPlayerCallbackResult> getStorage();

    public abstract Map<String, Object> serializeBPlayerImplementation(T profile);

    public abstract T deserializeBPlayerImplementation(Map<String, Object> map);

    public abstract T retrieveBPlayerImplementationInstance(UUID uuid);
}

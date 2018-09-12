package me.ohvalsgod.bedrock.player;

import com.mongodb.client.MongoCursor;
import me.ohvalsgod.bedrock.Bedrock;
import me.ohvalsgod.bedrock.mongo.BedrockMongo;
import me.ohvalsgod.bedrock.util.MongoUtil;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class BPlayerManager {

    private Map<UUID, BPlayer> cachedPlayers;
    private Bedrock bedrock;

    public BPlayerManager(Bedrock bedrock) {
        this.bedrock = bedrock;
        cachedPlayers = new HashMap<>();
    }

    /**
     * Cache a BPlayer in memory
     *
     * @param profile profile to be cached
     */
    public void cache(BPlayer profile) {
        cachedPlayers.put(profile.getUniqueId(), profile);
    }

    /**
     * Find alternate accounts through cached BPlayer objects
     *
     * @param profile main account
     * @return alt accounts
     */
    public List<UUID> findAlts(BPlayer profile) {
        if (!Bedrock.mainThread(getClass())) {
            return null;
        }

        List<UUID> alts = new ArrayList<>();
        if (profile.getCurrentAddress() != null) {
            try (MongoCursor<Document> cursor = BedrockMongo.getInstance().getPlayers().find(MongoUtil.find("currentAddress", profile.getCurrentAddress())).iterator()) {
                cursor.forEachRemaining(document -> {
                    UUID uuid = UUID.fromString(document.getString("uuid"));

                    if (!uuid.equals(profile.getUniqueId())) {
                        if (!profile.getAlternateAccounts().contains(uuid)) {
                            alts.add(uuid);
                        }
                    }
                });
            }
        }
        return alts;
    }

    /**
     * Get a BPlayer object by player
     *
     * @param player the player
     * @return the BPlayer object
     */
    public BPlayer getByPlayer(Player player) {
        return getByUUID(player.getUniqueId());
    }

    /**
     * Get a BPlayer object by player name
     *
     * @param name the player's name
     * @return the BPlayer object
     */
    public BPlayer getByName(String name) {
        Player target = Bukkit.getPlayer(name);
        BPlayer toReturn;

        if (target == null) {
            UUID uuid = Bedrock.getInstance().getUuidCache().getUUID(name);

            if (uuid != null) {
                toReturn = getByUUID(uuid);
            } else {
                return null;
            }
        } else {
            toReturn = getByUUID(target.getUniqueId());
        }
        return toReturn;
    }

    /**
     * Get a BPlayer object by player UUID
     *
     * @param uuid the player's uuid
     * @return the BPlayer object
     */
    public BPlayer getByUUID(UUID uuid) {
        BPlayer toReturn = cachedPlayers.get(uuid);
        return toReturn == null ? new BPlayer(uuid) : toReturn;
    }

}

package me.ohvalsgod.bedrock.player.profiling.provider;

import com.google.common.collect.Maps;
import lombok.Getter;
import me.ohvalsgod.bedrock.Language;
import me.ohvalsgod.bedrock.mongo.BedrockMongo;
import me.ohvalsgod.bedrock.player.BPlayer;
import me.ohvalsgod.bedrock.player.profiling.callback.result.ProfileCallbackResult;
import me.ohvalsgod.bedrock.player.profiling.storage.ObjectStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @param <T> The profile implementation we want to use.
 */

@Getter
public abstract class AbstractProfileProvider<T extends BPlayer> {

    private final JavaPlugin plugin;
    private BedrockMongo mongo;
    private Map<UUID, T> profiles = new HashMap<>();

    public AbstractProfileProvider(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void setMongo(BedrockMongo mongo) {
        this.mongo = mongo;
    }

    public T getProfileByUniqueId(UUID uniqueId) {
        return profiles.get(uniqueId);
    }

    public T getProfileByPlayer(Player player) {
        return getProfileByUniqueId(player.getUniqueId());
    }

    public void save(T profile) {
        getStorage().save(profile, (callbackBPlayer, result) -> {
            if (result == ProfileCallbackResult.STORAGE_IS_MISSING) {
                return;
            }

            if (result == ProfileCallbackResult.PROFILE_SAVED_SUCCESSFULLY) {
                if (Bukkit.getPlayer(callbackBPlayer.getUniqueId()) != null) {
                    profile.setLastSave(System.currentTimeMillis());
                    Bukkit.getPlayer(callbackBPlayer.getUniqueId()).sendMessage(Language.getInstance().getString("PROFILE_SAVED"));
                }
            }

        });
    }

    public void load(UUID uniqueId) {
        getStorage().load(uniqueId, (profile, result) -> {
            if (result == ProfileCallbackResult.STORAGE_IS_MISSING) {
                return;
            }

            if(result == ProfileCallbackResult.PROFILE_DOES_NOT_EXIST)  {
                profile = (T) new BPlayer(uniqueId);
            }

            if (result == ProfileCallbackResult.PROFILE_LOADED_SUCCESSFULLY) {
                if (Bukkit.getPlayer(uniqueId) != null) {
                    Bukkit.getPlayer(uniqueId).sendMessage(Language.getInstance().getString("PROFILE_LOADED"));
                }
            }

            profiles.put(uniqueId, profile);

        });
    }

    public Map<String, Object> serializeDefaultProfile(T profile) {
        Map<String, Object> map = Maps.newHashMap();

        //TODO: Do the exact opposite of the deserialize method
        map.put("toReply", profile.getToReply());
        map.put("ignoredPlayers", profile.getIgnoredPlayers());
        map.put("friendsList", profile.getFriendsList());

        if(serializeProfileImplementation(profile) != null && !serializeProfileImplementation(profile).isEmpty()) {
            map.putAll(serializeProfileImplementation(profile));
        }

        return map;
    }

    public T deserializeDefaultProfile(Map<String, Object> map) {
        UUID uuid = (UUID) map.get("uniqueId");
        T profile;

        if(retrieveProfileImplementationInstance(uuid) == null) {
            profile = (T) new BPlayer(uuid);
        } else {
            profile = retrieveProfileImplementationInstance(uuid);
        }

        if(deserializeProfileImplementation(map) == null) {
            profile = deserializeProfileImplementation(map);
        }

        //TODO: basically, just use the setters in the BPlayer object, and set the objects in the objects from the map, examples below
        profile.setToReply((UUID) map.get("toReply"));
        profile.setIgnoredPlayers((Set<UUID>) map.get("ignoredPlayers"));
        profile.setFriendsList((Set<UUID>) map.get("friendsList"));

        return profile;
    }


    public abstract ObjectStorage<UUID, T, ProfileCallbackResult> getStorage();

    public abstract Map<String, Object> serializeProfileImplementation(T profile);

    public abstract T deserializeProfileImplementation(Map<String, Object> map);

    public abstract T retrieveProfileImplementationInstance(UUID uuid);
}

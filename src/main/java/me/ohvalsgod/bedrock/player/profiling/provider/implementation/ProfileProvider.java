package me.ohvalsgod.bedrock.player.profiling.provider.implementation;

import com.google.common.collect.Maps;
import me.ohvalsgod.bedrock.player.BPlayer;
import me.ohvalsgod.bedrock.player.profiling.callback.result.ProfileCallbackResult;
import me.ohvalsgod.bedrock.player.profiling.provider.AbstractProfileProvider;
import me.ohvalsgod.bedrock.player.profiling.storage.ObjectStorage;
import me.ohvalsgod.bedrock.player.profiling.storage.implementation.MongoProfileStorage;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;

public class ProfileProvider extends AbstractProfileProvider<BPlayer> {

    private ObjectStorage<UUID, BPlayer, ProfileCallbackResult> storage;

    public ProfileProvider(JavaPlugin plugin) {
        super(plugin);
        storage = new MongoProfileStorage<>(this);
    }

    @Override
    public ObjectStorage<UUID, BPlayer, ProfileCallbackResult> getStorage() {
        return storage;
    }

    //This is already handled by the default serialization process.
    @Override
    public Map<String, Object> serializeProfileImplementation(BPlayer profile) {
        return Maps.newHashMap();
    }

    @Override
    public BPlayer retrieveProfileImplementationInstance(UUID uuid) {
        return new BPlayer(uuid);
    }

    //This is already handled by the default deserialization process just as well.
    @Override
    public BPlayer deserializeProfileImplementation(Map<String, Object> map) {
        return deserializeDefaultProfile(map);
    }

}

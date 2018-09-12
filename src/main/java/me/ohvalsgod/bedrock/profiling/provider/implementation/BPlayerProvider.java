package me.ohvalsgod.bedrock.profiling.provider.implementation;

import com.google.common.collect.Maps;
import me.ohvalsgod.bedrock.Bedrock;
import me.ohvalsgod.bedrock.player.BPlayer;
import me.ohvalsgod.bedrock.profiling.callback.result.BPlayerCallbackResult;
import me.ohvalsgod.bedrock.profiling.provider.AbstractBPlayerProvider;
import me.ohvalsgod.bedrock.profiling.storage.ObjectStorage;
import me.ohvalsgod.bedrock.profiling.storage.implementation.MongoBPlayerStorage;

import java.util.Map;
import java.util.UUID;

public class BPlayerProvider extends AbstractBPlayerProvider<BPlayer> {

    public BPlayerProvider(Bedrock bedrock) {
        super(bedrock);
    }

    @Override
    public ObjectStorage<UUID, BPlayer, BPlayerCallbackResult> getStorage() {
        return new MongoBPlayerStorage<>(this);
    }

    //This is already handled by the default serialization process.
    @Override
    public Map<String, Object> serializeBPlayerImplementation(BPlayer profile) {
        return Maps.newHashMap();
    }

    @Override
    public BPlayer retrieveBPlayerImplementationInstance(UUID uuid) {
        return new BPlayer(uuid);
    }

    //This is already handled by the default deserialization process just as well.
    @Override
    public BPlayer deserializeBPlayerImplementation(Map<String, Object> map) {
        return null;
    }

}

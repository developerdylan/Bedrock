package me.ohvalsgod.bedrock.profiling.storage.implementation;

import me.ohvalsgod.bedrock.player.BPlayer;
import me.ohvalsgod.bedrock.profiling.callback.Callback;
import me.ohvalsgod.bedrock.profiling.callback.result.BPlayerCallbackResult;
import me.ohvalsgod.bedrock.profiling.provider.AbstractBPlayerProvider;
import me.ohvalsgod.bedrock.profiling.storage.ObjectStorage;
import me.ohvalsgod.bedrock.util.MongoUtil;
import org.bson.Document;

import java.util.Map;
import java.util.UUID;

public class MongoBPlayerStorage<T extends BPlayer> implements ObjectStorage<UUID, T, BPlayerCallbackResult> {

    private AbstractBPlayerProvider<T> provider;

    public MongoBPlayerStorage(AbstractBPlayerProvider<T> provider) {
        this.provider = provider;
    }

    @Override
    public void save(T value, Callback<T, BPlayerCallbackResult> callback) {

        if (provider.getBedrock().getMongo().getClient() == null)
            callback.accept(null, BPlayerCallbackResult.STORAGE_IS_MISSING);

        if(value == null)
            callback.accept(null, BPlayerCallbackResult.BPLAYER_DOESNT_EXIST);

        Map<String, Object> data = provider.serializeBPlayerImplementation(value);



    }

    @Override
    public T load(UUID key, Callback<T, BPlayerCallbackResult> callback) {
        BPlayer bplayer = new BPlayer(key);

        Document document = provider.getBedrock().getMongo().getPlayers().find(MongoUtil.find("uniqueId", key.toString())).first();

        return (T) bplayer;
    }
}

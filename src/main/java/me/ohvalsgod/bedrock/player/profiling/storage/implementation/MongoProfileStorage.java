package me.ohvalsgod.bedrock.player.profiling.storage.implementation;

import me.ohvalsgod.bedrock.player.BPlayer;
import me.ohvalsgod.bedrock.player.profiling.callback.Callback;
import me.ohvalsgod.bedrock.player.profiling.callback.result.ProfileCallbackResult;
import me.ohvalsgod.bedrock.player.profiling.provider.AbstractProfileProvider;
import me.ohvalsgod.bedrock.player.profiling.storage.ObjectStorage;
import me.ohvalsgod.bedrock.util.MongoUtil;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MongoProfileStorage<T extends BPlayer> implements ObjectStorage<UUID, T, ProfileCallbackResult> {

    private AbstractProfileProvider<T> provider;

    public MongoProfileStorage(AbstractProfileProvider<T> provider) {
        this.provider = provider;
    }

    @Override
    public void save(T value, Callback<T, ProfileCallbackResult> callback) {
        if(provider.getMongo().getClient() == null) {
            callback.accept(null, ProfileCallbackResult.STORAGE_IS_MISSING);
        }

        if(value == null) {
            callback.accept(null, ProfileCallbackResult.PROFILE_DOES_NOT_EXIST);
            return;
        }

        Map<String, Object> data = provider.serializeDefaultProfile(value);
        Document document = provider.getMongo().getPlayers().find(MongoUtil.find("uniqueId", value.getUniqueId().toString())).first();

        if (document == null) {
            document = new Document();
        }

        data.forEach(document::put);
        //  replace document

        callback.accept(value, ProfileCallbackResult.PROFILE_SAVED_SUCCESSFULLY);
    }

    @Override
    public T load(UUID key, Callback<T, ProfileCallbackResult> callback) {
        if(provider.getMongo().getClient() == null) {
            callback.accept(null, ProfileCallbackResult.STORAGE_IS_MISSING);
        }

        Document document = provider.getMongo().getPlayers().find(MongoUtil.find("uniqueId", key.toString())).first();

        Map<String, Object> data = new HashMap<>();

        for (String string : document.keySet()) {
            data.put(string, document.get(string));
        }

        T profile = provider.deserializeDefaultProfile(data);

        profile.setLoadedAt(System.currentTimeMillis());
        profile.setLoaded(true);

        callback.accept(profile, ProfileCallbackResult.PROFILE_LOADED_SUCCESSFULLY);

        return profile;
    }
}

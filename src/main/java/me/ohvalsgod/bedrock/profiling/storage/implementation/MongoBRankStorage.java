package me.ohvalsgod.bedrock.profiling.storage.implementation;

import me.ohvalsgod.bedrock.permissions.rank.BRank;
import me.ohvalsgod.bedrock.profiling.callback.Callback;
import me.ohvalsgod.bedrock.profiling.callback.result.BRankCallbackResult;
import me.ohvalsgod.bedrock.profiling.storage.ObjectStorage;

import java.util.UUID;

public class MongoBRankStorage<T extends BRank> implements ObjectStorage<UUID, T, BRankCallbackResult> {

    @Override
    public void save(T value, Callback<T, BRankCallbackResult> callback) {

    }

    @Override
    public T load(UUID key, Callback<T, BRankCallbackResult> callback) {
        return null;
    }

}

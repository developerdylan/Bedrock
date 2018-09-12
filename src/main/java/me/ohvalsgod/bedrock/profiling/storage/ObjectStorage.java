package me.ohvalsgod.bedrock.profiling.storage;

import me.ohvalsgod.bedrock.profiling.callback.Callback;

/**
 * This class will be used to store and load objects of any kind
 * @param <K> The key. Sample, {@link java.util.UUID}
 * @param <V> The value. Sample, {@link me.ohvalsgod.bedrock.player.BPlayer}
 * @param <R> The result. Sample, {@link me.ohvalsgod.bedrock.profiling.callback.implementation.CommonResultType}
 */
public interface ObjectStorage<K, V, R extends Enum<R>> {

    void save(V value, Callback<V, R> callback);

    V load(K key, Callback<V, R> callback);

}

package me.ohvalsgod.bedrock.profiling.callback.implementation;

import me.ohvalsgod.bedrock.profiling.callback.Callback;

/**
 * A more simpler approach to the {@link Callback} class, that only takes in a value, with a result of {@link CommonResultType}
 * @param <V>
 */
public interface SimpleCallback<V> extends Callback<V, CommonResultType> {}

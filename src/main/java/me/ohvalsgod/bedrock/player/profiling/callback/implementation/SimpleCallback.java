package me.ohvalsgod.bedrock.player.profiling.callback.implementation;

import me.ohvalsgod.bedrock.player.profiling.callback.Callback;

/**
 * A more simpler approach to the {@link Callback} class, that only takes in a value, with a result of {@link CommonResultType}
 * @param <V>
 */
public interface SimpleCallback<V> extends Callback<V, CommonResultType> {}

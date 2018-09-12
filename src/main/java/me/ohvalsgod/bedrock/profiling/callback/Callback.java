package me.ohvalsgod.bedrock.profiling.callback;


/**
 * @param <V> Generic type {@link V} serves as the value.
 * @param <R> Generic type {@link R} serves as the result of the callback.
 */
public interface Callback<V, R extends Enum<R>> {

    void accept(V value, R resultType);

}

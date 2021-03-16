package com.eprabab.kafka.consumer;

public interface Callback<V> {
    public void call(V data);
}

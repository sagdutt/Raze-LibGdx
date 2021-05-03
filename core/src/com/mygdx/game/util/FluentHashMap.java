package com.mygdx.game.util;

import java.util.HashMap;

public class FluentHashMap<K, V> extends HashMap<K, V> {

    public FluentHashMap<K, V> withEntry(K key, V value) {
        put(key, value);
        return this;
    }
}

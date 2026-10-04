package io.github.dulidanci.whereroadsmeet.registry;

import java.util.HashMap;
import java.util.Map;

public class Registry<K, V> {
    private final Map<K, V> map = new HashMap<>();

    public V register(K key, V value) {
        if (map.containsKey(key)) {
            throw new IllegalArgumentException("Key " + key + " is already registered in Registry<" + value.getClass().getName() + ">");
        }
        map.put(key, value);
        return value;
    }

    public V get(K key) {
        return map.get(key);
    }

    public Iterable<K> getAll() {
        return map.keySet();
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }
}

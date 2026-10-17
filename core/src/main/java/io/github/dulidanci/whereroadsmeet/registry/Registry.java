package io.github.dulidanci.whereroadsmeet.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Registry<V> {
    private final Map<RegistryKey, V> map = new HashMap<>();
    public final boolean manualNumbering;

    public Registry(boolean manualNumbering) {
        this.manualNumbering = manualNumbering;
    }

    public V register(RegistryKey key, V value) {
        if (key == null || Objects.equals(key.id(), "") || key.number() < 0)
            throw new IllegalStateException("Can not register entry with a half key: " + key);
        if (map.containsKey(key))
            throw new IllegalArgumentException("Key " + key + " is already registered in Registry<" + value.getClass().getName() + ">");
        map.put(key, value);
        return value;
    }

    public RegistryKey getKey(int number) {
        return this.getKey(RegistryKey.halfKey(number));
    }

    public RegistryKey getKey(String id) {
        return this.getKey(RegistryKey.halfKey(id));
    }

    private RegistryKey getKey(RegistryKey key) {
        for (RegistryKey registryKey : map.keySet()) {
            if (Objects.equals(registryKey, key)) {
                return registryKey;
            }
        }
        return null;
    }

    public V get(RegistryKey key) {
        return map.get(key);
    }

    public Set<RegistryKey> getAll() {
        return map.keySet();
    }

    public int getSize() {
        return map.size();
    }
}

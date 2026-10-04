package io.github.dulidanci.whereroadsmeet.registry;

import io.github.dulidanci.whereroadsmeet.level.block.Block;

public class Registries {
    public static final Registry<String, Block> BLOCK_TYPES = new Registry<>();

    public static <K, V> V registerEntry(Registry<K, V> registry, K key, V value) {
        return registry.register(key, value);
    }
}

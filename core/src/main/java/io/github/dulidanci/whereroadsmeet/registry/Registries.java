package io.github.dulidanci.whereroadsmeet.registry;

import io.github.dulidanci.whereroadsmeet.level.block.Block;

public class Registries {
    public static final Registry<Block> BLOCK_TYPES = new Registry<>(false);

    public static <V> V registerEntry(Registry<V> registry, RegistryKey key, V value) {
        if (!registry.manualNumbering)
            throw new IllegalStateException("Manual numbering must be enabled to write number ids in registry " + registry);
        return registry.register(key, value);
    }

    public static <V> V registerEntryNext(Registry<V> registry, String id, V value) {
        if (registry.manualNumbering)
            throw new IllegalStateException("Manual numbering must be disabled to use automatic numbering in registry " + registry);
        return registry.register(RegistryKey.of(id, registry.getSize()), value);
    }
}

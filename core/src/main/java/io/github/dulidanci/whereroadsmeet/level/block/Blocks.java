package io.github.dulidanci.whereroadsmeet.level.block;

import io.github.dulidanci.whereroadsmeet.registry.Registries;

public class Blocks {
    public static final Block AIR;
    public static final Block LOG;
    public static final Block PLANKS;
    public static final Block COBBLESTONE;
    public static final Block GRASS;
    public static final Block LEAVES;

    public static Block register(Block block) {
        return Registries.registerEntryNext(Registries.BLOCK_TYPES, block.getId(), block);
    }

    public static void init() {
        System.out.println("Initializing Block Types");
    }

    static {
        AIR = register(new Block("air").setTransparent(true).setSolid(false));
        LOG = register(new Block("log"));
        PLANKS = register(new Block("planks"));
        COBBLESTONE = register(new Block("cobblestone"));
        GRASS = register(new Block("grass"));
        LEAVES = register(new Block("leaves").setTransparent(true));
    }
}

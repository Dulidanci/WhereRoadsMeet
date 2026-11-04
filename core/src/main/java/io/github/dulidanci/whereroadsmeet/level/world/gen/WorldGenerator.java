package io.github.dulidanci.whereroadsmeet.level.world.gen;

import io.github.dulidanci.whereroadsmeet.level.block.Blocks;
import io.github.dulidanci.whereroadsmeet.level.world.Chunk;

public class WorldGenerator {
    public static Chunk generateChunk(int x, int z) {
        Chunk chunk = new Chunk();

        for (int i = 0; i < Chunk.SIZE; i++) {
            for (int j = 0; j < Chunk.SIZE; j++) {
                double perlin = Perlin2D.perlinCalc((x + (float) i / Chunk.SIZE) / 2, (z + (float) j / Chunk.SIZE) / 2);
                for (int h = 0; h < Chunk.SIZE; h++) {
                    if ((float) h / Chunk.SIZE * 2 <= perlin) {
                        chunk.setBlock(i, h, j, Blocks.GRASS);
                    }
                }
            }
        }

        return chunk;
    }
}

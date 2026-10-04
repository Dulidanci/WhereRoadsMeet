package io.github.dulidanci.whereroadsmeet.level.world;

import com.badlogic.gdx.math.GridPoint3;
import io.github.dulidanci.whereroadsmeet.level.block.Block;
import io.github.dulidanci.whereroadsmeet.level.block.Blocks;

import java.util.function.BiConsumer;

public class Chunk {
    public static final int SIZE = 16;
    private final Block[][][] blocks = new Block[SIZE][SIZE][SIZE];

    public Chunk() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                for (int k = 0; k < SIZE; k++) {
                    blocks[i][j][k] = Blocks.AIR;
                }
            }
        }
    }

    public Block getBlock(int x, int y, int z) {
        if (x < 0 || y < 0 || z < 0 || x >= SIZE || y >= SIZE || z >= SIZE) {
            throw new IllegalArgumentException("Local chunk coordinate out of bounds [" + x + ", " + y + ", " + z + "]");
        }
        return blocks[x][y][z];
    }

    public void setBlock(int x, int y, int z, Block block) {
        if (x < 0 || y < 0 || z < 0 || x >= SIZE || y >= SIZE || z >= SIZE) {
            throw new IllegalArgumentException("Local chunk coordinate out of bounds [" + x + ", " + y + ", " + z + "]");
        }
        blocks[x][y][z] = block;
    }

    public void forEach(GridPoint3 chunkPos, BiConsumer<GridPoint3, Block> blockConsumer) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                for (int k = 0; k < SIZE; k++) {
                    if (blocks[i][j][k] != null && blocks[i][j][k] != Blocks.AIR) {
                        blockConsumer.accept(new GridPoint3(chunkPos.x * SIZE + i, chunkPos.y * SIZE + j, chunkPos.z * SIZE + k), blocks[i][j][k]);
                    }
                }
            }
        }
    }
}

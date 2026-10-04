package io.github.dulidanci.whereroadsmeet.level.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint3;
import com.badlogic.gdx.math.Matrix3;
import io.github.dulidanci.whereroadsmeet.level.block.Block;
import io.github.dulidanci.whereroadsmeet.level.block.Blocks;
import io.github.dulidanci.whereroadsmeet.screen.Direction;
import io.github.dulidanci.whereroadsmeet.util.Pair;

import java.util.*;

public class World {
    public static final Matrix3 WORLD_TO_SCREEN_TM_LEFT = new Matrix3(new float[] {
        20f, 5f, 0f,
        0f, 16f, 0f,
        -11f, 10f, 1f
    });
    public static final Matrix3 WORLD_TO_SCREEN_TM_RIGHT = new Matrix3(new float[] {
        11f, 10f, 0f,
        0f, 16f, 0f,
        -20f, 5f, 0f
    });
    private final WorldRenderer worldRenderer;
    private final Map<GridPoint3, Chunk> chunks = new HashMap<>();

    public World() {

        this.worldRenderer = new WorldRenderer(this);
        this.initRoofedHouse();
    }

    public void update() {}

    public void render(SpriteBatch batch, float delta, Direction direction) {
        PriorityQueue<Pair<GridPoint3, Block>> queue = new PriorityQueue<>(Comparator.comparing(
            pair -> -pair.first().x + pair.first().y * 2 - pair.first().z
        ));
        chunks.forEach((chunkPos, chunk) -> chunk.forEach(chunkPos, (blockPos, block) -> {
            if (getBlock(new GridPoint3(blockPos).add(direction.getVisibleSides().x, 0, 0)).isTransparent() ||
                getBlock(new GridPoint3(blockPos).add(0, direction.getVisibleSides().y, 0)).isTransparent() ||
                getBlock(new GridPoint3(blockPos).add(0, 0, direction.getVisibleSides().z)).isTransparent()
            ) {
                queue.add(Pair.of(direction.rotateCoords(blockPos), block));
            }
        }));

        worldRenderer.render(batch, delta, direction, queue);
    }

    public void setBlock(GridPoint3 blockPos, Block block) {
        GridPoint3 chunkPos = new GridPoint3(Math.floorDiv(blockPos.x, Chunk.SIZE), Math.floorDiv(blockPos.y, Chunk.SIZE), Math.floorDiv(blockPos.z, Chunk.SIZE));
        if (!chunks.containsKey(chunkPos)) {
            chunks.put(chunkPos, new Chunk());
        }
        chunks.get(chunkPos).setBlock(Math.floorMod(blockPos.x, Chunk.SIZE), Math.floorMod(blockPos.y, Chunk.SIZE), Math.floorMod(blockPos.z, Chunk.SIZE), block);
    }

    public Block getBlock(GridPoint3 blockPos) {
        GridPoint3 chunkPos = new GridPoint3(Math.floorDiv(blockPos.x, Chunk.SIZE), Math.floorDiv(blockPos.y, Chunk.SIZE), Math.floorDiv(blockPos.z, Chunk.SIZE));
        if (!chunks.containsKey(chunkPos)) {
            return Blocks.AIR;
        }
        return chunks.get(chunkPos).getBlock(Math.floorMod(blockPos.x, Chunk.SIZE), Math.floorMod(blockPos.y, Chunk.SIZE), Math.floorMod(blockPos.z, Chunk.SIZE));
    }

    public void initRoofedHouse() {
        chunks.clear();

        // grass
        for (int i = -5; i <= 5; i++) {
            for (int j = -5; j <= 5; j++) {
                if (Math.abs(i) + Math.abs(j) <= 7) {
                    setBlock(new GridPoint3(i, 0, j), Blocks.GRASS);
                }
            }
        }

        // cobblestone
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                setBlock(new GridPoint3(i, 1, j), Blocks.COBBLESTONE);
            }
        }

        // walls
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                for (int k = 2; k <= 5; k++) {
                    if (Math.abs(i) == 2 || Math.abs(j) == 2) {
                        if (k == 5 || (Math.abs(i) == 2 && Math.abs(j) == 2)) {
                            setBlock(new GridPoint3(i, k, j), Blocks.LOG);
                        } else {
                            setBlock(new GridPoint3(i, k, j), Blocks.PLANKS);
                        }
                    }
                }
            }
        }

        // openings
        setBlock(new GridPoint3(0, 2, 2), Blocks.AIR);
        setBlock(new GridPoint3(0, 3, 2), Blocks.AIR);
        setBlock(new GridPoint3(-2, 3, 0), Blocks.AIR);
        setBlock(new GridPoint3(2, 3, 0), Blocks.AIR);

        // roof
        for (int i = 0; i <= 3; i++) {
            for (int j = -3; j <= 3; j++) {
                setBlock(new GridPoint3(i, 8 - i, j), Blocks.PLANKS);
                setBlock(new GridPoint3(-i, 8 - i, j), Blocks.PLANKS);
            }
        }
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j += 2) {
                setBlock(new GridPoint3(i, 6, 2 * j), Blocks.LOG);
                if (i == 0) setBlock(new GridPoint3(i, 7, 2 * j), Blocks.LOG);
            }
        }

        // under tree
        for (int i = -6; i >= -9; i--) {
            for (int j = -2; j <= 2; j++) {
                if (-i + Math.abs(j) == 11) {
                    setBlock(new GridPoint3(-5, 0, (int) (j * 1.5f)), Blocks.GRASS);
                } else {
                    setBlock(new GridPoint3(i, 0, j), Blocks.GRASS);
                }
            }
        }

        //tree
        for (int i = -5; i >= -9; i--) {
            for (int j = -2; j <= 2; j++) {
                for (int k = 4; k <= 5; k++) {
                    setBlock(new GridPoint3(i, k, j), Blocks.LEAVES);
                }
            }
        }
        for (int i = -6; i >= -8; i--) {
            for (int j = -1; j <= 1; j++) {
                for (int k = 6; k <= 7; k++) {
                    if (k < 7 || Math.abs(j) != 1 || Math.abs(i + 7) != 1) {
                        setBlock(new GridPoint3(i, k, j), Blocks.LEAVES);
                    }
                }
            }
        }
        for (int i = 1; i <= 6; i++) {
            setBlock(new GridPoint3(-7, i, 0), Blocks.LOG);
        }
        setBlock(new GridPoint3(-5, 4, 2), Blocks.AIR);
        setBlock(new GridPoint3(-9, 5, -2), Blocks.AIR);
    }
}

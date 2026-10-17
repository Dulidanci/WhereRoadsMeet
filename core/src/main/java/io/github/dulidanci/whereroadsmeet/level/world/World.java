package io.github.dulidanci.whereroadsmeet.level.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint3;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.collision.Ray;
import io.github.dulidanci.whereroadsmeet.level.block.Block;
import io.github.dulidanci.whereroadsmeet.level.block.Blocks;
import io.github.dulidanci.whereroadsmeet.level.world.collision.RaycastResult;
import io.github.dulidanci.whereroadsmeet.level.world.collision.Raycaster;
import io.github.dulidanci.whereroadsmeet.registry.Registries;
import io.github.dulidanci.whereroadsmeet.screen.Direction;
import io.github.dulidanci.whereroadsmeet.util.Pair;

import java.util.*;

public class World {
    public static final Matrix3 WORLD_TO_SCREEN_TM_RIGHT = new Matrix3(new float[] {
        20f,  5f,  0f,
        0f,   16f, 0f,
        -11f, 10f, 0f
    });
    public static final Matrix3 WORLD_TO_SCREEN_TM_LEFT = new Matrix3(new float[] {
        11f,  10f, 0f,
        0f,   16f, 0f,
        -20f, 5f,  0f
    });
    public final Settings settings;
    private final WorldRenderer worldRenderer;
    private final Raycaster rayCaster;
    private final Map<GridPoint3, Chunk> chunks = new HashMap<>();
    private Block selectedBlock;

    public World() {
        this.settings = new Settings();
        this.worldRenderer = new WorldRenderer(this);
        this.rayCaster = new Raycaster(this);
        this.selectedBlock = Blocks.COBBLESTONE;

        this.reset();
    }

    public void reset() {
        this.initRoofedHouse();
    }

    public void update() {}

    public void render(SpriteBatch batch, float delta, Direction direction) {
        GridPoint3 directionBias = new GridPoint3()
            .add(0, 4, 0)
            .add(Math.abs(direction.getFacingLargeSide().getVector().x) * 2, 0, Math.abs(direction.getFacingLargeSide().getVector().z) * 2)
            .add(Math.abs(direction.getFacingSmallSide().getVector().x), 0, Math.abs(direction.getFacingSmallSide().getVector().z));
        PriorityQueue<Pair<GridPoint3, Block>> queue = new PriorityQueue<>(Comparator.comparing(
            pair -> -pair.first().x * directionBias.x + pair.first().y * directionBias.y - pair.first().z * directionBias.z
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
        if (block == null || blockPos == null || blockPos.y > Settings.getMaximalY() || blockPos.y < Settings.getMinimalY()) return;
        GridPoint3 chunkPos = new GridPoint3(Math.floorDiv(blockPos.x, Chunk.SIZE), Math.floorDiv(blockPos.y, Chunk.SIZE), Math.floorDiv(blockPos.z, Chunk.SIZE));
        if (!chunks.containsKey(chunkPos)) {
            chunks.put(chunkPos, new Chunk());
        }
        chunks.get(chunkPos).setBlock(Math.floorMod(blockPos.x, Chunk.SIZE), Math.floorMod(blockPos.y, Chunk.SIZE), Math.floorMod(blockPos.z, Chunk.SIZE), block);
    }

    public void removeBlock(GridPoint3 blockPos) {
        this.setBlock(blockPos, Blocks.AIR);
    }

    public Block getBlock(GridPoint3 blockPos) {
        GridPoint3 chunkPos = new GridPoint3(Math.floorDiv(blockPos.x, Chunk.SIZE), Math.floorDiv(blockPos.y, Chunk.SIZE), Math.floorDiv(blockPos.z, Chunk.SIZE));
        if (!chunks.containsKey(chunkPos)) {
            return Blocks.AIR;
        }
        return chunks.get(chunkPos).getBlock(Math.floorMod(blockPos.x, Chunk.SIZE), Math.floorMod(blockPos.y, Chunk.SIZE), Math.floorMod(blockPos.z, Chunk.SIZE));
    }

    public Block getBlock(int x, int y, int z) {
        GridPoint3 chunkPos = new GridPoint3(Math.floorDiv(x, Chunk.SIZE), Math.floorDiv(y, Chunk.SIZE), Math.floorDiv(z, Chunk.SIZE));
        if (!chunks.containsKey(chunkPos)) {
            return Blocks.AIR;
        }
        return chunks.get(chunkPos).getBlock(Math.floorMod(x, Chunk.SIZE), Math.floorMod(y, Chunk.SIZE), Math.floorMod(z, Chunk.SIZE));
    }

    public RaycastResult rayCast(Ray ray) {
        return rayCaster.raycast(ray, 30f);
    }

    public void nextBlock() {
        List<Integer> list = new ArrayList<>();
        Registries.BLOCK_TYPES.getAll().forEach(registryKey -> list.add(registryKey.number()));
        list.sort(Comparator.naturalOrder());

        int currentNumber = Registries.BLOCK_TYPES.getKey(selectedBlock.getId()).number();
        boolean next = false;
        for (Integer i : list) {
            if (next) {
                selectedBlock = Registries.BLOCK_TYPES.get(Registries.BLOCK_TYPES.getKey(i));
                return;
            }
            if (i == currentNumber) {
                next = true;
            }
        }
        selectedBlock = Registries.BLOCK_TYPES.get(Registries.BLOCK_TYPES.getKey(list.get(1)));
    }

    public Block getSelectedBlock() {
        return selectedBlock;
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

    public void initChunk() {
        chunks.clear();
        for (int i = 0; i < Chunk.SIZE; i++) {
            for (int j = 0; j < Chunk.SIZE; j++) {
                for (int k = 0; k < Chunk.SIZE; k++) {
                    setBlock(new GridPoint3(i, j, k), Blocks.LOG);
                }
            }
        }

        setBlock(new GridPoint3(0, 15, 0), Blocks.PLANKS);
        setBlock(new GridPoint3(15, 15, 0), Blocks.LEAVES);
        setBlock(new GridPoint3(0, 15, 15), Blocks.GRASS);
    }

    public static class Settings {
        public final int RENDER_DISTANCE = 4;
        public static final int MINIMAL_CHUNK_Y = 0;
        public static final int MAXIMAL_CHUNK_Y = 0;

        public static int getMaximalY() {
            return (MAXIMAL_CHUNK_Y + 1) * Chunk.SIZE - 1;
        }

        public static int getMinimalY() {
            return MINIMAL_CHUNK_Y * Chunk.SIZE;
        }
    }
}

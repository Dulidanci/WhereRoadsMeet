package io.github.dulidanci.whereroadsmeet.level.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.github.dulidanci.whereroadsmeet.asset.AssetManagerWrapper;
import io.github.dulidanci.whereroadsmeet.level.block.Block;
import io.github.dulidanci.whereroadsmeet.screen.Direction;
import io.github.dulidanci.whereroadsmeet.util.Pair;

import java.util.PriorityQueue;

public class WorldRenderer {
    public static final float RENDER_SCALE = 4f;
    private static final Vector2[] ROTATIONAL_TRANSLATION = new Vector2[] {
        new Vector2(11, 17),
        new Vector2(20, 17),
        new Vector2(31, 22),
        new Vector2(31, 27),
        new Vector2(20, 31),
        new Vector2(11, 31),
        new Vector2(0, 27),
        new Vector2(0, 22)
    };
    private static final float[] TRANSPARENCY = new float[] {0.5f, 0.7f, 0.3f, 0.0f};
    private final World world;

    public WorldRenderer(World world) {
        this.world = world;
    }

    public void render(SpriteBatch batch, float delta, Direction direction, PriorityQueue<Pair<GridPoint3, Block>> queue) {
        while (!queue.isEmpty()) {
            Pair<GridPoint3, Block> pair = queue.poll();
            GridPoint3 grid = pair.first();
            Block block = pair.second();

            Vector3 vector = calculatePosition(grid, direction);

            drawBlock(batch, vector, direction, block);

            if (!world.getBlock(direction.inverseRotateCoords(new GridPoint3(grid)).add(direction.getFacingLargeSide().getVector())).isSolid()) {
                drawShadow(batch, vector, direction, true);
            }
            if (!world.getBlock(direction.inverseRotateCoords(new GridPoint3(grid)).add(direction.getFacingSmallSide().getVector())).isSolid()) {
                drawShadow(batch, vector, direction, false);
            }
        }
    }

    public void drawBlock(SpriteBatch batch, Vector3 vector, Direction direction, Block block) {
        Texture texture = AssetManagerWrapper.getTexture(block.getId());
        batch.draw(texture, vector.x, vector.y, texture.getWidth() * RENDER_SCALE, texture.getHeight() * RENDER_SCALE, 0, 0, texture.getWidth(), texture.getHeight(), direction.ordinal() % 2 == 1, false);
    }

    public void drawShadow(SpriteBatch batch, Vector3 vector, Direction direction, boolean large) {
        if (large) {
            batch.setColor(0f, 0f, 0f, TRANSPARENCY[(direction.ordinal() * 7 % 8 / 2 + 1) % 4]);
            Texture texture = AssetManagerWrapper.getTexture("large_shadow");
            batch.draw(texture, vector.x, vector.y, texture.getWidth() * RENDER_SCALE, texture.getHeight() * RENDER_SCALE, 0, 0, texture.getWidth(), texture.getHeight(), direction.ordinal() % 2 == 1, false);
        } else {
            batch.setColor(0f, 0f, 0f, TRANSPARENCY[direction.ordinal() * 3 % 8 / 2]);
            Texture texture = AssetManagerWrapper.getTexture("small_shadow");
            batch.draw(texture, vector.x, vector.y, texture.getWidth() * RENDER_SCALE, texture.getHeight() * RENDER_SCALE, 0, 0, texture.getWidth(), texture.getHeight(), direction.ordinal() % 2 == 1, false);
        }
        batch.setColor(1f, 1f, 1f, 1f);
    }

    public static Vector3 calculatePosition(GridPoint3 grid, Direction direction) {
        return new Vector3(grid.x, grid.y, grid.z)
            .mul(direction.getTransformationMatrix())
            .scl(RENDER_SCALE)
            .add(0, -RENDER_SCALE * 16 * (Chunk.SIZE - 1), 0f)
            .sub(ROTATIONAL_TRANSLATION[direction.ordinal()].x * RENDER_SCALE, ROTATIONAL_TRANSLATION[direction.ordinal()].y * RENDER_SCALE, 0f);
    }


}

package io.github.dulidanci.whereroadsmeet.level.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint3;
import com.badlogic.gdx.math.Vector3;
import io.github.dulidanci.whereroadsmeet.asset.AssetManagerWrapper;
import io.github.dulidanci.whereroadsmeet.level.block.Block;
import io.github.dulidanci.whereroadsmeet.screen.Direction;
import io.github.dulidanci.whereroadsmeet.util.Pair;

import java.util.PriorityQueue;

public class WorldRenderer {
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

            Vector3 vector = new Vector3(grid.x, grid.y, grid.z)
                .mul(direction.getTransformationMatrix())
                .scl(5f)
                .add((float) Gdx.graphics.getWidth() / 2, 32, 0);

            drawBlock(batch, vector, direction, block);

            if (!world.getBlock(direction.inverseRotateCoords(new GridPoint3(grid)).add(direction.getVisibleSides().x, 0, 0)).isSolid()) {
                drawShadow(batch, vector, direction, !direction.getXSideLarger());
            }
            if (!world.getBlock(direction.inverseRotateCoords(new GridPoint3(grid)).add(0, 0, direction.getVisibleSides().z)).isSolid()) {
                drawShadow(batch, vector, direction, direction.getXSideLarger());
            }
        }
    }

    private void drawBlock(SpriteBatch batch, Vector3 vector, Direction direction, Block block) {
        batch.draw(AssetManagerWrapper.getTexture(block.getId()), vector.x, vector.y, 160, 160, 0, 0, 32, 32, direction.ordinal() % 2 == 1, false);
    }

    private void drawShadow(SpriteBatch batch, Vector3 vector, Direction direction, boolean large) {
        if (large) {
            batch.setColor(0f, 0f, 0f, TRANSPARENCY[(direction.ordinal() * 7 % 8 / 2 + 1) % 4]);
            batch.draw(AssetManagerWrapper.getTexture("large_shadow"), vector.x, vector.y, 160, 160, 0, 0, 32, 32, direction.ordinal() % 2 == 1, false);
        } else {
            batch.setColor(0f, 0f, 0f, TRANSPARENCY[direction.ordinal() * 3 % 8 / 2]);
            batch.draw(AssetManagerWrapper.getTexture("small_shadow"), vector.x, vector.y, 160, 160, 0, 0, 32, 32, direction.ordinal() % 2 == 1, false);
        }
        batch.setColor(1f, 1f, 1f, 1f);
    }
}

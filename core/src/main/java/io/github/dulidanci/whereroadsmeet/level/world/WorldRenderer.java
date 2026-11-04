package io.github.dulidanci.whereroadsmeet.level.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.github.dulidanci.whereroadsmeet.asset.AssetManagerWrapper;
import io.github.dulidanci.whereroadsmeet.level.block.Block;
import io.github.dulidanci.whereroadsmeet.level.entity.Entity;
import io.github.dulidanci.whereroadsmeet.screen.Direction;

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
    private static final float[] TRANSPARENCY = new float[] {0.4f, 0.55f, 0.25f, 0.0f};

    public static void drawEntity(SpriteBatch batch, float delta, Vector3 vector, Direction direction, Entity entity) {

    }

    public static void drawBlock(SpriteBatch batch, float delta, Vector3 vector, Direction direction, Block block, boolean smallShadow, boolean largeShadow) {
        calculatePosition(vector, direction);
        TextureRegion texture = AssetManagerWrapper.getTexture(AssetManagerWrapper.BLOCK_ATLAS, block.getId());
        batch.draw(texture, vector.x, vector.y,
            texture.getRegionWidth() * RENDER_SCALE / 2, texture.getRegionHeight() * RENDER_SCALE / 2,
            texture.getRegionWidth() * RENDER_SCALE, texture.getRegionHeight() * RENDER_SCALE,
            direction.ordinal() % 2 == 1 ? -1 : 1, 1, 0);

        if (smallShadow) {
            batch.setColor(0f, 0f, 0f, TRANSPARENCY[direction.ordinal() * 3 % 8 / 2]);
            TextureRegion shadowTexture = AssetManagerWrapper.getTexture(AssetManagerWrapper.BLOCK_ATLAS, "small_shadow");
            batch.draw(shadowTexture, vector.x, vector.y,
                shadowTexture.getRegionWidth() * RENDER_SCALE / 2, shadowTexture.getRegionHeight() * RENDER_SCALE / 2,
                shadowTexture.getRegionWidth() * RENDER_SCALE, shadowTexture.getRegionHeight() * RENDER_SCALE,
                direction.ordinal() % 2 == 1 ? -1 : 1, 1, 0);
        }

        if (largeShadow) {
            batch.setColor(0f, 0f, 0f, TRANSPARENCY[(direction.ordinal() * 7 % 8 / 2 + 1) % 4]);
            TextureRegion shadowTexture = AssetManagerWrapper.getTexture(AssetManagerWrapper.BLOCK_ATLAS, "large_shadow");
            batch.draw(shadowTexture, vector.x, vector.y,
                shadowTexture.getRegionWidth() * RENDER_SCALE / 2, shadowTexture.getRegionHeight() * RENDER_SCALE / 2,
                shadowTexture.getRegionWidth() * RENDER_SCALE, shadowTexture.getRegionHeight() * RENDER_SCALE,
                direction.ordinal() % 2 == 1 ? -1 : 1, 1, 0);
        }

        batch.setColor(1f, 1f, 1f, 1f);
    }

    public static void calculatePosition(Vector3 vector, Direction direction) {
        vector
            .mul(direction.getTransformationMatrix())
            .scl(RENDER_SCALE)
            .add(0, -RENDER_SCALE * 16 * (Chunk.SIZE - 1), 0f)
            .sub(ROTATIONAL_TRANSLATION[direction.ordinal()].x * RENDER_SCALE, ROTATIONAL_TRANSLATION[direction.ordinal()].y * RENDER_SCALE, 0f);
    }

    public static Vector3 calculateNewPosition(GridPoint3 grid, Direction direction) {
        return new Vector3(grid.x, grid.y, grid.z)
            .mul(direction.getTransformationMatrix())
            .scl(RENDER_SCALE)
            .add(0, -RENDER_SCALE * 16 * (Chunk.SIZE - 1), 0f)
            .sub(ROTATIONAL_TRANSLATION[direction.ordinal()].x * RENDER_SCALE, ROTATIONAL_TRANSLATION[direction.ordinal()].y * RENDER_SCALE, 0f);
    }
}

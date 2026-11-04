package io.github.dulidanci.whereroadsmeet.level.world.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint3;
import com.badlogic.gdx.math.Vector3;
import io.github.dulidanci.whereroadsmeet.level.block.Block;
import io.github.dulidanci.whereroadsmeet.level.world.WorldRenderer;
import io.github.dulidanci.whereroadsmeet.screen.Direction;

public class RenderableBlock implements Renderable {
    public Vector3 position;
    public Block block;
    public boolean smallShadow;
    public boolean largeShadow;

    public RenderableBlock(GridPoint3 position, Block block) {
        this.position = new Vector3(position.x, position.y, position.z);
        this.block = block;
        this.smallShadow = false;
        this.largeShadow = false;
    }

    @Override
    public void render(SpriteBatch batch, float delta, Direction direction) {
        WorldRenderer.drawBlock(batch, delta, position, direction, block, smallShadow, largeShadow);
    }

    @Override
    public Vector3 getPosition() {
        return position;
    }

    @Override
    public RenderableType type() {
        return Renderable.RenderableType.BLOCK;
    }
}

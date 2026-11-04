package io.github.dulidanci.whereroadsmeet.level.world.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import io.github.dulidanci.whereroadsmeet.level.entity.Entity;
import io.github.dulidanci.whereroadsmeet.level.world.WorldRenderer;
import io.github.dulidanci.whereroadsmeet.screen.Direction;

public class RenderableEntity implements Renderable {
    public Vector3 position;
    public Entity entity;

    public RenderableEntity(Vector3 position, Entity entity) {
        this.position = position;
        this.entity = entity;
    }

    @Override
    public void render(SpriteBatch batch, float delta, Direction direction) {
        WorldRenderer.drawEntity(batch, delta, position, direction, entity);
    }

    @Override
    public Vector3 getPosition() {
        return position;
    }

    @Override
    public RenderableType type() {
        return RenderableType.ENTITY;
    }
}

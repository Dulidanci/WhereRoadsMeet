package io.github.dulidanci.whereroadsmeet.level.world.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import io.github.dulidanci.whereroadsmeet.screen.Direction;

public interface Renderable {
    void render(SpriteBatch batch, float delta, Direction direction);

    Vector3 getPosition();

    RenderableType type();

    enum RenderableType {
        BLOCK(RenderableBlock.class),
        ENTITY(RenderableEntity.class);

        public final Class<? extends Renderable> renderableClass;

        RenderableType(Class<? extends Renderable> renderableClass) {
            this.renderableClass = renderableClass;
        }
    }
}

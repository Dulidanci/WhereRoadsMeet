package io.github.dulidanci.whereroadsmeet.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.dulidanci.whereroadsmeet.asset.AssetManagerWrapper;
import io.github.dulidanci.whereroadsmeet.level.world.World;
import io.github.dulidanci.whereroadsmeet.registry.Registries;

public class WorldScreen implements Screen {
    private final SpriteBatch batch;
    private final ScreenViewport viewport;
    private final OrthographicCamera camera;
    public World world;
    private boolean previousTouched;
    private Vector2 previousPosition;
    public Direction direction;
    public boolean debug_corner = false;

    public WorldScreen() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);

        previousTouched = false;
        previousPosition = new Vector2();
        direction = Direction.SOUTH_WEST;

        Registries.BLOCK_TYPES.getAll().forEach(AssetManagerWrapper::loadTexture);
        AssetManagerWrapper.loadTexture("small_shadow");
        AssetManagerWrapper.loadTexture("large_shadow");
        AssetManagerWrapper.loadTexture("corner");
        AssetManagerWrapper.finishLoading();

        world = new World();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // #input process
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            direction = direction.getLeft();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            direction = direction.getRight();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            debug_corner = !debug_corner;
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (previousTouched) {
                camera.position.add(
                    -(Gdx.input.getX() - previousPosition.x) * camera.zoom,
                    (Gdx.input.getY() - previousPosition.y) * camera.zoom,
                    0
                );
            }
            previousTouched = true;
            previousPosition = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        } else {
            previousTouched = false;
        }
        // #input process end

        world.update();

        ScreenUtils.clear(Color.LIGHT_GRAY);

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();

        world.render(batch, delta, direction);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;

        viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        this.dispose();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public void scrolled(float amountY) {
        if (amountY != 0) {
            Vector3 pivot = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            float oldZoom = camera.zoom;
            float newZoom = Math.clamp(oldZoom + amountY * 0.1f, 0.4f, 2.5f);

            if (newZoom != oldZoom) {
                camera.position.x = pivot.x + (camera.position.x - pivot.x) * newZoom / oldZoom;
                camera.position.y = pivot.y + (camera.position.y - pivot.y) * newZoom / oldZoom;

                camera.zoom = newZoom;
                camera.update();
            }
        }
    }
}

package io.github.dulidanci.whereroadsmeet.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.dulidanci.whereroadsmeet.asset.AssetManagerWrapper;
import io.github.dulidanci.whereroadsmeet.level.block.Blocks;
import io.github.dulidanci.whereroadsmeet.level.world.Axis;
import io.github.dulidanci.whereroadsmeet.level.world.World;
import io.github.dulidanci.whereroadsmeet.level.world.WorldRenderer;
import io.github.dulidanci.whereroadsmeet.level.world.collision.RaycastResult;
import io.github.dulidanci.whereroadsmeet.level.world.collision.Raycaster;

public class WorldScreen implements Screen {
    private final SpriteBatch batch;
    private final ScreenViewport viewport;
    private final OrthographicCamera camera;
    public final World world;
    private boolean previousTouched;
    private Vector2 previousPosition;
    public Direction direction;
    public final Ray ray;
    private GLProfiler glProfiler;

    public WorldScreen() {
        batch = new SpriteBatch(8191);
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);

        previousTouched = false;
        previousPosition = new Vector2();
        direction = Direction.NORTH_WEST;

        ray = new Ray();

        AssetManagerWrapper.loadAtlas(AssetManagerWrapper.BLOCK_ATLAS);

        world = new World();
    }

    @Override
    public void show() {
        glProfiler = new GLProfiler(Gdx.graphics);
        glProfiler.enable();
    }

    @Override
    public void render(float delta) {
        // #input process
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            RaycastResult raycastResult = raycastFromScreen((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
            direction = direction.getLeft();
            setCameraOnTarget(raycastResult);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            RaycastResult raycastResult = raycastFromScreen((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
            direction = direction.getRight();
            setCameraOnTarget(raycastResult);
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            RaycastResult raycastResult = raycastFromScreen(Gdx.input.getX(), Gdx.input.getY());
            world.removeBlock(raycastResult.targetCoords());
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            RaycastResult raycastResult = raycastFromScreen(Gdx.input.getX(), Gdx.input.getY());
            if (raycastResult.side() != Axis.NEGATIVE_Y) {
                if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                    if (world.getBlock(raycastResult.targetCoords().add(raycastResult.side().getOpposite().getVector())) == Blocks.AIR) {
                        world.setBlock(raycastResult.targetCoords(), world.getSelectedBlock());
                    }
                } else {
                    world.setBlock(raycastResult.targetCoords().add(raycastResult.side().getVector()), world.getSelectedBlock());
                }
            }
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)) {
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            world.nextBlock();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            world.reset();
        }
        // #input process end

        long start = System.nanoTime();

        world.update(delta);

        ScreenUtils.clear(Color.LIGHT_GRAY);

        viewport.apply();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        world.render(batch, delta, direction);

        Vector3 vector = new Vector3(Gdx.graphics.getWidth() - 128, Gdx.graphics.getHeight(), 0);
        viewport.unproject(vector);
        TextureRegion texture = AssetManagerWrapper.getTexture(AssetManagerWrapper.BLOCK_ATLAS, world.getSelectedBlock().getId());
        batch.draw(texture, vector.x, vector.y,
            texture.getRegionWidth() * WorldRenderer.RENDER_SCALE * camera.zoom / 2, texture.getRegionHeight() * WorldRenderer.RENDER_SCALE * camera.zoom / 2,
            texture.getRegionWidth() * WorldRenderer.RENDER_SCALE * camera.zoom, texture.getRegionHeight() * WorldRenderer.RENDER_SCALE * camera.zoom,
            direction.ordinal() % 2 == 1 ? -1 : 1, 1, 0);

        batch.end();

        /// LOGGING
        if (true) {

            if (Gdx.graphics.getFrameId() % 60 == 0) {
                Gdx.app.log("Performance",
                    "FPS: " + Gdx.graphics.getFramesPerSecond());
            }

            long elapsed = System.nanoTime() - start;

            if (Gdx.graphics.getFrameId() % 60 == 0) {
                Gdx.app.log("Performance",
                    "Render time: " + elapsed / 1_000_000.0 + " ms");
            }

        } else {

            Gdx.app.log("Rendering",
                "OpenGL calls: " + glProfiler.getCalls()
                    + ", Draw calls: " + glProfiler.getDrawCalls()
                    + ", texture bindings: " + glProfiler.getTextureBindings()
                    + ", vertices: " + glProfiler.getVertexCount().total);

            glProfiler.reset();

        }
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;

        viewport.update(width, height, false);
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

    public RaycastResult raycastFromScreen(float screenX, float screenY) {
        Vector3 screenPosition = new Vector3(screenX, screenY, 0);
        viewport.unproject(screenPosition);
        screenPosition.scl(1 / WorldRenderer.RENDER_SCALE);

        ray.set(Raycaster.screenToWorldTopTM(screenPosition, direction), Raycaster.getRotatedCamera(direction));
        return world.rayCast(ray);
    }

    public void setCameraOnTarget(RaycastResult raycastResult) {
        /// top: 16 24
        /// small left: 6 13
        /// small right 24 13
        /// large left: 21 11
        /// large right: 11 11
        camera.position.set(WorldRenderer.calculateNewPosition(direction.rotateCoords(raycastResult.targetCoords()), direction));

        if (raycastResult.side().isVertical()) {
            camera.position.add(16 * WorldRenderer.RENDER_SCALE, 24 * WorldRenderer.RENDER_SCALE, 0);
        } else if (raycastResult.side() == direction.getFacingLargeSide() || raycastResult.side() == direction.getFacingLargeSide().getOpposite()) {
            camera.position.add(
                (direction.ordinal() % 2 == 0 ? 21 : 11) * WorldRenderer.RENDER_SCALE,
                11 * WorldRenderer.RENDER_SCALE,
                0
            );
        } else {
            camera.position.add(
                (direction.ordinal() % 2 == 0 ? 24 : 6) * WorldRenderer.RENDER_SCALE,
                13 * WorldRenderer.RENDER_SCALE,
                0
            );
        }
    }
}

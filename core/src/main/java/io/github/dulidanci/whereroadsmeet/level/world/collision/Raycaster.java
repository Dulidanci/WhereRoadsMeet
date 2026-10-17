package io.github.dulidanci.whereroadsmeet.level.world.collision;

import com.badlogic.gdx.math.GridPoint3;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import io.github.dulidanci.whereroadsmeet.level.block.Block;
import io.github.dulidanci.whereroadsmeet.level.block.Blocks;
import io.github.dulidanci.whereroadsmeet.level.world.Axis;
import io.github.dulidanci.whereroadsmeet.level.world.World;
import io.github.dulidanci.whereroadsmeet.screen.Direction;

public class Raycaster {
    public static final Matrix3 SCREEN_TO_WORLD_TOP_TM_RIGHT = new Matrix3(new float[] {
        10f / 255f, 11f / 255f, 0f,
        -5f / 255f, 20f / 255f, 0f,
        0f,         0f,         0f
    });
    public static final Matrix3 SCREEN_TO_WORLD_TOP_TM_LEFT = new Matrix3(new float[] {
          5f / 255f, 20f / 255f, 0f,
        -10f / 255f, 11f / 255f, 0f,
          0f,         0f,        0f
    });
    public static final Vector3 CAMERA_DIRECTION = new Vector3(176, -255, 320).nor();
    private final World world;

    public Raycaster(World world) {
        this.world = world;
    }

    public RaycastResult raycast(Ray ray, float maxDistance) {
        Vector3 origin = ray.origin;
        Vector3 direction = ray.direction;

        int x = MathUtils.floor(origin.x);
        int y = MathUtils.floor(origin.y);
        int z = MathUtils.floor(origin.z);

        int stepX = direction.x >= 0 ? 1 : -1;
        int stepY = direction.y >= 0 ? 1 : -1;
        int stepZ = direction.z >= 0 ? 1 : -1;

        float tDeltaX =
            direction.x == 0
                ? Float.POSITIVE_INFINITY
                : Math.abs(1f / direction.x);

        float tDeltaY =
            direction.y == 0
                ? Float.POSITIVE_INFINITY
                : Math.abs(1f / direction.y);

        float tDeltaZ =
            direction.z == 0
                ? Float.POSITIVE_INFINITY
                : Math.abs(1f / direction.z);

        float tMaxX;

        if (direction.x >= 0) {
            tMaxX = (x + 1 - origin.x) * tDeltaX;
        } else {
            tMaxX = (origin.x - x) * tDeltaX;
        }

        float tMaxY;

        if (direction.y >= 0) {
            tMaxY = (y + 1 - origin.y) * tDeltaY;
        } else {
            tMaxY = (origin.y - y) * tDeltaY;
        }

        float tMaxZ;

        if (direction.z >= 0) {
            tMaxZ = (z + 1 - origin.z) * tDeltaZ;
        } else {
            tMaxZ = (origin.z - z) * tDeltaZ;
        }

        float distance = 0;
        Axis lastAxis = Axis.POSITIVE_Y;

        while (y >= World.Settings.getMinimalY() && distance < maxDistance) {

            Block block = world.getBlock(x, y, z);

            if (block != null && block != Blocks.AIR) {
                return new RaycastResult(new GridPoint3(x, y, z), block, lastAxis);
            }

            if (tMaxX < tMaxY) {

                if (tMaxX < tMaxZ) {
                    x += stepX;
                    distance = tMaxX;
                    tMaxX += tDeltaX;
                    lastAxis = stepX > 0 ? Axis.NEGATIVE_X : Axis.POSITIVE_X;
                } else {
                    z += stepZ;
                    distance = tMaxZ;
                    tMaxZ += tDeltaZ;
                    lastAxis = stepZ > 0 ? Axis.NEGATIVE_Z : Axis.POSITIVE_Z;
                }

            } else {

                if (tMaxY < tMaxZ) {
                    y += stepY;
                    distance = tMaxY;
                    tMaxY += tDeltaY;
                    lastAxis = stepY > 0 ? Axis.NEGATIVE_Y : Axis.POSITIVE_Y;
                } else {
                    z += stepZ;
                    distance = tMaxZ;
                    tMaxZ += tDeltaZ;
                    lastAxis = stepZ > 0 ? Axis.NEGATIVE_Z : Axis.POSITIVE_Z;
                }
            }
        }
        return new RaycastResult(new GridPoint3(x, y - stepY, z), Blocks.AIR, Axis.NEGATIVE_Y);
    }

    public static Vector3 screenToWorldTopTM(Vector3 screenPosition, Direction direction) {
        screenPosition.traMul(direction.ordinal() % 2 == 0 ? Raycaster.SCREEN_TO_WORLD_TOP_TM_RIGHT : Raycaster.SCREEN_TO_WORLD_TOP_TM_LEFT);
        return direction.inverseRotateCoords(new Vector3(screenPosition.x, World.Settings.getMaximalY() + 1, screenPosition.y));
    }

    public static Vector3 getRotatedCamera(Direction direction) {
        return new Vector3(direction.inverseRotateCoords(new Vector3(
            direction.ordinal() % 2 == 0 ? CAMERA_DIRECTION.x : CAMERA_DIRECTION.z,
            CAMERA_DIRECTION.y,
            direction.ordinal() % 2 == 0 ? CAMERA_DIRECTION.z : CAMERA_DIRECTION.x
        )));
    }
}

package io.github.dulidanci.whereroadsmeet.screen;

import com.badlogic.gdx.math.GridPoint3;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector3;
import io.github.dulidanci.whereroadsmeet.level.world.Axis;
import io.github.dulidanci.whereroadsmeet.level.world.World;

import java.util.function.Function;

public enum Direction {
    NORTH_WEST(Vector3::new, Axis.NEGATIVE_Z, Axis.NEGATIVE_X),
    WEST_NORTH(Vector3::new, Axis.NEGATIVE_X, Axis.NEGATIVE_Z),
    WEST_SOUTH(vector -> new Vector3(-vector.z, vector.y, vector.x), Axis.NEGATIVE_X, Axis.POSITIVE_Z),
    SOUTH_WEST(vector -> new Vector3(-vector.z, vector.y, vector.x), Axis.POSITIVE_Z, Axis.NEGATIVE_X),
    SOUTH_EAST(vector -> new Vector3(-vector.x, vector.y, -vector.z), Axis.POSITIVE_Z, Axis.POSITIVE_X),
    EAST_SOUTH(vector -> new Vector3(-vector.x, vector.y, -vector.z), Axis.POSITIVE_X, Axis.POSITIVE_Z),
    EAST_NORTH(vector -> new Vector3(vector.z, vector.y, -vector.x), Axis.POSITIVE_X, Axis.NEGATIVE_Z),
    NORTH_EAST(vector -> new Vector3(vector.z, vector.y, -vector.x), Axis.NEGATIVE_Z, Axis.POSITIVE_X);

    private final Function<Vector3, Vector3> coordsRotation;
    private final Axis facingLargeSide;
    private final Axis facingSmallSide;

    Direction(Function<Vector3, Vector3> rotator, Axis facingLargeSide, Axis facingSmallSide) {
        this.coordsRotation = rotator;
        this.facingLargeSide = facingLargeSide;
        this.facingSmallSide = facingSmallSide;
    }

    public GridPoint3 rotateCoords(GridPoint3 coords) {
        Vector3 result = this.coordsRotation.apply(new Vector3(coords.x, coords.y, coords.z));
        return new GridPoint3((int) result.x, (int) result.y, (int) result.z);
    }

    public Vector3 rotateCoords(Vector3 coords) {
        return this.coordsRotation.apply(coords);
    }

    public GridPoint3 inverseRotateCoords(GridPoint3 coords) {
        Vector3 result = this.coordsRotation.apply(this.coordsRotation.apply(this.coordsRotation.apply(new Vector3(coords.x, coords.y, coords.z))));
        return new GridPoint3((int) result.x, (int) result.y, (int) result.z);
    }

    public Vector3 inverseRotateCoords(Vector3 coords) {
        return this.coordsRotation.apply(this.coordsRotation.apply(this.coordsRotation.apply(coords)));
    }

    public Matrix3 getTransformationMatrix() {
        return ordinal() % 2 == 0 ? World.WORLD_TO_SCREEN_TM_RIGHT : World.WORLD_TO_SCREEN_TM_LEFT;
    }

    public Direction getRight() {
        return Direction.values()[(this.ordinal() - 1 + Direction.values().length) % Direction.values().length];
    }

    public Direction getLeft() {
        return Direction.values()[(this.ordinal() + 1) % Direction.values().length];
    }

    public Axis getFacingLargeSide() {
        return facingLargeSide;
    }

    public Axis getFacingSmallSide() {
        return facingSmallSide;
    }

    public GridPoint3 getVisibleSides() {
        return new GridPoint3().add(facingLargeSide.getVector()).add(facingSmallSide.getVector()).add(Axis.POSITIVE_Y.getVector());
    }
}

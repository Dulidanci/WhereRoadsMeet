package io.github.dulidanci.whereroadsmeet.screen;

import com.badlogic.gdx.math.GridPoint3;
import com.badlogic.gdx.math.Matrix3;
import io.github.dulidanci.whereroadsmeet.level.world.World;

import java.util.function.Function;

public enum Direction {
    NORTH_WEST(GridPoint3::new, new GridPoint3(-1, 1, -1), true),
    WEST_NORTH(GridPoint3::new, new GridPoint3(-1, 1, -1), false),
    WEST_SOUTH(grid -> new GridPoint3(-grid.z, grid.y, grid.x), new GridPoint3(-1, 1, 1), false),
    SOUTH_WEST(grid -> new GridPoint3(-grid.z, grid.y, grid.x), new GridPoint3(-1, 1, 1), true),
    SOUTH_EAST(grid -> new GridPoint3(-grid.x, grid.y, -grid.z), new GridPoint3(1, 1, 1), true),
    EAST_SOUTH(grid -> new GridPoint3(-grid.x, grid.y, -grid.z), new GridPoint3(1, 1, 1), false),
    EAST_NORTH(grid -> new GridPoint3(grid.z, grid.y, -grid.x), new GridPoint3(1, 1, -1), false),
    NORTH_EAST(grid -> new GridPoint3(grid.z, grid.y, -grid.x), new GridPoint3(1, 1, -1), true);

    private final Function<GridPoint3, GridPoint3> coordsRotation;
    private final GridPoint3 visibleSides;
    private final Boolean xSideLarger;

    Direction(Function<GridPoint3, GridPoint3> rotator, GridPoint3 visibleSides, Boolean xSideLarger) {
        this.coordsRotation = rotator;
        this.visibleSides = visibleSides;
        this.xSideLarger = xSideLarger;
    }

    public GridPoint3 rotateCoords(GridPoint3 coords) {
        return this.coordsRotation.apply(coords);
    }

    public GridPoint3 inverseRotateCoords(GridPoint3 coords) {
        return this.coordsRotation.apply(this.coordsRotation.apply(this.coordsRotation.apply(coords)));
    }

    public Matrix3 getTransformationMatrix() {
        return ordinal() % 2 == 0 ? World.WORLD_TO_SCREEN_TM_LEFT : World.WORLD_TO_SCREEN_TM_RIGHT;
    }

    public Direction getRight() {
        return Direction.values()[(this.ordinal() - 1 + Direction.values().length) % Direction.values().length];
    }

    public Direction getLeft() {
        return Direction.values()[(this.ordinal() + 1) % Direction.values().length];
    }

    public GridPoint3 getVisibleSides() {
        return visibleSides;
    }

    public Boolean getXSideLarger() {
        return xSideLarger;
    }
}

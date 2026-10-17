package io.github.dulidanci.whereroadsmeet.level.world;

import com.badlogic.gdx.math.GridPoint3;

public enum Axis {
    POSITIVE_X(new GridPoint3(1, 0, 0)),
    POSITIVE_Y(new GridPoint3(0, 1, 0)),
    POSITIVE_Z(new GridPoint3(0, 0, 1)),
    NEGATIVE_X(new GridPoint3(-1, 0, 0)),
    NEGATIVE_Y(new GridPoint3(0, -1, 0)),
    NEGATIVE_Z(new GridPoint3(0, 0, -1));

    private final GridPoint3 vector;

    Axis(GridPoint3 vector) {
        this.vector = vector;
    }

    public GridPoint3 getVector() {
        return vector;
    }

    public Axis getOpposite() {
        return Axis.values()[(ordinal() + 3) % Axis.values().length];
    }

    public boolean isVertical() {
        return this == POSITIVE_Y || this == NEGATIVE_Y;
    }
}

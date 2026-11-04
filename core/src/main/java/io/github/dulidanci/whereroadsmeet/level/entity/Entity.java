package io.github.dulidanci.whereroadsmeet.level.entity;

import com.badlogic.gdx.math.Vector3;
import io.github.dulidanci.whereroadsmeet.level.world.World;
import io.github.dulidanci.whereroadsmeet.screen.Direction;

public abstract class Entity {
    protected final EntityType<?> type;
    public final World world;
    public float width;
    public float height;
    public float length;
    public float x;
    public float y;
    public float z;
    public float vx;
    public float vy;
    public float vz;
    public Direction facing;
    public boolean falling;

    protected Entity(EntityType<?> type, World world) {
        this.type = type;
        this.world = world;
    }

    public void update(float delta) {

    }

    public void setPosition(Vector3 position) {}
}

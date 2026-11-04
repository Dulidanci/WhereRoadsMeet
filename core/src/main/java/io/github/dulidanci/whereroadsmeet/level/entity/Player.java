package io.github.dulidanci.whereroadsmeet.level.entity;

import io.github.dulidanci.whereroadsmeet.level.world.World;

public class Player extends Entity {
    public Player(World world) {
        super(EntityType.PLAYER, world);
    }
}

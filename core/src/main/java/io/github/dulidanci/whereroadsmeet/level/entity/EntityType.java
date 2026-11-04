package io.github.dulidanci.whereroadsmeet.level.entity;

public class EntityType<T extends Entity> {
    public static final EntityType<Player> PLAYER;
    private String type;

    static {
        PLAYER = new EntityType<>();
    }
}

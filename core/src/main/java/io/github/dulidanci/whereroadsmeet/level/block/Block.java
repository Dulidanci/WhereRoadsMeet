package io.github.dulidanci.whereroadsmeet.level.block;

public class Block {
    private final String id;
    private boolean transparent;
    private boolean solid;

    public Block(String id) {
        this.id = id;
        this.transparent = false;
        this.solid = true;
    }

    public String getId() {
        return id;
    }

    public boolean isTransparent() {
        return transparent;
    }

    public Block setTransparent(boolean transparent) {
        this.transparent = transparent;
        return this;
    }

    public boolean isSolid() {
        return solid;
    }

    public Block setSolid(boolean solid) {
        this.solid = solid;
        return this;
    }

    @Override
    public String toString() {
        return "Block." + id;
    }
}

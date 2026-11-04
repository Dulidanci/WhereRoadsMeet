package io.github.dulidanci.whereroadsmeet.asset;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.dulidanci.whereroadsmeet.WhereRoadsMeet;

public class AssetManagerWrapper {
    public static final String BLOCK_TEXTURES = "/textures/blocks/";
    public static final String PLAYER_TEXTURES = "/textures/player/";
    public static final String ATLAS_TEXTURES = "/textures/atlases/";
    public static final String BLOCK_ATLAS = "blocks";
    public static final String PLAYER_ATLAS = "player";

    private final AssetManager assetManager;

    public AssetManagerWrapper() {
        assetManager = new AssetManager();
    }

    public void dispose() {
        assetManager.dispose();
    }

    public static AssetManagerWrapper getInstance() {
        return WhereRoadsMeet.getInstance().getAssetManager();
    }

    public static void loadTexture(String path) {
        getInstance().assetManager.load(WhereRoadsMeet.ID + BLOCK_TEXTURES + path + ".png", Texture.class);
        getInstance().assetManager.finishLoading();
    }

    public static void loadAtlas(String path) {
        getInstance().assetManager.load(WhereRoadsMeet.ID + ATLAS_TEXTURES + path + ".atlas", TextureAtlas.class);
        getInstance().assetManager.finishLoading();
    }

    public static TextureRegion getTexture(String atlas, String path) {
        return getInstance().assetManager.get(WhereRoadsMeet.ID + ATLAS_TEXTURES + atlas + ".atlas", TextureAtlas.class).findRegion(path);
    }

    public static Texture getTexture(String path) {
        return getInstance().assetManager.get(WhereRoadsMeet.ID + BLOCK_TEXTURES + path + ".png");
    }
}

package io.github.dulidanci.whereroadsmeet.asset;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import io.github.dulidanci.whereroadsmeet.WhereRoadsMeet;

public class AssetManagerWrapper {
    private static final String TEXTURES = "/textures/";
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
        getInstance().assetManager.load(WhereRoadsMeet.ID + TEXTURES + path + ".png", Texture.class);
        getInstance().assetManager.finishLoading();
    }

    public static Texture getTexture(String path) {
        return getInstance().assetManager.get(WhereRoadsMeet.ID + TEXTURES + path + ".png");
    }

    public static void finishLoading() {
        getInstance().assetManager.finishLoading();
    }
}

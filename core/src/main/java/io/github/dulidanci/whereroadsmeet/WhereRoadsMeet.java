package io.github.dulidanci.whereroadsmeet;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.github.dulidanci.whereroadsmeet.asset.AssetManagerWrapper;
import io.github.dulidanci.whereroadsmeet.input.InputProcessor;
import io.github.dulidanci.whereroadsmeet.level.block.Blocks;
import io.github.dulidanci.whereroadsmeet.screen.ScreenManager;
import io.github.dulidanci.whereroadsmeet.screen.WorldScreen;

public class WhereRoadsMeet implements ApplicationListener {
    public static final String ID = "whereroadsmeet";
    public static WhereRoadsMeet instance;
    private ScreenManager screenManager;
    private AssetManagerWrapper assetManager;

    private WhereRoadsMeet() {}

    public static WhereRoadsMeet getInstance() {
        if (instance == null) {
            instance = new WhereRoadsMeet();
        }
        return instance;
    }

    @Override
    public void create() {
        screenManager = new ScreenManager();
        assetManager = new AssetManagerWrapper();

        Gdx.input.setInputProcessor(new InputProcessor());
        Blocks.init();

        screenManager.setScreen(new WorldScreen());
    }

    @Override
    public void resize(int width, int height) {
        if(width <= 0 || height <= 0) return;

        screenManager.resize(width, height);
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        screenManager.render();
    }

    @Override
    public void pause() {
        screenManager.pause();
    }

    @Override
    public void resume() {
        screenManager.resume();
    }

    @Override
    public void dispose() {
        screenManager.dispose();
        assetManager.dispose();
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }

    public AssetManagerWrapper getAssetManager() {
        return assetManager;
    }
}

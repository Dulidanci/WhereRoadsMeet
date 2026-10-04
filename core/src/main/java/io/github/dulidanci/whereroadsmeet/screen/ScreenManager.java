package io.github.dulidanci.whereroadsmeet.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import io.github.dulidanci.whereroadsmeet.WhereRoadsMeet;

public class ScreenManager {
    private Screen screen;

    public static ScreenManager getInstance() {
        return WhereRoadsMeet.getInstance().getScreenManager();
    }

    public void dispose() {
        if (screen != null) screen.hide();
    }

    public void pause() {
        if (screen != null) screen.pause();
    }

    public void resume() {
        if (screen != null) screen.resume();
    }

    public void render() {
        if (screen != null) screen.render(Gdx.graphics.getDeltaTime());
    }

    public void resize(int width, int height) {
        if (screen != null) screen.resize(width, height);
    }

    public void setScreen(Screen screen) {
        if (this.screen != null) this.screen.hide();
        this.screen = screen;
        if (this.screen != null) {
            this.screen.show();
            this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    public Screen getScreen() {
        return screen;
    }
}

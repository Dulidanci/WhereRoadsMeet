package io.github.dulidanci.whereroadsmeet.input;

import com.badlogic.gdx.InputAdapter;
import io.github.dulidanci.whereroadsmeet.screen.ScreenManager;
import io.github.dulidanci.whereroadsmeet.screen.WorldScreen;

public class InputProcessor extends InputAdapter {
    @Override
    public boolean scrolled(float amountX, float amountY) {
        if (ScreenManager.getInstance().getScreen() instanceof WorldScreen screen) {
            screen.scrolled(amountY);
            return true;
        }
        return false;
    }
}

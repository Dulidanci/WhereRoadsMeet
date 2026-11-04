package io.github.dulidanci.whereroadsmeet.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import io.github.dulidanci.whereroadsmeet.WhereRoadsMeet;
import io.github.dulidanci.whereroadsmeet.asset.AssetManagerWrapper;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return;
        createApplication();
    }

    private static void createApplication() {
        TexturePacker.process(
            new TexturePacker.Settings(),
            "assets/" + WhereRoadsMeet.ID + AssetManagerWrapper.BLOCK_TEXTURES,
            "assets/" + WhereRoadsMeet.ID + AssetManagerWrapper.ATLAS_TEXTURES,
            AssetManagerWrapper.BLOCK_ATLAS
        );

        TexturePacker.process(
            new TexturePacker.Settings(),
            "assets/" + WhereRoadsMeet.ID + AssetManagerWrapper.PLAYER_TEXTURES,
            "assets/" + WhereRoadsMeet.ID + AssetManagerWrapper.ATLAS_TEXTURES,
            AssetManagerWrapper.PLAYER_ATLAS
        );

        new Lwjgl3Application(WhereRoadsMeet.getInstance(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Where Roads Meet");
        configuration.useVsync(true);
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        configuration.setWindowedMode(640, 480);
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");

        //// This could improve compatibility with Windows machines with buggy OpenGL drivers, Macs
        //// with Apple Silicon that have to emulate compatibility with OpenGL anyway, and more.
        //// This uses the dependency `com.badlogicgames.gdx:gdx-lwjgl3-angle` to function.
        //// You would need to add this line to lwjgl3/build.gradle , below the dependency on `gdx-backend-lwjgl3`:
        ////     implementation "com.badlogicgames.gdx:gdx-lwjgl3-angle:$gdxVersion"
        //// You can choose to add the following line and the mentioned dependency if you want; they
        //// are not intended for games that use GL30 (which is compatibility with OpenGL ES 3.0).
        //// Know that it might not work well in some cases.
//        configuration.setOpenGLEmulation(Lwjgl3ApplicationConfiguration.GLEmulation.ANGLE_GLES20, 0, 0);

        return configuration;
    }
}

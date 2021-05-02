package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.RazeGame;
import com.mygdx.game.constant.AppConstants;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = AppConstants.APP_NAME;
		config.width = AppConstants.APP_WIDTH;
		config.height = AppConstants.APP_HEIGHT;
		new LwjglApplication(new RazeGame(), config);
	}
}

package com.mygdx.game.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.mygdx.game.RazeGame;
import com.mygdx.game.constant.AppConstants;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                // Resizable application, uses available space in browser
//                return new GwtApplicationConfiguration(true);
                // Fixed size application:
                return new GwtApplicationConfiguration(AppConstants.APP_WIDTH, AppConstants.APP_HEIGHT);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new RazeGame();
        }
}
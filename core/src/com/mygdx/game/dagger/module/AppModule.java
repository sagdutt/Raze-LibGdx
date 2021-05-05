package com.mygdx.game.dagger.module;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.constant.AppConstants;
import com.mygdx.game.event.EventBus;
import com.mygdx.game.event.eventbus.InMemoryEventBus;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class AppModule {

    @Provides
    @Singleton
    public Batch provideBatch() {
        return new SpriteBatch();
    }

    @Provides
    @Singleton
    public Camera provideCamera() {
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, AppConstants.APP_WIDTH, AppConstants.APP_HEIGHT);
        return camera;
    }

    @Provides
    @Singleton
    public EventBus provideEventBus() {
        return new InMemoryEventBus();
    }
}

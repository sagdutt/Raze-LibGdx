package com.mygdx.game.dagger.module;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.constant.AppConstants;
import com.mygdx.game.event.EventBus;
import com.mygdx.game.event.eventbus.InMemoryEventBus;
import dagger.Module;
import dagger.Provides;

import javax.inject.Named;
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

    @Provides
    @Singleton
    public Stage provideStage() {
        return new Stage(new ScreenViewport());
    }

    @Provides
    @Singleton
    @Named(AppConstants.CHARACTER_SELECT)
    public Skin provideSkin() {
        return new Skin(Gdx.files.internal("Skins/glassy/glassy-ui.json"));
    }
}

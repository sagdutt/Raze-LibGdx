package com.mygdx.game.dagger.component;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.dagger.module.AppModule;
import com.mygdx.game.event.EventBus;
import com.mygdx.game.screen.ArenaScreen;
import com.mygdx.game.screen.CharacterSelectScreen;
import dagger.Component;

import javax.inject.Singleton;

@Component(modules = AppModule.class)
@Singleton
public interface AppComponent {

    Batch getBatch();

    EventBus getEventBus();

    ArenaScreen getArenaScreen();

    CharacterSelectScreen getCharacterSelectScreen();
}

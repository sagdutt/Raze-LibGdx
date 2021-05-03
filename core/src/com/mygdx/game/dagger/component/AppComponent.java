package com.mygdx.game.dagger.component;

import com.mygdx.game.dagger.module.AppModule;
import com.mygdx.game.screen.ArenaScreen;
import dagger.Component;

import javax.inject.Singleton;

@Component(modules = AppModule.class)
@Singleton
public interface AppComponent {

    ArenaScreen getArenaScreen();
}

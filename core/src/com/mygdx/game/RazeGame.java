package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.dagger.component.AppComponent;
import com.mygdx.game.dagger.component.DaggerAppComponent;
import com.mygdx.game.event.Event;
import com.mygdx.game.event.EventHandler;
import com.mygdx.game.event.events.CharacterSelectedEvent;
import com.mygdx.game.screen.ArenaScreen;

import java.util.Collections;
import java.util.List;

public class RazeGame extends Game implements EventHandler {

	private AppComponent appComponent;
	
	@Override
	public void create () {
		appComponent = DaggerAppComponent.create();
		this.setScreen(appComponent.getCharacterSelectScreen());
		appComponent.getEventBus().subscribe(this);
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		appComponent.getBatch().dispose();
	}

	@Override
	public List<Class<?>> getSubscribedEventClasses() {
		return Collections.singletonList(CharacterSelectedEvent.class);
	}

	@Override
	public void handleEvent(Event<?> event) {
		if (CharacterSelectedEvent.class == event.getClass()) {
			ArenaScreen arenaScreen = appComponent.getArenaScreen();
			this.setScreen(arenaScreen);
			arenaScreen.initializePlayer(((CharacterSelectedEvent) event).getPayload());
		}
	}
}

package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.dagger.component.AppComponent;
import com.mygdx.game.dagger.component.DaggerAppComponent;

public class RazeGame extends Game {

	private AppComponent appComponent;
	
	@Override
	public void create () {
		appComponent = DaggerAppComponent.create();
		this.setScreen(appComponent.getArenaScreen());
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
	}

}

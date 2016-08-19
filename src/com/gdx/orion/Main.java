package com.gdx.orion;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
//import com.badlogic.gdx.audio.Music;
import com.gdx.orion.screens.GameStateManager;

public class Main extends Game implements ApplicationListener{
	
	public static boolean console = true;
	public static final float GAME_WORLD_WIDTH = 6000;
	public static final float GAME_WORLD_HEIGHT = 6000;
	
	@Override
	public void create() {
		//final Music music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music/1 - Fathers Day - Silent Loser.mp3"));
		
		GameStateManager.initiate(this);
		Gdx.graphics.setContinuousRendering(true);
		Gdx.graphics.setVSync(true);
		Gdx.input.setCursorCatched(false);
		GameStateManager.setScreen(GameStateManager.LEVELSELECT);
		//music.play();
		
		// TODO: Configure the campaign singleton by reading back from persistent storage
	}

	@Override
	public void render () {
		super.render();
	}
	@Override
	public void resize (int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause () {
		super.pause();
	}

	@Override
	public void resume () {
		super.resume();
	}

	@Override
	public void dispose () {
		super.dispose();
	}
	
	/**
	 * This determines the global application log level.  All loggers should reference
	 * this value for the current log level
	 * 
	 * @return  current log level
	 */
	public static int getAppLogLevel() {
		return Logger.INFO;  // TODO: Should get this from a runtime config property
	}
}


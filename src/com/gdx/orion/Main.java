package com.gdx.orion;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.audio.Music;
import com.gdx.orion.screens.GameStateManager;

public class Main extends Game implements ApplicationListener{
	
	public static boolean console = true;
	public static final float GAME_WORLD_WIDTH = 1080;
	public static final float GAME_WORLD_HEIGHT = 720;
	
	@Override
	public void create() {
		//final Music music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music/1 - Fathers Day - Silent Loser.mp3"));
		
		GameStateManager.initiate(this);
		Gdx.graphics.setContinuousRendering(true);
		Gdx.graphics.setVSync(true);
		Gdx.input.setCursorCatched(false);
		GameStateManager.setScreen(GameStateManager.MENU);
		//music.play();
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
}


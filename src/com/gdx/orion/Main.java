package com.gdx.orion;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.gdx.orion.screens.GameStateManager;
import com.gdx.orion.utils.PlayController;

public class Main extends Game implements ApplicationListener{
	
	public static boolean console = true;
	
	@Override
	public void create() {
		//final Music music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music/1 - Fathers Day - Silent Loser.mp3"));
		
		GameStateManager.initiate(this);
		Gdx.graphics.setContinuousRendering(true);
		Gdx.graphics.setVSync(true);
		Gdx.input.setCursorCatched(false);
		GameStateManager.setScreen(0);
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


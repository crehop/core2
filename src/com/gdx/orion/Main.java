package com.gdx.orion;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.gdx.orion.screens.GameStateManager;
import com.gdx.orion.utils.Console;

public class Main extends Game implements ApplicationListener{
	
	public static boolean console = true;
	
	@Override
	public void create(){
		GameStateManager.initiate(this);
		Gdx.graphics.setContinuousRendering(true);
		Gdx.graphics.setVSync(true);
		Gdx.input.setCursorCatched(false);
		GameStateManager.setScreen(1);
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


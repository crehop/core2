package com.gdx.orion;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.gdx.orion.screens.GameStateManager;
import com.gdx.orion.utils.Console;
import com.gdx.orion.utils.PlayController;

public class Main extends Game implements ApplicationListener{
	
	public static boolean console = true;
	public static PlayController playController = new PlayController();
	
	@Override
	public void create(){
		GameStateManager.initiate(this);
		Gdx.graphics.setContinuousRendering(true);
		Gdx.graphics.setVSync(true);
		Gdx.input.setCursorCatched(false);
		GameStateManager.setScreen(1);
		Gdx.input.setInputProcessor(playController);
	}

	@Override
	public void render () {
		super.render();
		playController.checkInput();
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


package com.gdx.orion.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class GameStateExample extends GameState implements Screen{
	public GameStateExample(int ID){
		//THIS ID NEEDS TO BE SET IN GAME STATE MANAGER (Ex: GameStateManager.MENU) and named after this class (ex:
		//GameStateManager.GAMESTATEEXAMPLE as a FINAL int
		super(ID);
	}

	@Override
	public void render(float delta) {
		if(active){
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);	
		}
	}
	
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}
	
	@Override
	public void show() {
		this.setActive(true);
	}
	
	@Override
	public void hide() {
		this.setActive(false);
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void setActive(boolean active) {
		super.active = active;
	}

	@Override
	public boolean isActive() {
		return super.active;
	}
}

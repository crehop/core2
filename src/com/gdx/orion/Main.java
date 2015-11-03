package com.gdx.orion;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.gdx.orion.camera.Camera;
import com.gdx.orion.gamestates.GameStateManager;

public class Main extends ApplicationAdapter{
	
	private GameStateManager gsm;
	public Camera cam;
	
	public void create() {
		gsm = new GameStateManager();
		cam = new Camera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), null);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Render the game state and draw it
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.draw();
		
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
	}
}


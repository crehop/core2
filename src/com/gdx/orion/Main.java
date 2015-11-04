package com.gdx.orion;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.gdx.orion.gamestates.GameStateManager;
import com.gdx.orion.utils.Console;

public class Main extends Game implements ApplicationListener{
	
	private static GameStateManager gsm;
	private static OrthographicCamera cam;
	public static boolean console = true;
	
	public void create() {
		Main.setCam(new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		gsm = new GameStateManager(this);
		Gdx.graphics.setContinuousRendering(true);
		Gdx.graphics.setVSync(true);
		Gdx.input.setCursorCatched(false );
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Console.setLine1("FPS : " + Gdx.graphics.getFramesPerSecond());
		// Render the game state and draw it
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.draw();
		cam.update();
		
		//ALWAYS LAST
		if(console){
			Console.render();
		}
		
	}
	@Override
	public void resize (int width, int height) {
		Console.resize(width,height);
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
	
	public static OrthographicCamera getCam() {
		return cam;
	}

	public static void setCam(OrthographicCamera cam) {
		Main.cam = cam;
	}
}


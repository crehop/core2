package com.gdx.orion.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.gdx.orion.utils.Console;

public class Play implements Screen {
	public static Game game;
	public static OrthographicCamera cam;
	public static StretchViewport viewport;  
	final float GAME_WORLD_WIDTH = 1080;
	final float GAME_WORLD_HEIGHT = 720;

	protected Play(Game game) {
		super();
		cam = new OrthographicCamera();
		cam.position.set(GAME_WORLD_WIDTH/2,GAME_WORLD_HEIGHT/2,0);
		viewport = new StretchViewport(1080, 720, cam);
		viewport.apply();
		Play.game = game;
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);	
		Console.setLine2("SCREEN:" + Gdx.graphics.getWidth() + "/" + Gdx.graphics.getHeight());
		Console.setLine3("VIEWPORT:"+ cam.viewportWidth + "/" +" pla");
		Console.setLine1("FPS : " + Gdx.graphics.getFramesPerSecond());
		cam.update();
		Console.render(cam);
	}

	@Override
	public void resize(int width, int height) {
		Console.resize(width,height,Play.cam);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	

}

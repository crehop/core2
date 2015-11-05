package com.gdx.orion.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.gdx.orion.utils.Console;

public class Play implements Screen {
	public static Game game;
	public static OrthographicCamera cam;
	public static ScalingViewport viewport;  
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
		Console.setLine3("CAM:"+ cam.viewportWidth + "/" + cam.viewportHeight);
		Console.setLine4("VIEWPORT:"+ viewport.getScreenWidth() + "/" + viewport.getScreenHeight());
		Console.setLine5("CONSOLE:"+ Console.x + "/" + Console.y);
		Console.setLine1("FPS : " + Gdx.graphics.getFramesPerSecond());
		cam.update();
		Console.render(cam);
		GameStateManager.setScreen(GameStateManager.LEVELSELECT);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		viewport.apply();
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

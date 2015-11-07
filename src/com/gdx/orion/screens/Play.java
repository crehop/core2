package com.gdx.orion.screens;

import gdx.orion.entities.Ball;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.gdx.orion.gamevars.Location;
import com.gdx.orion.utils.Console;
import com.gdx.orion.utils.WorldUtils;

public class Play extends GameState implements Screen {
	public Game game;
	public OrthographicCamera cam;
	public ScalingViewport viewport;  
	public final float GAME_WORLD_WIDTH = 1080;
	public final float GAME_WORLD_HEIGHT = 720;
	private Stage stage;
	private World gameWorld;
	private Box2DDebugRenderer renderer = new Box2DDebugRenderer();
	private ShapeRenderer sr = new ShapeRenderer();
	
	protected Play(Game game, int level) {
		super(GameStateManager.PLAY);
		cam = new OrthographicCamera();
		viewport = new ScalingViewport(Scaling.stretch, GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT, cam);
		viewport.apply();
		cam.position.set(0, 0, 0);
		cam.update();
		cam.zoom = 1.8f;
		this.stage = new Stage(viewport);
		this.game = game;
		this.gameWorld = new World(new Vector2(-0.1f,-5000f), true);
		WorldUtils.GenerateWorldBorder(gameWorld, 0, GAME_WORLD_WIDTH, 0, GAME_WORLD_HEIGHT);
		new Ball(gameWorld, new Location(100,120,0));
		new Ball(gameWorld, new Location(120,120,0));
		new Ball(gameWorld, new Location(130,112,0));
		new Ball(gameWorld, new Location(140,140,0));
	}

	@Override
	public void render(float delta) {
		if(isActive()){
			Random random = new Random();
			if(gameWorld.getBodyCount() < 500) {
				new Ball(gameWorld, new Location((int)(random.nextFloat() * GAME_WORLD_WIDTH * .70),140,0));
			}
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);	
			Console.setLine2("SCREEN:" + Gdx.graphics.getWidth() + "/" + Gdx.graphics.getHeight());
			Console.setLine3("CAM:"+ cam.viewportWidth + "/" + cam.viewportHeight);
			Console.setLine4("VIEWPORT:"+ viewport.getScreenWidth() + "/" + viewport.getScreenHeight());
			Console.setLine5("CONSOLE:"+ Console.x + "/" + Console.y);
			Console.setLine1("FPS : " + Gdx.graphics.getFramesPerSecond());
			Console.setLine6("WORLD ENTITIES: " + gameWorld.getBodyCount());
			Console.setLine7("CAMERA LOCATION:" + cam.position.x + "/"+ cam.position.y + "/" + cam.position.z);
			cam.update();
			Console.render(cam);
			renderer.render(gameWorld, viewport.getCamera().combined);
			gameWorld.step(Gdx.graphics.getDeltaTime(), 2, 2);
		}
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		viewport.apply();
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

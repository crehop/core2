package com.gdx.orion.screens;

import gdx.orion.entities.Asteroid;
import gdx.orion.entities.Ball;
import gdx.orion.entities.PlayerShip;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
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
	public OrthographicCamera consoleCam;
	public ScalingViewport viewport;  
	public ScalingViewport consoleViewport;  
	public final float GAME_WORLD_WIDTH = 10800;
	public final float GAME_WORLD_HEIGHT = 7200;
	private Stage stage;
	private World gameWorld;
	private Box2DDebugRenderer renderer = new Box2DDebugRenderer();
	private ShapeRenderer sr = new ShapeRenderer();
	private PlayerShip ship;
	Random rand = new Random();
	
	protected Play(Game game, int level) {
		super(GameStateManager.PLAY);
		cam = new OrthographicCamera();
		consoleCam = new OrthographicCamera();
		viewport = new ScalingViewport(Scaling.stretch, 640, 480, cam);
		consoleViewport = new ScalingViewport(Scaling.stretch, 1280, 720, consoleCam);
		consoleViewport.apply();
		viewport.apply();
		this.stage = new Stage(viewport);
		this.game = game;
		this.setGameWorld(new World(new Vector2(0f,0f), false));
		WorldUtils.GenerateWorldBorder(getGameWorld(), 0, GAME_WORLD_WIDTH, 0, GAME_WORLD_HEIGHT);
		ship = new PlayerShip(getGameWorld(),new Location(140,140,0));
		
		new Asteroid(getGameWorld(), new Location(0,0,0),1,1);
	}

	@Override
	public void render(float delta) {
		if(isActive()){
			cam.position.set(ship.getBody().getWorldCenter(), 0);
			while(getGameWorld().getBodyCount() < 2000) {
				new Asteroid(getGameWorld(), new Location(MathUtils.random(0,10000) ,MathUtils.random(-100,6200), 0),MathUtils.random(0.1f,10f),MathUtils.random(1,3));
			}
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);	
			Console.setLine2("SCREEN:" + Gdx.graphics.getWidth() + "/" + Gdx.graphics.getHeight());
			Console.setLine3("CAM:"+ cam.viewportWidth + "/" + cam.viewportHeight);
			Console.setLine4("VIEWPORT:"+ viewport.getScreenWidth() + "/" + viewport.getScreenHeight());
			Console.setLine5("CONSOLE:"+ Console.x + "/" + Console.y);
			Console.setLine1("FPS : " + Gdx.graphics.getFramesPerSecond());
			Console.setLine6("WORLD ENTITIES: " + getGameWorld().getBodyCount());
			Console.setLine7("CAMERA LOCATION:" + cam.position.x + "/"+ cam.position.y + "/" + cam.position.z);
			cam.update();
			renderer.render(getGameWorld(), viewport.getCamera().combined);
			getGameWorld().step(Gdx.graphics.getDeltaTime(), 8, 3);
			//MUST BE LAST
			Console.render(consoleCam);
			consoleCam.update();
		}
	}

	@Override
	public void resize(int width, int height) {
		consoleViewport.update(width, height);
		consoleViewport.apply();
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

	public PlayerShip getPlayerShip() {
		return this.ship;
	}

	public World getGameWorld() {
		return gameWorld;
	}

	public void setGameWorld(World gameWorld) {
		this.gameWorld = gameWorld;
	}
}

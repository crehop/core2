package com.gdx.orion.screens;

import gdx.orion.entities.Asteroid;
import gdx.orion.entities.PlayerShip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.gdx.orion.gamevars.Location;
import com.gdx.orion.utils.Console;
import com.gdx.orion.utils.PlayController;
import com.gdx.orion.utils.WorldUtils;

public class Play extends GameState implements Screen, ContactListener {
	public String test;
	public Game game;
	public OrthographicCamera cam;
	public OrthographicCamera consoleCam;
	public ScalingViewport viewport;  
	public ScalingViewport consoleViewport;  
	public final float GAME_WORLD_WIDTH = 1080000;
	public final float GAME_WORLD_HEIGHT = 7200000;
	private static PlayController playController = new PlayController();
	private Stage stage;
	private World gameWorld;
	private Box2DDebugRenderer renderer = new Box2DDebugRenderer();
	private ShapeRenderer sr = new ShapeRenderer();
	private PlayerShip ship;
	Random rand = new Random();
	private ArrayList<Asteroid> asteroids= new ArrayList<Asteroid>();
	public HashMap<String,Asteroid> asteroidMap = new HashMap<String,Asteroid>();
	
	
	protected Play (Game game, int level) {
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
		this.gameWorld.setContactListener(this);
		ship = new PlayerShip(getGameWorld(),new Location(140,140,0));
		ship.getBody().setAngularDamping(2.00f);
		
		Asteroid roid = new Asteroid(getGameWorld(), new Location(0,0,0),1,1);
		asteroidMap.put("" + roid.getBody().getUserData(), roid);	}
	
	@Override
	public void render(float delta) {
		if(isActive()){
			cam.position.set(ship.getBody().getWorldCenter(), 0);
			Gdx.input.setInputProcessor(playController);
			playController.checkInput();
			while(getGameWorld().getBodyCount() < 500) {
				Asteroid roid = new Asteroid(getGameWorld(), new Location(MathUtils.random(0,10000) ,MathUtils.random(-100,6200), 0),MathUtils.random(0.1f,10f),MathUtils.random(1,3));
				asteroidMap.put("" + roid.getBody().getUserData(), roid);
			}
			for(Asteroid asteroid: asteroids){
				asteroid.fragment(10);
			}
			asteroids.clear();
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);	
			Console.setLine2("SCREEN:" + Gdx.graphics.getWidth() + "/" + Gdx.graphics.getHeight());
			Console.setLine3("CAM:"+ cam.viewportWidth + "/" + cam.viewportHeight);
			Console.setLine4("VIEWPORT:"+ viewport.getScreenWidth() + "/" + viewport.getScreenHeight());
			Console.setLine5("CONSOLE:"+ Console.x + "/" + Console.y);
			Console.setLine1("FPS : " + Gdx.graphics.getFramesPerSecond());
			Console.setLine6("WORLD ENTITIES: " + getGameWorld().getBodyCount());
			Console.setLine7("CAMERA LOCATION:" + cam.position.x + "/"+ cam.position.y + "/" + cam.position.z);
			Console.setLine11("SHIP ANGLE:" + ship.getBody().getWorldVector(Vector2.Y).angle());
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

	@Override
	public void beginContact(Contact contact) {
		if(contact.getFixtureA().getBody().getUserData() != null && contact.getFixtureB().getBody().getUserData() == null){
			test =  "" + contact.getFixtureA().getBody().getUserData();
			if(asteroidMap.containsKey(test)){
					asteroids.add(asteroidMap.get(test));
					asteroidMap.remove(test);
				}
			test =  "" + contact.getFixtureB().getBody().getUserData();
			if(asteroidMap.containsKey(test)){
				asteroids.add(asteroidMap.get(test));
				asteroidMap.remove(test);
			}
		}
		if(contact.getFixtureB().getBody().getUserData() != null && contact.getFixtureA().getBody().getUserData() == null){
			test =  "" + contact.getFixtureA().getBody().getUserData();
			if(asteroidMap.containsKey(test)){
					asteroids.add(asteroidMap.get(test));
					asteroidMap.remove(test);
				}
			test =  "" + contact.getFixtureB().getBody().getUserData();
			if(asteroidMap.containsKey(test)){
				asteroids.add(asteroidMap.get(test));
				asteroidMap.remove(test);
			}
		}
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}
}

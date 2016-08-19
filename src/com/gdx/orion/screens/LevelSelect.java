package com.gdx.orion.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.gdx.orion.entities.PlayerShip;

import java.util.Random;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.gdx.orion.Main;
import com.gdx.orion.handlers.ContactHandler;
import com.gdx.orion.handlers.ControlHandler;
import com.gdx.orion.utils.Console;
import com.gdx.orion.utils.EffectUtils;
import com.gdx.orion.utils.WorldUtils;

public class LevelSelect extends GameState implements Screen{
	public LevelSelect(int ID) {
		super(ID);
		// TODO Auto-generated constructor stub
	}
	public Game game;
	public OrthographicCamera cam;
	public OrthographicCamera mapCam;
	public OrthographicCamera consoleCam;
	public ScalingViewport viewport;  
	public ScalingViewport consoleViewport;  
	public ScalingViewport mapViewport;  
	private static ControlHandler playController = new ControlHandler();
	private World gameWorld;
	private Box2DDebugRenderer renderer = new Box2DDebugRenderer();
	private PlayerShip ship;
	@SuppressWarnings("unused")
	private Stage stage;
	Random rand = new Random();
	private SpriteBatch batch = new SpriteBatch();
	
    private Texture texture = new Texture(Gdx.files.internal("images/stars.png"));
    private Sprite stars = new Sprite(texture);
   
    private Vector2 position = new Vector2(0,0);
    private Vector2 force = new Vector2(-50,0);
    
	double newTime;
    double frameTime;
    private double accumulator = 1.0f / 60.0f;;
    private double currentTime;
    private float step = 1.0f / 60.0f;
    float deltaTime = (float)frameTime;
	public int aliveTime = 0;
	
	private float[] shapeVerts;
	FixtureDef fdef = new FixtureDef();
	private Body body;


	public final int MAX_BODIES = 2500;
	public final int FRAGMENT_CULL_PER_FRAME = 10;
	public final int MAX_FRAGMENT_SIZE = 3;
	public final int MAX_ALIVE_TIME = 30;
	public final int VIEW_DISTANCE = 1500;


	
	protected LevelSelect (Game game, int level) {
		super(GameStateManager.PLAY);
		stars.scale(10f);
		stars.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
		cam = new OrthographicCamera();
		consoleCam = new OrthographicCamera();
		mapCam = new OrthographicCamera();
		mapCam.translate(-Main.GAME_WORLD_WIDTH,-Main.GAME_WORLD_HEIGHT);
		viewport = new ScalingViewport(Scaling.stretch, 60, 40,cam);
		consoleViewport = new ScalingViewport(Scaling.stretch, 1280, 720, consoleCam);
		mapViewport = new ScalingViewport(Scaling.stretch, Main.GAME_WORLD_WIDTH * 4, Main.GAME_WORLD_HEIGHT * 4, mapCam);
		mapViewport.apply();
		consoleViewport.apply();
		viewport.apply();
		this.stage = new Stage(viewport);
		this.game = game;
		this.setGameWorld(new World(new Vector2(10f,-10.9f), false));
		this.getGameWorld().setVelocityThreshold(1.99f);
		WorldUtils.GenerateWorldBorder(getGameWorld(), 0, 25, 0, 25);
		this.gameWorld.setContactListener(new ContactHandler());
		cam.zoom = 2.0f;
		createShape();
	}

	@Override
	public void render(float delta) {
		if(isActive()) {
			playController.checkInput();
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			batch.setProjectionMatrix(cam.combined);
			batch.begin();
			batch.draw(stars, 0 , 0, Main.GAME_WORLD_WIDTH, Main.GAME_WORLD_HEIGHT);
			batch.end();
			
			
			if(WorldUtils.isWireframe()){
				renderer.render(getGameWorld(), viewport.getCamera().combined);
			}
			
			Console.setLine1("FPS : " + Gdx.graphics.getFramesPerSecond());
			Console.setLine2("WORLD ENTITIES: " + getGameWorld().getBodyCount());
			Console.setLine4("WORLD MAX VELOCITY: " + this.getGameWorld().getVelocityThreshold());

			cam.update();
			
			
			//TIMESTEP AND WORLD UPDATE INFORMATION
			newTime = TimeUtils.millis() / 1000.0;
		    currentTime = newTime;
		    frameTime = Math.min(newTime - currentTime, 1/120);
		    deltaTime = (float)frameTime;
			accumulator += delta;
		    while (accumulator >= step) {
		        gameWorld.step(step, 1, 1);
		        EffectUtils.updateEffects(step);
		        accumulator -= step;
		    }
		    ///////////////////////////////////////
		    
		    
			//MUST BE LAST
			renderer.render(gameWorld, mapCam.combined);              
			Console.render(consoleCam);
			cam.position.set(0,0,0);
			consoleCam.update();
			//////////////
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
		Gdx.input.setInputProcessor(playController);
		this.setActive(true);
	}
	
	@Override
	public void hide() {
		this.setActive(false);
	}

	@Override
	public void dispose() {
		gameWorld.dispose();
		texture.dispose();
		renderer.dispose();
	}

	@Override
	public void setActive(boolean active) {
		super.active = active;
	}

	@Override
	public boolean isActive() {
		return super.active;
	}
	
	
	private void createShape() {
		PolygonShape shape = new PolygonShape();
		BodyDef def = new BodyDef();
		def.position.set(2,22);
		def.type = BodyType.DynamicBody;
		def.angle = 200;
		body = gameWorld.createBody(def);
		shape.set(new float[]{0f,0f,0f,1f,1f,1f,1f,0f} );
		fdef.shape = shape;
		fdef.density = 1.0f;
		fdef.friction = 1;
		body.createFixture(fdef);
		body.setAngularVelocity(MathUtils.random(-4f,4f));
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
	public int getAliveTime() {
		return aliveTime;
	}
	public boolean isOnScreen(Vector2 point, Vector2 check){
		if(check.x - point.x < VIEW_DISTANCE && check.y - point.y < VIEW_DISTANCE && point.x - check.x < VIEW_DISTANCE && point.y - check.y < VIEW_DISTANCE){
			return true;
		}
		return false;
	}
	public SpriteBatch getBatch(){
		return batch;
	}
}

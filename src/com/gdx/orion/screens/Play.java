package com.gdx.orion.screens;

import gdx.orion.entities.Asteroid;
import gdx.orion.entities.EntityData;
import gdx.orion.entities.EntityType;
import gdx.orion.entities.PlayerShip;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.gdx.orion.systems.Rope;
import com.gdx.orion.utils.BodyHandler;
import com.gdx.orion.utils.Console;
import com.gdx.orion.utils.ContactHandler;
import com.gdx.orion.utils.EffectUtils;
import com.gdx.orion.utils.GravityUtils;
import com.gdx.orion.utils.PlayController;
import com.gdx.orion.utils.WorldUtils;

public class Play extends GameState implements Screen{
	public Game game;
	public OrthographicCamera cam;
	public OrthographicCamera mapCam;
	public OrthographicCamera consoleCam;
	public ScalingViewport viewport;  
	public ScalingViewport consoleViewport;  
	public ScalingViewport mapViewport;  
	public final float GAME_WORLD_WIDTH = 1080;
	public final float GAME_WORLD_HEIGHT = 720;
	private static EntityData entityDataA;
	private static PlayController playController = new PlayController();
	private World gameWorld;
	private Box2DDebugRenderer renderer = new Box2DDebugRenderer();
	private PlayerShip ship;
	@SuppressWarnings("unused")
	private Stage stage;
	Random rand = new Random();
	public Array<Body> destroy = new Array<Body>();
	private Array<Body> bodies = new Array<Body>();
	private ArrayList<Asteroid> asteroids= new ArrayList<Asteroid>();
	private SpriteBatch batch = new SpriteBatch();
    private Texture texture = new Texture(Gdx.files.internal("images/stars.png"));
    private Texture texture2 = new Texture(Gdx.files.internal("images/planets/Jupiter.png"));
    private Sprite sprite = new Sprite(texture);
    private Sprite sprite2 = new Sprite(texture2);
    private String fragmentShader;
    private String vertexShader;
    private Vector2 position = new Vector2(0,0);
	public final int MAX_BODIES = 2500;
	public final int FRAGMENT_CULL_PER_FRAME = 10;
	int fragmentsCulled = 0;
	int maxAliveTime = 30;
	public int MAX_FRAGMENT_SIZE = 3;
	public int aliveTime = 0;
	private ShaderProgram shader;
	private final int VIEW_DISTANCE = 300;
	//TODO, create an int[maxAliveTime] and put objects in and pass those objects to bullets so multiple int objects arent constantly created
	//TODO, COMMENT CODE
	//TODO, FIX CRASH WITH GRAVITY WELL, most likely due to static body and bullets.
	double newTime;
    double frameTime;
    private double accumulator = 1.0f / 60.0f;
    private double currentTime;
    private float step = 1.0f / 60.0f;
    float deltaTime = (float)frameTime;
	public Array<JointDef> addJoint = new Array<JointDef>();
	public Array<Joint> clearJoint = new Array<Joint>();
	

	
	protected Play (Game game, int level) {
		super(GameStateManager.PLAY);
		sprite.scale(10f);
		sprite.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
		cam = new OrthographicCamera();
		consoleCam = new OrthographicCamera();
		mapCam = new OrthographicCamera();
		mapCam.translate(-GAME_WORLD_WIDTH,-GAME_WORLD_HEIGHT);
		viewport = new ScalingViewport(Scaling.stretch, 60, 40,cam);
		consoleViewport = new ScalingViewport(Scaling.stretch, 1280, 720, consoleCam);
		mapViewport = new ScalingViewport(Scaling.stretch, GAME_WORLD_WIDTH * 4, GAME_WORLD_HEIGHT * 4, mapCam);
		mapViewport.apply();
		consoleViewport.apply();
		viewport.apply();
		this.stage = new Stage(viewport);
		this.game = game;
		this.setGameWorld(new World(new Vector2(0f,0f), false));
		WorldUtils.GenerateWorldBorder(getGameWorld(), 0, GAME_WORLD_WIDTH, 0, GAME_WORLD_HEIGHT);
		this.gameWorld.setContactListener(new ContactHandler());
		ship = new PlayerShip(getGameWorld(),new Vector2(140,140));
		cam.zoom = 2.0f;
		while(getGameWorld().getBodyCount() < 50) {
			position.set(MathUtils.random(0,GAME_WORLD_WIDTH) ,MathUtils.random(0,GAME_WORLD_HEIGHT));
			new Asteroid(getGameWorld(), position,MathUtils.random(5,500),MathUtils.random(1,3));
		}
		GravityUtils.addGravityWell(GAME_WORLD_WIDTH/2, GAME_WORLD_HEIGHT/2, 40.03f,66, gameWorld, true, sprite2);
        vertexShader = Gdx.files.internal("shaders/vertex/asteroid.vsh").readString();
        fragmentShader = Gdx.files.internal("shaders/fragment/asteroid.fsh").readString();
		shader = new ShaderProgram(vertexShader, fragmentShader);
		if (!shader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + shader.getLog());
		EffectUtils.initilize();
	}
	
	@Override
	public void render(float delta) {
		accumulator += delta;
		if(isActive()) {
			aliveTime++;
			if(aliveTime > maxAliveTime){
				aliveTime = 0;
			}
			gameWorld.getBodies(bodies);
			cam.position.set(ship.getBody().getWorldCenter(), 0);
			mapCam.position.set(ship.getBody().getWorldCenter(), 0);
			Gdx.input.setInputProcessor(playController);
			playController.checkInput();
			asteroids.clear();
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			batch.setProjectionMatrix(cam.combined);
			batch.begin();
			batch.draw(sprite, 0 , 0, GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT);
			batch.end();
			EffectUtils.drawGrid(cam);
			batch.begin();
			GravityUtils.renderWells(batch);
			ship.draw(batch,false,cam.position);
			for(Body body:bodies){
				//GravityUtils.applyGravity(gameWorld,body);
				if(body.getUserData() instanceof EntityData){
				entityDataA = (EntityData)body.getUserData();
					if(entityDataA.getType() == EntityType.DESTROYME){
						((Asteroid)entityDataA.getObject()).fragment(10);
					}
					if(entityDataA.getType() == EntityType.DELETEME){
						gameWorld.destroyBody(body);
					}
					if(entityDataA.getType() == EntityType.ASTEROID){
					}
				}
			}
			for(JointDef def:addJoint){
				gameWorld.createJoint(def);
			}
			addJoint.clear();
			for(Joint joint:clearJoint){
				gameWorld.destroyJoint(joint);
			}
			clearJoint.clear();
			batch.setProjectionMatrix(mapCam.combined);
			GravityUtils.renderWells(batch);
			ship.draw(batch, true,mapCam.position);
			batch.end();
			BodyHandler.update(cam, gameWorld, bodies);
			for(Body body:destroy){
				gameWorld.destroyBody(body);    
			}
			destroy.clear();

			if(WorldUtils.isWireframe()){
				renderer.render(getGameWorld(), viewport.getCamera().combined);
			}
			Console.setLine1("FPS : " + Gdx.graphics.getFramesPerSecond());
			Console.setLine2("WORLD ENTITIES: " + getGameWorld().getBodyCount());
			Console.setLine3("CAMERA LOCATION:" + cam.position.x + "/"+ cam.position.y + "/" + cam.position.z);
			Console.setLine11("PRESS F1 TO TOGGLE WIREFRAME:" + WorldUtils.isWireframe());

			cam.update();
			newTime = TimeUtils.millis() / 1000.0;
		    currentTime = newTime;
		    frameTime = Math.min(newTime - currentTime, 0.25);
		    deltaTime = (float)frameTime;
		    while (accumulator >= step) {
		        gameWorld.step(step, 8, 6);
		        EffectUtils.updateEffects(step);
		        accumulator -= step;
		    }
			//MUST BE LAST
			renderer.render(gameWorld, mapCam.combined);              
			Console.render(consoleCam);
			cam.position.set(ship.getBody().getWorldCenter(), 0);
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
}

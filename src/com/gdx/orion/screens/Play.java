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
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.gdx.orion.gamevars.Location;
import com.gdx.orion.utils.Console;
import com.gdx.orion.utils.PlayController;
import com.gdx.orion.utils.WorldUtils;

public class Play extends GameState implements Screen, ContactListener {
	public EntityData entityDataA;
	public EntityData entityDataB;
	public Game game;
	public OrthographicCamera cam;
	public OrthographicCamera consoleCam;
	public ScalingViewport viewport;  
	public ScalingViewport consoleViewport;  
	public final float GAME_WORLD_WIDTH = 1080;
	public final float GAME_WORLD_HEIGHT = 720;
	private float[] tempAsteroid = new float[48];
	private static PlayController playController = new PlayController();
	private World gameWorld;
	private Box2DDebugRenderer renderer = new Box2DDebugRenderer();
	private ShapeRenderer sr = new ShapeRenderer();
	private PlayerShip ship;
	private Stage stage;
	Random rand = new Random();
	private Array<Body> bodies = new Array<Body>();
	private Array<Body> destroy = new Array<Body>();
	private ArrayList<Asteroid> asteroids= new ArrayList<Asteroid>();
	private SpriteBatch batch = new SpriteBatch();
    private Texture texture = new Texture(Gdx.files.internal("images/stars.png"));
    private Texture texture2 = new Texture(Gdx.files.internal("images/images.png"));
    private Sprite sprite = new Sprite(texture);
    private Mesh mesh;
    private String fragmentShader;
    private String vertexShader;
	int count = 0;
	int maxAliveTime = 30;
	int aliveTime = 0;
	private ShaderProgram shader;
	private ImmediateModeRenderer20 r = new ImmediateModeRenderer20(false, true, 0);
	//TODO, create an int[maxAliveTime] and put objects in and pass those objects to bullets so multiple int objects arent constantly created
	
	protected Play (Game game, int level) {
		super(GameStateManager.PLAY);
		sprite.scale(10f);
		sprite.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
		cam = new OrthographicCamera();
		consoleCam = new OrthographicCamera();
		viewport = new ScalingViewport(Scaling.stretch, 60, 40, cam);
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
		World.setVelocityThreshold(12.0f);
		cam.zoom = 2.0f;
		while(getGameWorld().getBodyCount() < 500) {
			new Asteroid(getGameWorld(), new Location(MathUtils.random(-200,200) ,MathUtils.random(-100,400), 0),MathUtils.random(700,1000),MathUtils.random(1,3));
		}
        vertexShader = Gdx.files.internal("shaders/vertex/asteroid.vsh").readString();
        fragmentShader = Gdx.files.internal("shaders/fragment/asteroid.fsh").readString();
		shader = new ShaderProgram(vertexShader, fragmentShader);
		System.out.println(shader.isCompiled());
		if (!shader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + shader.getLog());
	}
	
	@Override
	public void render(float delta) {
		if(isActive()) {
			aliveTime++;
			System.out.println(aliveTime);
			if(aliveTime > maxAliveTime){
				aliveTime = 0;
			}
			gameWorld.getBodies(bodies);
			cam.position.set(ship.getBody().getWorldCenter(), 0);
			Gdx.input.setInputProcessor(playController);
			playController.checkInput();
			asteroids.clear();
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			batch.setProjectionMatrix(cam.combined);
			batch.begin();
			batch.draw(sprite, -200, -200, GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT);
			for(Body body:bodies){
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
			batch.end();
			for(Body body:bodies){
				if(body.getUserData() instanceof EntityData){
					count = 0;
					entityDataA = (EntityData)body.getUserData();
					tempAsteroid = WorldUtils.getRenderData(body);
					if(entityDataA.getType() == EntityType.ASTEROID){
						((Asteroid)entityDataA.getObject()).draw(r,cam);
					}
				}
				if(body.getUserData() instanceof Integer){
					count = (Integer)body.getUserData();
					if(count == aliveTime){
						destroy.add(body);
					}
				}
			}
			for(Body body:destroy){
				gameWorld.destroyBody(body);
			}
			destroy.clear();

			renderer.render(getGameWorld(), viewport.getCamera().combined);
			Console.setLine1("FPS : " + Gdx.graphics.getFramesPerSecond());
			Console.setLine2("WORLD ENTITIES: " + getGameWorld().getBodyCount());
			Console.setLine3("CAMERA LOCATION:" + cam.position.x + "/"+ cam.position.y + "/" + cam.position.z);
			cam.update();
			getGameWorld().step(Gdx.graphics.getDeltaTime(), 2, 2);			
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

	@Override
	public void beginContact(Contact contact) {
		entityDataA = null;
		entityDataB = null;
		if(contact.getFixtureA().getBody().getUserData() instanceof EntityData){
			entityDataA = (EntityData)contact.getFixtureA().getBody().getUserData();
		}
		if(contact.getFixtureB().getBody().getUserData() instanceof EntityData){
			entityDataB = (EntityData)contact.getFixtureB().getBody().getUserData();
		}
		if(entityDataA != null){
			if(entityDataA.getType() == EntityType.ASTEROID && contact.getFixtureB().getBody().getUserData() instanceof Integer){
				entityDataA.damage(1);
				if(entityDataA.getLife() < 1){
					entityDataA.setType(EntityType.DESTROYME);
					}
			}
			if(entityDataA.getType() == EntityType.FRAGMENT && contact.getFixtureB().getBody().getUserData() instanceof Integer){
				entityDataA.damage(1);
				if(entityDataA.getLife() < 1){
					entityDataA.setType(EntityType.DELETEME);
				}
			}

		}		
		if(entityDataB != null){
			if(entityDataB.getType() == EntityType.ASTEROID && contact.getFixtureA().getBody().getUserData() instanceof Integer){
				entityDataB.damage(1);
				if(entityDataB.getLife() < 1){
					entityDataB.setType(EntityType.DESTROYME);
				}
			}
			
			if(entityDataB.getType() == EntityType.FRAGMENT && contact.getFixtureA().getBody().getUserData() instanceof Integer){
				entityDataB.damage(1);
				if(entityDataB.getLife()  < 1){
					entityDataB.setType(EntityType.DELETEME);
				}
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
	public int getAliveTime() {
		return aliveTime;
	}
}

package com.gdx.orion.screens;

import gdx.orion.entities.Asteroid;
import gdx.orion.entities.EntityData;
import gdx.orion.entities.EntityType;
import gdx.orion.entities.Fragment;
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
	private static PlayController playController = new PlayController();
	private World gameWorld;
	private Box2DDebugRenderer renderer = new Box2DDebugRenderer();
	private ShapeRenderer sr = new ShapeRenderer();
	private PlayerShip ship;
	private Stage stage;
	Random rand = new Random();
	private Array<Body> bodies = new Array<Body>();
	private ArrayList<Asteroid> asteroids= new ArrayList<Asteroid>();
	public ArrayList<Fragment> frags = new ArrayList<Fragment>();
	private SpriteBatch batch = new SpriteBatch();
    private Texture texture = new Texture(Gdx.files.internal("images/stars.png"));
    private Texture texture2 = new Texture(Gdx.files.internal("images/images.png"));
    private Sprite sprite = new Sprite(texture);
	int count = 0;
	private float[] verts = new float[16];
	
	
    
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
	}
	
	@Override
	public void render(float delta) {
		if(isActive()) {
			cam.position.set(ship.getBody().getWorldCenter(), 0);
			Gdx.input.setInputProcessor(playController);
			playController.checkInput();
			while(getGameWorld().getBodyCount() < 500) {
				new Asteroid(getGameWorld(), new Location(MathUtils.random(-200,200) ,MathUtils.random(-100,400), 0),MathUtils.random(700,1000),MathUtils.random(1,3));
			}
			for(Asteroid asteroid: asteroids){
				if(asteroid.getBody().getUserData() instanceof Vector2)asteroid.fragment(10);
			}
			asteroids.clear();
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			batch.setProjectionMatrix(cam.combined);
			batch.begin();
			batch.draw(sprite, -200, -200, GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT);
			batch.end();
			stage.act();
			stage.draw();
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
			gameWorld.getBodies(bodies);
			batch.begin();
			for(Body body:bodies){
				if(body.getUserData() instanceof EntityData){
					entityDataA = (EntityData)body.getUserData();
					if(entityDataA.getType() == EntityType.DESTROYME){
						((Asteroid)entityDataA.getObject()).fragment(10);
					}
					if(entityDataA.getType() == EntityType.DELETEME){
						gameWorld.destroyBody(body);
					}
					if(entityDataA.getType() == EntityType.BULLET ){
						entityDataA.alive();
						if(entityDataA.getAliveTime() > 50){
							gameWorld.destroyBody(body);
						}
					}
					if(entityDataA.getType() == EntityType.ASTEROID){
						//batch.draw(texture2, (WorldUtils.moveVerts(((Asteroid)entityDataA.getObject()).getVerts(),body)), 0, 16);						
					}
				}
			}
			batch.end();
			
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
		if(contact.getFixtureA().getBody().getUserData() != null){
			entityDataA = (EntityData)contact.getFixtureA().getBody().getUserData();
		}
		if(contact.getFixtureB().getBody().getUserData() != null){
			entityDataB = (EntityData)contact.getFixtureB().getBody().getUserData();
		}
		if(entityDataA != null){
			if(entityDataA.getType() == EntityType.ASTEROID && entityDataB == null){
				entityDataA.damage(1);
				if(entityDataA.getLife() < 1){
					entityDataA.setType(EntityType.DESTROYME);
					}
			}
			if(entityDataA.getType() == EntityType.FRAGMENT && entityDataB == null){
				entityDataA.damage(1);
				if(entityDataA.getLife() < 1){
					entityDataA.setType(EntityType.DELETEME);
				}
			}

		}		
		if(entityDataB != null){
			if(entityDataB.getType() == EntityType.ASTEROID && entityDataA == null){
				entityDataB.damage(1);
				if(entityDataB.getLife() < 1){
					entityDataB.setType(EntityType.DESTROYME);
				}
			}
			
			if(entityDataB.getType() == EntityType.FRAGMENT && entityDataA == null){
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
}

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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.gdx.orion.gamevars.Location;
import com.gdx.orion.utils.Console;
import com.gdx.orion.utils.GravityUtils;
import com.gdx.orion.utils.Map;
import com.gdx.orion.utils.PlayController;
import com.gdx.orion.utils.WorldUtils;

public class Play extends GameState implements Screen, ContactListener {
	public EntityData entityDataA;
	public EntityData entityDataB;
	public Game game;
	public OrthographicCamera cam;
	public OrthographicCamera mapCam;
	public OrthographicCamera consoleCam;
	public ScalingViewport viewport;  
	public ScalingViewport consoleViewport;  
	public ScalingViewport mapViewport;  
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
    private Texture texture2 = new Texture(Gdx.files.internal("images/ship.png"));
    private Sprite sprite = new Sprite(texture);
    private Sprite shipSprite = new Sprite(texture2);
    private Mesh mesh;
    private Vector2 tempV2 = new Vector2(0,0);
    private Vector2 midPoint = new Vector2(0,0);
    private Vector2 midPoint2 = new Vector2(0,0);
    private Vector2 midPoint3 = new Vector2(0,0);
    private String fragmentShader;
    private String vertexShader;
    private PolygonShape ps;
    private Location location = new Location(0,0,0);
	int count = 0;
	final int MAX_BODIES = 2500;
	final int FRAGMENT_CULL_PER_FRAME = 10;
	int fragmentsCulled = 0;
	int maxAliveTime = 30;
	int MAX_FRAGMENT_SIZE = 3;
	int aliveTime = 0;
	private ShaderProgram shader;
	private final int VIEW_DISTANCE = 150;
	private float velo = 0;
	private ImmediateModeRenderer20 r = new ImmediateModeRenderer20(false, true, 0);
	//TODO, create an int[maxAliveTime] and put objects in and pass those objects to bullets so multiple int objects arent constantly created
	
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
		this.gameWorld.setContactListener(this);
		ship = new PlayerShip(getGameWorld(),new Location(140,140,0));
		cam.zoom = 2.0f;
		while(getGameWorld().getBodyCount() < 50) {
			location.set(MathUtils.random(0,GAME_WORLD_WIDTH) ,MathUtils.random(0,GAME_WORLD_HEIGHT), 0);
			new Asteroid(getGameWorld(), location,MathUtils.random(5,500),MathUtils.random(1,3));
		}
		GravityUtils.addGravityWell(GAME_WORLD_WIDTH/2, GAME_WORLD_HEIGHT/2, .03f,66000, gameWorld, true);
        vertexShader = Gdx.files.internal("shaders/vertex/asteroid.vsh").readString();
        fragmentShader = Gdx.files.internal("shaders/fragment/asteroid.fsh").readString();
		shader = new ShaderProgram(vertexShader, fragmentShader);
		if (!shader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + shader.getLog());
	}
	
	@Override
	public void render(float delta) {
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
			shipSprite.setSize(5, 5);
			shipSprite.setOriginCenter();
			shipSprite.setRotation((float)(ship.getBody().getAngle() * 57.2958));
			shipSprite.setCenterX(ship.getBody().getWorldCenter().x);
			shipSprite.setCenterY(ship.getBody().getWorldCenter().y);
			//shipSprite.setRotation(ship.getBody().getAngle());
			shipSprite.draw(batch);	
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
			batch.setProjectionMatrix(mapCam.combined);
			shipSprite.setSize(50,50);
			shipSprite.setOriginCenter();
			shipSprite.setCenterX(ship.getBody().getWorldCenter().x);
			shipSprite.setCenterY(ship.getBody().getWorldCenter().y);
			shipSprite.draw(batch);	
			batch.end();
			fragmentsCulled = 0;
			for(Body body:bodies){
				if(body.getUserData() instanceof EntityData){
					count = 0;
					entityDataA = (EntityData)body.getUserData();
					tempAsteroid = WorldUtils.getRenderData(body);
					if(entityDataA.getType() == EntityType.ASTEROID){
						if(isOnScreen(ship.getBody().getPosition(),body.getPosition())){
							((Asteroid)entityDataA.getObject()).draw(r,cam);
						}
					}
					if(entityDataA.getType() == EntityType.PRE_FRAG){
						ps = (PolygonShape)body.getFixtureList().get(0).getShape();
						ps.getVertex(0, midPoint);
						ps.getVertex(1, tempV2);
						if((int)midPoint.x - (int)tempV2.x > MAX_FRAGMENT_SIZE ||(int)midPoint.y - (int)tempV2.y > MAX_FRAGMENT_SIZE){
							count = 0;
							for(int i = 0; i < ps.getVertexCount(); i++){
								ps.getVertex(i, tempV2);
								tempAsteroid[count++] = tempV2.x;
								tempAsteroid[count++] = tempV2.y;
							}
							midPoint.x = ((tempAsteroid[0] + tempAsteroid[2])/2);
							midPoint.y = ((tempAsteroid[1] + tempAsteroid[3])/2);
							midPoint2.x = ((tempAsteroid[2] + tempAsteroid[4])/2);
							midPoint2.y = ((tempAsteroid[3] + tempAsteroid[5])/2);
							midPoint3.x = ((tempAsteroid[0] + tempAsteroid[4])/2);
							midPoint3.y = ((tempAsteroid[1] + tempAsteroid[5])/2);
							WorldUtils.Fragment(midPoint2.x, midPoint2.y, midPoint.x, midPoint.y, tempAsteroid[2], tempAsteroid[3], gameWorld,body);
							WorldUtils.Fragment(midPoint2.x, midPoint2.y, tempAsteroid[4], tempAsteroid[5], midPoint3.x, midPoint3.y,  gameWorld,body);
							WorldUtils.Fragment(midPoint.x, midPoint.y, midPoint3.x, midPoint3.y, tempAsteroid[0], tempAsteroid[1], gameWorld,body);
							WorldUtils.Fragment(midPoint.x, midPoint.y, midPoint2.x, midPoint2.y, midPoint3.x, midPoint3.y, gameWorld,body);
							
							destroy.add(body);
						}else{
							entityDataA.setType(EntityType.FRAGMENT);
						}
					}
					if(entityDataA.getType() == EntityType.FRAGMENT){
						if(isOnScreen(ship.getBody().getPosition(),body.getPosition())){
							WorldUtils.drawFragment(body,r,cam);
							if(gameWorld.getBodyCount() > MAX_BODIES * 1.5){
								if(fragmentsCulled < FRAGMENT_CULL_PER_FRAME){
									destroy.add(body);
									fragmentsCulled++;
								}
							}
						}else{
							if(gameWorld.getBodyCount() > MAX_BODIES){
								if(fragmentsCulled < FRAGMENT_CULL_PER_FRAME){
									destroy.add(body);
									fragmentsCulled++;
								}
							}
						}
					}
				}
				if(body.getUserData() instanceof Integer){
					count = (Integer)body.getUserData();
					if(count == aliveTime){
						destroy.add(body);
					}
					if(isOnScreen(ship.getBody().getPosition(),body.getPosition())){
						WorldUtils.drawBullet(body,sr,cam,Color.YELLOW);
					}
				}
				GravityUtils.applyGravity(gameWorld,body);
			}
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
			getGameWorld().step(Gdx.graphics.getDeltaTime(), 16, 8);			
			//MUST BE LAST
			Map.render(batch,cam,mapCam,gameWorld,renderer);
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
			if(entityDataA.getType() == EntityType.GRAVITY_WELL){
				if(entityDataB != null){
					if(entityDataB.getType() != EntityType.SHIP && entityDataA.getType() != EntityType.GRAVITY_WELL){
						destroy.add(contact.getFixtureB().getBody());
					}
				}else{
					destroy.add(contact.getFixtureB().getBody());
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
			if(entityDataB.getType() == EntityType.GRAVITY_WELL){
				if(entityDataA != null){
					if(entityDataA.getType() != EntityType.SHIP && entityDataA.getType() != EntityType.GRAVITY_WELL){
						destroy.add(contact.getFixtureA().getBody());
					}
				}else{
					destroy.add(contact.getFixtureA().getBody());
				}
			}
		}
		if(entityDataA != null && entityDataB != null){
			if(entityDataA.getType() == EntityType.ASTEROID && entityDataB.getType() == EntityType.SHIP){
				tempV2 = contact.getFixtureA().getBody().getLinearVelocity();
				midPoint = contact.getFixtureB().getBody().getLinearVelocity();
				velo =  tempV2.sub(midPoint).len();
				if(velo > 110){
					entityDataA.setType(EntityType.DESTROYME);
				}
			}else if(entityDataB.getType() == EntityType.ASTEROID && entityDataA.getType() == EntityType.SHIP){
				tempV2 = contact.getFixtureB().getBody().getLinearVelocity();
				midPoint = contact.getFixtureA().getBody().getLinearVelocity();
				velo = tempV2.sub(midPoint).len();
				Console.setLine6("FORCE OF IMPACT" + velo);
				if(velo > 110){
					entityDataB.setType(EntityType.DESTROYME);
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
	public boolean isOnScreen(Vector2 point, Vector2 check){
		if(check.x - point.x < VIEW_DISTANCE && check.y - point.y < VIEW_DISTANCE && point.x - check.x < VIEW_DISTANCE && point.y - check.y < VIEW_DISTANCE){
			return true;
		}
		return false;
	}
}

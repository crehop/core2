package com.gdx.orion.screens;

import com.gdx.orion.entities.Projectile;
import com.gdx.orion.entities.voxel.VoxelizedPhysicsObject;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.gdx.orion.Main;
import com.gdx.orion.handlers.BodyHandler;
import com.gdx.orion.handlers.ContactHandler;
import com.gdx.orion.handlers.JointHandler;
import com.gdx.orion.handlers.ControlHandler;
import com.gdx.orion.utils.Console;
import com.gdx.orion.utils.EffectUtils;
import com.gdx.orion.utils.GravityUtils;
import com.gdx.orion.utils.WorldUtils;

import finnstr.libgdx.liquidfun.ParticleDebugRenderer;
import finnstr.libgdx.liquidfun.ParticleDef.ParticleType;
import finnstr.libgdx.liquidfun.ParticleGroupDef;
import finnstr.libgdx.liquidfun.ParticleSystem;
import finnstr.libgdx.liquidfun.ParticleSystemDef;

public class Play extends GameState implements Screen{
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
    private ParticleDebugRenderer mParticleDebugRenderer;
    private ParticleSystem mParticleSystem;
    private ParticleGroupDef mParticleGroupDef1;
    private ParticleGroupDef mParticleGroupDef2;

	@SuppressWarnings("unused")
	private Stage stage;
	Random rand = new Random();
	public Array<Body> destroy = new Array<Body>();
	private Array<Body> bodies = new Array<Body>();
	private ArrayList<Projectile> asteroids= new ArrayList<Projectile>();
	private SpriteBatch batch = new SpriteBatch();
	
    private Texture texture = new Texture(Gdx.files.internal("images/stars.png"));
    private Sprite stars = new Sprite(texture);
    
    private String fragmentShader;
    private String vertexShader;
    
    private Vector2 position = new Vector2(0,0);
    private Vector2 force = new Vector2(-50,0);
    
	public final int MAX_BODIES = 2500;
	public final int FRAGMENT_CULL_PER_FRAME = 10;
	public final int MAX_FRAGMENT_SIZE = 3;
	public final int MAX_ALIVE_TIME = 30;
	public final int VIEW_DISTANCE = 1500;


	int fragmentsCulled = 0;
	public int aliveTime = 0;
	private ShaderProgram shader;
	private int count = 0;
	//TODO, create an int[maxAliveTime] and put objects in and pass those objects to bullets so multiple int objects arent constantly created
	//TODO, COMMENT CODE
	double newTime;
    double frameTime;
    private double accumulator = 1.0f / 60.0f;;
    private double currentTime;
    private float step = 1.0f / 60.0f;
    float deltaTime = (float)frameTime;
	public Array<JointDef> addJoint = new Array<JointDef>();
	public Array<Joint> clearJoint = new Array<Joint>();
	

	
	protected Play (Game game, int level) {
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
		this.setGameWorld(new World(new Vector2(0f,-10f), false));
		WorldUtils.GenerateWorldBorder(getGameWorld(), 0, Main.GAME_WORLD_WIDTH, 0, Main.GAME_WORLD_HEIGHT);
		this.gameWorld.setContactListener(new ContactHandler());
		cam.zoom = 2.0f;
		count = 0;
        vertexShader = Gdx.files.internal("shaders/vertex/asteroid.vsh").readString();
        fragmentShader = Gdx.files.internal("shaders/fragment/asteroid.fsh").readString();
		shader = new ShaderProgram(vertexShader, fragmentShader);
		if (!shader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + shader.getLog());
		EffectUtils.initilize();
		 //First we create a new particlesystem and 
        //set the radius of each particle to 6 / 120 m (5 cm)
        ParticleSystemDef systemDef = new ParticleSystemDef();
        systemDef.radius = 0.05f;
        systemDef.dampingStrength = 1.5f;

        mParticleSystem = new ParticleSystem(gameWorld, systemDef);
        mParticleSystem.setParticleDensity(1.3f);
        mParticleDebugRenderer = 
        		new ParticleDebugRenderer(new Color(0, 1, 0, 1), mParticleSystem.getParticleCount());
        
        //Create a new particlegroupdefinition and set some properties
        //For the flags you can set more than only one
        mParticleGroupDef1 = new ParticleGroupDef();
        mParticleGroupDef1.color.set(1f, 0, 0, 1);
        mParticleGroupDef1.flags.add(ParticleType.b2_waterParticle);
        //Create a shape, give it to the definition and
        //create the particlegroup in the particlesystem.
        //This will return you a ParticleGroup instance, but
        //we don't need it here, so we drop that.
        //The shape defines where the particles are created exactly
        //and how much are created
        PolygonShape parShape = new PolygonShape();
        parShape.setAsBox(4,4);
        mParticleGroupDef1.shape = parShape;
        mParticleSystem.createParticleGroup(mParticleGroupDef1);

        //Exactly the same! This is the second group with a different
        //color and shifted on the x-Axis
        mParticleGroupDef2 = new ParticleGroupDef();
        mParticleGroupDef2.shape = mParticleGroupDef1.shape;
        mParticleGroupDef2.flags = mParticleGroupDef1.flags;
        mParticleGroupDef2.groupFlags = mParticleGroupDef1.groupFlags;
        mParticleGroupDef2.color.set(0.2f, 1f, 0.3f, 1);
        mParticleSystem.createParticleGroup(mParticleGroupDef2);

        //Here we create a new shape and we set a
        //linear velocity. This is used in createParticles1()
        //and createParticles2()
        CircleShape partShape = new CircleShape();
        partShape.setRadius(0.02f);

        mParticleGroupDef1.shape = partShape;
        mParticleGroupDef2.shape = partShape;
        createParticles1(60,60);
        createParticles2(60,60);
        mParticleGroupDef1.linearVelocity.set(new Vector2(0, 0f));
        mParticleGroupDef2.linearVelocity.set(new Vector2(0, 0f));
        cam.combined.scale(120, 120, 1);
	}
	
	@Override
	public void render(float delta) {
		if(isActive()) {
			aliveTime++;
			if(aliveTime > MAX_ALIVE_TIME){
				aliveTime = 0;
			}
			gameWorld.getBodies(bodies);
			mapCam.position.set(0, 0, 0);
			playController.checkInput(gameWorld,cam);
			asteroids.clear();
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			batch.setProjectionMatrix(cam.combined);
			batch.begin();
			batch.draw(stars, 0 , 0, Main.GAME_WORLD_WIDTH, Main.GAME_WORLD_HEIGHT);
			batch.end();
			EffectUtils.drawGrid(cam);
			BodyHandler.update(cam, gameWorld, bodies);
			batch.begin();
			GravityUtils.renderWells(batch);
			BodyHandler.handleStates(gameWorld,bodies);
			gameWorld.getJoints(clearJoint);
			JointHandler.handleJoints(gameWorld,addJoint,clearJoint);
			addJoint.clear();
			clearJoint.clear();
			batch.setProjectionMatrix(mapCam.combined);
			GravityUtils.renderWells(batch);
			batch.setProjectionMatrix(cam.combined);
			BodyHandler.applyEffects(batch);
			BodyHandler.destroyBodies(gameWorld,destroy);
			batch.end();
			destroy.clear();
			
			
			if(WorldUtils.isWireframe()){
				renderer.render(getGameWorld(), viewport.getCamera().combined);
				mParticleDebugRenderer.render(mParticleSystem, 1f, viewport.getCamera().combined);
			}
			Console.setLine1("FPS : " + Gdx.graphics.getFramesPerSecond());
			Console.setLine2("WORLD PARTICLES: " + mParticleSystem.getParticleCount());

			cam.update();
			//FIXED DEPENDENCIES
			
			
			//TIMESTEP AND WORLD UPDATE INFORMATION
			newTime = TimeUtils.millis() / 1000.0;
		    currentTime = newTime;
		    frameTime = Math.min(newTime - currentTime, 1/120);
		    deltaTime = (float)frameTime;
			accumulator += delta;
		    while (accumulator >= step) {
		        gameWorld.step(step, 3,3,3);
		        EffectUtils.updateEffects(step);
		        accumulator -= step;
		    }
		    ///////////////////////////////////////
		    
		    
			//MUST BE LAST
			renderer.render(gameWorld, mapCam.combined);              
			Console.render(consoleCam);
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
	
    private void updateParticleCount() {
        if(mParticleSystem.getParticleCount() > mParticleDebugRenderer.getMaxParticleNumber()) {
            mParticleDebugRenderer.setMaxParticleNumber(mParticleSystem.getParticleCount() + 1000);
        }
    }
    
    public void createParticles1(float pX, float pY) {
        mParticleGroupDef1.position.set(pX, pY);
        mParticleSystem.createParticleGroup(mParticleGroupDef1);
        updateParticleCount();
    }

    private void createParticles2(float pX, float pY) {
        mParticleGroupDef2.position.set(pX, pY);
        mParticleSystem.createParticleGroup(mParticleGroupDef2);
        updateParticleCount();
    }
}

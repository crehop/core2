package com.gdx.orion.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.graphics.ParticleEmitterBox2D;
import com.badlogic.gdx.utils.Array;
import com.gdx.orion.Main;
import com.gdx.orion.screens.GameStateManager;

public class EffectUtils {
	static ParticleEffect thrust = new ParticleEffect();
	static ParticleEffect rotationThrusters = new ParticleEffect();
	static ParticleEffect cometTrail = new ParticleEffect();
	static ParticleEffect cometFragmentTrail = new ParticleEffect();
	static ParticleEffect muzzleFlash = new ParticleEffect();
	static ParticleEffect shieldMain = new ParticleEffect();
	static ParticleEffect bullet = new ParticleEffect();
	static SpriteBatch spriteBatch = new SpriteBatch();
	static ImmediateModeRenderer20 lineRenderer = new ImmediateModeRenderer20(false, true, 0);
	static float spacingH = 0;
	static float spacingW = 0;
	static Array<ParticleEffect> particles = new Array<ParticleEffect>();
	static Array<ParticleEmitter> emitters;
	static ScaledNumericValue val;
	static ParticleEmitterBox2D b2DEmitter;
	static float h1;
	static float h2;
	static float angle;
	public static void initilize(){
		thrust.load(Gdx.files.internal("emitters/mainThrust"), Gdx.files.internal("images"));
		thrust.scaleEffect(.04f);
		cometTrail.load(Gdx.files.internal("emitters/cometTrail"), Gdx.files.internal("images"));
		cometTrail.scaleEffect(.84f);
		cometFragmentTrail.load(Gdx.files.internal("emitters/cometFragmentTrail"), Gdx.files.internal("images"));
		cometFragmentTrail.scaleEffect(.04f);
		rotationThrusters.load(Gdx.files.internal("emitters/rotationThrusters"), Gdx.files.internal("images"));
		rotationThrusters.scaleEffect(.05f);
		muzzleFlash.load(Gdx.files.internal("emitters/bulletHit"), Gdx.files.internal("images"));
		muzzleFlash.scaleEffect(.01f);
		shieldMain.load(Gdx.files.internal("emitters/shieldsMain"), Gdx.files.internal("images"));
		shieldMain.scaleEffect(.21f);
		bullet.load(Gdx.files.internal("emitters/bullet"), Gdx.files.internal("images"));
		bullet.scaleEffect(.21f);
		particles.add(thrust);
		particles.add(rotationThrusters);
		particles.add(muzzleFlash);
		particles.add(shieldMain);
		particles.add(bullet);
		particles.add(cometTrail);
		particles.add(cometFragmentTrail);
	}
	public static void line(Vector2 origin,
			Vector2 destination,
			float r, float g, float b, float a) {
		lineRenderer.color(r, g, b, a);
		lineRenderer.vertex(origin.x, origin.y, 0);
		lineRenderer.color(r, g, b, a);
		lineRenderer.vertex(destination.x, destination.y, 0);
	}
	public static void line(float x1, float y1, float z1,
			float x2, float y2, float z2,
			float r, float g, float b, float a) {
		lineRenderer.color(r, g, b, a);
		lineRenderer.vertex(x1, y1, z1);
		lineRenderer.color(r, g, b, a);
		lineRenderer.vertex(x2, y2, z2);
	}
	public static void grid(float GAME_WORLD_WIDTH, float GAME_WORLD_HEIGHT,Color color,int numberOfLines) {		
		spacingH = GAME_WORLD_HEIGHT/numberOfLines;		
		spacingW = GAME_WORLD_WIDTH/numberOfLines;
		for(int x = 0; x <= GAME_WORLD_WIDTH; x += spacingH) {		
			line(x, 0, 0,		
					x, GAME_WORLD_HEIGHT, 0,		
					color.r, color.g, color.b, color.a);		
		}		
		for(int y = 0; y <= GAME_WORLD_HEIGHT; y += spacingW){		
			line(0, y, 0,		
					GAME_WORLD_WIDTH, y, 0,		
					color.r, color.g, color.b, color.a);		
		}		
	}		
	public static void drawGrid(Camera cam){				
		lineRenderer.begin(cam.combined, GL20.GL_LINES);		
		grid(Main.GAME_WORLD_WIDTH,Main.GAME_WORLD_HEIGHT,Color.DARK_GRAY,25);		
		lineRenderer.end();		
	}
	public static void thrustEffect(Vector2 position, Batch batch, float rotation){
		emitters = thrust.getEmitters();
		for(int i = 0; i< emitters.size; i++){
			val = emitters.get(i).getAngle();
			h1 = rotation - 90f;
			h2 = rotation - 90f;
			val.setHigh(h1,h2);
			val.setLow(h1,h2);
		}
		thrust.setPosition(position.x, position.y);
		thrust.start();
		thrust.draw(batch);
	}
	public static void cometTrailEffect(Vector2 position, Batch batch, float rotation){
		emitters = cometTrail.getEmitters();
		for(int i = 0; i< emitters.size; i++){
			val = emitters.get(i).getAngle();
			h1 = rotation - 90f;
			h2 = rotation - 90f;
			val.setHigh(h1,h2);
			val.setLow(h1,h2);
		}
		cometTrail.setPosition(position.x, position.y);
		cometTrail.start();
		cometTrail.draw(batch);
	}
	public static void cometFragmentTrailEffect(Vector2 position, Batch batch, float rotation){
		emitters = thrust.getEmitters();
		for(int i = 0; i< emitters.size; i++){
			val = emitters.get(i).getAngle();
			h1 = rotation;
			h2 = rotation;
			val.setHigh(h1,h2);
			val.setLow(h1,h2);
		}
		thrust.setPosition(position.x, position.y);
		thrust.start();
		thrust.draw(batch);
	}
	public static void rotationThrustEffect(Vector2 position, Batch batch, float rotation){
		emitters = rotationThrusters.getEmitters();
		for(int i = 0; i< emitters.size; i++){
			val = emitters.get(i).getAngle();
			h1 = rotation;
			h2 = rotation;
			val.setHigh(h1,h2);
			val.setLow(h1,h2);
		}
		rotationThrusters.setPosition(position.x, position.y);
		rotationThrusters.start();
		rotationThrusters.draw(batch);
	}
	public static void muzzleFlash(World world,Vector2 position, Batch batch, float rotation){
		emitters = muzzleFlash.getEmitters();
		for(int i = 0; i< emitters.size; i++){
			val = emitters.get(i).getAngle();
			h1 = rotation;
			h2 = rotation;
			val.setHigh(h1,h2);
			val.setLow(h1,h2);
		}
		muzzleFlash.setPosition(position.x, position.y);
		muzzleFlash.start();
		muzzleFlash.draw(batch);
	}
	public static void bullet(World world,Vector2 position, Batch batch, float rotation){
		if(b2DEmitter == null){
			b2DEmitter = new ParticleEmitterBox2D(world, bullet.getEmitters().get(0));
			b2DEmitter.allowCompletion();
		}
		val = b2DEmitter.getAngle();
		h1 = rotation;
		h2 = rotation;
		val.setHigh(h1,h2);
		val.setLow(h1,h2);
		b2DEmitter.setPosition(position.x, position.y);
		b2DEmitter.start();
		b2DEmitter.draw(batch);
	}
	public static void updateEffects(float delta){
 		for(ParticleEffect effect:particles){
			effect.update(delta);
		}
 		if(b2DEmitter != null)b2DEmitter.update(delta);
	}
	public static void inverterMainShieldEffect(Vector2 position, Batch batch){
		shieldMain.setPosition(position.x, position.y);
		shieldMain.start();
		shieldMain.draw(batch);
		
	}
}

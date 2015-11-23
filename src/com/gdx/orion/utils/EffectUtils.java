package com.gdx.orion.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdx.orion.screens.GameStateManager;

public class EffectUtils {
	static ParticleEffect thrust = new ParticleEffect();
	static ParticleEffect rotationThrusters = new ParticleEffect();
	static ParticleEffect bulletHit = new ParticleEffect();
	static ImmediateModeRenderer20 lineRenderer = new ImmediateModeRenderer20(false, true, 0);
	static float spacingH = 0;
	static float spacingW = 0;
	static Array<ParticleEffect> particles = new Array<ParticleEffect>();
	static Array<ParticleEmitter> emitters;
	static ScaledNumericValue val;
	static float h1;
	static float h2;
	static float angle;
	public static void initilize(){
		thrust.load(Gdx.files.internal("emitters/mainThrust"), Gdx.files.internal("images"));
		thrust.scaleEffect(.04f);
		rotationThrusters.load(Gdx.files.internal("emitters/rotationThrusters"), Gdx.files.internal("images"));
		rotationThrusters.scaleEffect(.05f);
		bulletHit.load(Gdx.files.internal("emitters/bulletHit"), Gdx.files.internal("images"));
		bulletHit.scaleEffect(.01f);
		particles.add(thrust);
		particles.add(rotationThrusters);
		particles.add(bulletHit);
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
		grid(GameStateManager.play.GAME_WORLD_WIDTH,GameStateManager.play.GAME_WORLD_HEIGHT,Color.DARK_GRAY,25);		
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
	public static void rotationThrustEffect(Vector2 position, Batch batch, float rotation){
		emitters = rotationThrusters.getEmitters();
		for(int i = 0; i< emitters.size; i++){
			val = emitters.get(i).getAngle();
			h1 = rotation - 90f;
			h2 = rotation - 90f;
			val.setHigh(h1,h2);
			val.setLow(h1,h2);
		}
		rotationThrusters.setPosition(position.x, position.y);
		rotationThrusters.start();
		rotationThrusters.draw(batch);
	}
	public static void fire(Vector2 position, Batch batch, float rotation){
		emitters = bulletHit.getEmitters();
		for(int i = 0; i< emitters.size; i++){
			val = emitters.get(i).getAngle();
			h1 = rotation;
			h2 = rotation;
			val.setHigh(h1,h2);
			val.setLow(h1,h2);
		}
		bulletHit.setPosition(position.x, position.y);
		bulletHit.start();
		bulletHit.draw(batch);
	}
	public static void updateEffects(float delta){
		for(ParticleEffect effect:particles){
			effect.update(delta);
		}
	}
	
	
	
}

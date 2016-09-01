package com.gdx.orion.entities;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import finnstr.libgdx.liquidfun.ColorParticleRenderer;
import finnstr.libgdx.liquidfun.ParticleGroupDef;
import finnstr.libgdx.liquidfun.ParticleSystem;
import finnstr.libgdx.liquidfun.ParticleSystemDef;
import finnstr.libgdx.liquidfun.ParticleDef.ParticleType;


public class WaterBalloon {
	private ParticleGroupDef particleGroupDef;
	
	
	public WaterBalloon(float x, float y, ParticleSystem particleSystem){
		   //First we create add new particlesystem and 
		   //set the radius of each particle to 6 / 120 m (5 cm)
		   
		   //For the flags you can set more than only one
		   particleGroupDef = new ParticleGroupDef();
		   particleGroupDef.color.set(1f, 1f, 0, 1);
		   particleGroupDef.flags.add(ParticleType.b2_staticPressureParticle);
		   particleGroupDef.strength = 0.30f;
		   particleGroupDef.lifetime = 20f;
		   particleGroupDef.color.set(0.2f, 0.1f, 0.9f, 1);
		   CircleShape partShape = new CircleShape();
		   partShape.setRadius(2.5f);
		   PolygonShape partShape2 = new PolygonShape();
		   partShape2.setAsBox(14f,1f);
		   particleGroupDef.shape = partShape;
		   particleGroupDef.position.set(x, y);
		   particleGroupDef.linearVelocity.set(50, -5f);
	       particleSystem.createParticleGroup(particleGroupDef);

	}
	public void render(ColorParticleRenderer colorParticleRenderer,ParticleSystem particleSystem,Camera cam){
		colorParticleRenderer.render(particleSystem, 0.25f, cam.combined);
	}
}

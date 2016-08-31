package com.gdx.orion.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

import finnstr.libgdx.liquidfun.ColorParticleRenderer;
import finnstr.libgdx.liquidfun.ParticleGroupDef;
import finnstr.libgdx.liquidfun.ParticleSystem;
import finnstr.libgdx.liquidfun.ParticleSystemDef;
import finnstr.libgdx.liquidfun.ParticleDef.ParticleType;


public class WaterBallon {
	private ParticleSystem particleSystem;
	private ParticleGroupDef particleGroupDef;
	private Body body;
	private BodyDef def;
	private FixtureDef fdef;
	private ParticleSystemDef systemDef;
	
	public WaterBallon(float x, float y, World world){
		   //First we create add new particlesystem and 
		   //set the radius of each particle to 6 / 120 m (5 cm)
		   systemDef = new ParticleSystemDef();
		   systemDef.radius = 0.25f;
		   systemDef.dampingStrength = 0.01f;
		   particleSystem = new ParticleSystem(world, systemDef);
		   particleSystem.setParticleDensity(1f);
		   
		   //For the flags you can set more than only one
		   particleGroupDef = new ParticleGroupDef();
		   particleGroupDef.color.set(1f, 1f, 0, 1);
		   particleGroupDef.flags.add(ParticleType.b2_elasticParticle);
		   particleGroupDef.strength = 0.001f;
		   particleGroupDef.color.set(0.2f, 0.1f, 0.9f, 1);
		   CircleShape partShape = new CircleShape();
		   partShape.setRadius(2f);
		   PolygonShape partShape2 = new PolygonShape();
		   partShape2.setAsBox(14f,1f);
		   particleGroupDef.shape = partShape;
		   particleGroupDef.position.set(166, 60);
		   createParticles2(60,60);
		   BodyDef def = new BodyDef();
		   def.position.x = 60;
		   def.position.y = 110;
		   body = world.createBody(def);
		   FixtureDef fdef = new FixtureDef();
		   fdef.shape = partShape2;
		   fdef.density = 0.2f;
		   fdef.friction = 1;
		   body.createFixture(fdef);
		   body.setType(BodyType.DynamicBody);
		   body.setUserData(new EntityData(100f,EntityType.WATER_BALLON, null));
	}
	public static void render(){
		
	}
    private void createParticles2(float pX, float pY) {
       particleSystem.createParticleGroup(particleGroupDef);
        updateParticleCount();
        System.out.println(mParticleGroupDef2.position.toString() + "");
    }
}

package com.gdx.orion.listeners;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import finnstr.libgdx.liquidfun.ParticleBodyContact;
import finnstr.libgdx.liquidfun.ParticleContact;
import finnstr.libgdx.liquidfun.ParticleDef;
import finnstr.libgdx.liquidfun.ParticleSystem;
import finnstr.libgdx.liquidfun.ParticleDef.ParticleType;

public class ContactHandler implements ContactListener {
	ParticleDef def = new ParticleDef();
	
	@Override
	public void beginContact(Contact contact) {
		
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beginParticleBodyContact(ParticleSystem system,
			ParticleBodyContact contact) {
		if(system.getParticleColorBuffer().get(contact.getIndex()).a == 1.0f){
			system.destroyParticle(contact.getIndex());
			def.flags.add(ParticleType.b2_powderParticle);
			def.position.set(12, 20);
			def.lifetime = 100;
			def.color.a = 0.99f;
			def.color.r = 100;
			def.color.b = 0;
			def.color.g = 0;
			System.out.println("Created" + system.createParticle(def));
		}
	}

	@Override
	public void endParticleBodyContact(Fixture fixture, ParticleSystem system,
			int index) {

	}

	@Override
	public void beginParticleContact(ParticleSystem system,
			ParticleContact contact) {
		System.out.println("contact! particle");
	}

	@Override
	public void endParticleContact(ParticleSystem system, int indexA, int indexB) {
		System.out.println("contact! END particle");
		
	}
}

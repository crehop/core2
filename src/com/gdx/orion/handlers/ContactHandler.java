package com.gdx.orion.handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import finnstr.libgdx.liquidfun.ParticleBodyContact;
import finnstr.libgdx.liquidfun.ParticleContact;
import finnstr.libgdx.liquidfun.ParticleSystem;

public class ContactHandler implements ContactListener {
	
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endParticleBodyContact(Fixture fixture, ParticleSystem system,
			int index) {
	}

	@Override
	public void beginParticleContact(ParticleSystem system,
			ParticleContact contact) {
	}

	@Override
	public void endParticleContact(ParticleSystem system, int indexA, int indexB) {
		// TODO Auto-generated method stub
		
	}
}

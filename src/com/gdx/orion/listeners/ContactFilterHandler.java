package com.gdx.orion.listeners;

import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.Fixture;

public class ContactFilterHandler implements ContactFilter {

	@Override
	public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
		System.out.println("COLLIDE");
		return true;
		
	}

}

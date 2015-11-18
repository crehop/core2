package com.gdx.orion.gamemodel.shields;

/**
 * The repulsor shield.  Game controller and view should update accordingly if 
 * this shield is in place on the player's ship.
 * 
 * @author jau
 */
public class RepulsorShield extends Shield {

	@Override
	public String getShieldName() {
		return "Repulsor Shield";
	}
}

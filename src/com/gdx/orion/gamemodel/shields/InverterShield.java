package com.gdx.orion.gamemodel.shields;


/**
 * The inverter shield.  Game controller and view should update accordingly if 
 * this shield is in place on the player's ship.
 * 
 * @author jau
 */
public class InverterShield extends Shield {
	@Override
	public String getShieldName() {
		return "Inverter Shield";
	}
}

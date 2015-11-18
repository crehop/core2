package com.gdx.orion.gamemodel.shields;

import java.io.Serializable;

import com.gdx.orion.gamemodel.Health;

/**
 * Describes common features of a shield.  Specific implementations will define behavior.
 * 
 * @author jau
 */
public abstract class Shield implements Health, Serializable {
	/**
	 * The current health of this shield
	 * 
	 * TODO: default value should be pulled from a resource file
	 */
	private double health = 100.0;
	
	/**
	 * The unique label for this shield should be used for UI and other logging, etc.
	 * 
	 * @return  unique label for this shield
	 */
	public abstract String getShieldName();
	
	@Override
	public void addHealth(double value) {
		health += value;
	}

	@Override
	public void setHealth(double value) {
		health = value;
	}

	@Override
	public double getHealth() {
		return health;
	}

	@Override
	public boolean isDisabled() {
		return health < 10;
	}

	@Override
	public boolean isDestroyed() {
		return health <= 0;
	}
}

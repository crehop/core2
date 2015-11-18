package com.gdx.orion.gamemodel.engines;

import java.io.Serializable;

import com.gdx.orion.gamemodel.Health;

/**
 * Represents common elements of an engine.  Specific types of this must be
 * declared with their own specific characteristics.
 * 
 * @author jau
 */
public abstract class Engine implements Health, Serializable {
	/**
	 * Current health of this engine
	 * 
	 * TODO: default value should be pulled from a resource file
	 */
	private double health = 100.0;
	
	/**
	 * The label for this engine which can be used in UI.
	 * Must be unique among all engine types!
	 * 
	 * @return  label for this engine
	 */
	public abstract String getEngineName();
	
	/**
	 * The current max thrust of this engine.  This could be decreased if damaged or
	 * increased with upgrades.
	 * 
	 * @return current max thrust of this engine.
	 */
	public abstract float maxThrust();
	
	
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

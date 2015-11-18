package com.gdx.orion.gamemodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gdx.orion.gamemodel.engines.ChemicalEngine;
import com.gdx.orion.gamemodel.engines.Engine;
import com.gdx.orion.gamemodel.shields.Shield;

/**
 * Describes a ship configuration and status.  At least one
 * object of this type should exist to track the player's current configuration.
 * This object should be persisted to save between game plays.
 * 
 * This object and its persisted form should be protected otherwise players can
 * cheat and give themselves resources.
 * 
 * @author jau
 */
public class ShipModel implements Health, Serializable {
	/**
	 * Shields that the player has acquired (usually by purchase, steal, scavenge, found, etc)
	 * Player can have any number of shields in inventory but only one can be active at a time.
	 * Never use this field directly!  Only use through {@link #getShieldInventory()
	 * 
	 * @see #getCurrentShield()
	 * @see #getShieldsInventory()
	 */
	private final Set<Shield> shieldInventory = new HashSet<Shield>();
	
	/**
	 * Engines that the player has acquired (usually by purchase, steal, scavenge, found, etc)
	 * Player can have more than any one type of engine but only one can be in use at any given
	 * time on this ship.  
	 * 
	 * @see #getCurrentEngine()
	 * @see #getEnginesInventory()
	 */
	private final Set<Engine> engineInventory = new HashSet<Engine>();
	
	/**
	 * Current engine but also part of inventory
	 * 
	 * @see #getCurrentEngine()
	 * @see #getEnginesInventory()
	 */
	private Engine currentEngine = new ChemicalEngine();
	
	/**
	 * Current shield but also part of inventory.  Never use this
	 * field directly!  Only use {@link #getShieldsInventory()}
	 * 
	 * @see #getCurrentShield()
	 * @see #getShieldsInventory()
	 */
	private Shield currentShield = null;
	
	/**
	 * The ship's current health
	 * 
	 * @see #addHealth(double)
	 * @see #setHealth(double)
	 * @see #isDisabled()
	 * @see #isDestroyed()
	 */
	private double health = 100.0;
	
	/**
	 * The current engine in use.  This likely will be used at the beginning of a map
	 * to draw the player's ship properly and other game logic.
	 * 
	 * @see #getEnginesInventory()
	 * @return  current engine in use
	 */
	private Engine getCurrentEngine() {
		return currentEngine;
	}
	
	/**
	 * The current inventory of engines including the current engine.
	 *
	 * @return  current inventory of engines including the current engine
	 */
	public Set<Engine> getEnginesInventory() {
		if (currentEngine != null) {
			// Always return the inventory set union with current engine.  Current engine may or may not
			// be in the set but set will remove any duplication naturally by being a mathematical set.
			engineInventory.add(currentEngine);
		}
		
		return engineInventory;
	}
	
	/**
	 * Adds an engine to the player's inventory through any acquisition such as purchase,
	 * find, steal, etc.
	 * 
	 * @param engine  the newly acquired engine
	 */
	public void addEngine(final Engine engine) {
		engineInventory.add(engine);
	}
	
	/**
	 * Returns the player's full shield inventory including the current shield
	 * 
	 * @return  player's full shield inventory including the current shield
	 */
	public Set<Shield> getShieldsInventory() {
		if (currentShield != null) {
			// Always return the inventory set union with current sheld (if any).  Current
			// shield  may or may not be in the set but set will remove any duplication 
			// naturally by being a mathematical set.
			shieldInventory.add(currentShield);
		}
		
		return shieldInventory;
	}
	
	/**
	 * Adds a shield to the player's inventory through any acquisition such as purchase,
	 * find, steal, etc.
	 * 
	 * @param engine  the newly acquired shield
	 */
	public void addShield(final Shield shield) {
		shieldInventory.add(shield);
	}
	
	/**
	 * The current shield in use.  This likely will be used at the beginning of a map
	 * to draw the player's ship properly and other game logic.
	 * 
	 * @see #getShieldsInventory()
	 * @return  current shield in use but could be <code>null</code> if player has no
	 *          shields or has not set a shield to use.
	 */
	public Shield getCurrentShield() {
		return currentShield;
	}
	
	@Override
	public double getHealth() {
		return health;
	}

	@Override
	public void addHealth(double value) {
		health += value;
	}

	@Override
	public void setHealth(double value) {
		health = value;
	}

	@Override
	public boolean isDisabled() {
		return health < 10.0;
	}

	@Override
	public boolean isDestroyed() {
		return health <= 0.0;
	}
}

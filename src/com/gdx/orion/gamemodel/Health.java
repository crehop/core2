package com.gdx.orion.gamemodel;

/**
 * Game items (including the ship) implement this to track health and damage
 * 
 * @author jau
 */
public interface Health {
	/**
	 * Add a negative value if item receives damage and a positive value if item
	 * receives repair
	 * 
	 * @param value
	 */
	void addHealth(double value);
	
	/**
	 * Sets the health level to an arbitrary amount
	 * 
	 * @param value new health level
	 */
	void setHealth(double value);
	
	/**
	 * Gets the current health level which is a primary for game state and drawing 
	 * health meter HUDs, etc.
	 * 
	 * @return current health level
	 */
	double getHealth();
	
	/**
	 * Determine if the item is "disabled"
	 * 
	 * @return  <code>true</code> if item is damage to the point of being disabled 
	 * but still repairable and <code>false</code> otherwise
	 */
	boolean isDisabled();
	
	/**
	 * Determines if the item is "destroyed".  If applied to the player's main ship
	 * then that will mean the end of the current life or possibly the game if player
	 * is on the last life.
	 * 
	 * @return  <code>true</code> if item is damage to the point of not being able to
	 * be used or repaired and <code>false</code> otherwise
	 */
	boolean isDestroyed();
}

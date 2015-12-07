package com.gdx.orion.gamemodel;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Represents the current game campaign.  This represents the player's current progress!  This
 * is implemented as a singleton as there is only ever one campaign in the game.  When restoring
 * a game from persisted data, use the setters only.  Constructor is not available for singleton.
 * 
 * @author jau
 */
public class Campaign implements Serializable {
	private static final Campaign INSTANCE = new Campaign();
	
	public int level = 1;
	public int livesRemaining = 2;
	public BigDecimal moneyBalance = BigDecimal.ZERO;
	public ShipModel shipModel;
	
	/**
	 * Sole constructor is private for singleton use only.  If the player is restarting
	 * the game after quitting the app, then use the setters to initialize the restore
	 * his/her game!
	 */
	private Campaign() {}
	
	/**
	 * Returns the one and only singleton instance of this class
	 * 
	 * @return  one and only singleton instance of this class
	 */
	public static Campaign getInstance() {
		return INSTANCE;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getLivesRemaining() {
		return livesRemaining;
	}
	
	public void setLivesRemaining(int livesRemaining) {
		this.livesRemaining = livesRemaining;
	}
	
	public BigDecimal getMoneyBalance() {
		return moneyBalance;
	}

	public void setMoneyBalance(BigDecimal moneyBalance) {
		this.moneyBalance = moneyBalance;
	}
	
	public ShipModel getShipModel() {
		// Lazy instantiation on first use
		if (shipModel == null) {
			shipModel = new ShipModel();
		}
		
		return shipModel;
	}
	
	public void setShipModel(ShipModel shipModel) {
		this.shipModel = shipModel;
	}
}

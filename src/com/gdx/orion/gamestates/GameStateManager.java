package com.gdx.orion.gamestates;

public class GameStateManager {

	private GameState gameState;
	
	public static final int MENU = 0;
	public static final int PLAY = 1;
	
	public GameStateManager() {
		
	}
	
	public void setState(int state) {
		if (state == MENU) {
			// switch
		}
		if (state == PLAY) {
			// switch
		}
	}
	
	public void update(float dt) {
		gameState.update(dt);
	}
	
	public void draw() {
		gameState.draw();
	}
	
	
	
}

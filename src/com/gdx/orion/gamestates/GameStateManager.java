package com.gdx.orion.gamestates;

public class GameStateManager {

	private GameState gameState;
	
	public static final int MENU = 0;
	public static final int PLAY = 1;
	public static final int LEVELEDIT = 2;
	public static final int LEVELSELECT = 3;
	public static final int STORE = 4;
	
	public GameStateManager() {
		setState(MENU);
	}
	
	public void setState(int state) {
		if (gameState != null) gameState.dispose();
		if (state == MENU) {
			gameState = new Menu(this);
		}
		if (state == PLAY) {
			gameState = new Play(this);
		}
	}
	
	public void update(float dt) {
		gameState.update(dt);
	}
	
	public void draw() {
		gameState.draw();
	}
	
	
	
}

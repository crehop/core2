package com.gdx.orion.gamestates;

import com.badlogic.gdx.Game;
import com.gdx.orion.Main;

public class GameStateManager {

	private GameState gameState;
	
	public static final int MENU = 0;
	public static final int PLAY = 1;
	public static final int LEVELEDIT = 2;
	public static final int LEVELSELECT = 3;
	public static final int STORE = 4;
	
	public GameStateManager(Game game) {
		setState(PLAY, game);
	}
	
	public void setState(int state, Game game) {
		if (gameState != null) gameState.dispose();
		if (state == MENU) {
			gameState = new Menu(this);
		}
		if (state == PLAY) {
			gameState = new Play(this, game);
		}
	}
	
	public void update(float dt) {
		gameState.update(dt);
	}
	
	public void draw() {
		gameState.draw();
	}
	
	
	
}

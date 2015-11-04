package com.gdx.orion.gamestates;

import com.badlogic.gdx.Game;
import com.gdx.orion.screens.Player;

public class Play extends GameState {
	public  static Player player;
	public static Game game;

	protected Play(GameStateManager gsm, Game game) {
		super(gsm);
		Play.game = game;
		player = new Player(0,0,0,game);
		init();
	}

	@Override
	public void init() {
		game.setScreen(player);
	}

	@Override
	public void update(float dt) {
		
	}

	@Override
	public void draw() {
		
	}

	@Override
	public void dispose() {
		
	}

}

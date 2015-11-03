package com.gdx.orion.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Menu extends GameState {

	private SpriteBatch sb;
	private GlyphLayout layout;
	
	private final String TITLE = "Asteroids";
	
	private int currentItem;
	private String[] menuItems;
	
	protected Menu(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		
		sb = new SpriteBatch();
		
		menuItems = new String[] {
				"Play",
				"HighScores",
				"Quit",
		};
		
	}

	@Override
	public void update(float dt) {
		
		//Input would be handled in here
		
	}

	@Override
	public void draw() {
		
		sb.setProjectionMatrix(null);
		
		sb.begin();
		
		layout.setText(titleFont, TITLE);
		
	}

	@Override
	public void dispose() {
		
	}

	
	
}

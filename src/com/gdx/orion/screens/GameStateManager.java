package com.gdx.orion.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class GameStateManager {
	
	public static final int MENU = 0;
	public static final int PLAY = 1;
	public static final int LEVELEDIT = 2;
	public static final int LEVELSELECT = 3;
	public static final int STORE = 4;
	
	public static Screen state;
	public static Play play;
	public static Menu menu;
	public static LevelSelect levelSelect;
	public static LevelEdit levelEdit;
	public static Store store;
	public static Game game;
	
	public static void initiate(Game game2){
		game = game2;
		levelEdit = new LevelEdit(game);
		levelSelect = new LevelSelect(game);
		play = new Play(game);
		menu = new Menu(game);
		store = new Store(game);
	}
	
	public static Screen getScreen(int screen){
		if(screen == PLAY){
			return play;
		}
		return null;
	}

	public static void setScreen(int levelselect2) {
		switch(levelselect2){
			case MENU:
				game.setScreen(menu);
			case PLAY:
				game.setScreen(play);
			case LEVELEDIT:
				game.setScreen(levelEdit);
			case LEVELSELECT:
				game.setScreen(levelSelect);
			case STORE:
				game.setScreen(store);
		}
	}
}

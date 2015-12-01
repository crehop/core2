package com.gdx.orion.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class GameStateManager {
	
	public static final int MENU = 0;
	public static final int PLAY = 1;
	public static final int LEVELEDIT = 2;
	public static final int LEVELSELECT = 3;
	public static final int STORE = 4;
	public static final int PAUSE = 5;
	public static final int HANGAR = 6;
	
	public static Screen state;
	public static Play play;
	public static Menu menu;
	public static LevelSelect levelSelect;
	public static LevelEdit levelEdit;
	public static Store store;
	public static Game game;
	public static Pause pause;
	public static Hangar hangar;
	public static ArrayList<GameState> gameStates = new ArrayList<GameState>();
	private static int lastScreen = 0;
	
	public static void initiate(Game game2){
		game = game2;
		levelEdit = new LevelEdit(game);
		levelSelect = new LevelSelect(game);
		play = new Play(game, 1);
		menu = new Menu(game, 0);
		store = new Store(game);
		pause = new Pause(game, 5);
		hangar = new Hangar(game, HANGAR);
		gameStates.add(play);
		gameStates.add(menu);
		gameStates.add(store);
		gameStates.add(levelEdit);
		gameStates.add(levelSelect);
		gameStates.add(pause);
		gameStates.add(hangar);
	}
	
	public static Screen getScreen(int screen){
		switch (screen) {
			case PLAY:   return play;
			case MENU:   return menu;
			case PAUSE:  return pause;
			case HANGAR: return hangar;
		}
		
		return null;
	}

	public static void setScreen(int levelselect2) {
		if(levelselect2 != PAUSE){
			setLastScreen(levelselect2);
		}
		for(GameState state: gameStates){
			if (state != null) {
				if (state.getID() != levelselect2) {
					state.setActive(false);
				} else {
					state.setActive(true);
				}
			}
		}
		switch(levelselect2){
			case MENU:
				game.setScreen(menu);
				break;
			case PLAY:
				game.setScreen(play);
				break;
			case LEVELEDIT:
				game.setScreen(levelEdit);
				break;
			case LEVELSELECT:
				game.setScreen(levelSelect);
				break;
			case STORE:
				game.setScreen(store);
				break;
			case PAUSE:
				game.setScreen(pause);
				break;
			case HANGAR:
				game.setScreen(hangar);
				break;
			default:
				System.out.printf("ERROR IN SETSCREEN: Unknown state %d%n", levelselect2);
		}
	}

	public static int getLastScreen() {
		return lastScreen;
	}

	public static void setLastScreen(int screen) {
		lastScreen = screen;
	}
}

package com.gdx.orion.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.World;

public class GameStateManager {
	
	public static final int MENU = 0;
	public static final int PLAY = 1;
	public static final int LEVELEDIT = 2;
	public static final int LEVELSELECT = 3;
	public static final int STORE = 4;
	public static final int PAUSE = 5;
	
	public static Screen state;
	public static Play play;
	public static Menu menu;
	public static LevelSelect levelSelect;
	public static Store store;
	public static Game game;
	public static Pause pause;
	public static ArrayList<GameState> gameStates = new ArrayList<GameState>();
	private static int lastScreen = 0;
	
	public static void initiate(Game game2){
		game = game2;
		levelSelect = new LevelSelect(game, 1);
		play = new Play(game, 1);
		menu = new Menu(game, 0);
		store = new Store(game);
		pause = new Pause(game, 5);
		gameStates.add(play);
		gameStates.add(menu);
		gameStates.add(store);
		gameStates.add(levelSelect);
		gameStates.add(pause);
	}
	
	public static Screen getScreen(int screen){
		switch (screen) {
			case PLAY:   return play;
			case MENU:   return menu;
			case PAUSE:  return pause;
			case LEVELSELECT: return levelSelect;
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
			case LEVELSELECT:
				game.setScreen(levelSelect);
				break;
			case STORE:
				game.setScreen(store);
				break;
			case PAUSE:
				game.setScreen(pause);
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

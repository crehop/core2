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
	
	public static void initiate(Game game){
		play = new Play(game);
		menu = new Menu(game);
	}
	
	public static Screen getScreen(int screen){
		if(screen == PLAY){
			return play;
		}
		return null;
	}
}

package com.gdx.orion.screens;

import com.badlogic.gdx.Screen;

public abstract class GameState {
	private int ID = 0;
	private Screen screen;
	boolean active = false;
	public abstract void setActive(boolean active);
	public abstract boolean isActive();
	public GameState(int ID){
		this.ID = ID;
	}
	public int getID(){
		return this.ID;
	}
	public Screen getScreen() {
		return screen;
	}
}

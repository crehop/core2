package com.gdx.orion.screens;

import com.badlogic.gdx.Screen;

public abstract class GameState extends ScreenLayerStack {
	public static final int VIEWPORT_WIDTH = 1920;
	public static final int VIEWPORT_HEIGHT = 1080;
	
	private int ID = 0;
	private Screen screen;
	protected boolean active = false;
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

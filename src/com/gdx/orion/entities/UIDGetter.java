package com.gdx.orion.entities;

public class UIDGetter {
	private static int ID = 0;
	public static int getID(){
		ID++;
		return ID;
	}
}

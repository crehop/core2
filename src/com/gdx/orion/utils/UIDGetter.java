package com.gdx.orion.utils;

public class UIDGetter {
	private static int ID = 0;
	public static int getID(){
		ID++;
		return ID;
	}
}

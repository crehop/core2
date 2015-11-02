package com.gdx.orion.gamevars;

public class Location {
	public float x;
	public float y;
	public float z;
	
	public Location(float x,float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Location(Location location){
		this.x = location.x;
		this.y = location.y;
		this.z = location.z;
	}
}

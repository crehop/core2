package com.gdx.orion.gamevars;

import com.badlogic.gdx.math.Vector2;

public class Location {
	public float x;
	public float y;
	public float z;
	private Vector2 position = new Vector2(0,0);
	
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
	public void update(Vector2 position){
		this.x = position.x;
		this.y = position.y;
	}
	public void update(Location location){
		this.x = location.x;
		this.y = location.y;
		this.z = location.z;
	}
	public Vector2 getPosition(){
		this.position.x = this.x;
		this.position.y = this.y;
		return this.position;
	}
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
}

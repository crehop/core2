package com.gdx.orion.gamevars;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Location {
	public float x;
	public float y;
	public float z;
	private Vector3 position = new Vector3(0,0,0);
	
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
	public void update(Vector3 position) {
		this.x = position.x;
		this.y = position.y;
		this.z = position.z;
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
	public Vector3 getPosition(){
		this.position.x = this.x;
		this.position.y = this.y;
		this.position.z = this.z;
		return this.position;
	}
}

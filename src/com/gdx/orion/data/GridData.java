package com.gdx.orion.data;

import com.badlogic.gdx.math.Vector2;

public class GridData {

	private String type;
	private Vector2 position;
	private Vector2 speed;
	private float density;
	private int size;
	
	public GridData(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public float getDensity() {
		return density;
	}

	public Vector2 getSpeed() {
		return speed;
	}

	public void setSpeed(Vector2 speed) {
		this.speed = speed;
	}

	public void setDensity(float density) {
		this.density = density;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
}

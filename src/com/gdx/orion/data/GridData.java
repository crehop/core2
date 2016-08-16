package com.gdx.orion.data;

import com.badlogic.gdx.math.Vector2;

public class GridData {

	public enum Type {
		ASTEROID, COMET, MOON, PLANET, PLAYER_SPAWN;
	}
	
	private Type type;
	
	//Both
	private Vector2 position;
	private Vector2 force; //or velocity
	private float density;
	
	//Asteroid or Comet
	private int size;
	
	//Gravity Well
	private float radius;
	private boolean inWardForce;
	private boolean canMove;
	
	public GridData(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
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

	public Vector2 getForce() {
		return force;
	}

	public void setForce(Vector2 force) {
		this.force = force;
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

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public boolean isInWardForce() {
		return inWardForce;
	}

	public void setInWardForce(boolean inWardForce) {
		this.inWardForce = inWardForce;
	}

	public boolean isCanMove() {
		return canMove;
	}

	public void setCanMove(boolean canMove) {
		this.canMove = canMove;
	}
	
}

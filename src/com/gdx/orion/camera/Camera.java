package com.gdx.orion.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gdx.orion.Main;
import com.gdx.orion.gamevars.Location;

public class Camera {
	protected Vector3 position = null;
	protected Location location;
	protected Viewport viewport;
	protected float far = 1000;
	protected float near = 0.1f;

	public Camera(float x, float y, float z){
		position = new Vector3(x,y,z);
		this.location = new Location(x,y,z);
		Main.setCam(new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		Main.getCam().near = near;
		Main.getCam().far = far;
		viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Main.getCam());
	}
	public void walkBackward(float distance){
		this.position.y -= distance;
		updateLocation();
		
	}
	public void walkForward(float distance){
		this.position.y += distance;
		updateLocation();
	}

	public void strafeRight(float distance){
		//moves camera forward relative to its current rotation;
		this.position.x += distance;
		updateLocation();
	}
	public void strafeLeft(float distance){
		//moves camera forward relative to its current rotation;
		this.position.x -= distance;
		updateLocation();
	}
	public void moveDown(float distance){
		this.position.y -= distance;
		this.location.update(position);
	}
	public void moveUp(float distance){
		this.position.y += distance;
		updateLocation();
	}
	
	public void setLocation(Location location){
		this.position.x = (location.x);
		this.position.y = (location.y);
		this.position.z = (location.z);
		this.location.update(position);
	}
	public Location getLocation(){
		return location;
	}
	protected void updateLocation(){
		this.location.update(position);

	}
}
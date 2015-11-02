package com.gdx.orion.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.gdx.orion.gamevars.Location;

public class Camera {
	public OrthographicCamera cam;
	public Location location;
	
	public Camera(float x, float y, float z){
		cam = new OrthographicCamera();
		location = new Location(x,y,z);
		cam.position.set(x, y, z);
	}
	
	public void update(float x, float y, float z){
		this.location.x = x;
		this.location.y = y;
		this.location.z = z;
		cam.position.set(location.x,location.y,location.z);
		cam.update();
	}
	
	public OrthographicCamera getCamera(){
		return this.cam;
	}
}

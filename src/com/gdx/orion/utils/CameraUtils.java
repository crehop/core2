package com.gdx.orion.utils;
//import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class CameraUtils {
	static protected Vector3 position = null;
	
	public static void backward(float distance, OrthographicCamera cam){
		position.x = cam.position.x;
		position.y = cam.position.y;
		position.y -= distance;
		updateLocation(cam);
	}
	public static void forward(float distance, OrthographicCamera cam){
		position.x = cam.position.x;
		position.y = cam.position.y;
		position.y += distance;
		updateLocation(cam);
	}
	public static void strafeRight(float distance, OrthographicCamera cam){
		position.x = cam.position.x;
		position.y = cam.position.y;
		position.x += distance;
		updateLocation(cam);
	}
	public static void strafeLeft(float distance ,OrthographicCamera cam){
		position.x = cam.position.x;
		position.y = cam.position.y;
		position.x -= distance;
		updateLocation(cam);
	}
	public static void moveDown(float distance, OrthographicCamera cam){
		position.y = cam.position.y;
		position.y -= distance;
		updateLocation(cam);
	}
	public static void moveUp(float distance, OrthographicCamera cam){
		position.x = cam.position.x;
		position.y = cam.position.y;
		position.y += distance;
		updateLocation(cam);
	}
	protected static void updateLocation(OrthographicCamera cam){
		cam.position.set(position.x, position.y, position.z);
	}
}
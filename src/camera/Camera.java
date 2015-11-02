package camera;

import game.Location;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class Camera {
	public OrthographicCamera cam;
	public Location location;
	
	public Camera(float x, float y, float z){
		cam = new OrthographicCamera();
		location = new Location(x,y,z);
		cam.position.set(x, y, z);
	}
}

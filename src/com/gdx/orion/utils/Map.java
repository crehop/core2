package com.gdx.orion.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class Map {
	private static float x;
	private static float y;
	private static Vector3 position = new Vector3(0,0,0);
	private static boolean enabled = true;
	public static void render(Batch batch, OrthographicCamera cam,OrthographicCamera mapCam, World world, Box2DDebugRenderer renderer) {
		batch.setProjectionMatrix(mapCam.combined);
		if(enabled){
			batch.begin();
			cam.update();
			renderer.render(world, mapCam.combined);
			batch.end();               
		}
	}
}

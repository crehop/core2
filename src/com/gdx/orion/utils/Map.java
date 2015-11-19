package com.gdx.orion.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class Map {
	private static boolean enabled = true;
	public static void render(Batch batch, OrthographicCamera cam,OrthographicCamera mapCam, World world, Box2DDebugRenderer renderer) {
		System.out.println(mapCam.viewportHeight);
		System.out.println(mapCam.viewportWidth);
		if(enabled){
			renderer.render(world, mapCam.combined);              
		}
	}
}

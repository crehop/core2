package com.gdx.orion.handlers;

import com.gdx.orion.entities.EntityData;
import com.gdx.orion.entities.EntityType;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;

public class RaycastHandler {
	//private static Vector2 collision = new Vector2();
	//private static Vector2 normal = new Vector2();
	private static RayCastCallback callback = new RayCastCallback(){
		@Override
		public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal2, float fraction){

			//collision.set(point);
			//normal.set(normal2).add(point);
			return 0;
		}
	};
	public static void ray(Vector2 p1, Vector2 p2, EntityType type2,World world){
		world.rayCast(callback, p1, p2);
	}
}

package com.gdx.orion.utils;


import java.util.Random;

import gdx.orion.entities.EntityData;
import gdx.orion.entities.EntityType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.gdx.orion.gamevars.Location;

public class WorldUtils {
	static PolygonShape shape = new PolygonShape();
	static FixtureDef fdef = new FixtureDef();
	private static Body body;
	private static BodyDef def = new BodyDef();
	private static Array<Body> bodies = new Array<Body>();
	private static float[] temp = new float[16];
	private static int count = 0;
	static CircleShape shape2 = new CircleShape();
	static Location location;
	static Random rand = new Random();


	public static void GenerateWorldBorder(World world,float x1,float x2,float y1,float y2){
		def.position.set(0 - Gdx.graphics.getWidth()/2,0 - Gdx.graphics.getHeight()/2);
		def.type = BodyType.StaticBody;
		world.getBodies(bodies);
		if(bodies.contains(body, false)){
			world.destroyBody(body);
		}
		body = world.createBody(def);
		float[] worldBorder = new float[8];
		worldBorder[0] = x1;
		worldBorder[1] = y1;
		worldBorder[2] = x1;
		worldBorder[3] = y2;
		worldBorder[4] = x1 + 2;
		worldBorder[5] = y2;
		worldBorder[6] = x1 + 2;
		worldBorder[7] = y1;
		shape.set(worldBorder);
		fdef.shape = shape;
		fdef.restitution = 2;
		body.createFixture(fdef);
		
		worldBorder[0] = x1;
		worldBorder[1] = y1;
		worldBorder[2] = x2;
		worldBorder[3] = y1;
		worldBorder[4] = x2;
		worldBorder[5] = y1 - 2;
		worldBorder[6] = x1;
		worldBorder[7] = y1 - 2;
		shape.set(worldBorder);
		fdef.shape = shape;
		fdef.restitution = 2;
		body.createFixture(fdef);		
		
		worldBorder[0] = x1;
		worldBorder[1] = y2;
		worldBorder[2] = x2;
		worldBorder[3] = y2;
		worldBorder[4] = x2;
		worldBorder[5] = y2 - 2;
		worldBorder[6] = x1;
		worldBorder[7] = y2 - 2;
		shape.set(worldBorder);
		fdef.shape = shape;
		fdef.restitution = 2;
		body.createFixture(fdef);
		
		worldBorder[0] = x2;
		worldBorder[1] = y2;
		worldBorder[2] = x2 + 2;
		worldBorder[3] = y2;
		worldBorder[4] = x2 + 2;
		worldBorder[5] = y1;
		worldBorder[6] = x2;
		worldBorder[7] = y1;
		shape.set(worldBorder);
		fdef.shape = shape;
		fdef.restitution = 2;
		body.createFixture(fdef);
		body.setSleepingAllowed(false);
		body.setUserData(new EntityData(9999,EntityType.WORLD_BOUNDRY,null));
	}
	
	public static float[] moveVerts(float[] verts, Body body){
		count = 0;
		for(float vert:verts){
			if(count%2 == 0){
				temp[count] = vert + body.getWorldCenter().y;
			}else{
				temp[count] = vert + body.getWorldCenter().x;
			}
			count++;
		}
		return temp;
	}

	public static void fireBullet(World world, Location position, float density, float size, Vector2 directionalForce){
		location = position;
		def = new BodyDef();
		def.position.set(location.x, location.y);
		def.type = BodyType.DynamicBody;
		def.angle = 200;
		shape2.setRadius(size);
		fdef.shape = shape2;
		fdef.density = density;
		fdef.friction = 1;
		fdef.restitution = 0.75f;
		body = world.createBody(def);
		body.createFixture(fdef);
		body.setLinearVelocity(directionalForce);
	}
}

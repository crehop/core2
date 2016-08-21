package com.gdx.orion.handlers;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.gdx.orion.screens.GameStateManager;
import com.gdx.orion.screens.Play;

public class VoxelizedPhysicsHandler {
	static ArrayList<Array[][]> buildMe = new ArrayList<Array[][]>();
	static ArrayList<Array[][]> deconstructMe = new ArrayList<Array[][]>();
	static Body body;
	static FixtureDef fdef;
	static World world;
	
	public static void build(Array[][] voxelArray, World world) {
			PolygonShape shape = new PolygonShape();
			BodyDef def = new BodyDef();
			fdef = new FixtureDef();
			def.type = BodyType.DynamicBody;
			def.angle = 0;
			def.position.set(22,44);
			shape.set(new float[]{0f,0f,0f,0.25f,0.25f,0.25f,0.25f,0f} );
			fdef.shape = shape;
			fdef.density = 1.0f;
			fdef.friction = 1;
			body = world.createBody(def);
			body.createFixture(fdef);
			body.setAngularVelocity(MathUtils.random(-4f,4f));
			
	}
}

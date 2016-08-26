package com.gdx.orion.handlers;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.orion.entities.voxel.Voxel;
import com.gdx.orion.entities.voxel.VoxelizedPhysicsObject;

public class VoxelizedPhysicsHandler {
	static ArrayList<Voxel[][]> buildMe = new ArrayList<Voxel[][]>();
	static ArrayList<Voxel[][]> check = new ArrayList<Voxel[][]>();
	static ArrayList<Voxel[][]> deconstructMe = new ArrayList<Voxel[][]>();
	static Body body;
	static FixtureDef fdef;
	static World world;
	
	public static Body build(VoxelizedPhysicsObject object, World world) {
			BodyDef def = new BodyDef();
			def.type = BodyType.DynamicBody;
			def.angle = 0;
			def.position.set(22,44);
			body = world.createBody(def);
			return body;
	}
}

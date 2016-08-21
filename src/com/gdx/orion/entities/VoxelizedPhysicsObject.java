package com.gdx.orion.entities;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.gdx.orion.handlers.VoxelizedPhysicsHandler;

public class VoxelizedPhysicsObject {
	Array[][] voxelArray;
	World world;
	public VoxelizedPhysicsObject(Array[][] voxels, World world){
		this.world = world;
		//for(int x = 0; x < voxels.length; x++){
		//	for(int y = 0; y < voxels[x].length; y++){
		//		
		//	}
		//}
		VoxelizedPhysicsHandler.build(voxelArray,world);
	}
	public VoxelizedPhysicsObject() {
		VoxelizedPhysicsHandler.build(voxelArray,world);
	}
}

package com.gdx.orion.entities.voxel;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.gdx.orion.handlers.VoxelizedPhysicsHandler;

public class VoxelizedPhysicsObject {
	
	private Voxel[][] voxelArray;
	private World world;
	
	public VoxelizedPhysicsObject(Voxel[][] voxels, World world){
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

	public Voxel[][] getVoxelArray() {
		return voxelArray;
	}

	public void setVoxelArray(Voxel[][] voxelArray) {
		this.voxelArray = voxelArray;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
	
}

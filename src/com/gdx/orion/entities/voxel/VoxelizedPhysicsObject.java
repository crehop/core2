package com.gdx.orion.entities.voxel;

import com.badlogic.gdx.physics.box2d.World;
import com.gdx.orion.handlers.VoxelizedPhysicsHandler;

public class VoxelizedPhysicsObject {
	
	private Voxel[][] voxelArray;
	private World world;
	private float[][] object2;
	private float[] object;
	private float xOffset = 0.25f;
	private float yOffset = 0.25f;
	private int count = 0;
	
	public VoxelizedPhysicsObject(Voxel[][] voxels, World world){
		this.voxelArray = voxels;
		this.object2 = new float[voxels.length * 2][voxels[0].length * 2];
		for(int x = 0; x < this.getVoxelArray().length; x++){
			for(int y = 0; y < this.getVoxelArray()[x].length; y++){
				if(voxels[x][y].type != VoxelType.AIR){
					object2[x*2][y*2] = x * xOffset;
					object2[x*2][y*2 + 1] = y * yOffset;
				}
			}
		}	
		count = 0;
		for(int x = 0; x < this.object2.length; x++){
			for(int y = 0; y < this.object2[x].length; y++){
				count++;
				count++;
				System.out.println(count);
			}
		}
		object = new float[count];
		count = 0;
		for(int x = 1; x < this.object2.length; x++){
			for(int y = 1; y < this.object2[x].length; y++){
				object[count++] = 
						object2[x*2-1][y*2-1];
				object[count++] = 
						object2[x*2][y*2];
			}
		}	
		this.world = world;
		VoxelizedPhysicsHandler.build(this,world);
	}
	
	public float[] getObject() {
		return object;
	}

	public void setObject(float[] object) {
		this.object = object;
	}

	public float getxOffset() {
		return xOffset;
	}

	public void setxOffset(float xOffset) {
		this.xOffset = xOffset;
	}

	public float getyOffset() {
		return yOffset;
	}

	public void setyOffset(float yOffset) {
		this.yOffset = yOffset;
	}

	public VoxelizedPhysicsObject() {
		VoxelizedPhysicsHandler.build(this,world);
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

package com.gdx.orion.entities.voxel;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.World;
import com.gdx.orion.handlers.VoxelizedPhysicsHandler;

public class VoxelizedPhysicsObject {
	
	private Voxel[][] voxelArray;
	private World world;
	private float[] object;
	private ArrayList<Float> floatList = new ArrayList<Float>();
	private int count = 0;
	private float voxelSize = .25f;
	
	public VoxelizedPhysicsObject(Voxel[][] voxels, World world){
		floatList.clear();
		this.voxelArray = voxels;
		System.out.println("" + this.getVoxelArray().length + "/" + this.getVoxelArray()[0].length);
		if(this.getVoxelArray().length == 1 && this.getVoxelArray()[0].length == 1){
			floatList.add(-voxelSize);
			floatList.add(voxelSize);
			floatList.add(voxelSize);
			floatList.add(voxelSize);
			floatList.add(-voxelSize);
			floatList.add(voxelSize);
			floatList.add(-voxelSize);
			floatList.add(-voxelSize);
		}
		for(int x = 0; x < this.getVoxelArray().length; x++){
			for(int y = 0; y < this.getVoxelArray()[x].length; y++){
				if(voxels[x][y].type != VoxelType.AIR){
					floatList.add(x * voxelSize);
					floatList.add(y * voxelSize);
				}
			}
		}	
		object = new float[floatList.size()];
		count = 0;
		for(float f:floatList){
			object[count++] = f;
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

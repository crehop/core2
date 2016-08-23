package com.gdx.orion.entities.voxel;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.orion.handlers.VoxelizedPhysicsHandler;
import com.gdx.orion.utils.VoxelUtils;

public class VoxelizedPhysicsObject {
	
	private Voxel[][] voxelArray;
	private World world;
	private float[] object;
	private ArrayList<Float> floatList = new ArrayList<Float>();
	private int count = 0;
	private Vector2 stored = new Vector2 (-1,-1);
	
	public VoxelizedPhysicsObject(Voxel[][] voxels, World world){
		floatList.clear();
		this.voxelArray = voxels;
		System.out.println("" + this.getVoxelArray().length + "/" + this.getVoxelArray()[0].length);
		floatList = VoxelUtils.getOuterShell(voxels);
		for(int i =0; i<floatList.size();){
			System.out.println("X:" + floatList.get(i++) + " Y:" + floatList.get(i++));
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

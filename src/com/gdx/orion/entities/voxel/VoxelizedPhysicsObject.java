package com.gdx.orion.entities.voxel;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.orion.handlers.VoxelizedPhysicsHandler;

public class VoxelizedPhysicsObject {
	
	private Voxel[][] voxelArray;
	private World world;
	private float[] object;
	private ArrayList<Float> floatList = new ArrayList<Float>();
	private int count = 0;
	private Vector2 stored = new Vector2 (-1,-1);
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
			floatList.add(voxelSize);
			floatList.add(-voxelSize);
			floatList.add(-voxelSize);
			floatList.add(-voxelSize);
		}
		
		for(int x = 0; x < this.getVoxelArray().length; x++){
			for(int y = 0; y < this.getVoxelArray()[x].length; y++){
				if(this.voxelArray[x][y].getType() != VoxelType.AIR){
					if(this.getVoxelWest(x, y) == VoxelType.AIR){
						floatList.add(-voxelSize * (x + 1));
						floatList.add(voxelSize * (voxelArray[0].length - y));
					}
					if(this.getVoxelEast(x, y).type)
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
	
	public VoxelType getVoxelNorth(int currentX, int currentY){
		if(currentY != 0){
			return voxelArray[currentX][currentY-1].type;
		}else{
			return VoxelType.AIR;
		}
	}
	public VoxelType getVoxelSouth(int currentX, int currentY){
		if(currentY != voxelArray[0].length - 1){
			return voxelArray[currentX][currentY+1].type;
		}
		return VoxelType.AIR;
	}
	public VoxelType getVoxelEast(int currentX, int currentY){
		if(currentX != voxelArray.length - 1){
			return voxelArray[currentX+1][currentY].type;
		}
		return VoxelType.AIR;
	}
	public VoxelType getVoxelWest(int currentX, int currentY){
		if(currentX != 0){
			return voxelArray[currentX-1][currentY].type;
		}
		return VoxelType.AIR;
	}
	public VoxelType getVoxelNorthWest(int currentX, int currentY){
		if(currentY != 0 && currentX != 0){
			return voxelArray[currentX-1][currentY - 1].type;
		}
		return VoxelType.AIR;
	}
	public VoxelType getVoxelSouthWest(int currentX, int currentY){
		if(currentX != 0 && currentY != voxelArray[0].length - 1){
			return voxelArray[currentX-1][currentY + 1].type;
		}
		return VoxelType.AIR;
	}
	public VoxelType getVoxelNorthEast(int currentX, int currentY){
		if(currentY != 0 && currentX != voxelArray.length - 1){
			return voxelArray[currentX + 1][currentY - 1].type;
		}
		return VoxelType.AIR;
	}
	public VoxelType getVoxelSouthEast(int currentX, int currentY){
		if(currentY != voxelArray[0].length - 1 && currentX != voxelArray.length - 1){
			return voxelArray[currentX-1][currentY- 1].type;
		}
		return VoxelType.AIR;
	}
}

package com.gdx.orion.entities.voxel;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.orion.utils.VoxelUtils;

public class VoxelizedPhysicsObject {
	
	private Voxel[][] voxelArray;
	private World world;
	@SuppressWarnings("unused")
	private int count = 0;
	private VoxelShell shell;
	private Body body;
	
	public VoxelizedPhysicsObject(Voxel[][] voxels, World world, int x, int y){
		this.world = world;
		this.voxelArray = voxels;
		shell = VoxelUtils.getOuterShell(voxels);
		body.setTransform(x, y, 0);
	}

	public VoxelizedPhysicsObject() {
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

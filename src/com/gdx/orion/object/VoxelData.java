package com.gdx.orion.object;

import java.io.Serializable;

import com.gdx.orion.entities.voxel.VoxelType;

public class VoxelData implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private VoxelType[][] voxels;
	
	public VoxelData(String name, VoxelType[][] voxels)
	{
		this.name = name;
		this.voxels = voxels;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public VoxelType[][] getVoxels() {
		return voxels;
	}

	public void setVoxels(VoxelType[][] voxels) {
		this.voxels = voxels;
	}
	
}

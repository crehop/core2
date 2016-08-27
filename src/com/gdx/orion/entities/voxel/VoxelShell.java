package com.gdx.orion.entities.voxel;

import java.util.ArrayList;

public class VoxelShell {
	private ArrayList<VoxelShellSegment> segments;
	public VoxelShell(ArrayList<VoxelShellSegment> segments){
		this.segments = segments;
	}
	public ArrayList<VoxelShellSegment> getSegments() {
		return segments;
	}
}

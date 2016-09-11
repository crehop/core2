package com.gdx.orion.entities.voxel.types;

import com.gdx.orion.entities.voxel.Voxel;
import com.gdx.orion.entities.voxel.VoxelType;

public class Grass extends Voxel { 
	
	protected Grass() {
		super();
		
		type = VoxelType.GRASS;
		
		durability = 10;
		density = 3.5f;
		absorbtion = 6.0f;
		
		breakable = true;
	}
	
}

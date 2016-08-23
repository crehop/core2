package com.gdx.orion.entities.voxel.types;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.gdx.orion.entities.voxel.Voxel;
import com.gdx.orion.entities.voxel.VoxelType;

public class Air extends Voxel {
	
	private static Texture texture = new Texture(Gdx.files.internal("Voxels/stone.jpg"));
	
	public Air() {
		super();
		
		type = VoxelType.AIR;
		
		durability = 0;
		density = 0f;
		absorbtion = 0f;
		
		breakable = false;
	}

	public static Texture getTexture() {
		return texture;
	}
	
}

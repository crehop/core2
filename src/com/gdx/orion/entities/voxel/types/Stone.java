package com.gdx.orion.entities.voxel.types;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.gdx.orion.entities.voxel.Voxel;
import com.gdx.orion.entities.voxel.VoxelType;

public class Stone extends Voxel {
	
	private static Texture texture = new Texture(Gdx.files.internal("Voxels/stone.jpg"));
	
	public Stone() {
		super();
		
		type = VoxelType.STONE;
		
		durability = 20;
		density = 8.0f;
		absorbtion = 8.0f;
		friction = 1.0f;
		
		breakable = true;
	}

	public static Texture getTexture() {
		return texture;
	}
	
}

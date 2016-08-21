package com.gdx.orion.entities.voxel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Stone extends Voxel {
	
	private static Texture texture = new Texture(Gdx.files.internal("Voxels/stone.jpg"));
	
	protected Stone() {
		super();
		
		type = VoxelType.STONE;
		
		durability = 20;
		density = 8.0f;
		absorbtion = 8.0f;
		
		breakable = true;
	}

	public static Texture getTexture() {
		return texture;
	}
	
}

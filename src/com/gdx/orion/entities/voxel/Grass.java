package com.gdx.orion.entities.voxel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Grass extends Voxel { 
	
	private static Texture texture = new Texture(Gdx.files.internal("Voxels/grass.jpg"));
	
	protected Grass() {
		super();
		
		type = VoxelType.GRASS;
		
		durability = 10;
		density = 3.5f;
		absorbtion = 6.0f;
		
		breakable = true;
	}

	public static Texture getTexture() {
		return texture;
	}
	
}

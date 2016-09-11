package com.gdx.orion.entities.voxel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public enum VoxelType {

	AIR(0, new Texture(Gdx.files.internal("Voxels/stone.jpg"))), 
	STONE(2, new Texture(Gdx.files.internal("Voxels/stone.jpg"))), 
	GRASS(1, new Texture(Gdx.files.internal("Voxels/grass.jpg")));
	
	private final int id;
	private final Texture texture;
	
	private VoxelType(int id, Texture texture) {
		this.id = id;
		this.texture = texture;
	}

	public int getId() {
		return id;
	}

	public Texture getTexture() {
		return texture;
	}
	
}

package com.gdx.orion.entities.voxel;

public abstract class Voxel {
	
	public static final float voxelSize = 0.25f;
	public static final float voxelGap = 0.256f;
	
	public VoxelType type;
	
	protected int durability;
	protected float density;
	protected float absorbtion;
	protected float friction;
	
	protected boolean breakable;
	
	public Voxel() {
		
	}
	
	public VoxelType getType() {
		return type;
	}
	public void setType(VoxelType type) {
		this.type = type;
	}
	public int getDurability() {
		return durability;
	}
	public void setDurability(int durability) {
		this.durability = durability;
	}
	public float getDensity() {
		return density;
	}
	public void setDensity(float density) {
		this.density = density;
	}
	public float getAbsorbtion() {
		return absorbtion;
	}
	public void setAbsorbtion(float absorbtion) {
		this.absorbtion = absorbtion;
	}
	public boolean isBreakable() {
		return breakable;
	}
	
}

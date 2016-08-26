package com.gdx.orion.entities.voxel;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.orion.handlers.VoxelizedPhysicsHandler;
import com.gdx.orion.utils.VoxelUtils;

public class VoxelizedPhysicsObject {
	
	private Voxel[][] voxelArray;
	private World world;
	private float[] object4 = new float[8];
	private float[] object6 = new float[12];;
	private int count = 0;
	private VoxelShell shell;
	private Body body;
	private float density = 1;
	private PolygonShape shape = new PolygonShape();
	
	public VoxelizedPhysicsObject(Voxel[][] voxels, World world){
		this.voxelArray = voxels;
		System.out.println("" + this.getVoxelArray().length + "/" + this.getVoxelArray()[0].length);
		shell = VoxelUtils.getOuterShell(voxels);
		body = VoxelizedPhysicsHandler.build(this,world);
		for(VoxelShellSegment segment:
			shell.getSegments()){
			count = 0;
			if(segment.getVertices().length == 8){
				for(float f:segment.getVertices()){
					object4[count++] = f;
					shape.set(object4);
				}
			}else if(segment.getVertices().length == 12){
				for(float f:segment.getVertices()){
					object6[count++] = f;
				}
				shape.set(object6);
			}
			body.createFixture(shape, density);

		}
		this.world = world;
	}

	public VoxelizedPhysicsObject() {
		VoxelizedPhysicsHandler.build(this,world);
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

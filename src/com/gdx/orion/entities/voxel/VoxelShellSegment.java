package com.gdx.orion.entities.voxel;

public class VoxelShellSegment {
	private float[] vertices;
	public VoxelShellSegment(float point1,float point2, float point3,float point4, 
			float point5, float point6, float point7, float point8){
		vertices = new float[8];
		vertices[0] = point1;
		vertices[1] = point2;
		vertices[2] = point3;
		vertices[3] = point4;
		vertices[4] = point5;
		vertices[5] = point6;
		vertices[6] = point7;
		vertices[7] = point8;
	}
	public float[] getVertices(){
		return vertices;
	}
}

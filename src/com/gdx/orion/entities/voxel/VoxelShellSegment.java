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
	public VoxelShellSegment(float point1,float point2, float point3,float point4, 
			float point5, float point6, float point7, float point8,
			float point9,float point10, float point11,float point12){
		vertices = new float[12];
		vertices[0] = point1;
		vertices[1] = point2;
		vertices[2] = point3;
		vertices[3] = point4;
		vertices[4] = point5;
		vertices[5] = point6;
		vertices[6] = point7;
		vertices[7] = point8;
		vertices[8] = point9;
		vertices[9] = point10;
		vertices[10] = point11;
		vertices[11] = point12;
	}
	public float[] getVertices(){
		return vertices;
	}
}

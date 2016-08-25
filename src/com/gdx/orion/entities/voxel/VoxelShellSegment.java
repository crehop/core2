package com.gdx.orion.entities.voxel;

import com.badlogic.gdx.math.Vector2;

public class VoxelShellSegment {
	private float[] vertices;
	public VoxelShellSegment(Vector2 point1, Vector2 point2, Vector2 point3, Vector2 point4){
		vertices = new float[8];
		vertices[0] = point1.x;
		vertices[1] = point1.y;
		vertices[2] = point2.x;
		vertices[3] = point2.y;
		vertices[4] = point3.x;
		vertices[5] = point3.y;
		vertices[6] = point4.x;
		vertices[7] = point4.y;
	}
	public VoxelShellSegment(Vector2 point1, Vector2 point2, Vector2 point3, Vector2 point4, Vector2 point5, Vector2 point6){
		vertices = new float[12];
		vertices[0] = point1.x;
		vertices[1] = point1.y;
		vertices[2] = point2.x;
		vertices[3] = point2.y;
		vertices[4] = point3.x;
		vertices[5] = point3.y;
		vertices[6] = point4.x;
		vertices[7] = point4.y;
		vertices[8] = point5.x;
		vertices[9] = point5.y;
		vertices[10] = point6.x;
		vertices[11] = point6.y;
	}
	public float[] getVertices(){
		return vertices;
	}
}

package com.gdx.orion.utils;

import java.util.ArrayList;

import com.gdx.orion.entities.voxel.Voxel;
import com.gdx.orion.entities.voxel.VoxelType;

public class VoxelUtils {

	public static int blockFoundX = 0;
	public static int blockFoundY = 0;
	public enum Direction {

		UP, DOWN, LEFT, RIGHT;
		
		private Direction() {
			
		}
		
	}
	public static int width;
	public static int height;

	public static int x; 
	public static int y;
	public static boolean blockFound = false;
	public static ArrayList<Voxel> result = new ArrayList<Voxel>();


    public static ArrayList<Voxel> getOuterShell(Voxel[][] voxelArray) {
    	result.clear();
        if(voxelArray == null || voxelArray.length == 0) return result;
 
        width = voxelArray.length;
        height = voxelArray[0].length;
 
        x = 0; 
        y = 0;
        
        blockFound = false;
        
        while(!blockFound){
        	if(voxelArray[x++][y].type != VoxelType.AIR){
        		blockFound = true;
        	}
        	if(x == width - 1){
        		x = 0;
        		y++;
        	}
        	if(y > height - 1){
        		return result;
        	}
        }
    }
	
	public VoxelType getVoxelNorth(Voxel[][] voxelArray,int currentX, int currentY){
		if(currentY != 0){
			return voxelArray[currentX][currentY-1].type;
		}else{
			return VoxelType.AIR;
		}
	}
	public VoxelType getVoxelSouth(Voxel[][] voxelArray,int currentX, int currentY){
		if(currentY != voxelArray[0].length - 1){
			return voxelArray[currentX][currentY+1].type;
		}
		return VoxelType.AIR;
	}
	public VoxelType getVoxelEast(Voxel[][] voxelArray,int currentX, int currentY){
		if(currentX != voxelArray.length - 1){
			return voxelArray[currentX+1][currentY].type;
		}
		return VoxelType.AIR;
	}
	public VoxelType getVoxelWest(Voxel[][] voxelArray,int currentX, int currentY){
		if(currentX != 0){
			return voxelArray[currentX-1][currentY].type;
		}
		return VoxelType.AIR;
	}
	public VoxelType getVoxelNorthWest(Voxel[][] voxelArray,int currentX, int currentY){
		if(currentY != 0 && currentX != 0){
			return voxelArray[currentX-1][currentY - 1].type;
		}
		return VoxelType.AIR;
	}
	public VoxelType getVoxelSouthWest(Voxel[][] voxelArray,int currentX, int currentY){
		if(currentX != 0 && currentY != voxelArray[0].length - 1){
			return voxelArray[currentX-1][currentY + 1].type;
		}
		return VoxelType.AIR;
	}
	public VoxelType getVoxelNorthEast(Voxel[][] voxelArray,int currentX, int currentY){
		if(currentY != 0 && currentX != voxelArray.length - 1){
			return voxelArray[currentX + 1][currentY - 1].type;
		}
		return VoxelType.AIR;
	}
	public VoxelType getVoxelSouthEast(Voxel[][] voxelArray,int currentX, int currentY){
		if(currentY != voxelArray[0].length - 1 && currentX != voxelArray.length - 1){
			return voxelArray[currentX-1][currentY- 1].type;
		}
		return VoxelType.AIR;
	}
}

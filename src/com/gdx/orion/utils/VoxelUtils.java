package com.gdx.orion.utils;

import java.util.ArrayList;

import com.gdx.orion.entities.voxel.Voxel;
import com.gdx.orion.entities.voxel.VoxelType;

public class VoxelUtils {

	public static int blockFoundX = 0;
	public static int blockFoundY = 0;
	public static enum Direction {

		RIGHT_DOWN, LEFT_UP;
		
		private Direction() {
			
		}
		
	}
	public static int width;
	public static int height;

	public static int x; 
	public static int y;
	public static boolean blockFound = false;
	public static boolean chainComplete = false;
	public static Direction currentDirection = Direction.RIGHT_DOWN;
	public static ArrayList<Float> result = new ArrayList<Float>();
	public static ArrayList<Voxel> unChecked = new ArrayList<Voxel>();
	public static ArrayList<Float> buildQueue = new ArrayList<Float>();

    public static ArrayList<Float> getOuterShell(Voxel[][] voxelArray) {
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
        		System.out.println("ARRAY CONTAINS NO VOXELS (VoxelUtils.java)");
        		return null;
        	}
        }
        currentDirection = Direction.RIGHT_DOWN;
        while(!chainComplete){
        	
        }
        return result;
    }
	
	private static boolean isComplete() {
		return false;
	}

	private static void cleanUpExtra() {
		// TODO Auto-generated method stub
		
	}

	private static boolean alreadyInQueue(float f, float g) {
		// TODO Auto-generated method stub
		return false;
	}

	public static VoxelType getVoxelUp(Voxel[][] voxelArray,int currentX, int currentY){
		if(currentY != 0){
			return voxelArray[currentX][currentY-1].type;
		}else{
			return VoxelType.AIR;
		}
	}
	public static VoxelType getVoxelDown(Voxel[][] voxelArray,int currentX, int currentY){
		if(currentY != voxelArray[0].length - 1){
			return voxelArray[currentX][currentY+1].type;
		}
		return VoxelType.AIR;
	}
	public static VoxelType getVoxelRight(Voxel[][] voxelArray,int currentX, int currentY){
		if(currentX != voxelArray.length - 1){
			return voxelArray[currentX+1][currentY].type;
		}
		return VoxelType.AIR;
	}
	public static VoxelType getVoxelLeft(Voxel[][] voxelArray,int currentX, int currentY){
		if(currentX != 0){
			return voxelArray[currentX-1][currentY].type;
		}
		return VoxelType.AIR;
	}
	public static VoxelType getVoxelUpLeft(Voxel[][] voxelArray,int currentX, int currentY){
		if(currentY != 0 && currentX != 0){
			return voxelArray[currentX-1][currentY - 1].type;
		}
		return VoxelType.AIR;
	}
	public static VoxelType getVoxelDownLeft(Voxel[][] voxelArray,int currentX, int currentY){
		if(currentX != 0 && currentY != voxelArray[0].length - 1){
			return voxelArray[currentX-1][currentY + 1].type;
		}
		return VoxelType.AIR;
	}
	public static VoxelType getVoxelUpRight(Voxel[][] voxelArray,int currentX, int currentY){
		if(currentY != 0 && currentX != voxelArray.length - 1){
			return voxelArray[currentX + 1][currentY - 1].type;
		}
		return VoxelType.AIR;
	}
	public static VoxelType getVoxelDownRight(Voxel[][] voxelArray,int currentX, int currentY){
		if(currentY != voxelArray[0].length - 1 && currentX != voxelArray.length - 1){
			return voxelArray[currentX-1][currentY- 1].type;
		}
		return VoxelType.AIR;
	}
	public static void addTopWall(){
		buildQueue.add(x * Voxel.voxelSize + Voxel.voxelSize);
		buildQueue.add(y * Voxel.voxelSize);
	} 
	public static void addRightWall(){
		buildQueue.add(x * Voxel.voxelSize + Voxel.voxelSize);
		buildQueue.add(y * Voxel.voxelSize);
		buildQueue.add(x * Voxel.voxelSize + Voxel.voxelSize);
		buildQueue.add(y * Voxel.voxelSize - Voxel.voxelSize);
	}
	public static void addBottomWall(){
		buildQueue.add(x * Voxel.voxelSize + Voxel.voxelSize);
		buildQueue.add(y * Voxel.voxelSize - Voxel.voxelSize);
		buildQueue.add(x * Voxel.voxelSize);
		buildQueue.add(y * Voxel.voxelSize - Voxel.voxelSize);
	}
	public static void addLeftWall(){
		buildQueue.add(x * Voxel.voxelSize);
		buildQueue.add(y * Voxel.voxelSize - Voxel.voxelSize);
		buildQueue.add(x * Voxel.voxelSize);
		buildQueue.add(y * Voxel.voxelSize);
	}
}

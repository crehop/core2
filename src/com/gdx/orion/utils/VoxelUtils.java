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
	public static int completeX;
	public static int completeY;
	public static int count;
	public static Direction direction = Direction.RIGHT_DOWN;
	public static boolean blockFound = false;
	public static boolean chainComplete = false;
	public static ArrayList<Float> result = new ArrayList<Float>();
	public static ArrayList<Voxel> unChecked = new ArrayList<Voxel>();
	public static ArrayList<Float> buildQueue = new ArrayList<Float>();

    public static ArrayList<Float> getOuterShell(Voxel[][] voxelArray) {
    	buildQueue.clear();
        if(voxelArray == null || voxelArray.length == 0) return result;
 
        width = voxelArray.length;
        height = voxelArray[0].length;
 
        x = 0; 
        y = 0;
        
        blockFound = false;
        while(!blockFound){
        	if(voxelArray[x][y].type != VoxelType.AIR){
        		blockFound = true;
        		completeX = x;
        		completeY = y;
        	}else if(x == width - 1){
        		x = 0;
        		y++;
        	}else if(y > height - 1){
        		System.out.println("ARRAY CONTAINS NO VOXELS (VoxelUtils.java)");
        		return null;
        	}
        }
        direction = Direction.RIGHT_DOWN;
        count = 0;
        while(!chainComplete){
        	count++;
        	if(direction == Direction.RIGHT_DOWN){
            	if(getVoxelUpRight(voxelArray,x,y) != VoxelType.AIR &&
            			getVoxelRight(voxelArray,x,y) != VoxelType.AIR && 
            			getVoxelUp(voxelArray,x,y) == VoxelType.AIR){
            		System.out.println("FUNCTION 1:" + x + "/" + y);
            		if(isComplete()){
            			chainComplete = true;
            			break;
            		}
            		addTopWall();
            		x++;
            		y--;
            	}else if(getVoxelRight(voxelArray,x,y) != VoxelType.AIR &&
            			getVoxelUp(voxelArray,x,y) == VoxelType.AIR){
            		System.out.println("FUNCTION 2:" + x + "/" + y);
            		if(isComplete()){
            			chainComplete = true;
            			break;
            		}
            		addTopWall();
            		x++;
            	}else if(getVoxelDownRight(voxelArray,x,y) != VoxelType.AIR &&
            			getVoxelDown(voxelArray,x,y) != VoxelType.AIR &&
            			getVoxelUp(voxelArray,x,y) == VoxelType.AIR &&
            			getVoxelRight(voxelArray,x,y) == VoxelType.AIR){
            		System.out.println("FUNCTION 3:" + x + "/" + y);
            		if(isComplete()){
            			chainComplete = true;
            			break;
            		}
            		addTopWall();
            		addRightWall();
            		x++;
            		y++;
            	}else if(getVoxelDown(voxelArray,x,y) != VoxelType.AIR &&
            			getVoxelUp(voxelArray,x,y) == VoxelType.AIR &&
            			getVoxelRight(voxelArray,x,y) == VoxelType.AIR){
            		System.out.println("FUNCTION 4:" + x + "/" + y + "/" +getVoxelRight(voxelArray,x,y));
            		if(isComplete()){
            			chainComplete = true;
            			break;
            		}
            		addTopWall();
            		addRightWall();
            		y++;
            	}else if(getVoxelDownLeft(voxelArray,x,y) != VoxelType.AIR && 
            			getVoxelLeft(voxelArray,x,y) != VoxelType.AIR &&
            			getVoxelDown(voxelArray,x,y) == VoxelType.AIR ){
            		System.out.println("FUNCTION 5:" + x + "/" + y);
            		if(isComplete()){
            			chainComplete = true;
            			break;
            		}
            		addBottomWall();
            		y++;
            		x--;
            	}else if(getVoxelLeft(voxelArray,x,y) != VoxelType.AIR &&
            			getVoxelDown(voxelArray,x,y) == VoxelType.AIR){
            		System.out.println("FUNCTION 6:" + x + "/" + y);
            		if(isComplete()){
            			chainComplete = true;
            			break;
            		}
            		addTopWall();
            		addRightWall();
            		addBottomWall();
            		x--;
            	}else if(getVoxelUpLeft(voxelArray,x,y) != VoxelType.AIR &&
            			getVoxelUp(voxelArray,x,y) != VoxelType.AIR &&
            			getVoxelDown(voxelArray,x,y) == VoxelType.AIR &&
            			getVoxelLeft(voxelArray,x,y) == VoxelType.AIR){
            		System.out.println("FUNCTION 7:" + x + "/" + y);
            		
            		if(isComplete()){
            			chainComplete = true;
            			break;
            		}
            		addBottomWall();
            		addLeftWall();
            		x--;
            		y--;
            	}else if(getVoxelUp(voxelArray,x,y) != VoxelType.AIR &&
            			getVoxelDown(voxelArray,x,y) == VoxelType.AIR &&
            			getVoxelLeft(voxelArray,x,y) == VoxelType.AIR){
            		System.out.println("FUNCTION 8:" + x + "/" + y);
            		if(isComplete()){
            			chainComplete = true;
            			break;
            		}
            		addBottomWall();
            		addLeftWall();
            		y--;
            	}else if (getVoxelUp(voxelArray,x,y) != VoxelType.AIR){
            		System.out.println("FUNCTION 9:" + x + "/" + y);
            		if(getVoxelRight(voxelArray,x,y) == VoxelType.AIR){
                		addRightWall();
            		}
            		if(getVoxelDown(voxelArray,x,y) == VoxelType.AIR){
                		addRightWall();
                		addBottomWall();
            		}
            		if(getVoxelLeft(voxelArray,x,y) == VoxelType.AIR){
                		addRightWall();
                		addBottomWall();
                		addLeftWall();
            		}
        			chainComplete = true;
        			break;
            	}else{
            		System.out.println("FUNCTION 10:" + x + "/" + y);
            		addTopWall();
            		addRightWall();
            		addBottomWall();
            		addLeftWall();
        			chainComplete = true;
        			break;
            	}
        	}
        }
        return buildQueue;
    }
	
	private static boolean isComplete() {
		if(x == completeX && y == completeY && count != 1){
			return true;
		}else{
			return false;
		}
	}

	private static void cleanUpExtra() {
		// TODO Auto-generated method stub
		
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
		if(currentX < voxelArray.length - 1){
			return voxelArray[currentX +1]
					[currentY].type;
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
		if(currentX != 0 && currentY < voxelArray[0].length - 1){
			return voxelArray[currentX-1][currentY + 1].type;
		}
		return VoxelType.AIR;
	}
	public static VoxelType getVoxelUpRight(Voxel[][] voxelArray,int currentX, int currentY){
		if(currentY != 0 && currentX < voxelArray.length - 1){
			return voxelArray[currentX + 1][currentY - 1].type;
		}
		return VoxelType.AIR;
	}
	public static VoxelType getVoxelDownRight(Voxel[][] voxelArray,int currentX, int currentY){
		if(currentX < voxelArray.length - 1 &&
				currentY < voxelArray[0].length - 1 && currentX > 0 && currentY > 0){
			return voxelArray[currentX-1][currentY - 1].type;
		}
		return VoxelType.AIR;
	}
	public static void addTopWall(){
		System.out.println("Added TOP Wall" + " :" + buildQueue.size());
		buildQueue.add(x * Voxel.voxelSize + Voxel.voxelSize);
		buildQueue.add(y * Voxel.voxelSize);
	} 
	public static void addRightWall(){
		System.out.println("Added RIGHT Wall" + " :" + buildQueue.size());
		buildQueue.add(x * Voxel.voxelSize + Voxel.voxelSize);
		buildQueue.add(y * Voxel.voxelSize - Voxel.voxelSize);
	}
	public static void addBottomWall(){
		System.out.println("Added BOTTOM Wall" + " :" + buildQueue.size());
		buildQueue.add(x * Voxel.voxelSize);
		buildQueue.add(y * Voxel.voxelSize - Voxel.voxelSize);

	}
	public static void addLeftWall(){
		System.out.println("Added LEFT Wall" + " :" + buildQueue.size());
		buildQueue.add(x * Voxel.voxelSize);
		buildQueue.add(y * Voxel.voxelSize);
	}
}

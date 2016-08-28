package com.gdx.orion.utils;

import java.util.ArrayList;

import com.gdx.orion.entities.voxel.Voxel;
import com.gdx.orion.entities.voxel.VoxelShell;
import com.gdx.orion.entities.voxel.VoxelShellSegment;
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
	public static int right = 1;
	public static int left = -1;
	public static int up = 1;
	public static int down = -1;
	
	public static Direction direction = Direction.RIGHT_DOWN;
	public static boolean blockFound = false;
	public static boolean chainComplete = false;
	public static ArrayList<Float> buildQueue = new ArrayList<Float>();
	public static ArrayList<VoxelShellSegment> segQueue = new ArrayList<VoxelShellSegment>();
	public static VoxelShell shell;

    public static VoxelShell getOuterShell(Voxel[][] voxelArray) {
    	shell = null;
    	buildQueue.clear();
        if(voxelArray == null || voxelArray.length == 0) return shell;
 
        width = voxelArray.length;
        height = voxelArray[0].length;
 
        x = 0; 
        y = voxelArray[0].length -1;
        blockFound = false;
        while(!blockFound){
        	System.out.println(voxelArray[x][y].type + " " + x + ":"+ y);
        	if(voxelArray[x++][y].type != VoxelType.AIR){
        		x--;
        		blockFound = true;
        		completeX = x;
        		completeY = y;
        	}else if(x == width - 1){
        		x = 0;
        		y--;
        	}else if(y < 0){
        		System.out.println("ARRAY CONTAINS NO VOXELS (VoxelUtils.java)");
        		return null;
        	}
        }
        direction = Direction.RIGHT_DOWN;
        count = 0;
		System.out.println("Start:" + x + ":" +y);
        while(!chainComplete){
        	count++;
    		System.out.println("Direction:" + direction);
        	if(direction == Direction.RIGHT_DOWN){
               	if(getVoxelUpRight(voxelArray,x,y) != VoxelType.AIR
               			&& getVoxelRight(voxelArray,x,y) != VoxelType.AIR
               			&& getVoxelUp(voxelArray,x,y) == VoxelType.AIR){
               		x++;
               		y++;
               		addLeftWall();
               		addTopWall();
               		addRightWall();
               		addBottomWall();
               		createSegment();
               		System.out.println("FUNCTION 1: UP -> RIGHT:" + x + "/" + y);
               		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
               		direction = Direction.LEFT_UP;
               	}else if(getVoxelRight(voxelArray,x,y) != VoxelType.AIR &&
               			getVoxelUpRight(voxelArray,x,y) == VoxelType.AIR){
               		addBottomWall();
               		addLeftWall();
               		while(getVoxelRight(voxelArray,x,y) != VoxelType.AIR &&
               				getVoxelUpRight(voxelArray,x,y) == VoxelType.AIR){
               			x++;
               		}
               		addTopWall();
               		addRightWall();
               		createSegment();
               		System.out.println("FUNCTION 2:RIGHT -> :" + x + "/" + y);
               		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
               		
               	}else if(getVoxelDownRight(voxelArray,x,y) != VoxelType.AIR 
               			&& getVoxelDown(voxelArray,x,y) !=  VoxelType.AIR
               			&& getVoxelRight(voxelArray,x,y) ==  VoxelType.AIR){
               		x++;
               		y--;
               		addLeftWall();
               		addTopWall();
               		addRightWall();
               		addBottomWall();
               		createSegment();
               		System.out.println("FUNCTION 3:RIGHT -> DOWN V " + x + "/" + y);
               		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
               	}else if(getVoxelDown(voxelArray,x,y) != VoxelType.AIR
               			&& getVoxelRight(voxelArray,x,y) == VoxelType.AIR){
               		addLeftWall();
               		addTopWall();
               		while(getVoxelDown(voxelArray,x,y) != VoxelType.AIR
               				&& getVoxelRight(voxelArray,x,y) == VoxelType.AIR){
               			y--;
               		}
               		addRightWall();
               		addBottomWall();
               		createSegment();
               		System.out.println("FUNCTION 4:DOWN V" + x + "/" + y);
               		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
               	}else if(getVoxelDownLeft(voxelArray,x,y) != VoxelType.AIR 
               			&& getVoxelDown(voxelArray,x,y) != VoxelType.AIR
               			&& getVoxelLeft(voxelArray,x,y) == VoxelType.AIR){
               		y--;
               		x--;
               		addLeftWall();
               		addTopWall();
               		addRightWall();
               		addBottomWall();
               		createSegment();
               		System.out.println("FUNCTION 5:DOWN V LEFT <" + x + "/" + y);
               		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
               	}else if(getVoxelLeft(voxelArray,x,y) != VoxelType.AIR &&
               			getVoxelUpLeft(voxelArray,x,y) == VoxelType.AIR){
               		addTopWall();
               		addRightWall();
               		while(getVoxelLeft(voxelArray,x,y) != VoxelType.AIR &&
                   			getVoxelUpLeft(voxelArray,x,y) == VoxelType.AIR){
               			x--;
               		}
               		addBottomWall();
               		addLeftWall();
               		createSegment();
               		System.out.println("FUNCTION 6: LEFT <" + x + "/" + y);
               		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
               		direction = Direction.LEFT_UP;
               	}else if(getVoxelUpLeft(voxelArray,x,y) != VoxelType.AIR 
               			&& getVoxelUp(voxelArray,x,y) != VoxelType.AIR
               			&& getVoxelLeft(voxelArray,x,y) == VoxelType.AIR){
               		x--;
               		y++; 
               		addTopWall();
               		addRightWall();
               		addTopWall();
               		addRightWall();
               		createSegment();
               		System.out.println("FUNCTION 7: LEFT < UP ^" + x + "/" + y);
               		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
               	}else if(getVoxelUp(voxelArray,x,y) != VoxelType.AIR &&
           			getVoxelLeft(voxelArray,x,y) == VoxelType.AIR){
               		addRightWall();
               		addBottomWall();
            		while(getVoxelUp(voxelArray,x,y) != VoxelType.AIR &&
            			getVoxelLeft(voxelArray,x,y) == VoxelType.AIR){
            			y++;
            		}
               		addLeftWall();
               		addTopWall();
               		createSegment();
               		System.out.println("FUNCTION 8:UP ^" + x + "/" + y);
            		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
            		direction = Direction.LEFT_UP;
               	}else{
               		direction = Direction.LEFT_UP;
               		System.out.println("FUNCTION NONE-FOUND: LEFT <" + x + "/" + y);
               	}
        	}else if(direction == Direction.LEFT_UP){
        		if(getVoxelLeft(voxelArray,x,y) != VoxelType.AIR &&
               			getVoxelDown(voxelArray,x,y) == VoxelType.AIR){
               		addTopWall();
               		addRightWall();
               		while(getVoxelLeft(voxelArray,x,y) != VoxelType.AIR &&
               				getVoxelDown(voxelArray,x,y) == VoxelType.AIR){
               			x--;
               		}
               		addBottomWall();
               		addLeftWall();
               		createSegment();
               		System.out.println("FUNCTION 10: LEFT <" + x + "/" + y);
               		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
        		}else if(getVoxelUpLeft(voxelArray,x,y) != VoxelType.AIR 
               			&& getVoxelUp(voxelArray,x,y) != VoxelType.AIR
               			&& getVoxelLeft(voxelArray,x,y) == VoxelType.AIR){
               		x--;
               		y++; 
               		addTopWall();
               		addRightWall();
               		addTopWall();
               		addRightWall();
               		createSegment();
               		System.out.println("FUNCTION 11: LEFT < UP ^" + x + "/" + y);
               		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
               	}else if(getVoxelUp(voxelArray,x,y) != VoxelType.AIR &&
           			getVoxelLeft(voxelArray,x,y) == VoxelType.AIR){
               		addRightWall();
               		addBottomWall();
               		while(getVoxelUp(voxelArray,x,y) != VoxelType.AIR &&
               				getVoxelLeft(voxelArray,x,y) == VoxelType.AIR){
               			y++;
               		}
               		addLeftWall();
               		addTopWall();
               		createSegment();
               		System.out.println("FUNCTION 12: UP ^" + x + "/" + y);
               		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
               	}else if(getVoxelUpRight(voxelArray,x,y) != VoxelType.AIR
               			&& getVoxelRight(voxelArray,x,y) != VoxelType.AIR
               			&& getVoxelUp(voxelArray,x,y) == VoxelType.AIR){
               		x++;
               		y++;
               		addTopWall();
               		addRightWall();
               		addBottomWall();
               		addLeftWall();
               		createSegment();
               		System.out.println("FUNCTION 13:UP ^ RIGHT >" + x + "/" + y);
               		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
               	}else if(getVoxelRight(voxelArray,x,y) != VoxelType.AIR &&
               			getVoxelUp(voxelArray,x,y) == VoxelType.AIR){
               		addBottomWall();
               		addLeftWall();
               		while(getVoxelRight(voxelArray,x,y) != VoxelType.AIR &&
               				getVoxelUp(voxelArray,x,y) == VoxelType.AIR){
               			x++;
               		}
               		addTopWall();
               		addRightWall();
               		createSegment();
               		System.out.println("FUNCTION 14: RIGHT >" + x + "/" + y);
               		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
               		direction = Direction.RIGHT_DOWN;
               		//COMP
               	}else if(getVoxelDownRight(voxelArray,x,y) != VoxelType.AIR 
               			&& getVoxelDown(voxelArray,x,y) !=  VoxelType.AIR
               			&& getVoxelRight(voxelArray,x,y) ==  VoxelType.AIR){
               		x++;
               		y--;
               		addLeftWall();
               		addTopWall();
               		addRightWall();
               		addBottomWall();
               		createSegment();
               		System.out.println("FUNCTION 15: RIGHT > DOWN V" + x + "/" + y);
               		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
               		direction = Direction.RIGHT_DOWN;
               		//COMP
               	}else if(getVoxelDown(voxelArray,x,y) != VoxelType.AIR
               			&& getVoxelRight(voxelArray,x,y) == VoxelType.AIR){
               		addLeftWall();
               		addTopWall();
               		while(getVoxelDown(voxelArray,x,y) != VoxelType.AIR
               				&& getVoxelRight(voxelArray,x,y) == VoxelType.AIR){
               			y--;
               		}
               		addRightWall();
               		addBottomWall();
               		createSegment();
               		System.out.println("FUNCTION 16:DOWN V" + x + "/" + y);
               		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
               		direction = Direction.RIGHT_DOWN;
               	}else if(getVoxelDownLeft(voxelArray,x,y) != VoxelType.AIR 
               			&& getVoxelDown(voxelArray,x,y) != VoxelType.AIR
               			&& getVoxelLeft(voxelArray,x,y) == VoxelType.AIR){
               		y--;
               		x--;
               		addTopWall();
               		addRightWall();
               		addBottomWall();
               		addLeftWall();
               		createSegment();
               		System.out.println("FUNCTION 9: LEFT < DOWN V" + x + "/" + y);
               		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
               		direction = Direction.RIGHT_DOWN;
               	}else{
               		direction = Direction.RIGHT_DOWN;
               		System.out.println("FUNCTION NONE-FOUND: RIGHT >" + x + "/" + y);
               	}
        	}
           	
        }
        return shell;
    }
	
	private static void createSegment() {
		if(buildQueue.size() == 8){
			segQueue.add(new VoxelShellSegment(buildQueue.get(0),buildQueue.get(1),buildQueue.get(2)
					,buildQueue.get(3),buildQueue.get(4),buildQueue.get(5),buildQueue.get(6)
					,buildQueue.get(7)));
		}
		buildQueue.clear();
	}



	private static boolean isComplete() {
		if((x == completeX && y == completeY && count != 1) || count == 9){
			shell = new VoxelShell(segQueue);
			return true;
		}else{
			return false;
		}
	}

	private static void cleanUpExtra() {
		// TODO Auto-generated method stub
		
	}

	public static VoxelType getVoxelUp(Voxel[][] voxelArray,int currentX, int currentY){
		try{
			return voxelArray[currentX][currentY + up].type;
		}catch(java.lang.ArrayIndexOutOfBoundsException e){
			return VoxelType.AIR;
		}
	}
	public static VoxelType getVoxelDown(Voxel[][] voxelArray,int currentX, int currentY){
		try{
			return voxelArray[currentX][currentY + down].type;
		}catch(java.lang.ArrayIndexOutOfBoundsException e){
			return VoxelType.AIR;
		}
	}
	public static VoxelType getVoxelRight(Voxel[][] voxelArray,int currentX, int currentY){
		try{	
			return voxelArray[currentX + right][currentY].type;
		}catch(java.lang.ArrayIndexOutOfBoundsException e){
			return VoxelType.AIR;
		}
	}
	public static VoxelType getVoxelLeft(Voxel[][] voxelArray,int currentX, int currentY){
		try{	
			return voxelArray[currentX + left][currentY].type;
		}catch(java.lang.ArrayIndexOutOfBoundsException e){
			return VoxelType.AIR;
		}
	}
	public static VoxelType getVoxelUpLeft(Voxel[][] voxelArray,int currentX, int currentY){
		try{
			return voxelArray[currentX + left][currentY + up].type;
		}catch(java.lang.ArrayIndexOutOfBoundsException e){
			return VoxelType.AIR;
		}
	}
	public static VoxelType getVoxelDownLeft(Voxel[][] voxelArray,int currentX, int currentY){
		try{
			return voxelArray[currentX + left][currentY + down].type;
		}catch(java.lang.ArrayIndexOutOfBoundsException e){
			return VoxelType.AIR;
		}
	}
	public static VoxelType getVoxelUpRight(Voxel[][] voxelArray,int currentX, int currentY){
		try{
			return voxelArray[currentX + right][currentY + up].type;
		}catch(java.lang.ArrayIndexOutOfBoundsException e){
			return VoxelType.AIR;
		}
	}
	public static VoxelType getVoxelDownRight(Voxel[][] voxelArray,int currentX, int currentY){
		try{
			return voxelArray[currentX + right][currentY + down].type;
		}catch(java.lang.ArrayIndexOutOfBoundsException e){
			return VoxelType.AIR;
		}
	}
	public static void addTopWall(){
		//System.out.println("Added TOP Wall" + " :" + buildQueue.size());
		buildQueue.add(x * Voxel.voxelSize + Voxel.voxelSize);
		buildQueue.add(y * Voxel.voxelSize);
		//System.out.println("X:" + (x * Voxel.voxelSize + Voxel.voxelSize) + " Y:" + (y * Voxel.voxelSize));
	} 
	public static void addRightWall(){
		//System.out.println("Added RIGHT Wall" + " :" + buildQueue.size());
		buildQueue.add(x * Voxel.voxelSize + Voxel.voxelSize);
		buildQueue.add(y * Voxel.voxelSize - Voxel.voxelSize);
		//System.out.println("X:" + (x * Voxel.voxelSize + Voxel.voxelSize) + " Y:" + (y * Voxel.voxelSize - Voxel.voxelSize));

	}
	public static void addBottomWall(){
		//System.out.println("Added BOTTOM Wall" + " :" + buildQueue.size());
		buildQueue.add(x * Voxel.voxelSize);
		buildQueue.add(y * Voxel.voxelSize - Voxel.voxelSize);
		//System.out.println("X:" + (x * Voxel.voxelSize) + " Y:" + (y * Voxel.voxelSize - Voxel.voxelSize));

	}
	public static void addLeftWall(){
		//System.out.println("Added LEFT Wall" + " :" + buildQueue.size());
		buildQueue.add(x * Voxel.voxelSize);
		buildQueue.add(y * Voxel.voxelSize);
		//System.out.println("X:" + x * (Voxel.voxelSize) + " Y:" + (y * Voxel.voxelSize));
	}
}

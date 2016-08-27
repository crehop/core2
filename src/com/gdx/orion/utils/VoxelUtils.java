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
    		System.out.println("Direction:" + direction);
        	if(direction == Direction.RIGHT_DOWN){
               	if(getVoxelUpRight(voxelArray,x,y) != VoxelType.AIR
               			&& getVoxelRight(voxelArray,x,y) != VoxelType.AIR
               			&& getVoxelUp(voxelArray,x,y) == VoxelType.AIR){
               		System.out.println("FUNCTION 1: UP -> RIGHT:" + x + "/" + y);
               		x++;
               		y--;
               		addLeftWall();
               		addTopWall();
               		addRightWall();
               		addBottomWall();
               		createSegment();
               		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
               		direction = Direction.LEFT_UP;
               	}else if(getVoxelRight(voxelArray,x,y) != VoxelType.AIR &&
               			getVoxelUp(voxelArray,x,y) == VoxelType.AIR){
               		System.out.println("FUNCTION 2:RIGHT ^ -> :" + x + "/" + y);
               		addBottomWall();
               		addLeftWall();
               		while(getVoxelRight(voxelArray,x,y) != VoxelType.AIR &&
               				getVoxelUp(voxelArray,x,y) == VoxelType.AIR &&
               				getVoxelUpRight(voxelArray,x,y) ==VoxelType.AIR){
               			x++;
               		}
               		addTopWall();
               		addRightWall();
               		createSegment();
               		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
               	}else if(getVoxelRightDown(voxelArray,x,y) != VoxelType.AIR 
               			&& getVoxelDown(voxelArray,x,y) !=  VoxelType.AIR
               			&& getVoxelRight(voxelArray,x,y) ==  VoxelType.AIR){
               		System.out.println("FUNCTION 3:RIGHT -> DOWN V " + x + "/" + y);
               		x++;
               		y++;
               		addLeftWall();
               		addTopWall();
               		addRightWall();
               		addBottomWall();
               		createSegment();
               		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
               	}else if(getVoxelDown(voxelArray,x,y) != VoxelType.AIR
               			&& getVoxelRight(voxelArray,x,y) == VoxelType.AIR){
               		System.out.println("FUNCTION 4:DOWN V" + x + "/" + y);
               		addLeftWall();
               		addTopWall();
               		while(getVoxelDown(voxelArray,x,y) != VoxelType.AIR
               				&& getVoxelRight(voxelArray,x,y) == VoxelType.AIR){
               			y++;
               		}
               		addRightWall();
               		addBottomWall();
               		createSegment();
               		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
               	}else if(getVoxelDownLeft(voxelArray,x,y) != VoxelType.AIR 
               			&& getVoxelDown(voxelArray,x,y) != VoxelType.AIR
               			&& getVoxelLeft(voxelArray,x,y) == VoxelType.AIR){
               		System.out.println("FUNCTION 5:DOWN V LEFT <" + x + "/" + y);
               		y++;
               		x--;
               		addLeftWall();
               		addTopWall();
               		addRightWall();
               		addBottomWall();
               		createSegment();
               		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
               	}else if(getVoxelLeft(voxelArray,x,y) != VoxelType.AIR &&
               			getVoxelDown(voxelArray,x,y) == VoxelType.AIR){
               		System.out.println("FUNCTION 6: LEFT <" + x + "/" + y);
               		addTopWall();
               		addRightWall();
               		while(getVoxelLeft(voxelArray,x,y) != VoxelType.AIR &&
               				getVoxelDown(voxelArray,x,y) == VoxelType.AIR){
               			x--;
               		}
               		addBottomWall();
               		addLeftWall();
               		createSegment();
               		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
               		direction = Direction.LEFT_UP;
               	}else if(getVoxelUpLeft(voxelArray,x,y) != VoxelType.AIR 
               			&& getVoxelUp(voxelArray,x,y) != VoxelType.AIR
               			&& getVoxelLeft(voxelArray,x,y) == VoxelType.AIR){
               		System.out.println("FUNCTION 7 LEFT > UP ^:" + x + "/" + y);
               		x--;
               		y--;
               		addLeftWall();    
               		addTopWall();
               		addRightWall();
               		addBottomWall();
               		createSegment();
                	if(isComplete()){
               			chainComplete = true;
               			break;
               		}
                	direction = Direction.LEFT_UP;
               	}else if(getVoxelUp(voxelArray,x,y) != VoxelType.AIR &&
           			getVoxelLeft(voxelArray,x,y) == VoxelType.AIR){
               		System.out.println("FUNCTION 8:UP ^" + x + "/" + y);
               		addRightWall();
               		addBottomWall();
            		while(getVoxelUp(voxelArray,x,y) != VoxelType.AIR &&
            			getVoxelLeft(voxelArray,x,y) == VoxelType.AIR){
            			y--;
            		}
            		addLeftWall();
            		addTopWall();
               		createSegment();
            		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
            		direction = Direction.LEFT_UP;
               	}else{
               		System.out.println("FUNCTION 9: BOX" + x + "/" + y);
               		addTopWall();
               		addRightWall();
               		addBottomWall();
               		addLeftWall();
               		createSegment();
               		chainComplete = true;
               		break;
               	}
        	}else if(direction == Direction.LEFT_UP){
        		if(getVoxelDownLeft(voxelArray,x,y) != VoxelType.AIR 
               			&& getVoxelDown(voxelArray,x,y) != VoxelType.AIR
               			&& getVoxelLeft(voxelArray,x,y) == VoxelType.AIR){
               		System.out.println("FUNCTION 17: LEFT < DOWN V" + x + "/" + y);
               		y++;
               		x--;
               		addTopWall();
               		addRightWall();
               		addBottomWall();
               		addLeftWall();
               		createSegment();
               		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
               		direction = Direction.RIGHT_DOWN;
               	}else if(getVoxelLeft(voxelArray,x,y) != VoxelType.AIR &&
               			getVoxelDown(voxelArray,x,y) == VoxelType.AIR){
               		System.out.println("FUNCTION 10: LEFT <" + x + "/" + y);
               		addTopWall();
               		addRightWall();
               		while(getVoxelLeft(voxelArray,x,y) != VoxelType.AIR &&
               				getVoxelDown(voxelArray,x,y) == VoxelType.AIR){
               			x--;
               		}
               		addBottomWall();
               		addLeftWall();
               		createSegment();
               		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
        		}else if(getVoxelUpLeft(voxelArray,x,y) != VoxelType.AIR 
               			&& getVoxelUp(voxelArray,x,y) != VoxelType.AIR
               			&& getVoxelLeft(voxelArray,x,y) == VoxelType.AIR){
               		System.out.println("FUNCTION 11: LEFT < UP ^" + x + "/" + y);
               		x--;
               		y--; 
               		addTopWall();
               		addRightWall();
               		addBottomWall();
               		addLeftWall();
               		createSegment();
               		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
               	}else if(getVoxelUp(voxelArray,x,y) != VoxelType.AIR &&
           			getVoxelLeft(voxelArray,x,y) == VoxelType.AIR){
               		System.out.println("FUNCTION 12: UP ^" + x + "/" + y);
               		addRightWall();
               		addBottomWall();
               		while(getVoxelUp(voxelArray,x,y) != VoxelType.AIR &&
               				getVoxelLeft(voxelArray,x,y) == VoxelType.AIR){
               			y--;
               		}
               		addLeftWall();
               		addTopWall();
               		createSegment();
               		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
               	}else if(getVoxelUpRight(voxelArray,x,y) != VoxelType.AIR
               			&& getVoxelRight(voxelArray,x,y) != VoxelType.AIR
               			&& getVoxelUp(voxelArray,x,y) == VoxelType.AIR){
               		System.out.println("FUNCTION 13:UP ^ RIGHT >" + x + "/" + y);
               		x++;
               		y--;
               		addTopWall();
               		addRightWall();
               		addBottomWall();
               		addLeftWall();
               		createSegment();
               		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
               	}else if(getVoxelRight(voxelArray,x,y) != VoxelType.AIR &&
               			getVoxelUp(voxelArray,x,y) == VoxelType.AIR){
               		System.out.println("FUNCTION 14: RIGHT >" + x + "/" + y);
               		addLeftWall();
               		addTopWall();
               		while(getVoxelRight(voxelArray,x,y) != VoxelType.AIR &&
               				getVoxelUp(voxelArray,x,y) == VoxelType.AIR){
               			x++;
               		}
               		addRightWall();
               		addBottomWall();
               		createSegment();
               		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
               		direction = Direction.RIGHT_DOWN;
               		//COMP
               	}else if(getVoxelRightDown(voxelArray,x,y) != VoxelType.AIR 
               			&& getVoxelDown(voxelArray,x,y) !=  VoxelType.AIR
               			&& getVoxelRight(voxelArray,x,y) ==  VoxelType.AIR){
               		System.out.println("FUNCTION 15: RIGHT > DOWN V" + x + "/" + y);
               		x++;
               		y++;
               		addLeftWall();
               		addTopWall();
               		addRightWall();
               		addBottomWall();
               		createSegment();
               		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
               		direction = Direction.RIGHT_DOWN;
               		//COMP
               	}else if(getVoxelDown(voxelArray,x,y) != VoxelType.AIR
               			&& getVoxelRight(voxelArray,x,y) == VoxelType.AIR){
               		System.out.println("FUNCTION 16:DOWN V" + x + "/" + y);
               		addLeftWall();
               		addTopWall();
               		while(getVoxelDown(voxelArray,x,y) != VoxelType.AIR
               				&& getVoxelRight(voxelArray,x,y) == VoxelType.AIR){
               			y++;
               		}
               		addRightWall();
               		addBottomWall();
               		createSegment();
               		if(isComplete()){
               			chainComplete = true;
               			break;
               		}
               		direction = Direction.RIGHT_DOWN;
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

	private static VoxelType getVoxelRightDown(Voxel[][] voxelArray, int x2,
			int y2) {
		// TODO Auto-generated method stub
		return null;
	}

	private static boolean isComplete() {
		if(x == completeX && y == completeY && count != 1){
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
		if(currentY > 0){
			return voxelArray[currentX][currentY-1].type;
		}else{
			return VoxelType.AIR;
		}
	}
	public static VoxelType getVoxelDown(Voxel[][] voxelArray,int currentX, int currentY){
		if(currentY < voxelArray[0].length - 1 && currentX <= voxelArray.length - 1){
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

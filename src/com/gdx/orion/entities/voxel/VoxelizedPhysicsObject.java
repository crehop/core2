package com.gdx.orion.entities.voxel;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.gdx.orion.utils.VoxelUtils;

public class VoxelizedPhysicsObject {
	
	private Voxel[][] voxelArray;
	private World world;
	@SuppressWarnings("unused")
	private int count = 0;
	private Body[][] bodyArray;
	private BodyDef def;
	private FixtureDef fdef;
	private PolygonShape shape = new PolygonShape();
	private WeldJointDef jdef = new WeldJointDef();

	
	
	public VoxelizedPhysicsObject(Voxel[][] voxels, World world, int xPos, int yPos){
		this.world = world;
		this.voxelArray = voxels;
		this.def = new BodyDef();
		this.fdef = new FixtureDef();
		bodyArray = new Body[voxelArray.length][voxelArray[0].length];
		//CREATE BODYS
		for(int x = 0; x < voxelArray.length; x++){
			for(int y = 0; y < voxelArray[0].length; y++){
				switch(voxelArray[x][y].type){
					case AIR:
						break;
					case STONE:
						def.active = true;
						def.type = BodyType.DynamicBody;
						def.position.set(((x * Voxel.voxelSize) + xPos) + (x * Voxel.voxelGap),((y * Voxel.voxelSize) + yPos) + (y * Voxel.voxelGap)) ;
						fdef.density = voxelArray[x][y].density;
						fdef.friction = voxelArray[x][y].friction;
						shape.setAsBox(Voxel.voxelSize, Voxel.voxelSize);
						fdef.shape = shape;
						bodyArray[x][y] = world.createBody(def);
						bodyArray[x][y].createFixture(fdef);
						break;
					case GRASS:
						break;
					default:
						break;
				}
			}
		}
		//CREATE JOINTS
		for(int x = 0; x < voxelArray.length; x++){
			for(int y = 0; y < voxelArray[0].length; y++){
				switch(voxelArray[x][y].type){
					case AIR:
						break;
					case STONE:
						if(VoxelUtils.getVoxelLeft(voxels, x, y) == VoxelType.STONE){
							jdef.bodyA = bodyArray[x][y];
							jdef.bodyB = bodyArray[x - 1][y];
							jdef.collideConnected = true;
							jdef.localAnchorA.set(bodyArray[x][y].getWorldCenter().x - Voxel.voxelSize, bodyArray[x][y].getWorldCenter().y);
							jdef.localAnchorB.set(bodyArray[x - 1][y].getWorldCenter().x + Voxel.voxelSize, bodyArray[x][y].getWorldCenter().y);
							world.createJoint(jdef);
							jdef.initialize(jdef.bodyA, jdef.bodyB, jdef.bodyB.getPosition().add(-Voxel.voxelSize, 0));
							System.out.println("JOINT CREATED");
						}
						break;
					case GRASS:
						break;
					default:
						break;
				}
			}
		}
	}

	public VoxelizedPhysicsObject() {
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

package com.gdx.orion.handlers;

import com.gdx.orion.entities.EntityData;
import com.gdx.orion.entities.EntityType;
import com.gdx.orion.entities.PlayerShip;

import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.utils.Array;

public class JointHandler {
	private static EntityData entityDataA;
	private static EntityData entityDataB;

	public static void handleJoints(World gameWorld,Array<JointDef> addJoint,
			Array<Joint> clearJoint, PlayerShip ship) {
		for(JointDef def:addJoint){
			gameWorld.createJoint(def);
		}	
		for(Joint joint: clearJoint){
			if(!ship.ropeFired()){
				if(joint.getBodyA().getUserData() instanceof EntityData){
					entityDataA = (EntityData)joint.getBodyA().getUserData();
					if(entityDataA.getType() == EntityType.SHIP && joint.getType() == JointType.RopeJoint){
						if(joint.getBodyB().getUserData() instanceof EntityData){
							entityDataA = (EntityData)joint.getBodyB().getUserData();
							if(entityDataA.getType() == EntityType.SHIELD){
								
							}else{
								gameWorld.destroyJoint(joint);
							}
						}else{
							gameWorld.destroyJoint(joint);
						}
					}
				}else if(joint.getBodyB().getUserData() instanceof EntityData){
					entityDataA = (EntityData)joint.getBodyB().getUserData();
					if(entityDataB.getType() == EntityType.SHIP && joint.getType() == JointType.RopeJoint){
						if(joint.getBodyA().getUserData() instanceof EntityData){
							entityDataA = (EntityData)joint.getBodyA().getUserData();
							if(entityDataA.getType() == EntityType.SHIELD){
								
							}else{
								gameWorld.destroyJoint(joint);
							}
						}else{
							gameWorld.destroyJoint(joint);
						}						
					}
				}
			}
		}
	}

}

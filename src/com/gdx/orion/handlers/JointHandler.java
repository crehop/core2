package com.gdx.orion.handlers;

import com.gdx.orion.entities.EntityData;

import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class JointHandler {
	private static EntityData entityDataA;
	private static EntityData entityDataB;

	public static void handleJoints(World gameWorld,Array<JointDef> addJoint,
			Array<Joint> clearJoint) {
		for(JointDef def:addJoint){
			gameWorld.createJoint(def);
		}	
		for(Joint joint: clearJoint){
			gameWorld.destroyJoint(joint);
		}
	}

}

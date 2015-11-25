package com.gdx.orion.systems;

import gdx.orion.entities.EntityData;
import gdx.orion.entities.EntityType;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.gdx.orion.screens.GameStateManager;

public class Rope {
	private float widthSeg = 0.1f;
	private float heightSeg = 3f;
	private Body anchor;
	private Body grapple;
	private Body[] segments;
	private RevoluteJoint[] revJoints;
	private RopeJoint[] ropeJoints;
	private Joint grappleJointRev;
	private Joint grappleJointRope;
	private Joint anchorJointRev;
	private Joint anchorJointRope;
	private BodyDef def;
	private RevoluteJointDef revDef;
	private RopeJointDef ropeDef;
	private World world;
	private PolygonShape shape;
	private ShapeRenderer sr;
	float stretch = 0.01f;
	Vector2 fireSpot;
	public Rope(Body anchor,Vector2 fireSpot,World world, int length){
		this.anchor = anchor;
		this.world = world;
		this.fireSpot = fireSpot;
		def = new BodyDef();
		def.position.set(anchor.getPosition());
		def.type = BodyType.DynamicBody;
		revDef = new RevoluteJointDef();
		revDef.localAnchorA.y = -heightSeg/2;
		revDef.localAnchorB.y = heightSeg/2;
		revDef.collideConnected = false;
		ropeDef = new RopeJointDef();
		ropeDef.collideConnected = false;
		CircleShape circle = new CircleShape();
		circle.setRadius(.1f);
		grapple = world.createBody(def);
		grapple.createFixture(circle,200);
		grapple.setUserData(new EntityData(0,EntityType.GRAPPLE,null));
		shape = new PolygonShape();
		shape.setAsBox(widthSeg/2, heightSeg/2);
		segments = new Body[length];
		revJoints = new RevoluteJoint[segments.length - 1];
		ropeJoints = new RopeJoint[segments.length - 1] ;
		createRope();
	}
	private Body[] createRope(){
		for(int i = 0; i < segments.length; i++){
			segments[i] = world.createBody(def);
			segments[i].createFixture(shape,2);
		}
		for(int i = 0; i < segments.length - 1; i++){
			revDef.bodyA = segments[i];
			revDef.bodyB = segments[i+1];
			revJoints[i] = (RevoluteJoint) world.createJoint(revDef);
			ropeDef.bodyA = anchor;
			ropeDef.maxLength = ((i + 1) * (heightSeg + stretch)) + (heightSeg/2);
			ropeDef.bodyB = segments[i + 1];
			ropeDef.localAnchorA.set(fireSpot);
			ropeDef.localAnchorB.set(segments[i+1].getLocalCenter().x,heightSeg/2);
			ropeJoints[i] = (RopeJoint) world.createJoint(ropeDef);
		}
		revDef.bodyA = anchor;
		revDef.localAnchorA.set(fireSpot);
		revDef.localAnchorB.set(segments[0].getLocalCenter().x,heightSeg/2);
		revDef.bodyB = segments[0];
		anchorJointRev = world.createJoint(revDef);
		revDef.bodyA = segments[segments.length - 1];
		revDef.localAnchorA.set(segments[segments.length - 1].getLocalCenter().x, -heightSeg/2);
		revDef.localAnchorB.set(grapple.getLocalCenter());
		revDef.bodyB = grapple;
		grappleJointRev = world.createJoint(revDef);
		ropeDef.bodyA = anchor;
		ropeDef.maxLength = segments.length * (heightSeg + stretch) +heightSeg/2;
		ropeDef.bodyB = grapple;
		ropeDef.localAnchorA.set(fireSpot);
		ropeDef.localAnchorB.set(grapple.getLocalCenter().x,0);
		grappleJointRope = world.createJoint(ropeDef);
		ropeDef.bodyA = anchor;
		ropeDef.bodyB = segments[0];
		ropeDef.localAnchorA.set(fireSpot);
		ropeDef.localAnchorB.set(segments[0].getLocalCenter().x,heightSeg/2);
		anchorJointRope = world.createJoint(ropeDef);

		return segments;
	}
	public Joint getGrappleJointRev() {
		return grappleJointRev;
	}
	public void setGrappleJointRev(Joint grappleJointRev) {
		this.grappleJointRev = grappleJointRev;
	}
	public Joint getGrappleJointRope() {
		return grappleJointRope;
	}
	public void setGrappleJointRope(Joint grappleJointRope) {
		this.grappleJointRope = grappleJointRope;
	}
	public Joint getAnchorJointRev() {
		return anchorJointRev;
	}
	public void setAnchorJointRev(Joint anchorJointRev) {
		this.anchorJointRev = anchorJointRev;
	}
	public Joint getAnchorJointRope() {
		return anchorJointRope;
	}
	public void setAnchorJointRope(Joint anchorJointRope) {
		this.anchorJointRope = anchorJointRope;
	}
	public void changeAnchor(Body body){
		anchor = body;
		GameStateManager.play.clearJoint.add(anchorJointRev);
		GameStateManager.play.clearJoint.add(anchorJointRope);
		ropeDef.bodyA = anchor;
		ropeDef.bodyB = segments[0];
		anchorJointRope = world.createJoint(ropeDef);
		revDef.bodyA = anchor;
		revDef.bodyB = segments[0];
		anchorJointRev = world.createJoint(revDef);
	}
	public void changeGrapple(Body body){
		grapple = body;
		GameStateManager.play.clearJoint.add(grappleJointRev);
		GameStateManager.play.clearJoint.add(grappleJointRope);
		revDef.bodyA = segments[segments.length - 1];
		revDef.bodyB = grapple;
		grappleJointRev = world.createJoint(revDef);
		ropeDef.bodyA = anchor;
		ropeDef.maxLength = segments.length * (heightSeg + stretch);
		ropeDef.bodyB = grapple;
		grappleJointRope = world.createJoint(ropeDef);
	}
}

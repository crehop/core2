package com.gdx.orion.gamemodel.weapons;

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

public class Grapple {
	private float widthSeg = 0.1f;
	private float heightSeg = 1f;
	private Body anchor;
	private Body grapple;
	private Body[] segments;
	private RevoluteJoint[] revJoints;
	private RopeJoint[] ropeJoints;
	private Joint grappleJointRev;
	private Joint grappleJointRope;
	private Joint anchorJointRev;
	private RopeJointDef anchorJointRope;
	private BodyDef def;
	private RevoluteJointDef revDef;
	private RopeJointDef ropeDef;
	private World world;
	private PolygonShape shape;
	private ShapeRenderer sr;
	float stretch = 0.01f;
	Vector2 fireSpot;
	public Grapple(Body anchor,Vector2 fireSpot,World world, int length){
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
		ropeDef.bodyA = anchor;
		ropeDef.maxLength = 100f;
		ropeDef.bodyB = grapple;
		ropeDef.localAnchorA.set(fireSpot);
		ropeDef.localAnchorB.set(grapple.getLocalCenter().x,0);
		anchorJointRope = ropeDef;
		world.createJoint(ropeDef);
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
	public RopeJointDef getAnchorJointRope() {
		return anchorJointRope;
	}
	public void setAnchorJointRope(RopeJointDef anchorJointRope) {
		this.anchorJointRope = anchorJointRope;
	}
	public void changeAnchor(Body body){
		anchor = body;
		ropeDef.bodyA = anchor;
		ropeDef.bodyB = segments[0];
		revDef.bodyA = anchor;
		revDef.bodyB = segments[0];
		GameStateManager.play.addJoint.add(ropeDef);
		GameStateManager.play.addJoint.add(revDef);
	}
	public void changeGrapple(Body body){
		this.grapple.setUserData(new EntityData(0,EntityType.DELETEME,null));
		grapple = body;
		ropeDef.bodyA = anchor;
		ropeDef.maxLength = 100f;
		ropeDef.bodyB = grapple;
		GameStateManager.play.addJoint.add(ropeDef);
		
	}
	public Body getGrapple() {
		return grapple;
	}
	public void destroyRope() {
		this.grapple.setUserData(new EntityData(0,EntityType.DELETEME,null));
	}
}

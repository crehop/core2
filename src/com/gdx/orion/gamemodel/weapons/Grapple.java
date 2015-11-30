package com.gdx.orion.gamemodel.weapons;

import gdx.orion.entities.EntityData;
import gdx.orion.entities.EntityType;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.gdx.orion.screens.GameStateManager;

public class Grapple {
	private float widthSeg = 0.1f;
	private float heightSeg = 1f;
	private Body anchor;
	private Body grapple;
	private Joint grappleJointRev;
	private Joint grappleJointRope;
	private Joint anchorJointRev;
	private RopeJointDef anchorJointRope;
	private BodyDef def;
	private RopeJointDef ropeDef;
	private World world;
	private FixtureDef fdef = new FixtureDef();
	private PolygonShape shape;
	float stretch = 0.01f;
	Vector2 fireSpot;
	public Grapple(Body anchor,Vector2 fireSpot,World world, int length){
		this.anchor = anchor;
		this.world = world;
		this.fireSpot = fireSpot;
		def = new BodyDef();
		def.position.set(anchor.getPosition());
		def.type = BodyType.DynamicBody;
		ropeDef = new RopeJointDef();
		ropeDef.collideConnected = false;
		CircleShape circle = new CircleShape();
		circle.setRadius(10f);
		fdef.shape = circle;
		fdef.isSensor = true;
		fdef.density = .1f;
		grapple = world.createBody(def);
		grapple.createFixture(fdef);
		grapple.setUserData(new EntityData(0,EntityType.GRAPPLE,null));
		shape = new PolygonShape();
		shape.setAsBox(widthSeg/2, heightSeg/2);
		createRope();
	}
	private void createRope(){
		ropeDef.bodyA = anchor;
		ropeDef.maxLength = 100f;
		ropeDef.bodyB = grapple;
		ropeDef.localAnchorA.set(anchor.getLocalCenter());
		ropeDef.localAnchorB.set(grapple.getLocalCenter().x,0);
		anchorJointRope = ropeDef;
		world.createJoint(ropeDef);
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
		ropeDef.bodyB = grapple;
		GameStateManager.play.addJoint.add(ropeDef);
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

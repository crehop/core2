package com.gdx.orion.gamemodel.shields;

import com.gdx.orion.entities.EntityData;
import com.gdx.orion.entities.EntityType;
import com.gdx.orion.entities.PlayerShip;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.utils.Array;
import com.gdx.orion.screens.GameStateManager;


/**
 * The inverter shield.  Game controller and view should update accordingly if 
 * this shield is in place on the player's ship.
 * 
 * @author jau
 */
@SuppressWarnings("serial")
public class InverterShield extends Shield {
	CircleShape shields = new CircleShape();
	Body shieldBody;
	JointDef[] disJoints;
	float[] degrees;
	private PlayerShip ship;
	private float power;
	private float radius;
	private Vector2 location = new Vector2();
	private	BodyDef bdef = new BodyDef();
	private RopeJointDef ropeDef = new RopeJointDef();
	private DistanceJointDef springDef = new DistanceJointDef();
	private boolean enabled = false;
	private World world;
	

	
	@Override
	public String getShieldName() {
		return "Inverter Shield";
	}
	public InverterShield(World world,PlayerShip ship, float radius, float power, float distance){
		this.setEnabled(false);
		this.ship = ship;
		this.setRadius(radius);
		this.setPower(power);
		this.world = world;
	}
	private void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public float getPower() {
		return power;
	}
	public void setPower(float power) {
		this.power = power;
	}
	public float getRadius() {
		return radius;
	}
	public void setRadius(float radius) {
		this.radius = radius;
	}
	public Body getBody() {
		return shieldBody;
	}
	public void enable() {
		bdef.bullet = true;
		bdef.angularDamping = 100.5f;
		bdef.type = BodyType.DynamicBody;
		shields.setRadius(radius);
		bdef.position.set(ship.getBody().getWorldCenter());
		shieldBody = world.createBody(bdef);
		shieldBody.createFixture(shields,0.8f);
		shieldBody.setUserData(new EntityData(1000,EntityType.SHIELD,null));
		springDef.localAnchorB.x = ship.getBody().getLocalCenter().x;
		springDef.localAnchorB.y = ship.getBody().getLocalCenter().y;
		springDef.localAnchorA.x = radius;
		springDef.localAnchorA.y = 0;
		springDef.frequencyHz = 3f;
		springDef.bodyA = shieldBody;
		springDef.bodyB = ship.getBody();
		springDef.length = radius;
		world.createJoint(springDef);
		springDef.localAnchorA.x = -radius;
		springDef.localAnchorA.y = 0;
		world.createJoint(springDef);
		springDef.localAnchorA.x = 0;
		springDef.localAnchorA.y = -radius;
		world.createJoint(springDef);
		springDef.localAnchorA.x = 0;
		springDef.localAnchorA.y = radius;
		world.createJoint(springDef);
		ropeDef.bodyA = ship.getBody();
		ropeDef.bodyB = shieldBody;
		ropeDef.maxLength = 1;
		ropeDef.localAnchorA.x = ship.getBody().getLocalCenter().x;
		ropeDef.localAnchorA.y = ship.getBody().getLocalCenter().y;
		ropeDef.localAnchorB.x = radius;
		ropeDef.localAnchorB.y = 0;
		world.createJoint(ropeDef);
		ropeDef.localAnchorB.x = -radius;
		ropeDef.localAnchorB.y = 0;
		world.createJoint(ropeDef);
		ropeDef.localAnchorB.x = 0;
		ropeDef.localAnchorB.y = -radius;
		world.createJoint(ropeDef);
		ropeDef.localAnchorB.x = 0;
		ropeDef.localAnchorB.y = radius;
		world.createJoint(ropeDef);
		this.setEnabled(true);
	}
	public void disable(){
		((EntityData)shieldBody.getUserData()).setType(EntityType.DELETEME);
		this.setEnabled(false);
	}
	public boolean isEnabled(){
		return this.enabled;
	}
}


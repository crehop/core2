package gdx.orion.entities;
	
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.orion.gamevars.Location;
import com.gdx.orion.utils.WorldUtils;

public class PlayerShip {
	private float offset = (float) Math.toRadians(90);
	PolygonShape shape = new PolygonShape();
	Body body;
	BodyDef def;
	Location location;
	FixtureDef fdef;
	Random rand = new Random();
	Vector2 force = new Vector2();
	Location fireSpot = new Location(0,0,0);
	World world;
	
	public PlayerShip(World world, Location position){
		this.world = world;
		this.location = position;
		def = new BodyDef();
		def.position.set(location.x, location.y);
		def.type = BodyType.DynamicBody;
		def.angle = 200;
		float[] creature = new float[6];
		creature[0] = 0f;
		creature[1] = 0;
		creature[2] = 2.5f;
		creature[3] = 3.5f; 
		creature[4] = 5.0f;
		creature[5] = 0;
		shape.set(creature);
		
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = 200;
		fdef.friction = .5f;
		fdef.restitution = 0;
		body = world.createBody(def);
		body.createFixture(fdef);
		body.setUserData(new EntityData(1000,EntityType.SHIP,this            ));
	}

	public Location getLocation() {
		return this.location;
	}

	public Body getBody() {
		return this.body;
	}
	
	public void turn(float f){
		body.applyAngularImpulse(f, false);
	}
	public void forward(int strength){
		force.x = (float) (Math.cos(body.getAngle() + offset) * strength);
		force.y = (float) (Math.sin(body.getAngle() + offset) * strength);
		body.applyForceToCenter(force, false);
	}
	public void fire(){
		fireSpot.x = body.getWorldCenter().x + (float) (Math.cos(body.getAngle() + offset) * 1.5);
		fireSpot.y = body.getWorldCenter().y + (float) (Math.sin(body.getAngle() + offset) * 1.5);
		force.x = (float) (Math.cos(body.getAngle() + offset) * 200000 + body.getLocalCenter().x + body.getLinearVelocity().x);
		force.y = (float) (Math.sin(body.getAngle() + offset) * 200000 + body.getLocalCenter().y) + body.getLinearVelocity().y;
		WorldUtils.fireBullet(this.world,fireSpot,10,.01f,force);
	}

	public Vector2 getPosition() {
		return location.getPosition();
	}
}

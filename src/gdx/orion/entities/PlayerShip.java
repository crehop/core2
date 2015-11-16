package gdx.orion.entities;
	
import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.orion.gamevars.Location;
import com.gdx.orion.utils.Box2DUtils;
import com.gdx.orion.utils.Console;
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
	private int fire = 0;
	private final int FIRE_DELAY = 2;
	private float linearDamping = 0.4f;
	private float angularDamping = 2.0f;
	
	public PlayerShip(World world, Location position){
		this.world = world;
		this.location = position;
		def = new BodyDef();
		def.position.set(location.x, location.y);
		def.type = BodyType.DynamicBody;
		def.angle = 200;
		float[] creature = new float[6];
		creature[0] = 0f;
		creature[1] = -1.2f;
		creature[2] = 2.5f;
		creature[3] = 3.0f; 
		creature[4] = 5.0f;
		creature[5] = -1.2f;
		shape.set(creature);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = 2650;
		fdef.friction = .5f;
		fdef.restitution = 0;
		body = world.createBody(def);
		body.createFixture(fdef);
		creature = new float[8];
		creature[0] = 0f;
		creature[1] = 0;
		creature[2] = 0f;
		creature[3] = -1.7f; 
		creature[4] = -1.0f;
		creature[5] = -1.7f;
		creature[6] = -1.0f;
		creature[7] = 0f;
		shape.set(Box2DUtils.setShapePosition(creature, new Vector2(2.1f,-.5f)));
		fdef.shape = shape;
		fdef.density = 10;
		fdef.friction = .5f;
		fdef.restitution = 0;
		body.createFixture(fdef);
		shape.set(Box2DUtils.setShapePosition(creature, new Vector2(1.9f, 0f)));
		fdef.shape = shape;
		fdef.density = 100;
		fdef.friction = .5f;
		fdef.restitution = 0;
		body.createFixture(fdef);
		body.setUserData(new EntityData(1000,EntityType.SHIP,this));
		body.setAngularDamping(angularDamping);
		body.setLinearDamping(linearDamping);
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
		Console.setLine4("SPEED:" + body.getLinearVelocity());
		force.x = (float) (Math.cos(body.getAngle() + offset) * strength);
		force.y = (float) (Math.sin(body.getAngle() + offset) * strength);
		body.applyForceToCenter(force, false);
	}
	public void fire(){
		fire++;
		if(fire > FIRE_DELAY){
			fireSpot.x = body.getWorldCenter().x + (float) ((Math.cos(body.getAngle() + offset + Math.toRadians(25))));
			fireSpot.y = body.getWorldCenter().y + (float) ((Math.sin(body.getAngle() + offset + Math.toRadians(25))));
			force.x = (float) (Math.cos(body.getAngle() + offset) * 999999999 + body.getLocalCenter().x);
			force.y = (float) (Math.sin(body.getAngle() + offset) * 999999999 + body.getLocalCenter().y);
			WorldUtils.fireBullet(this.world,fireSpot,.01f,.011f,force);
			fireSpot.x = body.getWorldCenter().x + (float) ((Math.cos(body.getAngle() + offset + Math.toRadians(-25))));
			fireSpot.y = body.getWorldCenter().y + (float) ((Math.sin(body.getAngle() + offset + Math.toRadians(-25))));
			force.x = (float) (Math.cos(body.getAngle() + offset) * 999999999 + body.getLocalCenter().x);
			force.y = (float) (Math.sin(body.getAngle() + offset) * 999999999 + body.getLocalCenter().y);
			WorldUtils.fireBullet(this.world,fireSpot,.01f,.011f,force);
			fire = 0;
		}
	}

	public Vector2 getPosition() {
		return location.getPosition();
	}
}

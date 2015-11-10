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
import com.gdx.orion.utils.Console;

public class PlayerShip {
	private float offset = (float) Math.toRadians(90);
	private ArrayList<Ball> bullets = new ArrayList<Ball>();
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
		creature[2] = 5;
		creature[3] = 20; 
		creature[4] = 10;
		creature[5] = 0;
		shape.set(creature);
		
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = 5;
		fdef.friction = .5f;
		fdef.restitution = 0;
		body = world.createBody(def);
		body.createFixture(fdef);
	}

	public Location getLocation() {
		return this.location;
	}

	public Body getBody() {
		return this.body;
	}
	
	public void turn(int force){
		body.applyAngularImpulse(force, false);
	}
	public void forward(int strength){
		force.x = (float) (Math.cos(body.getAngle() + offset) * strength);
		force.y = (float) (Math.sin(body.getAngle() + offset) * strength);
		body.applyForceToCenter(force, false);
	}
	public void fire(){
		fireSpot.x = body.getWorldCenter().x + (float) (Math.cos(body.getAngle() + offset) * 19.5);
		fireSpot.y = body.getWorldCenter().y + (float) (Math.sin(body.getAngle() + offset) * 19.5);
		Console.setLine10("BULLET X/Y:" + (body.getPosition().x  + (float) (Math.cos(body.getAngle() + offset)) + "/" +  (body.getPosition().y  + (float) (Math.cos(body.getAngle() + offset)))));
		Ball ball = new Ball(this.world,fireSpot,100000,1);
		force.x = (float) (Math.cos(body.getAngle() + offset) * 2000000000 * 20000000 + body.getLocalCenter().x);
		force.y = (float) (Math.sin(body.getAngle() + offset) * 2000000000 * 20000000+ body.getLocalCenter().y);
		ball.body.setBullet(true);
		ball.body.setLinearVelocity(force);
		bullets.add(ball);
		if(bullets.size() > 100){
			world.destroyBody(bullets.get(0).body);
			bullets.remove(0);
		}
	}
}

package gdx.orion.entities;

import java.util.Random;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.orion.gamevars.Location;

public class Asteroid {
	private PolygonShape shape = new PolygonShape();
	private Body body;
	private BodyDef def;
	private Location location;
	private float torque;
	private float direction;
	private Vector2 speed;
	private float width;
	private float height;
	int numpoints = 0;

	public Asteroid(World world, Location position, float density, int size){

		this.location = position;
		def = new BodyDef();
		def.position.set(location.x, location.y);
		def.type = BodyType.DynamicBody;
		def.angle = 200;
		body = world.createBody(def);

		shape.set(setShape(size));
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = density;
		fdef.friction = 1;
		body.createFixture(fdef);
		body.applyForce(speed,body.getWorldCenter(), false);
		body.applyAngularImpulse(MathUtils.random(-400000,400000), false);
	}

	private float[] setShape(int size) {
		numpoints = 8;
		float force = 0;
		if(size == 1){
			force = MathUtils.random(-1000,1000);
			width = height = MathUtils.random(15,50);
			numpoints = 8;
		}
		if(size == 2){
			numpoints = 5;
			force = MathUtils.random(-750,750);
			width = height = MathUtils.random(15,40);
			numpoints = 8;
		}
		if(size == 3){
			numpoints = 8;
			force = MathUtils.random(-600,600);
			width = height = MathUtils.random(20,60);
		}
		this.speed = new Vector2((location.x + MathUtils.random(force * 10000)),(location.y + MathUtils.random(force *10000)));
		float radians = (float) (Math.toRadians(360)/numpoints);
		float[] shapex = new float[numpoints];
		float[] shapey = new float[numpoints];
		float[] dists = new float[numpoints];
		float[] creature = new float[numpoints * 2];
		float angle = 0;
		float radius = width/2;
		for(int i = 0; i < numpoints; i++){
			dists[i] = MathUtils.random(radius /.75f, radius);
		} 
		for(int i = 0; i < numpoints; i++){
			shapex[i] = body.getLocalCenter().x + MathUtils.cos(radians * i) * dists[i];
			shapey[i] = body.getLocalCenter().y + MathUtils.sin(radians * i) * dists[i];
			angle += 4 * 3.1415f/numpoints;

		}
		int count = 0;
		for(int i = 0; i < numpoints; i++){
			creature[count++] = shapex[i];
			creature[count++] = shapey[i];
		}
		return creature;
	}
}

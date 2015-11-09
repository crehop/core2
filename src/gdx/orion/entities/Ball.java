package gdx.orion.entities;

import java.util.Random;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.orion.gamevars.Location;

public class Ball {
	PolygonShape shape = new PolygonShape();
	CircleShape shape2 = new CircleShape();
	Body body;
	BodyDef def;
	Location location;
	FixtureDef fdef;
	Random rand = new Random();

	public Ball(World world, Location position, float density, int size){
		this.location = position;
		def = new BodyDef();
		def.position.set(location.x, location.y);
		def.type = BodyType.DynamicBody;
		def.angle = 200;
		float[] creature = new float[8];
		creature[0] = 1f;
		creature[1] = 0;
		creature[2] = 43;
		creature[3] = 88; 
		creature[4] = 80;
		creature[5] = 26;
		creature[6] = 86;
		creature[7] = 0;
		shape.set(creature);
		shape2.setRadius(size);
		
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape2;
		fdef.density = density;
		fdef.friction = 1;
		body = world.createBody(def);
		body.createFixture(fdef);
	}
}
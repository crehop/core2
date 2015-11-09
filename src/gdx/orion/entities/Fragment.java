package gdx.orion.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gdx.orion.gamevars.Location;

public class Fragment {
	private World world;
	private float[] vertices;
	private BodyDef def;
	private Body body;
	private PolygonShape shape;
	private Location location;
	private Vector2 speed;
	public Fragment(float points, float points2, float points3, float points4, float points5, float points6, World world, Vector2 position, float density){
		shape = new PolygonShape();
		vertices = new float[6];
		vertices[0] = points;
		vertices[1] = points2;
		vertices[2] = points3;
		vertices[3] = points4;
		vertices[4] = points5;
		vertices[5] = points6;
		this.world = world;
		def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.angle = 200;
		def.position.set(position);
		body = world.createBody(def);
		shape.set(vertices);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = density;
		fdef.friction = 1;
		float force = MathUtils.random(1f,100f);
		body.createFixture(fdef);
		this.location = new Location(body.getPosition().x, body.getPosition().y,0);
		body.getPosition().set(location.x, location.y);
		this.speed = new Vector2((location.x + MathUtils.random(force * 10000)),(location.y + MathUtils.random(force *10000)));
		body.applyForce(speed,body.getWorldCenter(), false);
		body.applyAngularImpulse(MathUtils.random(-400000,400000), false);
		System.out.println("CREATING FRAGMENT!");
	}
}

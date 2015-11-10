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
import com.gdx.orion.screens.GameStateManager;

public class Fragment {
	private World world;
	private float[] vertices;
	private BodyDef def;
	private Body body;
	private PolygonShape shape;
	private Location location;
	private float offset = (float) Math.toRadians(180f);
	public Fragment(float points, float points2, float points3, float points4, float points5, float points6, World world, Body body2){
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
		def.position.set(body2.getPosition());
		body = world.createBody(def);
		shape.set(vertices);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = 10;
		fdef.friction = 1;
		body.createFixture(fdef);
		//hit.x = body.getWorldCenter().x + (float) (Math.cos(body.getAngle() + offset) * force * MathUtils.random(100));
		//hit.y = body.getWorldCenter().y + (float) (Math.sin(body.getAngle() + offset) * force * MathUtils.random(100));
		this.location = new Location(body.getPosition().x, body.getPosition().y,0);
		body.getPosition().set(location.x, location.y);
		body.setLinearVelocity(body2.getLinearVelocity());
		body.setAngularVelocity(body2.getAngularVelocity());
		GameStateManager.play.frags.add(this);
		if(GameStateManager.play.frags.size() > 1000){
			world.destroyBody(GameStateManager.play.frags.get(0).body);
			GameStateManager.play.frags.remove(0);
		}
	}
	public Body getBody() {
		return body;
	}
}

package gdx.orion.entities;
	
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
	private static final int FIRE_DELAY = 2;
	private static final float LINEAR_DAMPENING = 0.01f;
	private static final float ANGULAR_DAMPENING = 3.0f;
	public static final float SIZE_MOD = 1.0f;
    private Texture texture2 = new Texture(Gdx.files.internal("images/ship.png"));
    private Sprite shipSprite = new Sprite(texture2);

	
	public PlayerShip(World world, Location position){
		this.world = world;
		this.location = position;
		def = new BodyDef();
		def.position.set(location.x, location.y);
		def.type = BodyType.DynamicBody;
		def.angle = 200;
		float[] creature = new float[6];
		creature[0] = 0f * SIZE_MOD;
		creature[1] = -1.2f * SIZE_MOD;
		creature[2] = 2.5f * SIZE_MOD;
		creature[3] = 3.0f * SIZE_MOD; 
		creature[4] = 5.0f * SIZE_MOD;
		creature[5] = -1.2f * SIZE_MOD;
		shape.set(creature);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = 2650;
		fdef.friction = .5f;
		fdef.restitution = 0;
		body = world.createBody(def);
		body.createFixture(fdef);
		creature = new float[8];
		creature[0] = 0f * SIZE_MOD;
		creature[1] = 0 * SIZE_MOD;
		creature[2] = 0f * SIZE_MOD;
		creature[3] = -1.7f * SIZE_MOD; 
		creature[4] = -1.0f * SIZE_MOD;
		creature[5] = -1.7f * SIZE_MOD;
		creature[6] = -1.0f * SIZE_MOD;
		creature[7] = 0f;
		shape.set(Box2DUtils.setShapePosition(creature, new Vector2(2.1f * SIZE_MOD,-.5f * SIZE_MOD)));
		fdef.shape = shape;
		fdef.density = 10;
		fdef.friction = .5f;
		fdef.restitution = 0;
		body.createFixture(fdef);
		shape.set(Box2DUtils.setShapePosition(creature, new Vector2(1.9f * SIZE_MOD, 0f * SIZE_MOD)));
		fdef.shape = shape;
		fdef.density = 100;
		fdef.friction = .5f;
		fdef.restitution = 0;
		body.createFixture(fdef);
		body.setUserData(new EntityData(1000,EntityType.SHIP,this));
		body.setAngularDamping(ANGULAR_DAMPENING);
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
	public void forward(float f){
		Console.setLine4("SPEED:" + (int)body.getLinearVelocity().x + "/" + (int)body.getLinearVelocity().y);
		force.x = (float) (Math.cos(body.getAngle() + offset) * f);
		force.y = (float) (Math.sin(body.getAngle() + offset) * f);
		body.applyForceToCenter(force, false);
	}
	public void fire(){
		fire++;
		if(fire > FIRE_DELAY){
			fireSpot.x = body.getWorldCenter().x + (float) ((Math.cos(body.getAngle() + offset + Math.toRadians(25)))) * 1.5f  * SIZE_MOD;
			fireSpot.y = body.getWorldCenter().y + (float) ((Math.sin(body.getAngle() + offset + Math.toRadians(25)))) * 1.5f * SIZE_MOD;
			force.x = (float) (Math.cos(body.getAngle() + offset) * 999999999 + body.getLocalCenter().x);
			force.y = (float) (Math.sin(body.getAngle() + offset) * 999999999 + body.getLocalCenter().y);
			WorldUtils.fireBullet(this.world,fireSpot,.01f,.011f,force);
			fireSpot.x = body.getWorldCenter().x + (float) ((Math.cos(body.getAngle() + offset + Math.toRadians(-25))) * 1.5f * SIZE_MOD);
			fireSpot.y = body.getWorldCenter().y + (float) ((Math.sin(body.getAngle() + offset + Math.toRadians(-25))) * 1.5f * SIZE_MOD);
			WorldUtils.fireBullet(this.world,fireSpot,.01f,.011f,force);
			fire = 0;
		}
	}

	public Vector2 getPosition() {
		return location.getPosition();
	}

	public void draw(Batch batch, boolean map) {
		if(!map){
			shipSprite.setSize(5, 5);
			shipSprite.setOriginCenter();
			shipSprite.setRotation((float)(this.getBody().getAngle() * 57.2958));
			shipSprite.setCenterX(this.getBody().getWorldCenter().x);
			shipSprite.setCenterY(this.getBody().getWorldCenter().y);
			//shipSprite.setRotation(ship.getBody().getAngle());
			shipSprite.draw(batch);
		}
		else{
			shipSprite.setSize(50,50);
			shipSprite.setOriginCenter();
			shipSprite.setCenterX(this.getBody().getWorldCenter().x);
			shipSprite.setCenterY(this.getBody().getWorldCenter().y);
			shipSprite.draw(batch);	
		}
	}
}

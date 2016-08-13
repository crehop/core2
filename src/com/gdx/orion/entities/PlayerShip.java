package com.gdx.orion.entities;
	
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.gdx.orion.gamemodel.Campaign;
import com.gdx.orion.gamemodel.engines.ChemicalEngine;
import com.gdx.orion.gamemodel.engines.Engine;
import com.gdx.orion.gamemodel.shields.InverterShield;
import com.gdx.orion.gamemodel.weapons.Grapple;
import com.gdx.orion.handlers.RaycastHandler;
import com.gdx.orion.utils.Box2DUtils;
import com.gdx.orion.utils.Console;
import com.gdx.orion.utils.EffectUtils;
import com.gdx.orion.utils.Side;

public class PlayerShip {
	private float offset = (float) Math.toRadians(90);
	PolygonShape shape = new PolygonShape();
	Body body;
	BodyDef def;
	FixtureDef fdef;
	Random rand = new Random();
	Vector2 force = new Vector2();
	Vector2 fireSpot = new Vector2(0,0);
	Vector2 position = new Vector2(0,0);
	World world;
	private int fire = 0;
	private boolean forward = false;
	private boolean backward = false;
	private boolean left = false;
	private boolean right = false;
	private boolean fired = false;
	private boolean ropeFired = false;
	private EntityData entityData;
	Grapple rope;
	private static final int FIRE_DELAY = 2;
	private static final float ANGULAR_DAMPENING = 3.0f;
	public static final float SIZE_MOD = 1.0f;
	private Array<Grapple> ropes = new Array<Grapple>();
	private Sprite shipSprite = new Sprite(generateShipImageFromConfig());
    private InverterShield shield;
    //private final Sound soundFire = Gdx.audio.newSound(Gdx.files.internal("sound/shoot01.wav"));
	
	public PlayerShip(World world, Vector2 position){
		this.world = world;
		this.position = position;
		def = new BodyDef();
		def.position.set(this.position.x, this.position.y);
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
		body.setBullet(true);
		body.setUserData(new EntityData(1000,EntityType.SHIP,this));
		body.setAngularDamping(ANGULAR_DAMPENING);
	}

	public Body getBody() {
		return this.body;
	}
	
	public void turn(float f){
		if(f > 0){
			if(!right){
				right  = true;
			}
		}else if(f < 0){
			if(!left){
				left  = true;
			}
		}

		body.applyAngularImpulse(f, false);
	}
	public void forward(float f){
		if(f > 0){
			if(!forward){
				forward = true;
			}
		}else if(f < 0){
			if(!backward){
				backward = true;
			}
		}
		Console.setLine4("SPEED:" + (int)body.getLinearVelocity().x + "/" + (int)body.getLinearVelocity().y);
		force.x = (float) (Math.cos(body.getAngle() + offset) * f);
		force.y = (float) (Math.sin(body.getAngle() + offset) * f);
		body.applyForceToCenter(force, false);
	}
	public void fire(){
		fire++;
		if(this.isSheilded() == false){
			if(fire > FIRE_DELAY){
				if(!fired)fired = true;
				
				//soundFire.play();
			}
		}else{
			fired = false;
		}
	}
	private boolean isSheilded() {
		if(this.shield == null){
			return false;
		}
		return this.shield.isEnabled();
	}

	public void fireGrapple(){
		if(ropeFired){
			ropeFired = false;
			if(rope.getGrapple().getUserData() instanceof EntityData){
				entityData = (EntityData)rope.getGrapple().getUserData();
				if(entityData.getType() == EntityType.ASTEROID || entityData.getType() == EntityType.COMET){
				}else if(entityData.getType() == EntityType.GRAPPLE){
					entityData.setType(EntityType.DELETEME);
				}
			}else{
				rope.getGrapple().setUserData(new EntityData(0,EntityType.DELETEME,null));
			}
		}else{
			fireSpot.x = body.getLocalCenter().x;
			fireSpot.y = body.getLocalCenter().y + 2.7f;
			rope = new Grapple(body,fireSpot,world,60);
			force.x = (float) (Math.cos(body.getAngle() + offset) * 999999 + body.getWorldCenter().x);
			force.y = (float) (Math.sin(body.getAngle() + offset) * 999999 + body.getWorldCenter().y);
			rope.getGrapple().applyForceToCenter(force, false);
			ropes.add(rope);
			ropeFired = true;
		}
	}

	public Vector2 getPosition() {
		return this.position;
	}

	public void draw(Batch batch, boolean map, Vector3 position) {
		if(!map){
			if(forward){
				fireSpot.x = body.getWorldCenter().x + (float) ((Math.cos(body.getAngle() + offset + Math.toRadians(20)))) * -2.75f  * SIZE_MOD;
				fireSpot.y = body.getWorldCenter().y + (float) ((Math.sin(body.getAngle() + offset + Math.toRadians(20)))) * -2.75f * SIZE_MOD;
				EffectUtils.thrustEffect(fireSpot, batch,(float)(this.getBody().getAngle() * 57.2958));
				fireSpot.x = body.getWorldCenter().x + (float) ((Math.cos(body.getAngle() + offset + Math.toRadians(-20))) * -2.75f * SIZE_MOD);
				fireSpot.y = body.getWorldCenter().y + (float) ((Math.sin(body.getAngle() + offset + Math.toRadians(-20))) * -2.75f * SIZE_MOD);
				EffectUtils.thrustEffect(fireSpot, batch,(float)(this.getBody().getAngle() * 57.2958));
			}else if(backward){
				fireSpot.x = body.getWorldCenter().x + (float) ((Math.cos(body.getAngle() + offset + Math.toRadians(5)))) * 2.5f  * SIZE_MOD;
				fireSpot.y = body.getWorldCenter().y + (float) ((Math.sin(body.getAngle() + offset + Math.toRadians(5)))) * 2.5f * SIZE_MOD;
				EffectUtils.rotationThrustEffect(fireSpot, batch,(float)(this.getBody().getAngle() * 57.2958) - 180);
				fireSpot.x = body.getWorldCenter().x + (float) ((Math.cos(body.getAngle() + offset + Math.toRadians(-5))) * 2.5f * SIZE_MOD);
				fireSpot.y = body.getWorldCenter().y + (float) ((Math.sin(body.getAngle() + offset + Math.toRadians(-5))) * 2.5f * SIZE_MOD);
				EffectUtils.rotationThrustEffect(fireSpot, batch,(float)(this.getBody().getAngle() * 57.2958) - 180);
			}
			if(left){
				fireSpot.x = body.getWorldCenter().x + (float) ((Math.cos(body.getAngle() + offset + Math.toRadians(25)))) * 1.7f  * SIZE_MOD;
				fireSpot.y = body.getWorldCenter().y + (float) ((Math.sin(body.getAngle() + offset + Math.toRadians(25)))) * 1.7f * SIZE_MOD;
				EffectUtils.rotationThrustEffect(fireSpot, batch,(float)(this.getBody().getAngle() * 57.2958) - 90);
			}else if(right){
				fireSpot.x = body.getWorldCenter().x + (float) ((Math.cos(body.getAngle() + offset + Math.toRadians(-25))) * 1.7f * SIZE_MOD);
				fireSpot.y = body.getWorldCenter().y + (float) ((Math.sin(body.getAngle() + offset + Math.toRadians(-25))) * 1.7f * SIZE_MOD);
				EffectUtils.rotationThrustEffect(fireSpot, batch,(float)(this.getBody().getAngle() * 57.2958) + 90);
			}
			if(fired){
				fireSpot.x = body.getWorldCenter().x + (float) ((Math.cos(body.getAngle() + offset + Math.toRadians(12)))) * 2.2f  * SIZE_MOD;
				fireSpot.y = body.getWorldCenter().y + (float) ((Math.sin(body.getAngle() + offset + Math.toRadians(12)))) * 2.2f * SIZE_MOD;
				EffectUtils.muzzleFlash(world,fireSpot, batch,(float)(this.getBody().getAngle() * 57.2958) + 90);
				fireSpot.x = body.getWorldCenter().x + (float) ((Math.cos(body.getAngle() + offset + Math.toRadians(-12))) * 2.2f * SIZE_MOD);
				fireSpot.y = body.getWorldCenter().y + (float) ((Math.sin(body.getAngle() + offset + Math.toRadians(-12))) * 2.2f * SIZE_MOD);
				EffectUtils.muzzleFlash(world,fireSpot, batch,(float)(this.getBody().getAngle() * 57.2958) + 90);
				fireSpot.x = body.getWorldCenter().x + (float) ((Math.cos(body.getAngle() + offset + Math.toRadians(7)))) * 3.5f  * SIZE_MOD;
				fireSpot.y = body.getWorldCenter().y + (float) ((Math.sin(body.getAngle() + offset + Math.toRadians(7)))) * 3.5f * SIZE_MOD;
				force.x = body.getWorldCenter().x + (float) ((Math.cos(body.getAngle() + offset + Math.toRadians(3))) * 23.6f * SIZE_MOD);
				force.y = body.getWorldCenter().y + (float) ((Math.sin(body.getAngle() + offset + Math.toRadians(3))) * 23.6f * SIZE_MOD);
				EffectUtils.bullet(world,fireSpot, batch,(float)(this.getBody().getAngle() * 57.2958) + 90);
				RaycastHandler.ray(fireSpot, force, EntityType.BULLET,world);
				EffectUtils.line(fireSpot,force,100,0,0,1);
				fireSpot.x = body.getWorldCenter().x + (float) ((Math.cos(body.getAngle() + offset + Math.toRadians(-7))) * 3.5f * SIZE_MOD);
				fireSpot.y = body.getWorldCenter().y + (float) ((Math.sin(body.getAngle() + offset + Math.toRadians(-7))) * 3.5f * SIZE_MOD);
				force.x = body.getWorldCenter().x + (float) ((Math.cos(body.getAngle() + offset + Math.toRadians(-3))) * 23.6f * SIZE_MOD);
				force.y = body.getWorldCenter().y + (float) ((Math.sin(body.getAngle() + offset + Math.toRadians(-3))) * 23.6f * SIZE_MOD);
				EffectUtils.bullet(world,fireSpot, batch,(float)(this.getBody().getAngle() * 57.2958) + 90);
				RaycastHandler.ray(fireSpot, force, EntityType.BULLET,world);
				EffectUtils.line(fireSpot,force,100,0,0,1);

				
			}
			if(ropeFired){
				EffectUtils.line(body.getWorldCenter(), rope.getGrapple().getPosition(),
						/*color*/ 0, 100, 10, 1);
			}
			if(this.shield != null && this.shield.isEnabled())EffectUtils.inverterMainShieldEffect(shield.getBody().getWorldCenter(), batch);
			left = false;
			right = false;
			forward = false;
			backward = false;
			shipSprite.setSize(5, 5);
			shipSprite.setOriginCenter();
			shipSprite.setRotation((float)(this.getBody().getAngle() * 57.2958));
			shipSprite.setCenterX(position.x);
			shipSprite.setCenterY(position.y);
			shipSprite.draw(batch);
		}
		else{
			shipSprite.setSize(150,150);
			shipSprite.setOriginCenter();
			shipSprite.setCenterX(position.x);
			shipSprite.setCenterY(position.y);
			shipSprite.draw(batch);	
		}
	}
	public boolean ropeFired(){
		return ropeFired;
	}
	public Grapple getRope() {
		return rope;
	}

	public void shieldToggle() {
		if(shield == null){
			shield = new InverterShield(world,this,10,1000,10);
		}
		if(shield.isEnabled()){
			shield.disable();
		}else{
			shield.enable();
		}

	}
	public void setFired(boolean fired){
		this.fired = fired;
	}

	public void setRopeFired(boolean ropeFired) {
		this.ropeFired = ropeFired;
	}
	
	public void updateShipImage() {
		shipSprite = new Sprite(generateShipImageFromConfig());
	}
	
	/**
	 * Creates the ship's image properly with respect to the current campaign configuration
	 * 
	 * @return  a texture representing the ship with respect to the current campaign configuration
	 */
	private Texture generateShipImageFromConfig() {
		final Texture textureShip = new Texture(Gdx.files.internal("images/PlayerShip-base-1920.png"));
		final Texture textureEngineLeft = new Texture(Gdx.files.internal(enginePath(Campaign.getInstance().getShipModel().getCurrentEngine(), Side.LEFT)));
		final Texture textureEngineRight = new Texture(Gdx.files.internal(enginePath(Campaign.getInstance().getShipModel().getCurrentEngine(), Side.RIGHT)));
		
		textureShip.getTextureData().prepare();
		textureEngineLeft.getTextureData().prepare();
		textureEngineRight.getTextureData().prepare();
		
		final Pixmap pixmap = new Pixmap(textureShip.getWidth(), (int)(1.25 * textureShip.getHeight()) , Format.RGBA8888);
		pixmap.drawPixmap(textureShip.getTextureData().consumePixmap(), 0, 0);
		pixmap.drawPixmap(textureEngineLeft.getTextureData().consumePixmap(), 380, 965); // TODO: Need to account X position for slightly different engine widths
		pixmap.drawPixmap(textureEngineRight.getTextureData().consumePixmap(), 1200, 965);   // TODO: Need to account X position for slightly different engine widths
		
		// Ship texture should now have all engines, shields, and weapons configured
		return new Texture(pixmap);
	}
	
	private String enginePath(Engine engine, final Side side) {
		if (engine == null) {
			engine = new ChemicalEngine();
		}
		
		switch (side) {
			case LEFT:  return String.format("images/Engine%s-left.png", engine.getEngineName());
			case RIGHT: return String.format("images/Engine%s-right.png", engine.getEngineName());
			default:    throw new IllegalArgumentException("Engine side must be specified");  // Fatal
		}
	}
}

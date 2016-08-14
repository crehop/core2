package com.gdx.orion.utils;

import com.gdx.orion.entities.EntityData;
import com.gdx.orion.entities.EntityType;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
public class GravityUtils{
	private static ArrayList<Body> planetVector = new ArrayList<Body>();
	private static ArrayList<Sprite> gravitySprites = new ArrayList<Sprite>();
	private static Vector2 forceVector = new Vector2();
	private static Vector2 planetPosition = new Vector2();
	private static Vector2 planetDistance = new Vector2();
	private static FixtureDef fixtureDef = new FixtureDef();
	private static CircleShape circleShape;
	private static BodyDef bodyDef = new BodyDef();
	private static Body thePlanet;
	private static float radius;
	private static float finalDistance;
	private static float vecSum;
	private static int count = 0;
	private static final int GRAVITATIONAL_REACH = 3000000;
	
	public static void addGravityWell(float x,float y, float radius, float density, World world, boolean inWardForce, Sprite sprite, Vector2 velocity, boolean isStatic) {
		sprite.setPosition(x - radius, y - radius);
		sprite.setSize(radius * 2, radius * 2);
		sprite.setOriginCenter();

		fixtureDef.restitution=0;
		fixtureDef.density = density;
		circleShape =new CircleShape();
		circleShape.setRadius(radius);
		fixtureDef.shape= circleShape;
		bodyDef = new BodyDef();
		bodyDef.position.set(x,y);
		if(isStatic){
			bodyDef.type = BodyType.StaticBody;
		}else{
			bodyDef.type = BodyType.DynamicBody;
		}
		thePlanet = world.createBody(bodyDef);
		thePlanet.setLinearVelocity(velocity);
		planetVector.add(thePlanet);
		gravitySprites.add(sprite);
		if(inWardForce)thePlanet.setUserData(new EntityData(1000, EntityType.GRAVITY_WELL, null));
		else{
		}
		thePlanet.createFixture(fixtureDef);
	}
	public static void applyGravity(World world,Body body){
		forceVector = body.getWorldCenter();
			for (int j = 0; j < planetVector.size(); j++) {
				if(planetVector.get(j).getFixtureList().size > 0){
					circleShape = (CircleShape)planetVector.get(j).getFixtureList().get(0).getShape();
					radius = circleShape.getRadius();
					planetPosition = planetVector.get(j).getWorldCenter();
					planetDistance =new Vector2(0,0);
					planetDistance.add(forceVector);
					planetDistance.sub(planetPosition);
					finalDistance = planetDistance.len();
					if(finalDistance <= radius * GRAVITATIONAL_REACH) {
						planetDistance.x = planetDistance.x * -1;
						planetDistance.y = planetDistance.y * -1;
						vecSum = Math.abs(planetDistance.x) + Math.abs(planetDistance.y);			
						planetDistance.x = planetDistance.x * ((1/vecSum)* radius/finalDistance) * body.getMass() * planetVector.get(j).getFixtureList().get(0).getDensity();
						planetDistance.y = planetDistance.y * ((1/vecSum)* radius/finalDistance) * body.getMass() * planetVector.get(j).getFixtureList().get(0).getDensity();
						if(((EntityData)body.getUserData()).getID() != ((EntityData)planetVector.get(j).getUserData()).getID()){
							body.applyForce(planetDistance,body.getWorldCenter(),true);
						}
					}
				}	
		}

	}
	public static void renderWells(Batch batch){
		count = 0;
		for(Sprite sprite:gravitySprites){
			sprite.setOriginCenter();
			sprite.setPosition(planetVector.get(count).getPosition().x - ((CircleShape)planetVector.get(count).getFixtureList().get(0).getShape()).getRadius(), 
								planetVector.get(count).getPosition().y -((CircleShape)planetVector.get(count).getFixtureList().get(0).getShape()).getRadius());
			count++;
			sprite.draw(batch);
		}
	}
}		
package com.gdx.orion.utils;
import gdx.orion.entities.EntityData;
import gdx.orion.entities.EntityType;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
public class GravityUtils{
	private static ArrayList<Body> planetVector = new ArrayList<Body>();
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
	private static final int GRAVITATIONAL_REACH = 15;
	
	public static void addGravityWell(float px,float py, float r, float density, World world, boolean inWardForce) {
		fixtureDef.restitution=0;
		fixtureDef.density = density;
		circleShape =new CircleShape();
		circleShape.setRadius(r);
		fixtureDef.shape= circleShape;
		bodyDef = new BodyDef();
		bodyDef.position.set(px,py);
		bodyDef.type = BodyType.StaticBody;
		thePlanet = world.createBody(bodyDef);
		planetVector.add(thePlanet);
		if(inWardForce)thePlanet.setUserData(new EntityData(1000, EntityType.GRAVITY_WELL, null));
		else{
		}
		thePlanet.createFixture(fixtureDef);
	}
	public static void applyGravity(World world,Array<Body> body){
		for (int i = 0; i < body.size; i++){
			forceVector = body.get(i).getWorldCenter();
			for (int j = 0; j < planetVector.size(); j++) {
				if(planetVector.get(j).getFixtureList().size > 0){
					circleShape = (CircleShape)planetVector.get(j).getFixtureList().get(0).getShape();
					radius =circleShape.getRadius();
					planetPosition = planetVector.get(j).getWorldCenter();
					planetDistance =new Vector2(0,0);
					planetDistance.add(forceVector);
					planetDistance.sub(planetPosition);
					finalDistance = planetDistance.len();
					if(finalDistance <= radius * GRAVITATIONAL_REACH) {
						planetDistance.x = planetDistance.x * -1;
						planetDistance.y = planetDistance.y * -1;
						vecSum = Math.abs(planetDistance.x) + Math.abs(planetDistance.y);			
						planetDistance.x = planetDistance.x * ((1/vecSum)* radius/finalDistance) * body.get(i).getMass() * planetVector.get(j).getFixtureList().get(0).getDensity();
						planetDistance.y = planetDistance.y * ((1/vecSum)* radius/finalDistance) * body.get(i).getMass() * planetVector.get(j).getFixtureList().get(0).getDensity();
						body.get(i).applyForce(planetDistance,body.get(i).getWorldCenter(),false);
					}
				}
			}	
		}	
	}	
}		
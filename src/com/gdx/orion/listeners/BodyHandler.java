package com.gdx.orion.listeners;

import java.util.ArrayList;

import com.gdx.orion.entities.Projectile;
import com.gdx.orion.entities.EntityData;
import com.gdx.orion.entities.EntityType;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.gdx.orion.screens.GameStateManager;
import com.gdx.orion.utils.WorldUtils;

public class BodyHandler {
	private static EntityData entityDataA;
    private static Vector2 tempV2 = new Vector2(0,0);
    private static Vector2 midPoint = new Vector2(0,0);
    private static Vector2 midPoint2 = new Vector2(0,0);
    private static Vector2 midPoint3 = new Vector2(0,0);
    private static ArrayList<Body> effectBody = new ArrayList<Body>();
	private static float[] tempAsteroid = new float[48];
	private static int count;
    private static PolygonShape ps = new PolygonShape();
	public static void update(OrthographicCamera cam,World gameWorld,Array<Body> bodies){
		for(Body body:bodies){
			if(body.getUserData() instanceof EntityData){
				count = 0;
				entityDataA = (EntityData)body.getUserData();
				tempAsteroid = WorldUtils.getRenderAsteroidData(body);
				
				if(entityDataA.getType() == EntityType.PRE_FRAG_ASTEROID){
					ps = (PolygonShape)body.getFixtureList().get(0).getShape();
					ps.getVertex(0, midPoint);
					ps.getVertex(1, tempV2);
					if(((entityDataA.getEntropy() < 13)) &&((int)midPoint.x - (int)tempV2.x > GameStateManager.play.MAX_FRAGMENT_SIZE ||(int)midPoint.y - (int)tempV2.y > GameStateManager.play.MAX_FRAGMENT_SIZE)){
						for(int i = 0; i < ps.getVertexCount(); i++){
							ps.getVertex(i, tempV2);
							tempAsteroid[count++] = tempV2.x;
							tempAsteroid[count++] = tempV2.y;
						}
						midPoint.x = ((tempAsteroid[0] + tempAsteroid[2])/2);
						midPoint.y = ((tempAsteroid[1] + tempAsteroid[3])/2);
						midPoint2.x = ((tempAsteroid[2] + tempAsteroid[4])/2);
						midPoint2.y = ((tempAsteroid[3] + tempAsteroid[5])/2);
						midPoint3.x = ((tempAsteroid[0] + tempAsteroid[4])/2);
						midPoint3.y = ((tempAsteroid[1] + tempAsteroid[5])/2);
						WorldUtils.fragmentAsteroid(midPoint2.x, midPoint2.y, midPoint.x, midPoint.y, tempAsteroid[2], tempAsteroid[3], gameWorld,body);
						WorldUtils.fragmentAsteroid(midPoint2.x, midPoint2.y, tempAsteroid[4], tempAsteroid[5], midPoint3.x, midPoint3.y,  gameWorld,body);
						WorldUtils.fragmentAsteroid(midPoint.x, midPoint.y, midPoint3.x, midPoint3.y, tempAsteroid[0], tempAsteroid[1], gameWorld,body);
						WorldUtils.fragmentAsteroid(midPoint.x, midPoint.y, midPoint2.x, midPoint2.y, midPoint3.x, midPoint3.y, gameWorld,body);
						
						GameStateManager.play.destroy.add(body);
					}else{
						entityDataA.setType(EntityType.ASTEROID_FRAGMENT);
					}
				}
				
			if(body.getUserData() instanceof Integer){
				count = (Integer)body.getUserData();
				if(count == GameStateManager.play.aliveTime){
					GameStateManager.play.destroy.add(body);
				}
			}
		}
	}
	}
	public static void handleStates(World gameWorld , Array<Body> bodies) {
		for(Body body:bodies){
				if(entityDataA.getType() == EntityType.DESTROYME_ASTEROID){
					((Projectile)entityDataA.getObject()).fragment(0);
				}
				if(entityDataA.getType() == EntityType.DELETEME){
					gameWorld.destroyBody(body);
				}
				if(entityDataA.getType() == EntityType.ASTEROID){
				}
		}		
	}
	public static void destroyBodies(World gameWorld,Array<Body> destroy) {
		for(Body body:destroy){
			gameWorld.destroyBody(body);    
		}		
	}
	public static void applyEffects(Batch batch){
		for(Body body:effectBody){
			entityDataA = (EntityData)body.getUserData();
		}
		effectBody.clear();
	}
	public static void createJoints(World gameWorld,
			Array<JointDef> createJoints) {
		for(JointDef def: createJoints){
			gameWorld.createJoint(def);
		}
		
	}
}

package com.gdx.orion.handlers;

import java.util.ArrayList;

import com.gdx.orion.entities.Asteroid;
import com.gdx.orion.entities.Comet;
import com.gdx.orion.entities.EntityData;
import com.gdx.orion.entities.EntityType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.gdx.orion.screens.GameStateManager;
import com.gdx.orion.utils.EffectUtils;
import com.gdx.orion.utils.GravityUtils;
import com.gdx.orion.utils.WorldUtils;

public class BodyHandler {
	private static EntityData entityDataA;
    private static Vector2 tempV2 = new Vector2(0,0);
    private static Vector2 midPoint = new Vector2(0,0);
    private static Vector2 midPoint2 = new Vector2(0,0);
    private static Vector2 midPoint3 = new Vector2(0,0);
    private static ArrayList<Body> effectBody = new ArrayList<Body>();
	private static float[] tempAsteroid = new float[48];
	private static int fragmentsCulled;
	private static int count;
    private static PolygonShape ps = new PolygonShape();
	private static ImmediateModeRenderer20 r = new ImmediateModeRenderer20(false, true, 0);
	private static ShapeRenderer sr = new ShapeRenderer();
	public static void update(OrthographicCamera cam,World gameWorld,Array<Body> bodies){
		fragmentsCulled = 0;
		for(Body body:bodies){
			if(body.getUserData() instanceof EntityData){
				count = 0;
				entityDataA = (EntityData)body.getUserData();
				tempAsteroid = WorldUtils.getRenderAsteroidData(body);
				if(entityDataA.getType() == EntityType.ASTEROID){
					if(GameStateManager.play.isOnScreen(GameStateManager.play.getPlayerShip().getBody().getPosition(),body.getPosition())){
						((Asteroid)entityDataA.getObject()).draw(r,cam);
					}
				}
				if(entityDataA.getType() == EntityType.COMET){
					if(GameStateManager.play.isOnScreen(GameStateManager.play.getPlayerShip().getBody().getPosition(),body.getPosition())){
						((Comet)entityDataA.getObject()).draw(r,cam);
					}
					effectBody.add(body);
				}
				if(entityDataA.getType() == EntityType.PRE_FRAG_ASTEROID){
					ps = (PolygonShape)body.getFixtureList().get(0).getShape();
					ps.getVertex(0, midPoint);
					ps.getVertex(1, tempV2);
					if(((entityDataA.getEntropy() < 3)) &&((int)midPoint.x - (int)tempV2.x > GameStateManager.play.MAX_FRAGMENT_SIZE ||(int)midPoint.y - (int)tempV2.y > GameStateManager.play.MAX_FRAGMENT_SIZE)){
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
				if(entityDataA.getType() == EntityType.PRE_FRAG_COMET){
					ps = (PolygonShape)body.getFixtureList().get(0).getShape();
					ps.getVertex(0, midPoint);
					ps.getVertex(1, tempV2);
					if(((entityDataA.getEntropy() < 3)) &&((int)midPoint.x - (int)tempV2.x > GameStateManager.play.MAX_FRAGMENT_SIZE ||(int)midPoint.y - (int)tempV2.y > GameStateManager.play.MAX_FRAGMENT_SIZE)){
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
						WorldUtils.fragmentComet(midPoint2.x, midPoint2.y, midPoint.x, midPoint.y, tempAsteroid[2], tempAsteroid[3], gameWorld,body);
						WorldUtils.fragmentComet(midPoint2.x, midPoint2.y, tempAsteroid[4], tempAsteroid[5], midPoint3.x, midPoint3.y,  gameWorld,body);
						WorldUtils.fragmentComet(midPoint.x, midPoint.y, midPoint3.x, midPoint3.y, tempAsteroid[0], tempAsteroid[1], gameWorld,body);
						WorldUtils.fragmentComet(midPoint.x, midPoint.y, midPoint2.x, midPoint2.y, midPoint3.x, midPoint3.y, gameWorld,body);
						
						GameStateManager.play.destroy.add(body);
					}else{
						entityDataA.setType(EntityType.COMET_FRAGMENT);
					}
				}
				if(entityDataA.getType() == EntityType.ASTEROID_FRAGMENT){
					if(GameStateManager.play.isOnScreen(GameStateManager.play.getPlayerShip().getBody().getPosition(),body.getPosition())){
						WorldUtils.drawAsteroidFragment(body,r,cam);
						if(gameWorld.getBodyCount() > GameStateManager.play.MAX_BODIES * 1.5){
							if(fragmentsCulled < GameStateManager.play.FRAGMENT_CULL_PER_FRAME){
								GameStateManager.play.destroy.add(body);
								fragmentsCulled++;
							}
						}
					}else{
						if(gameWorld.getBodyCount() > GameStateManager.play.MAX_BODIES){
							if(fragmentsCulled < GameStateManager.play.FRAGMENT_CULL_PER_FRAME){
								GameStateManager.play.destroy.add(body);
								fragmentsCulled++;
							}
						}
					}
				}
				if(entityDataA.getType() == EntityType.COMET_FRAGMENT){
					if(GameStateManager.play.isOnScreen(GameStateManager.play.getPlayerShip().getBody().getPosition(),body.getPosition())){
						WorldUtils.drawCometFragment(body,r,cam);
						if(gameWorld.getBodyCount() > GameStateManager.play.MAX_BODIES * 1.5){
							if(fragmentsCulled < GameStateManager.play.FRAGMENT_CULL_PER_FRAME){
								GameStateManager.play.destroy.add(body);
								fragmentsCulled++;
							}
						}
					}else{
						if(gameWorld.getBodyCount() > GameStateManager.play.MAX_BODIES){
							if(fragmentsCulled < GameStateManager.play.FRAGMENT_CULL_PER_FRAME){
								GameStateManager.play.destroy.add(body);
								fragmentsCulled++;
							}
						}
					}
					//effectBodies.add(body);
				}
			}
			if(body.getUserData() instanceof Integer){
				count = (Integer)body.getUserData();
				if(count == GameStateManager.play.aliveTime){
					GameStateManager.play.destroy.add(body);
				}
				if(GameStateManager.play.isOnScreen(GameStateManager.play.getPlayerShip().getBody().getPosition(),body.getPosition())){
					WorldUtils.drawBullet(body,sr,cam,Color.YELLOW);
				}
			}
		}
	}
	public static void handleStates(World gameWorld , Array<Body> bodies) {
		for(Body body:bodies){
			GravityUtils.applyGravity(gameWorld,body);
			if(body.getUserData() instanceof EntityData){
			entityDataA = (EntityData)body.getUserData();
				if(entityDataA.getType() == EntityType.DESTROYME_ASTEROID){
					((Asteroid)entityDataA.getObject()).fragment(10);
				}
				if(entityDataA.getType() == EntityType.DESTROYME_COMET){
					((Comet)entityDataA.getObject()).fragment(10);
				}
				if(entityDataA.getType() == EntityType.DELETEME){
					gameWorld.destroyBody(body);
				}
				if(entityDataA.getType() == EntityType.ASTEROID){
				}
				if(entityDataA.getType() == EntityType.COMET){
				}
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
			 if(entityDataA.getType() == EntityType.COMET){
					EffectUtils.cometTrailEffect(body.getWorldCenter(),((Comet)entityDataA.getObject()).getParticleEffect(), batch, body.getLinearVelocity().angle() - 90);
			}
		}
		effectBody.clear();
	}
}

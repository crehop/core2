package com.gdx.orion.handlers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.gdx.orion.screens.GameStateManager;
import com.gdx.orion.utils.Console;

import com.gdx.orion.entities.EntityData;
import com.gdx.orion.entities.EntityType;

public class ContactHandler implements ContactListener {
	private  EntityData entityDataA;
	private  EntityData entityDataB;
    private  Vector2 tempV2 = new Vector2(0,0);
    private  Vector2 midPoint = new Vector2(0,0);
    private  float velo;
    
	@Override
	public void beginContact(Contact contact) {
		entityDataA = null;
		entityDataB = null;
		if(GameStateManager.play.isOnScreen(GameStateManager.play.getPlayerShip().getPosition(), contact.getFixtureA().getBody().getPosition())){
			if(contact.getFixtureA().getBody().getUserData() instanceof EntityData){
				entityDataA = (EntityData)contact.getFixtureA().getBody().getUserData();
			}
			if(contact.getFixtureB().getBody().getUserData() instanceof EntityData){
				entityDataB = (EntityData)contact.getFixtureB().getBody().getUserData();
			}
			if(entityDataA != null){
				if(entityDataA.getType() == EntityType.ASTEROID && contact.getFixtureB().getBody().getUserData() instanceof Integer){
					entityDataA.damage(1);
					if(entityDataA.getLife() < 1){
						entityDataA.setType(EntityType.DESTROYME_ASTEROID);
						}
				}
				if(entityDataA.getType() == EntityType.COMET && contact.getFixtureB().getBody().getUserData() instanceof Integer){
					entityDataA.damage(1);
					if(entityDataA.getLife() < 1){
						entityDataA.setType(EntityType.DESTROYME_COMET);
						}
				}
				if((entityDataA.getType() == EntityType.ASTEROID_FRAGMENT || entityDataA.getType() == EntityType.COMET_FRAGMENT) && contact.getFixtureB().getBody().getUserData() instanceof Integer){
					entityDataA.damage(1);
					if(entityDataA.getLife() < 1){
						entityDataA.setType(EntityType.DELETEME);
					}
				}

				if(entityDataA.getType() == EntityType.GRAVITY_WELL){
					if(entityDataB != null){
						if(entityDataB.getType() != EntityType.SHIP && 
					       entityDataB.getType() != EntityType.GRAVITY_WELL && 
						   entityDataB.getType() != EntityType.SHIELD &&
						   entityDataB.getType() != EntityType.WORLD_BOUNDRY){
							
							if(entityDataB.getType() == EntityType.ASTEROID || entityDataB.getType() == EntityType.COMET){
								if(GameStateManager.play.getPlayerShip().ropeFired()){
									if(entityDataB.getID() == GameStateManager.play.getPlayerShip().getRope().getGrappleID()){
										GameStateManager.play.getPlayerShip().getRope().destroyRope();
										GameStateManager.play.getPlayerShip().setRopeFired(false);
									}else{
										entityDataB.setType(EntityType.DELETEME);
									}
								}else{
									entityDataB.setType(EntityType.DELETEME);
								}
							}else{
								entityDataB.setType(EntityType.DELETEME);
							}
						}
					}
				}

			}		
			if(entityDataB != null){
				if(entityDataB.getType() == EntityType.ASTEROID && contact.getFixtureA().getBody().getUserData() instanceof Integer){
					entityDataB.damage(1);
					if(entityDataB.getLife() < 1){
						entityDataB.setType(EntityType.DESTROYME_ASTEROID);
					}
				}
				if(entityDataB.getType() == EntityType.COMET && contact.getFixtureA().getBody().getUserData() instanceof Integer){
					entityDataB.damage(1);
					if(entityDataB.getLife() < 1){
						entityDataB.setType(EntityType.DESTROYME_COMET);
					}
				}
				if((entityDataB.getType() == EntityType.COMET_FRAGMENT || entityDataB.getType() == EntityType.ASTEROID_FRAGMENT) && contact.getFixtureA().getBody().getUserData() instanceof Integer){
					entityDataB.damage(1);
					if(entityDataB.getLife()  < 1){
						entityDataB.setType(EntityType.DELETEME);
					}
				}
				if(entityDataB.getType() == EntityType.GRAVITY_WELL){
					if(entityDataA != null){
						if(entityDataA.getType() != EntityType.SHIP && 
					       entityDataA.getType() != EntityType.GRAVITY_WELL && 
						   entityDataA.getType() != EntityType.SHIELD &&
						   entityDataA.getType() != EntityType.WORLD_BOUNDRY){
							
							if(entityDataA.getType() == EntityType.ASTEROID
								||entityDataA.getType() == EntityType.COMET){
								if(GameStateManager.play.getPlayerShip().ropeFired()){
									if(entityDataA.getID() == GameStateManager.play.getPlayerShip().getRope().getGrappleID()){
										GameStateManager.play.getPlayerShip().getRope().destroyRope();
										GameStateManager.play.getPlayerShip().setRopeFired(false);
									}else{
										entityDataA.setType(EntityType.DELETEME);
									}
								}else{
									entityDataA.setType(EntityType.DELETEME);
								}
							}else{
								entityDataA.setType(EntityType.DELETEME);
							}
						}
					}
				}
			}
			if(entityDataA != null && entityDataB != null){
				if(entityDataA.getType() == EntityType.ASTEROID && entityDataB.getType() == EntityType.SHIP){
					tempV2 = contact.getFixtureA().getBody().getLinearVelocity();
					midPoint = contact.getFixtureB().getBody().getLinearVelocity();
					velo =  tempV2.sub(midPoint).len();
					if(velo > 110){
						entityDataA.setType(EntityType.DESTROYME_ASTEROID);
					}
				}else if(entityDataA.getType() == EntityType.COMET && entityDataB.getType() == EntityType.SHIP){
					tempV2 = contact.getFixtureA().getBody().getLinearVelocity();
					midPoint = contact.getFixtureB().getBody().getLinearVelocity();
					velo =  tempV2.sub(midPoint).len();
					if(velo > 110){
						entityDataA.setType(EntityType.DESTROYME_COMET);
					}
				}else if(entityDataB.getType() == EntityType.ASTEROID && entityDataA.getType() == EntityType.SHIP){
					tempV2 = contact.getFixtureB().getBody().getLinearVelocity();
					midPoint = contact.getFixtureA().getBody().getLinearVelocity();
					velo = tempV2.sub(midPoint).len();
					Console.setLine6("FORCE OF IMPACT" + velo);
					if(velo > 110){
						entityDataB.setType(EntityType.DESTROYME_ASTEROID);
					}
				}else if(entityDataB.getType() == EntityType.COMET && entityDataA.getType() == EntityType.SHIP){
					tempV2 = contact.getFixtureB().getBody().getLinearVelocity();
					midPoint = contact.getFixtureA().getBody().getLinearVelocity();
					velo = tempV2.sub(midPoint).len();
					Console.setLine6("FORCE OF IMPACT" + velo);
					if(velo > 110){
						entityDataB.setType(EntityType.DESTROYME_COMET);
					}
				}
				if(entityDataB.getType() == EntityType.ASTEROID && entityDataA.getType() == EntityType.GRAPPLE
					||entityDataB.getType() == EntityType.COMET && entityDataA.getType() == EntityType.GRAPPLE){
					GameStateManager.play.getPlayerShip().getRope().changeGrapple(contact.getFixtureB().getBody());
				}else if(entityDataA.getType() == EntityType.ASTEROID && entityDataB.getType() == EntityType.GRAPPLE
						||entityDataA.getType() == EntityType.COMET && entityDataB.getType() == EntityType.GRAPPLE){
					GameStateManager.play.getPlayerShip().getRope().changeGrapple(contact.getFixtureA().getBody());
				}else if(entityDataB.getType() == EntityType.ASTEROID && entityDataA.getType() == EntityType.ASTEROID ||
						entityDataB.getType() == EntityType.COMET && entityDataA.getType() == EntityType.ASTEROID){
					tempV2 = contact.getFixtureB().getBody().getLinearVelocity();
					midPoint = contact.getFixtureA().getBody().getLinearVelocity();
					velo = tempV2.sub(midPoint).len();
					Console.setLine6("FORCE OF IMPACT" + velo);
					if(velo > 110){
						entityDataA.setType(EntityType.DESTROYME_ASTEROID);
					}
				}else if(entityDataB.getType() == EntityType.ASTEROID && entityDataA.getType() == EntityType.COMET ||
						entityDataB.getType() == EntityType.COMET && entityDataA.getType() == EntityType.COMET){
					tempV2 = contact.getFixtureB().getBody().getLinearVelocity();
					midPoint = contact.getFixtureA().getBody().getLinearVelocity();
					velo = tempV2.sub(midPoint).len();
					Console.setLine6("FORCE OF IMPACT" + velo);
					if(velo > 110){
						entityDataA.setType(EntityType.DESTROYME_COMET);
					}
				}
			}
		}
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}
}

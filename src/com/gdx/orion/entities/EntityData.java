package com.gdx.orion.entities;

import com.badlogic.gdx.math.MathUtils;


public class EntityData {
	private float life = 0;
	private int ID = 0;
	private EntityType type;
	private int alive;
	private Object object;
	private int entropy = 0;
	public EntityData(float life,EntityType type,Object object){
		this.type = type;
		this.ID = UIDGetter.getID();
		this.life = life;
		this.object = object;
		this.alive = 0;
		this.setEntropy(MathUtils.random(-1,1));
	}
	public EntityType getType(){
		return type;
	}
	public int getID(){
		return this.ID;
	}
	public float getLife(){
		return life;
	}
	public void damage(float amount){
		life -= amount;
		if(life<0){
			if(this.object instanceof Asteroid){
				if(((Asteroid)object).getBody().getUserData() instanceof EntityData){
					((EntityData)((Asteroid)object).getBody().getUserData()).setType(EntityType.DESTROYME_ASTEROID);
				}
			}
		}
	}
	public void setType(EntityType type) {
		this.type = type;
	}
	public void alive(){
		this.alive = this.alive + 1;
	}
	public int getAliveTime(){
		return this.alive;
	}
	public Object getObject(){
		return this.object;
	}
	public int getEntropy() {
		return entropy;
	}
	public void setEntropy(int entropy) {
		this.entropy = entropy;
	}
}

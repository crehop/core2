package com.gdx.orion.entities;


public class EntityData {
	private float life = 0;
	private int ID = 0;
	private EntityType type;
	private int alive;
	private Object object;
	public EntityData(float life,EntityType type,Object object){
		this.type = type;
		this.ID = UIDGetter.getID();
		this.life = life;
		this.object = object;
		this.alive = 0;
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
					((EntityData)((Asteroid)object).getBody().getUserData()).setType(EntityType.DESTROYME);
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
}

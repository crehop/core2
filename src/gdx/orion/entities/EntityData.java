package gdx.orion.entities;

import com.gdx.orion.utils.UIDGetter;

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

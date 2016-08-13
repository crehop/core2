package com.gdx.orion.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.DelaunayTriangulator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.orion.Main;
import com.gdx.orion.screens.Play;
import com.gdx.orion.utils.EffectUtils;
import com.gdx.orion.utils.WorldUtils;
//TODO MAKE STATIC FOR REUSABLILITY! NO NEED TO STORE DUE TO USERDATA!
public class Comet{
	private PolygonShape shape = new PolygonShape();
	private Body body;
	private BodyDef def;
	private Vector2 speed;
	private float width;
	private int size;
	private float xmin;
	private float xmax;
	private float ymin;
	private float ymax;
	int numpoints = 0;
	private float[] shapeVerts;
	private World world;
	private float[] points;
	private float density;
	private Color[] colors = new Color[15];
	private int ID;

	public Comet(World world, Vector2 position, float density, int size){
		ID = UIDGetter.getID();
		this.density = density;
		this.world = world;
		for(int i = 0; i < colors.length; i++){
			switch(MathUtils.random(1,4)){
				case 1:
					colors[i] = Color.BLUE;
					break;
				case 2:
					colors[i] = Color.NAVY;
					break;
				case 3:
					colors[i] = Color.ROYAL;
					break;
				default:
					colors[i] = Color.SLATE;
					break;
			}
		}
		def = new BodyDef();
		def.position.set(position.x, position.y);
		def.type = BodyType.DynamicBody;
		def.angle = 200;
		body = world.createBody(def);
		this.density = density;
		shape.set(setShape(size,position));
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = density;
		fdef.friction = 1;
		body.createFixture(fdef);
		body.applyForce(speed,body.getWorldCenter(), false);
		body.applyAngularImpulse(MathUtils.random(-400000,400000), false);
		body.setUserData(new EntityData(MathUtils.random(10) * size,EntityType.COMET,this));
		this.size = size;
	}

	private float[] setShape(int size, Vector2 position) {
		this.size = size;
		numpoints = 8;
		float force = 0;
		if(this.size == 1){
			force = MathUtils.random(-5.0f,5.0f);
			width = MathUtils.random(7.0f,10.0f);
			numpoints = 8;
		}
		if(this.size == 2){
			force = MathUtils.random(-7.50f,7.50f);
			width = MathUtils.random(10.5f,15.5f);
			numpoints = 8;
		}
		if(this.size == 3){
			numpoints = 8;
			force = MathUtils.random(-60.0f,60.0f);
			width = MathUtils.random(17,25);
		}
		this.speed = new Vector2((position.x + MathUtils.random(force * 10000 * size * density)),(position.y + MathUtils.random(force *100)));
		float radians = (float) (Math.toRadians(360)/numpoints);
		float[] shapex = new float[numpoints];
		float[] shapey = new float[numpoints];
		float[] dists = new float[numpoints];
		shapeVerts = new float[numpoints * 2];
		points = new float[shapeVerts.length * 2];
		float radius = width/2;
		for(int i = 0; i < numpoints; i++){
			dists[i] = MathUtils.random(radius /.75f, radius);
		} 
		@SuppressWarnings("unused")
		float angle = 0;
		for(int i = 0; i < numpoints; i++){
			shapex[i] = body.getLocalCenter().x + MathUtils.cos(radians * i) * dists[i];
			shapey[i] = body.getLocalCenter().y + MathUtils.sin(radians * i) * dists[i];
			angle += 4 * 3.1415f/numpoints;

		}
		int count = 0;
		ymin = shapey[0];
		xmin = shapex[0];
		ymax = shapey[0];
		xmax = shapex[0];
		for(int i = 0; i < numpoints; i++){
			if(xmax < shapex[i]) xmax = shapex[i];
			if(xmin > shapex[i]) xmin = shapex[i];
			if(xmax < shapex[i]) xmax = shapex[i];
			if(xmin > shapex[i]) xmin = shapex[i];
			if(ymax < shapex[i]) ymax = shapey[i];
			if(ymin > shapex[i]) ymin = shapey[i];
			if(ymax < shapex[i]) ymax = shapey[i];
			if(ymin > shapex[i]) ymin = shapey[i];
			shapeVerts[count++] = shapex[i];
			shapeVerts[count++] = shapey[i];
		}
		return shapeVerts;
	}
	public void fragment(int force){
		for(int i = 0; i < shapeVerts.length; i++){
			points[i] = shapeVerts[i];
		}
		DelaunayTriangulator triangulator = new DelaunayTriangulator();
		triangulator.computeTriangles(points, 0, force, true);
		int count = 0;
		for(int i = 0; i < points.length; i+=6){
			count++;
			if(count <= 3){
				WorldUtils.fragmentComet(points[i], points[i+1], points[i+2],
						points[i+3], points[i+4], points[i+5], world, body);
			}
		}
		WorldUtils.fragmentComet(points[16],points[17],points[6],points[7],points[10], points[11],world, body);
		WorldUtils.fragmentComet(points[0],points[1],points[6],points[7],points[16], points[17],world, body);
		WorldUtils.fragmentComet(points[6],points[7],points[4],points[5],points[0], points[1],world, body);
		WorldUtils.fragmentComet(points[16],points[17],points[0],points[1],points[14], points[15],world, body);
		WorldUtils.fragmentComet(points[10],points[11],points[12],points[13],points[16], points[17],world, body);
		world.destroyBody(body);
	}

	public void draw(ImmediateModeRenderer20 r, OrthographicCamera cam){
		r.begin(cam.combined, GL20.GL_TRIANGLES);
		for(int i = 0; i < shapeVerts.length; i++){
			points[i] = shapeVerts[i];
		}
		//Vector2 impact = new Vector2(impactx,impacty);
		//Vector2 exit = new Vector2((impactx-xmin)/(xmax - xmin),(impacty - ymin)/(ymax-ymin));
		DelaunayTriangulator triangulator = new DelaunayTriangulator();
		triangulator.computeTriangles(points, 0, 10, true);
		int count = 0;
		for(int i = 0; i < points.length; i+=6){
			count++;
			if(count <= 3){
				r.color(colors[1]);
				r.vertex((float)(((points[i]) * Math.cos(body.getAngle())) - ((points[i+1]) * Math.sin(body.getAngle()))) + body.getPosition().x,
						(float)(((points[i + 1]) * Math.cos(body.getAngle())) + ((points[i]) * Math.sin(body.getAngle()))) + body.getPosition().y,0);
				r.color(colors[14]);
				r.vertex((float)(((points[i+2]) * Math.cos(body.getAngle())) - ((points[i+3]) * Math.sin(body.getAngle()))) + body.getPosition().x,
						(float)(((points[i+3]) * Math.cos(body.getAngle())) + ((points[i+2]) * Math.sin(body.getAngle()))) + body.getPosition().y,0);
				r.color(colors[4]);
				r.vertex((float)(((points[i+4]) * Math.cos(body.getAngle())) - ((points[i+5]) * Math.sin(body.getAngle()))) + body.getPosition().x,
						(float)(((points[i+5]) * Math.cos(body.getAngle())) + ((points[i+4]) * Math.sin(body.getAngle()))) + body.getPosition().y,0);
			}
		}
		r.color(colors[0]);
		r.vertex((float)(((points[16]) * Math.cos(body.getAngle())) - ((points[17]) * Math.sin(body.getAngle()))) + body.getPosition().x,
				(float)(((points[17]) * Math.cos(body.getAngle())) + ((points[16]) * Math.sin(body.getAngle()))) + body.getPosition().y,0);
		r.color(colors[1]);
		r.vertex((float)(((points[6]) * Math.cos(body.getAngle())) - ((points[7]) * Math.sin(body.getAngle()))) + body.getPosition().x,
				(float)(((points[7]) * Math.cos(body.getAngle())) + ((points[6]) * Math.sin(body.getAngle()))) + body.getPosition().y,0);
		r.color(colors[2]);
		r.vertex((float)(((points[10]) * Math.cos(body.getAngle())) - ((points[11]) * Math.sin(body.getAngle()))) + body.getPosition().x,
				(float)(((points[11]) * Math.cos(body.getAngle())) + ((points[10]) * Math.sin(body.getAngle()))) + body.getPosition().y,0);	
		r.color(colors[3]);
		r.vertex((float)(((points[0]) * Math.cos(body.getAngle())) - ((points[1]) * Math.sin(body.getAngle()))) + body.getPosition().x,
				(float)(((points[1]) * Math.cos(body.getAngle())) + ((points[0]) * Math.sin(body.getAngle()))) + body.getPosition().y,0);
		r.color(colors[4]);
		r.vertex((float)(((points[6]) * Math.cos(body.getAngle())) - ((points[7]) * Math.sin(body.getAngle()))) + body.getPosition().x,
				(float)(((points[7]) * Math.cos(body.getAngle())) + ((points[6]) * Math.sin(body.getAngle()))) + body.getPosition().y,0);
		r.color(colors[5]);
		r.vertex((float)(((points[16]) * Math.cos(body.getAngle())) - ((points[17]) * Math.sin(body.getAngle()))) + body.getPosition().x,
				(float)(((points[17]) * Math.cos(body.getAngle())) + ((points[16]) * Math.sin(body.getAngle()))) + body.getPosition().y,0);
		r.color(colors[6]);
		r.vertex((float)(((points[6]) * Math.cos(body.getAngle())) - ((points[7]) * Math.sin(body.getAngle()))) + body.getPosition().x,
				(float)(((points[7]) * Math.cos(body.getAngle())) + ((points[6]) * Math.sin(body.getAngle()))) + body.getPosition().y,0);
		r.color(colors[7]);
		r.vertex((float)(((points[4]) * Math.cos(body.getAngle())) - ((points[5]) * Math.sin(body.getAngle()))) + body.getPosition().x,
				(float)(((points[5]) * Math.cos(body.getAngle())) + ((points[4]) * Math.sin(body.getAngle()))) + body.getPosition().y,0);
		r.color(Color.DARK_GRAY);
		r.vertex((float)(((points[0]) * Math.cos(body.getAngle())) - ((points[1]) * Math.sin(body.getAngle()))) + body.getPosition().x,
				(float)(((points[1]) * Math.cos(body.getAngle())) + ((points[0]) * Math.sin(body.getAngle()))) + body.getPosition().y,0);
		r.color(colors[8]);
		r.vertex((float)(((points[16]) * Math.cos(body.getAngle())) - ((points[17]) * Math.sin(body.getAngle()))) + body.getPosition().x,
				(float)(((points[17]) * Math.cos(body.getAngle())) + ((points[16]) * Math.sin(body.getAngle()))) + body.getPosition().y,0);
		r.color(colors[9]);
		r.vertex((float)(((points[0]) * Math.cos(body.getAngle())) - ((points[1]) * Math.sin(body.getAngle()))) + body.getPosition().x,
				(float)(((points[1]) * Math.cos(body.getAngle())) + ((points[0]) * Math.sin(body.getAngle()))) + body.getPosition().y,0);
		r.color(colors[10]);
		r.vertex((float)(((points[14]) * Math.cos(body.getAngle())) - ((points[15]) * Math.sin(body.getAngle()))) + body.getPosition().x,
				(float)(((points[15]) * Math.cos(body.getAngle())) + ((points[14]) * Math.sin(body.getAngle()))) + body.getPosition().y,0);
		r.color(colors[11]);
		r.vertex((float)(((points[10]) * Math.cos(body.getAngle())) - ((points[11]) * Math.sin(body.getAngle()))) + body.getPosition().x,
				(float)(((points[11]) * Math.cos(body.getAngle())) + ((points[10]) * Math.sin(body.getAngle()))) + body.getPosition().y,0);
		r.color(colors[12]);
		r.vertex((float)(((points[12]) * Math.cos(body.getAngle())) - ((points[13]) * Math.sin(body.getAngle()))) + body.getPosition().x,
				(float)(((points[13]) * Math.cos(body.getAngle())) + ((points[12]) * Math.sin(body.getAngle()))) + body.getPosition().y,0);
		r.color(colors[13]);
		r.vertex((float)(((points[16]) * Math.cos(body.getAngle())) - ((points[17]) * Math.sin(body.getAngle()))) + body.getPosition().x,
				(float)(((points[17]) * Math.cos(body.getAngle())) + ((points[16]) * Math.sin(body.getAngle()))) + body.getPosition().y,0);
		r.end();
	}
	public Body getBody() {
		return body;
	}
	public void drawEffects(Batch batch){
	}
	public float[] getVerts() {
		return shapeVerts;
	}
	public int getID(){
		return ID;
	}
}


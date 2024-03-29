package com.gdx.orion.utils;


import java.util.Random;

import com.gdx.orion.entities.EntityData;
import com.gdx.orion.entities.EntityType;
import com.gdx.orion.entities.voxel.VoxelType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.gdx.orion.screens.GameStateManager;

public class WorldUtils {
	static PolygonShape shape = new PolygonShape();
	static FixtureDef fdef = new FixtureDef();
	private static Body body;
	private static BodyDef def = new BodyDef();
	private static Array<Body> bodies = new Array<Body>();
	private static int count = 0;
	static CircleShape shape2 = new CircleShape();
	static Random rand = new Random();
	@SuppressWarnings("unused")
	private static World world;
	public static final int POSITION_COMPONENTS = 2;
	public static final int COLOR_COMPONENTS = 4;
	public static final float LARGE_BODY_DAMPING = 0.45f;
	private static float[] temp = new float[48];
	private static float[] vertices = new float[6];
	private static boolean wireframe = true;
	private static Vector2 tempV2 = new Vector2();
	private static Color asteroid = Color.GRAY;
	private static Color comet = Color.BLUE;
	private static Mesh mesh;
	private static int force;
	public static void GenerateWorldBorder(World world,float x1,float x2,float y1,float y2){
		def.position.set(0,0);
		def.type = BodyType.StaticBody;
		world.getBodies(bodies);
		if(bodies.contains(body, false)){
			world.destroyBody(body);
		}
		body = world.createBody(def);
		float[] worldBorder = new float[8];
		worldBorder[0] = x1;
		worldBorder[1] = y1;
		worldBorder[2] = x1;
		worldBorder[3] = y2;
		worldBorder[4] = x1 + 2;
		worldBorder[5] = y2;
		worldBorder[6] = x1 + 2;
		worldBorder[7] = y1;
		shape.set(worldBorder);
		fdef.shape = shape;
		fdef.restitution = 0;
		body.createFixture(fdef);
		
		worldBorder[0] = x1;
		worldBorder[1] = y1;
		worldBorder[2] = x2;
		worldBorder[3] = y1;
		worldBorder[4] = x2;
		worldBorder[5] = y1 - 2;
		worldBorder[6] = x1;
		worldBorder[7] = y1 - 2;
		shape.set(worldBorder);
		fdef.shape = shape;
		body.createFixture(fdef);		
		
		worldBorder[0] = x1;
		worldBorder[1] = y2;
		worldBorder[2] = x2;
		worldBorder[3] = y2;
		worldBorder[4] = x2;
		worldBorder[5] = y2 - 2;
		worldBorder[6] = x1;
		worldBorder[7] = y2 - 2;
		shape.set(worldBorder);
		fdef.shape = shape;
		fdef.restitution = 0;
		body.createFixture(fdef);
		
		worldBorder[0] = x2;
		worldBorder[1] = y2;
		worldBorder[2] = x2 + 2;
		worldBorder[3] = y2;
		worldBorder[4] = x2 + 2;
		worldBorder[5] = y1;
		worldBorder[6] = x2;
		worldBorder[7] = y1;
		shape.set(worldBorder);
		fdef.shape = shape;
		fdef.restitution = 0;
		body.createFixture(fdef);
		body.setSleepingAllowed(false);
		body.setUserData(new EntityData(9999,EntityType.WORLD_BOUNDRY,null, VoxelType.AIR));
	}
	
	public static float[] moveVerts(Body body){
		count = 0;
		for(int i = 0; i < ((PolygonShape)body.getFixtureList().get(0).getShape()).getVertexCount(); i++){
			((PolygonShape)body.getFixtureList().get(0).getShape()).getVertex(i, tempV2);
			temp[count++] = tempV2.x;
			temp[count++] = tempV2.y;
		}
		return temp;
	}

	public static void fireBullet(World world, Vector2 position, float density, float size, Vector2 directionalForce){
		def.position.set(position.x, position.y); 
		def.type = BodyType.DynamicBody;
		def.angle = 200;
		shape2.setRadius(size);
		fdef.shape = shape2;
		fdef.density = density;
		fdef.restitution = 0.75f;
		body = world.createBody(def);
		body.createFixture(fdef);
		body.setLinearVelocity(directionalForce);
		body.setBullet(true);
		int data = GameStateManager.play.getAliveTime();
		body.setUserData((data));
	}
	public static void fragmentAsteroid(float points, float points2, float points3, float points4, float points5, float points6, World world, Body body2){
		shape = new PolygonShape();
		vertices[0] = points;
		vertices[1] = points2;
		vertices[2] = points3;
		vertices[3] = points4;
		vertices[4] = points5;
		vertices[5] = points6;
		def.type = BodyType.DynamicBody;
		def.position.set(body2.getPosition());
		def.angle = body2.getAngle();
		body = world.createBody(def);
		shape.set(vertices);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = body2.getFixtureList().get(0).getDensity();
		fdef.friction = body2.getFixtureList().get(0).getFriction();
		fdef.restitution = body2.getFixtureList().get(0).getRestitution();
		body.createFixture(fdef);
		body.setBullet(true);
		body.getPosition().set(body.getPosition().x, body.getPosition().y);
		body.setLinearVelocity(body2.getLinearVelocity());
		body.setAngularVelocity(body2.getAngularVelocity());
		force = 3;
		body.applyForce(body.getPosition().x, body.getPosition().y, body.getPosition().x + MathUtils.random(-force * body.getMass(),force * body.getMass()), body.getPosition().y + MathUtils.random(-force * body.getMass(),force * body.getMass()), true);
		body.setUserData(body2.getUserData());
		if(((EntityData)body.getUserData()).getEntropy() + MathUtils.random(0, 1) < 3){
			((EntityData)body.getUserData()).setType(EntityType.PRE_FRAG_ASTEROID);
		}else{
			((EntityData)body.getUserData()).setType(EntityType.ASTEROID_FRAGMENT);
			((EntityData)body.getUserData()).setEntropy(((EntityData)body.getUserData()).getEntropy() + 1);
		}
	}
	public static void fragmentComet(float points, float points2, float points3, float points4, float points5, float points6, World world, Body body2){
		shape = new PolygonShape();
		vertices[0] = points;
		vertices[1] = points2;
		vertices[2] = points3;
		vertices[3] = points4;
		vertices[4] = points5;
		vertices[5] = points6;
		def.type = BodyType.DynamicBody;
		def.position.set(body2.getPosition());
		def.angle = body2.getAngle();
		body = world.createBody(def);
		shape.set(vertices);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = body2.getFixtureList().get(0).getDensity();
		fdef.friction = body2.getFixtureList().get(0).getFriction();
		fdef.restitution = body2.getFixtureList().get(0).getRestitution();
		body.createFixture(fdef);
		body.setBullet(true);
		body.getPosition().set(body.getPosition().x, body.getPosition().y);
		body.setLinearVelocity(body2.getLinearVelocity());
		body.setAngularVelocity(body2.getAngularVelocity());
		force = 3;
		body.applyForce(body.getPosition().x, body.getPosition().y, body.getPosition().x + MathUtils.random(-force * body.getMass(),force * body.getMass()), body.getPosition().y + MathUtils.random(-force * body.getMass(),force * body.getMass()), true);
		body.setUserData(body2.getUserData());
		((EntityData)body.getUserData()).setType(EntityType.PRE_FRAG_ASTEROID);
		((EntityData)body.getUserData()).setEntropy(((EntityData)body.getUserData()).getEntropy() + 1);
	}
	public Body getBody() {
		return body;
	}

	public static Mesh createAsteroidMesh(Body body2) {
		count = 0;
		for(int i = 0; i < ((PolygonShape)body2.getFixtureList().get(0).getShape()).getVertexCount(); i++){
			((PolygonShape)body2.getFixtureList().get(0).getShape()).getVertex(i, tempV2);
			temp[count++] = tempV2.x;
			temp[count++] = tempV2.y;
			temp[count++] = asteroid.r + randomPosNegValue(20,20);;
			temp[count++] = asteroid.g + randomPosNegValue(20,20);;
			temp[count++] = asteroid.b + randomPosNegValue(20,20);;
			temp[count++] = asteroid.a + randomPosNegValue(20,20);;
		}
		if(mesh == null){
			mesh = new Mesh(true, temp.length/6, 0,  
		            new VertexAttribute(Usage.Position, POSITION_COMPONENTS, "a_position"),
		            new VertexAttribute(Usage.ColorUnpacked, COLOR_COMPONENTS, "a_color"));
		}
		System.out.println("COUNT" + count + " TEMP:" + mesh.getMaxVertices() + "Mesh Verts" + mesh.getNumVertices());
		mesh.setVertices(temp);
		return mesh;
	}
	public static Mesh createCometMesh(Body body2) {
		count = 0;
		for(int i = 0; i < ((PolygonShape)body2.getFixtureList().get(0).getShape()).getVertexCount(); i++){
			((PolygonShape)body2.getFixtureList().get(0).getShape()).getVertex(i, tempV2);
			temp[count++] = tempV2.x;
			temp[count++] = tempV2.y;
			temp[count++] = comet.r + randomPosNegValue(20,20);
			temp[count++] = comet.g + randomPosNegValue(20,20);
			temp[count++] = comet.b + randomPosNegValue(20,20);
			temp[count++] = comet.a + randomPosNegValue(20,20);
		}
		if(mesh == null){
			mesh = new Mesh(true, temp.length/6, 0,  
		            new VertexAttribute(Usage.Position, POSITION_COMPONENTS, "a_position"),
		            new VertexAttribute(Usage.ColorUnpacked, COLOR_COMPONENTS, "a_color"));
		}
		System.out.println("COUNT" + count + " TEMP:" + mesh.getMaxVertices() + "Mesh Verts" + mesh.getNumVertices());
		mesh.setVertices(temp);
		return mesh;
	}

	public static float[] getRenderAsteroidData(Body body2) {
		if(body2.getFixtureList().get(0).getShape() instanceof PolygonShape){
			count = 0;
			for(int i = 0; i < ((PolygonShape)body2.getFixtureList().get(0).getShape()).getVertexCount(); i++){
				((PolygonShape)body2.getFixtureList().get(0).getShape()).getVertex(i, tempV2);
				temp[count++] = asteroid.r + randomPosNegValue(20,20);;
				temp[count++] = asteroid.g + randomPosNegValue(20,20);;
				temp[count++] = asteroid.b + randomPosNegValue(20,20);;
				temp[count++] = asteroid.a + randomPosNegValue(20,20);;
				temp[count++] = tempV2.x + 10;
				//System.out.println("" + temp[count - 1]);
				temp[count++] = tempV2.y + 10;
				//System.out.println("" + temp[count - 1]);
			}
		}
		return temp;
	}
	public static float[] getRenderCometData(Body body2) {
		if(body2.getFixtureList().get(0).getShape() instanceof PolygonShape){
			count = 0;
			for(int i = 0; i < ((PolygonShape)body2.getFixtureList().get(0).getShape()).getVertexCount(); i++){
				((PolygonShape)body2.getFixtureList().get(0).getShape()).getVertex(i, tempV2);
				temp[count++] = comet.r + randomPosNegValue(20,20);;
				temp[count++] = comet.g + randomPosNegValue(20,20);;
				temp[count++] = comet.b + randomPosNegValue(20,20);;
				temp[count++] = comet.a + randomPosNegValue(20,20);;
				temp[count++] = tempV2.x + 10;
				//System.out.println("" + temp[count - 1]);
				temp[count++] = tempV2.y + 10;
				//System.out.println("" + temp[count - 1]);
			}
		}
		return temp;
	}

	public static void drawAsteroidFragment(Body body2, ImmediateModeRenderer20 r, OrthographicCamera cam) {
		count = 0;
		for(int i = 0; i < ((PolygonShape)body2.getFixtureList().get(0).getShape()).getVertexCount(); i++){
			((PolygonShape)body2.getFixtureList().get(0).getShape()).getVertex(i, tempV2);
			vertices[count++] = tempV2.x;
			vertices[count++] = tempV2.y;
		}	
		r.begin(cam.combined, GL20.GL_TRIANGLES);
		r.color(Color.DARK_GRAY);
		r.vertex((float)(((vertices[0]) * Math.cos(body2.getAngle())) - ((vertices[0+1]) * Math.sin(body2.getAngle()))) + body2.getPosition().x,
				(float)(((vertices[0 + 1]) * Math.cos(body2.getAngle())) + ((vertices[0]) * Math.sin(body2.getAngle()))) + body2.getPosition().y,0);
		r.color(Color.GRAY);
		r.vertex((float)(((vertices[0+2]) * Math.cos(body2.getAngle())) - ((vertices[0+3]) * Math.sin(body2.getAngle()))) + body2.getPosition().x ,
				(float)(((vertices[0+3]) * Math.cos(body2.getAngle())) + ((vertices[0+2]) * Math.sin(body2.getAngle()))) + body2.getPosition().y,0);
		r.color(Color.DARK_GRAY);
		r.vertex((float)(((vertices[0+4]) * Math.cos(body2.getAngle())) - ((vertices[0+5]) * Math.sin(body2.getAngle()))) + body2.getPosition().x,
				(float)(((vertices[0+5]) * Math.cos(body2.getAngle())) + ((vertices[0+4]) * Math.sin(body2.getAngle()))) + body2.getPosition().y,0);
		r.end();
	}
	public static void drawCometFragment(Body body2, ImmediateModeRenderer20 r, OrthographicCamera cam) {
		count = 0;
		for(int i = 0; i < ((PolygonShape)body2.getFixtureList().get(0).getShape()).getVertexCount(); i++){
			((PolygonShape)body2.getFixtureList().get(0).getShape()).getVertex(i, tempV2);
			vertices[count++] = tempV2.x;
			vertices[count++] = tempV2.y;
		}	
		r.begin(cam.combined, GL20.GL_TRIANGLES);
		r.color(Color.ROYAL);
		r.vertex((float)(((vertices[0]) * Math.cos(body2.getAngle())) - ((vertices[0+1]) * Math.sin(body2.getAngle()))) + body2.getPosition().x,
				(float)(((vertices[0 + 1]) * Math.cos(body2.getAngle())) + ((vertices[0]) * Math.sin(body2.getAngle()))) + body2.getPosition().y,0);
		r.color(Color.ROYAL);
		r.vertex((float)(((vertices[0+2]) * Math.cos(body2.getAngle())) - ((vertices[0+3]) * Math.sin(body2.getAngle()))) + body2.getPosition().x ,
				(float)(((vertices[0+3]) * Math.cos(body2.getAngle())) + ((vertices[0+2]) * Math.sin(body2.getAngle()))) + body2.getPosition().y,0);
		r.color(Color.BLUE);
		r.vertex((float)(((vertices[0+4]) * Math.cos(body2.getAngle())) - ((vertices[0+5]) * Math.sin(body2.getAngle()))) + body2.getPosition().x,
				(float)(((vertices[0+5]) * Math.cos(body2.getAngle())) + ((vertices[0+4]) * Math.sin(body2.getAngle()))) + body2.getPosition().y,0);
		r.end();
	}

	public static void drawFragment(Body body2, ImmediateModeRenderer20 r, OrthographicCamera cam, Color color) {
		count = 0;
		for(int i = 0; i < ((PolygonShape)body2.getFixtureList().get(0).getShape()).getVertexCount(); i++){
			((PolygonShape)body2.getFixtureList().get(0).getShape()).getVertex(i, tempV2);
			vertices[count++] = tempV2.x;
			vertices[count++] = tempV2.y;
		}
		r.begin(cam.combined, GL20.GL_TRIANGLES);
		r.color(color);
		r.vertex((float)(((vertices[0]) * Math.cos(body2.getAngle())) - ((vertices[0+1]) * Math.sin(body2.getAngle()))) + body2.getPosition().x,
				(float)(((vertices[0 + 1]) * Math.cos(body2.getAngle())) + ((vertices[0]) * Math.sin(body2.getAngle()))) + body2.getPosition().y,0);
		r.color(Color.BLACK);
		r.vertex((float)(((vertices[0+2]) * Math.cos(body2.getAngle())) - ((vertices[0+3]) * Math.sin(body2.getAngle()))) + body2.getPosition().x ,
				(float)(((vertices[0+3]) * Math.cos(body2.getAngle())) + ((vertices[0+2]) * Math.sin(body2.getAngle()))) + body2.getPosition().y,0);
		r.color(color);
		r.vertex((float)(((vertices[0+4]) * Math.cos(body2.getAngle())) - ((vertices[0+5]) * Math.sin(body2.getAngle()))) + body2.getPosition().x,
				(float)(((vertices[0+5]) * Math.cos(body2.getAngle())) + ((vertices[0+4]) * Math.sin(body2.getAngle()))) + body2.getPosition().y,0);
		r.end();
		
	}

	public static void drawBullet(Body body2, ShapeRenderer r, OrthographicCamera cam, Color color) {
		r.begin(ShapeType.Filled);
		r.setProjectionMatrix(cam.combined);
		r.setColor(color);
		r.circle(body2.getPosition().x, body2.getPosition().y,0.15f);
		r.end();
	}
	public static boolean isWireframe() {
		return wireframe;
	}

	public static void setWireframe(boolean wireframe) {
		WorldUtils.wireframe = wireframe;
	}
	public static int randomPosNegValue(int max , int min) {
	    int randValue = -min + (int) (Math.random() * ((max - (-min)) + 1));
	    return randValue;
	}
}


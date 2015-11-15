package com.gdx.orion.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

public class Box2DUtils {
	public static PolygonShape ps;
	public static CircleShape cs;
	public static Vector2 tempV2;
	public static int count = 0;
	public static float[] temp;

	public Vector2 getShapePosition(Shape shape)
	{
		return null;
	}
	public static float[] setShapePosition(float[] creature,Vector2 vect){
		temp = creature;
		
		for(int i = 0; i<temp.length; i+=2){
			temp[i] = temp[i] + vect.x;
			temp[i+1] = temp[i+1] + vect.y;
		}
		return temp;
	}
}

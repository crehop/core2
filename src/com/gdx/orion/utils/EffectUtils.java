package com.gdx.orion.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.gdx.orion.screens.GameStateManager;

public class EffectUtils {
	static ImmediateModeRenderer20 lineRenderer = new ImmediateModeRenderer20(false, true, 0);
	static float spacingH = 0;
	static float spacingW = 0;
	public static void line(float x1, float y1, float z1,
			float x2, float y2, float z2,
			float r, float g, float b, float a) {
		lineRenderer.color(r, g, b, a);
		lineRenderer.vertex(x1, y1, z1);
		lineRenderer.color(r, g, b, a);
		lineRenderer.vertex(x2, y2, z2);
	}
	public static void grid(float GAME_WORLD_WIDTH, float GAME_WORLD_HEIGHT,Color color,int numberOfLines) {		
		spacingH = GAME_WORLD_HEIGHT/numberOfLines;		
		spacingW = GAME_WORLD_WIDTH/numberOfLines;
		for(int x = 0; x <= GAME_WORLD_WIDTH; x += spacingH) {		
			line(x, 0, 0,		
					x, GAME_WORLD_HEIGHT, 0,		
					color.r, color.g, color.b, color.a);		
		}		
		for(int y = 0; y <= GAME_WORLD_HEIGHT; y += spacingW){		
			line(0, y, 0,		
					GAME_WORLD_WIDTH, y, 0,		
					color.r, color.g, color.b, color.a);		
		}		
	}		
	public static void drawGrid(Camera cam){				
		lineRenderer.begin(cam.combined, GL20.GL_LINES);		
		grid(GameStateManager.play.GAME_WORLD_WIDTH,GameStateManager.play.GAME_WORLD_HEIGHT,Color.DARK_GRAY,25);		
		lineRenderer.end();		
	}
}

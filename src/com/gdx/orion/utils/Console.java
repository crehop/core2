package com.gdx.orion.utils;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.gdx.orion.Main;

public class Console{
	private static boolean consoleOn = true;
	private static String line1 = "LINE 1:";
	private static String line2 = "LINE 2:";
	private static String line3 = "LINE 3:";
	private static String line4 = "LINE 4:";
	private static String line5 = "LINE 5:";
	private static String line6 = "LINE 6:";
	private static String line7 = "LINE 7:";
	private static String line8 = "LINE 8:";
	private static String line9 = "LINE 9:";
	private static String line10 = "LINE 10:";
	private static String line11 = "LINE 11:";
	public static int x = 5;
	public static int y = 5;
	private static FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Exo2-Thin.ttf"));
	private static FreeTypeFontParameter parameter = new FreeTypeFontParameter();
	private static BitmapFont font = generator.generateFont(parameter); // font size 12 pixels


	private static boolean enabled = true;
	private static boolean initiated = false;
	static Random rand = new Random();
	static SpriteBatch batch = new SpriteBatch();
	

	public static void render(OrthographicCamera cam) {
		batch.setProjectionMatrix(cam.combined);
		if(initiated == false){
			parameter.size = 12;
			initiated = true;
		}
		if(enabled){
			x= (int) (-cam.viewportWidth/2) + 5;
			y= (int) (cam.viewportHeight/2 -5);
			batch.begin();
			checkConsole();
			font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			font.draw(batch, line1, x, y); 
			y -= font.getCapHeight() + 3;
			font.draw(batch, line2, x, y); 
			y -= font.getCapHeight() + 3;
			font.draw(batch, line3, x, y); 
			y -= font.getCapHeight() + 3;
			font.draw(batch, line4, x, y); 
			y -= font.getCapHeight() + 3;
			font.draw(batch, line5, x, y); 
			y -= font.getCapHeight() + 3;
			font.draw(batch, line6, x, y); 
			y -= font.getCapHeight() + 3;
			font.draw(batch, line7, x, y); 
			y -= font.getCapHeight() + 3;
			font.draw(batch, line8, x, y); 
			y -= font.getCapHeight() + 3;
			font.draw(batch, line9, x, y); 
			y -= font.getCapHeight() + 3;
			font.draw(batch, line10, x, y); 
			y -= font.getCapHeight() + 3;
			font.draw(batch, line11, x, y); 
			y -= font.getCapHeight() + 3;
			batch.end();
		}
	}
	public static void checkConsole(){
		if(consoleOn){
			if(line1 == null){
				line1 = "null";
			}
			if(line2 == null){
				line2 = "null";
			}
			if(line3 == null){
				line3 = "null";
			}
			if(line4 == null){
				line4 = "null";
			}
			if(line5 == null){
				line5 = "null";
			}
			if(line6 == null){
				line6 = "null";
			}
			if(line7 == null){
				line7 = "null";
			}
			if(line8 == null){
				line8 = "null";
			}
			if(line9 == null){
				line9 = "null";
			}
			if(line10 == null){
				line10 = "null";
			}
			if(line11 == null){
				line11 = "null";
			}
		}
	}
	public static boolean isConsoleOn() {
		return consoleOn;
	}
	public static void setConsoleOn(boolean consoleOn) {
		Console.consoleOn = consoleOn;
	}
	public static String getLine1() {
		return line1;
	}
	public static void setLine1(String line1) {
		Console.line1 = line1;
	}
	public static String getLine2() {
		return line2;
	}
	public static void setLine2(String line2) {
		Console.line2 = line2;
	}
	public static String getLine3() {
		return line3;
	}
	public static void setLine3(String line3) {
		Console.line3 = line3;
	}
	public static String getLine4() {
		return line4;
	}
	public static void setLine4(String line4) {
		Console.line4 = line4;
		line4 = "" + rand.nextInt(20);
	}
	public static String getLine5() {
		return line5;
	}
	public static void setLine5(String line5) {
		Console.line5 = line5;
	}
	public static String getLine6() {
		return line6;
	}
	public static void setLine6(String line6) {
		Console.line6 = line6;
	}
	public static String getLine7() {
		return line7;
	}
	public static void setLine7(String line7) {
		Console.line7 = line7;
	}
	public static String getLine8() {
		return line8;
	}
	public static void setLine8(String line8) {
		Console.line8 = line8;
	}
	public static String getLine9() {
		return line9;
	}
	public static void setLine9(String line9) {
		Console.line9 = line9;
	}
	public static String getLine10() {
		return line10;
	}
	public static void setLine10(String line10) {
		Console.line10 = line10;
	}

	public static void setLine11(String line11) {
		Console.line11 = line11;
	}
}

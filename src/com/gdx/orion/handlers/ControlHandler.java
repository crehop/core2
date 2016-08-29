package com.gdx.orion.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.orion.screens.GameStateManager;
import com.gdx.orion.screens.LevelSelect;
import com.gdx.orion.utils.Console;
import com.gdx.orion.utils.WorldUtils;

public class ControlHandler extends InputAdapter {
	public static final float TURN_RATE = 150f;
	public static final int THRUST_REVERSE_FACTOR = -150000;
	public static final int THRUST_FORWARD_FACTOR = 300000;
	private boolean exitKey = false;
	private boolean forward = false;
	private boolean back = false;
	private boolean strafeLeft = false;
	private boolean strafeRight = false;
	private boolean fired = false;
	private int screen;
	private Vector3 position2;
	private Vector2 position;
	private Vector2 force = new Vector2(0,100);
	public void checkInput(World world, Camera cam) {
		screen = GameStateManager.getLastScreen();
		if(exitKey){
			switch(screen){
				case GameStateManager.LEVELSELECT:
					break;
				case GameStateManager.PLAY:
					break;
				case GameStateManager.LEVELEDIT:
					break;
				case GameStateManager.PAUSE:
					break;
			}
		}
		if(forward){
			switch(GameStateManager.getLastScreen()){
				case GameStateManager.LEVELSELECT:
					break;
				case GameStateManager.PLAY:
					break;
				case GameStateManager.LEVELEDIT:
					break;
				case GameStateManager.PAUSE:
					break;
			}
		}
		if(back){
			switch(GameStateManager.getLastScreen()){
				case GameStateManager.LEVELSELECT:
					break;
				case GameStateManager.PLAY:
					break;
				case GameStateManager.LEVELEDIT:
					break;
				case GameStateManager.PAUSE:
					break;
			}
		}
		if(strafeLeft){
			switch(GameStateManager.getLastScreen()){
				case GameStateManager.LEVELSELECT:
					break;
				case GameStateManager.PLAY:
					break;
				case GameStateManager.LEVELEDIT:
					break;
				case GameStateManager.PAUSE:
					break;
			}
		}	
		if(strafeRight){
			switch(GameStateManager.getLastScreen()){
				case GameStateManager.LEVELSELECT:
					break;
				case GameStateManager.PLAY:
					break;
				case GameStateManager.LEVELEDIT:
					break;
				case GameStateManager.PAUSE:
					break;
			}
		}
		if(fired){
			switch(GameStateManager.getLastScreen()){
				case GameStateManager.LEVELSELECT:
					position2.set(Gdx.input.getX(), Gdx.input.getY(), 0);
					position.set(cam.unproject(position2).x,cam.unproject(position2).y);
					WorldUtils.fireBullet(world, position, 10f, 1, force);
					break;
				case GameStateManager.PLAY:
					break;
				case GameStateManager.LEVELEDIT:
					break;
				case GameStateManager.PAUSE:
					break;
			}
		}
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode){
			case Input.Keys.W:
				forward = true;
				break;
			case Input.Keys.S:
				back = true;
				break;
			case Input.Keys.A:
				strafeLeft = true;
				break;
			case Input.Keys.D:
				strafeRight = true;
				break;
			case Input.Keys.ESCAPE:
				exitKey = true;
				break;
			case Input.Keys.SPACE:
				if(!fired) fired = true;
				break;
		}
		return super.keyDown(keycode);
	}

	@Override
	public boolean keyUp(int keycode) {
		switch(keycode){
			case Input.Keys.W:
				forward = false;
				break;
			case Input.Keys.S:
				back = false;
				break;
			case Input.Keys.A:
				strafeLeft = false;
				break;
			case Input.Keys.D:
				strafeRight = false;
				break;
			case Input.Keys.SPACE:
				fired = false;
				break;
			case Input.Keys.E:
				break;
			case Input.Keys.Q:
				break;
			case Input.Keys.F1:
				if(WorldUtils.isWireframe()){
					WorldUtils.setWireframe(false);
				}else{
					WorldUtils.setWireframe(true);
				}
		}
		return super.keyUp(keycode);
	}
	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return super.keyTyped(character);
	}
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return super.touchDown(screenX, screenY, pointer, button);
	}
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return super.touchUp(screenX, screenY, pointer, button);
	}
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return super.touchDragged(screenX, screenY, pointer);
	}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return super.mouseMoved(screenX, screenY);
	}
	@Override
	public boolean scrolled(int amount) {
		System.out.println("SCROLLED" + amount + " " + GameStateManager.levelSelect.isActive());
		if(GameStateManager.play.isActive()){
			if(!(GameStateManager.play.cam.zoom + amount < 1)){
				GameStateManager.play.cam.zoom += amount;
				Console.setLine8(""+GameStateManager.play.cam.zoom);
			}
			if(GameStateManager.play.cam.zoom < 0){
				GameStateManager.play.cam.zoom = 1;
			}
		}
		if(GameStateManager.levelSelect.isActive()){
			if(!(GameStateManager.levelSelect.cam.zoom + amount < 1)){
				GameStateManager.levelSelect.cam.zoom += amount;
				Console.setLine8(""+GameStateManager.levelSelect.cam.zoom);
			}
			if(GameStateManager.levelSelect.cam.zoom < 0){
				GameStateManager.levelSelect.cam.zoom = 1;
			}
		}
		return super.scrolled(amount);
	}
}

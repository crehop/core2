package com.gdx.orion.handlers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.gdx.orion.screens.GameStateManager;
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
	
	public void checkInput() {
		if(exitKey){
			
		}
		if(forward){
			
		}
		if(back){
			
		}
		if(strafeLeft){
			
		}
		if(strafeRight){
			
		}
		if(fired){
			
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
				GameStateManager.play.getPlayerShip().fireGrapple();
				break;
			case Input.Keys.Q:
				GameStateManager.play.getPlayerShip().shieldToggle();
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
		if(GameStateManager.play.isActive()){
			if(!(GameStateManager.play.cam.zoom + amount < 1)){
				GameStateManager.play.cam.zoom += amount;
				Console.setLine8(""+GameStateManager.play.cam.zoom);
			}
			if(GameStateManager.play.cam.zoom < 0){
				GameStateManager.play.cam.zoom = 1;
			}
		}
		return super.scrolled(amount);
	}
}

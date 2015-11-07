package com.gdx.orion.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.gdx.orion.screens.GameStateManager;

public class PlayController extends InputAdapter implements InputProcessor {
	
	public PlayController(){
	}
	private boolean exitKey = false;
	private boolean forward = false;
	private boolean back = false;
	private boolean strafeLeft = false;
	private boolean strafeRight = false;
	
	public  void checkInput() {
		if(exitKey){
			Gdx.app.exit();
		}
		if(forward){
			if(GameStateManager.play.isActive()){
				GameStateManager.play.cam.translate(0, 5, 0);
			}
		}
		if(back){
			if(GameStateManager.play.isActive()){
				GameStateManager.play.cam.translate(0, -5, 0);
			}
		}
		if(strafeLeft){
			if(GameStateManager.play.isActive()){
				GameStateManager.play.cam.translate(-5, 0, 0);
			}
		}
		if(strafeRight){
			if(GameStateManager.play.isActive()){
				GameStateManager.play.cam.translate(5, 0, 0);
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

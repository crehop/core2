package com.gdx.orion.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.gdx.orion.screens.GameStateManager;

public class PlayController extends InputAdapter implements InputProcessor {

	private boolean exitKey;
	private boolean forward;
	private boolean back;
	private boolean strafeLeft;
	private boolean strafeRight;
	
	public  void checkInput() {
		if(exitKey){
			
		}
		if(forward){
		}
		if(back){
			if(Gdx.input.isCursorCatched()){
			}
		}
		if(strafeLeft){
			if(Gdx.input.isCursorCatched()){
			}
		}
		if(strafeRight){	
			if(Gdx.input.isCursorCatched()){
				
			}
		}
	}
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return super.keyDown(keycode);
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return super.scrolled(amount);
	}
}

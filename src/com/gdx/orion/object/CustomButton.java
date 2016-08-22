package com.gdx.orion.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class CustomButton extends Button {

	private Sprite current;
	private Texture normal;
	private Texture pressed;
	
	public CustomButton(Texture normal, Texture pressed, float x, float y, float width, float height) {
		this.normal = normal;
		this.pressed = pressed;
		this.current = new Sprite(normal);
		this.setPosition(x, y);
		this.setSize(width, height);
		this.current.setPosition(x, y);
		this.current.setSize(width, height);
	}
	
	public void update(SpriteBatch sb) {
		if (this.isChecked()) {
			current.setTexture(pressed);
		} else {
			if (current.getTexture() != normal) current.setTexture(normal);
		}
		current.draw(sb);
	}
	
	@Override
	public void draw(Batch batch, float parcentAlpha) {
		return;
	}
	
}

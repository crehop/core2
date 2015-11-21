package com.gdx.orion.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class Scene2dUtils {
	
	public static TextButtonStyle createTextButtonStyle(Skin skin, String fontName) {
		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.newDrawable("white", Color.DARK_GRAY);
		style.over = skin.newDrawable("white", Color.LIGHT_GRAY);

		style.font = skin.getFont(fontName);
		
		skin.add("default", style);
		return style;
	}
	
}

package com.gdx.orion.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
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
	
	
	public static Skin createDefaultSkin(Texture t)
	{
		Skin skin = new Skin();
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Exo2-Regular.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		BitmapFont font = generator.generateFont(parameter);
		skin.add("default", font);
		skin.add("white", t);
		return skin;
	}
}

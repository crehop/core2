package com.gdx.orion.screens;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class Menu implements Screen {

	private SpriteBatch sb;
    private BitmapFont font;
    
    private Stage stage;
    private Skin skin;
    private TextureAtlas buttonAtlas;
    private TextButton button;
    private TextButtonStyle textBS;
    private Game game;
    
    private final String TITLE = "Asteroids";
	

	public Menu(Game game) {
		super();
		this.game = game;
		//init();
	}

	public void init() {
		
		sb = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		skin = new Skin();
		
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		buttonAtlas = new TextureAtlas(Gdx.files.getFileHandle("reeeeeeee.jpg", FileType.Local));
		skin.addRegions(buttonAtlas);
		textBS = new TextButtonStyle();
		textBS.font = font;
		textBS.up = skin.getDrawable("Start");
		button = new TextButton("StartButton", textBS);
		stage.addActor(button);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		sb.getProjectionMatrix();
		
		stage.draw();
		
		sb.begin();
		font.draw(sb, TITLE, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 + 100);
		
		sb.end();
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	
	
}

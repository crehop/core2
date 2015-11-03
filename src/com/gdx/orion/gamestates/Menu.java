package com.gdx.orion.gamestates;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class Menu extends GameState {

	private SpriteBatch sb;
    private BitmapFont font;
    
    private Stage stage;
    private Skin skin;
    private TextureAtlas buttonAtlas;
    private TextButton button;
    private TextButtonStyle textBS;
    
    private final String TITLE = "Asteroids";
	
	protected Menu(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		
		sb = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		skin = new Skin();
		
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		buttonAtlas = new TextureAtlas(Gdx.files.getFileHandle("/project-gdx-orion-core//assets//reeeeeeee.jpg", FileType.Local));
		skin.addRegions(buttonAtlas);
		textBS = new TextButtonStyle();
		textBS.font = font;
		textBS.up = skin.getDrawable("Start");
		button = new TextButton("StartButton", textBS);
		stage.addActor(button);
	}

	@Override
	public void update(float dt) {
		
		//Input would be handled in here
		
	}

	@Override
	public void draw() {
		
		sb.getProjectionMatrix();
		
		stage.draw();
		
		sb.begin();
		font.draw(sb, TITLE, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 + 100);
		
		sb.end();
		
	}

	@Override
	public void dispose() {
		
	}

	
	
}

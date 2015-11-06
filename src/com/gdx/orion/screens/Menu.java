package com.gdx.orion.screens;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class Menu extends GameState implements Screen {

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
		super(GameStateManager.MENU);
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
	public void render(float delta) {
		if(active){
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);	
			sb.getProjectionMatrix();
			stage.draw();
			sb.begin();
			font.draw(sb, TITLE, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 + 100);
			sb.end();
		}
	}
	
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}
	
	@Override
	public void show() {
		this.setActive(true);
	}
	
	@Override
	public void hide() {
		this.setActive(false);
	}

	@Override
	public void dispose() {
		
	}
	
	@Override
	public void setActive(boolean active) {
		super.active = active;
	}

	@Override
	public boolean isActive() {
		return super.active;
	}
	
	
}

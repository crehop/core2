package com.gdx.orion.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.gdx.orion.utils.Scene2dUtils;

public class Pause extends GameState implements Screen {

	private Game game;
	private OrthographicCamera cam;
	private ScalingViewport viewport;
	private Texture texture = new Texture(Gdx.files.internal("starfield_by_phillipsj2.png"));
	private Stage stage;
	private Skin skin;
	private FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Exo2-Thin.ttf"));
	private FreeTypeFontParameter parameter = new FreeTypeFontParameter();
	private BitmapFont font;
	private TextButton.TextButtonStyle style;
	private TextButton resume;
	private TextButton quit;
	
	private final float GAME_WORLD_WIDTH = 1000;
	private final float GAME_WORLD_HEIGHT = 700;
	
	protected Pause(Game game, int ID) {
		super(ID);
		this.game = game;
		cam = new OrthographicCamera();
		viewport = new ScalingViewport(Scaling.stretch, 100, 70, cam);
		stage = new Stage(viewport);
		skin = new Skin();
		skin.add("white", texture);
		font= generator.generateFont(parameter);
		skin.add("default",font);
		style = Scene2dUtils.createTextButtonStyle(skin, "default");
		resume = new TextButton("RESUME", style);
		resume.setPosition(GAME_WORLD_WIDTH/2, GAME_WORLD_HEIGHT/2 + 200);
		quit = new TextButton("QUIT", style);
		quit.setPosition(GAME_WORLD_WIDTH/2 + 100, GAME_WORLD_HEIGHT/2 + 100);
		stage.addActor(resume);
		stage.addActor(quit);
		
		resume.addListener(new ClickListener(){
            @Override 
            public void clicked(InputEvent event, float x, float y){
            	GameStateManager.setScreen(1);
            }
        });
		
		quit.addListener(new ClickListener(){
            @Override 
            public void clicked(InputEvent event, float x, float y){
            	GameStateManager.setScreen(0);
            }
        });
	}

	public void render(float delta) {
		Gdx.input.setInputProcessor(stage);
		stage.act();
		stage.draw();
	}

	public void resize(int width, int height) {
		
	}
	
	public void dispose() {
		texture.dispose();
		skin.dispose();
		stage.dispose();
	}

	public void pause() {
		
	}

	public void resume() {
		
	}

	public void show() {
		this.setActive(true);
	}
	
	public void hide() {
		this.setActive(false);
	}

	public void setActive(boolean active) {
		super.active = active;
	}

	public boolean isActive() {
		return super.active;
	}

	
	
}

package com.gdx.orion.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
	private Pixmap pixmap;
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
		super(GameStateManager.PAUSE);
		this.game = game;
		cam = new OrthographicCamera();
		viewport = new ScalingViewport(Scaling.stretch, 1000, 700, cam);
		viewport.apply();
		stage = new Stage(viewport);
		skin = new Skin();
		pixmap = new Pixmap(200, 100, Format.RGBA8888);
		pixmap.setColor(Color.GOLD);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));
		font= generator.generateFont(parameter);
		skin.add("default",font);
		style = Scene2dUtils.createTextButtonStyle(skin, "default");
		resume = new TextButton("RESUME", style);
		resume.setPosition(GAME_WORLD_WIDTH/2 - 100, GAME_WORLD_HEIGHT/2 + 200);
		quit = new TextButton("QUIT", style);
		quit.setPosition(GAME_WORLD_WIDTH/2 - 100, GAME_WORLD_HEIGHT/2 - 200);
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
		if (active) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
		cam.update();
		}
	}

	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
		stage.getViewport().apply();
	}
	
	public void dispose() {
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

package com.gdx.orion.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.gdx.orion.utils.Console;
import com.gdx.orion.utils.Scene2dUtils;

public class LevelEdit extends GameState implements Screen {
	public static Game game;
	public static OrthographicCamera cam;
	public static ScalingViewport viewport;  
	final float GAME_WORLD_WIDTH = 1080;
	final float GAME_WORLD_HEIGHT = 720;
	
	private Stage stage;
	private Skin skin;
	private Pixmap pixmap;
	
	private FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Exo2-Thin.ttf"));
	private FreeTypeFontParameter parameter = new FreeTypeFontParameter();
	private BitmapFont font;
	private TextButton.TextButtonStyle style;
	
	private TextButton worldBorder;
	private TextButton asteroid;
	private TextButton gravityWell;
	private TextButton spawnPoint;
	
	private OrthographicCamera consoleCam;
	private ScalingViewport consoleViewport;
	
	protected LevelEdit(Game game) {
		super(GameStateManager.LEVELEDIT);
		cam = new OrthographicCamera();
		viewport = new ScalingViewport(Scaling.stretch, 1080, 720, cam);
		viewport.apply();
		
		stage = new Stage(viewport);
		skin = new Skin();
		
		pixmap = new Pixmap(100, 100, Format.RGBA8888);
		pixmap.setColor(Color.GRAY);
		pixmap.fill();
		pixmap.setColor(Color.GREEN);
		pixmap.drawRectangle(0, 0, 100, 100);
		skin.add("white", new Texture(pixmap));
		
		font = generator.generateFont(parameter);
		skin.add("default", font);
		style = Scene2dUtils.createTextButtonStyle(skin, "default");
		
		worldBorder = new TextButton("BORDER", style);
		worldBorder.setPosition(0, 0);
		asteroid = new TextButton("ASTEROID", style);
		asteroid.setPosition(100, 0);
		gravityWell = new TextButton("GRAVITY", style);
		gravityWell.setPosition(0, 100);
		spawnPoint = new TextButton("SPAWN", style);
		spawnPoint.setPosition(100, 100);
		
		stage.addActor(worldBorder);
		stage.addActor(asteroid);
		stage.addActor(gravityWell);
		stage.addActor(spawnPoint);
		
		consoleCam = new OrthographicCamera();
		consoleViewport = new ScalingViewport(Scaling.stretch, 1280, 720, consoleCam);
		consoleViewport.apply();
	}
	
	@Override
	public void render(float delta) {
		if(active){
		  Gdx.gl.glClearColor(0, 0, 0, 1);
		  Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		  Gdx.input.setInputProcessor(stage);
		  
		  stage.act();
		  stage.draw();
		  cam.update();
		  
		  Console.setLine2("SCREEN:" + Gdx.graphics.getWidth() + "/" + Gdx.graphics.getHeight());
		  Console.setLine3("CAM:"+ cam.viewportWidth + "/" + cam.viewportHeight);
		  Console.setLine4("VIEWPORT:"+ viewport.getScreenWidth() + "/" + viewport.getScreenHeight());
		  Console.setLine5("CONSOLE:"+ Console.x + "/" + Console.y);
		  Console.setLine1("FPS : " + Gdx.graphics.getFramesPerSecond());
		  consoleCam.update();
		  Console.render(cam);	
		}
	}
	
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
		stage.getViewport().apply();
		consoleViewport.update(width, height);
		consoleViewport.apply();
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
		stage.dispose();
		skin.dispose();
		pixmap.dispose();
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

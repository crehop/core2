package com.gdx.orion.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.gdx.orion.utils.Console;
import com.gdx.orion.utils.Scene2dUtils;

public class Menu extends GameState implements Screen {

	@SuppressWarnings("unused")
	private Game game;
    private static OrthographicCamera consoleCam;
    private static ScalingViewport consoleViewport;  
    
    private static OrthographicCamera cam;
    private static ScalingViewport viewport;
    private static Stage stage;
    private static Skin skin;
    private static Pixmap pixmap;
    private static TextButton.TextButtonStyle style;
	private SpriteBatch batch;
	private Sprite titleS = new Sprite(new Texture(Gdx.files.internal("images/TitleScreen.png")));
    
	private static final int BUTTON_X_OFFSET = 0;
	private static final int BUTTON_Y_OFFSET = 0;
	
	protected Menu(Game game, int level) {
		super(GameStateManager.MENU);
		this.game = game;
		batch = new SpriteBatch();
		cam = new OrthographicCamera();
		viewport = new ScalingViewport(Scaling.fit, VIEWPORT_WIDTH, VIEWPORT_HEIGHT, cam);
		viewport.apply();
		stage = new Stage(viewport);
		
		skin = new Skin();

		pixmap = new Pixmap(240, 200, Format.RGBA8888);
		pixmap.setColor(Color.GRAY);
		pixmap.fill();
		pixmap.setColor(Color.BLACK);
		pixmap.drawRectangle(0, 0, 240, 200);

		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 50;
		skin.add("white", new Texture(pixmap));
		skin.add("default",
				new FreeTypeFontGenerator(Gdx.files.internal("fonts/Exo2-Regular.ttf")).generateFont(parameter));
		style = Scene2dUtils.createTextButtonStyle(skin, "default");

		final Button playButton = new TextButton("Play", style);
		playButton.setPosition(0 /5 + BUTTON_X_OFFSET, BUTTON_Y_OFFSET);
		playButton.setWidth(400);
		final Button hangar = new TextButton("Level\nSelect", style);
		hangar.setPosition(1 * cam.viewportWidth / 5 + BUTTON_X_OFFSET, BUTTON_Y_OFFSET);
		hangar.setWidth(400);
		final Button settings = new TextButton("Settings", style);
		settings.setPosition(2 * cam.viewportWidth / 5 + BUTTON_X_OFFSET, BUTTON_Y_OFFSET);
		settings.setWidth(400);
		final Button levelEditor = new TextButton("Editor", style);
		levelEditor.setPosition(3 * cam.viewportWidth / 5 + BUTTON_X_OFFSET, BUTTON_Y_OFFSET);
		levelEditor.setWidth(400);
		final Button exit = new TextButton("Exit", style);
		exit.setPosition(4 * cam.viewportWidth / 5 + BUTTON_X_OFFSET, BUTTON_Y_OFFSET);
		exit.setWidth(400);
		
		stage.addActor(playButton);
		stage.addActor(hangar);
		stage.addActor(settings);
		stage.addActor(levelEditor);
		stage.addActor(exit);
		
		playButton.addListener(new ClickListener(){
            @Override 
            public void clicked(InputEvent event, float x, float y){
            	GameStateManager.setScreen(GameStateManager.LEVELSELECT);
            }
        });
		
		hangar.addListener(new ClickListener(){
            @Override 
            public void clicked(InputEvent event, float x, float y){
            }
        });
		
		levelEditor.addListener(new ClickListener(){
            @Override 
            public void clicked(InputEvent event, float x, float y){
            	GameStateManager.setScreen(GameStateManager.LEVELEDIT);
            }
        });
		
		exit.addListener(new ClickListener(){
            @Override 
            public void clicked(InputEvent event, float x, float y){
            	Gdx.app.exit();
            }
        });
		
		consoleCam = new OrthographicCamera();
		consoleViewport = new ScalingViewport(Scaling.stretch, 1280, 720, consoleCam);
		consoleViewport.apply();
		
	}

	@Override
	public void render(float delta) {
		if(active){
			Gdx.gl.glClearColor(0, 0, 0, 0);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			batch.begin();
			batch.setProjectionMatrix(stage.getViewport().getCamera().combined);
			batch.draw(titleS, 0, 200, cam.viewportWidth, cam.viewportHeight - 200);
			batch.end();
			stage.act();
			stage.draw();
			
			Console.setLine1("Welcome to the main menu!");
			Console.setLine2(Gdx.input.getX() + ", " + Gdx.input.getY());
			Console.render(consoleCam);
			consoleCam.update();
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
		Gdx.input.setInputProcessor(stage);
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

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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
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
	private Sprite text = new Sprite(new Texture(Gdx.files.internal("images/AsteroidsText.png")));
    
	protected Menu(Game game, int level) {
		super(GameStateManager.MENU);
		this.game = game;
		batch = new SpriteBatch();
		cam = new OrthographicCamera();
		viewport = new ScalingViewport(Scaling.fit, VIEWPORT_WIDTH, VIEWPORT_HEIGHT, cam);
		viewport.apply();
		stage = new Stage(viewport);
		
		skin = new Skin();

		pixmap = new Pixmap(120, 100, Format.RGBA8888);
		pixmap.setColor(Color.GRAY);
		pixmap.fill();
		pixmap.setColor(Color.BLACK);
		pixmap.drawRectangle(0, 0, 120, 100);

		skin.add("white", new Texture(pixmap));
		skin.add("default",
				new FreeTypeFontGenerator(Gdx.files.internal("fonts/Exo2-Thin.ttf")).generateFont(new FreeTypeFontParameter()));

		style = Scene2dUtils.createTextButtonStyle(skin, "default");

		final Button playButton = new TextButton("PLAY", style);
		playButton.setPosition(0, 0);
		final Button hangar = new ImageButton(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("buttons/button_hangar.png")))));
		hangar.setPosition(1 * cam.viewportWidth / 5, -40);
		final Button settings = new TextButton("SETTINGS", style);
		settings.setPosition(2 * cam.viewportWidth / 5, 0);
		final Button levelEditor = new TextButton("EDITOR", style);
		levelEditor.setPosition(3 * cam.viewportWidth / 5, 0);
		final Button exit = new TextButton("EXIT", style);
		exit.setPosition(4 * cam.viewportWidth / 5, 0);
		
		stage.addActor(playButton);
		stage.addActor(hangar);
		stage.addActor(settings);
		stage.addActor(levelEditor);
		stage.addActor(exit);
		
		playButton.addListener(new ClickListener(){
            @Override 
            public void clicked(InputEvent event, float x, float y){
            	GameStateManager.setScreen(GameStateManager.PLAY);
            }
        });
		
		hangar.addListener(new ClickListener(){
            @Override 
            public void clicked(InputEvent event, float x, float y){
            	GameStateManager.setScreen(GameStateManager.HANGAR);
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
		consoleViewport = new ScalingViewport(Scaling.fit, 1280, 720, consoleCam);
		consoleViewport.apply();
		
	}

	@Override
	public void render(float delta) {
		if(active){
			Gdx.gl.glClearColor(0, 0, 0, 0);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			batch.begin();
			batch.setProjectionMatrix(stage.getViewport().getCamera().combined);
			batch.draw(titleS, 0, 0, cam.viewportWidth, cam.viewportHeight);
			batch.draw(text, 20, 120, 300, 200);
			batch.end();
			Gdx.input.setInputProcessor(stage);
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

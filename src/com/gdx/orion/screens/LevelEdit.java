package com.gdx.orion.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.gdx.orion.utils.Console;
import com.gdx.orion.utils.Scene2dUtils;

public class LevelEdit extends GameState implements Screen {
	public static Game game;
	public static OrthographicCamera cam;
	public static ScalingViewport viewport;  
	
	private final SpriteBatch batch;
	private final Sprite vacantSquare;
	private final Sprite occupiedSquare;
	
	private final int GRID_MAX_X = 100;
	private final int GRID_MAX_Y = 100;
	private final int GRID_SQUARE_SIZE = 10;
	
	private Stage stage;
	private Skin skin;
	private Pixmap pixmap;
	
	private FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Exo2-Thin.ttf"));
	private FreeTypeFontParameter parameter = new FreeTypeFontParameter();
	private BitmapFont font;
	private TextButton.TextButtonStyle style;
	private TextButton.TextButtonStyle editStyle;
	private TextButton.TextButtonStyle wStyle;
	
	private TextButton worldBorder;
	private TextButton asteroid;
	private TextButton gravityWell;
	private TextButton spawnPoint;
	private TextButton back;
	
	private OrthographicCamera consoleCam;
	private ScalingViewport consoleViewport;
	
	protected LevelEdit(Game game) {
		super(GameStateManager.LEVELEDIT);
		cam = new OrthographicCamera();
		viewport = new ScalingViewport(Scaling.stretch, 1000, 1000, cam);
		viewport.apply();
		
		stage = new Stage(viewport);
		skin = new Skin();
		
		pixmap = new Pixmap(100, 100, Format.RGBA8888);
		pixmap.setColor(Color.GRAY);
		pixmap.fill();
		pixmap.setColor(Color.GREEN);
		pixmap.drawRectangle(0, 0, 100, 100);
		skin.add("white", new Texture(pixmap));
		
		Pixmap pixmap2 = new Pixmap(1080, 720, Format.RGB888);
		pixmap2.setColor(Color.YELLOW);
		pixmap2.drawRectangle(0, 0, 1080, 720);
		skin.add("world", new Texture(pixmap2));
		
		font = generator.generateFont(parameter);
		skin.add("default", font);
		
		style = Scene2dUtils.createTextButtonStyle(skin, "default");
		editStyle = new TextButtonStyle();
		editStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
		editStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		editStyle.checked = skin.newDrawable("white", Color.DARK_GRAY);
		editStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
		editStyle.font = skin.getFont("default");
		
		wStyle = new TextButtonStyle();
		wStyle.up = skin.newDrawable("world", Color.DARK_GRAY);
		wStyle.down = skin.newDrawable("world", Color.DARK_GRAY);
		wStyle.checked = skin.newDrawable("world", Color.DARK_GRAY);
		wStyle.over = skin.newDrawable("world", Color.LIGHT_GRAY);
		wStyle.font = skin.getFont("default");
		
		skin.add("editStyle", editStyle);
		skin.add("default", style);
		skin.add("wstyle", wStyle);
		
		worldBorder = new TextButton("BORDER", editStyle);
		worldBorder.setPosition(cam.viewportWidth - 100, cam.viewportHeight - 100);
		asteroid = new TextButton("ASTEROID", editStyle);
		asteroid.setPosition(cam.viewportWidth - 100, cam.viewportHeight - 200);
		gravityWell = new TextButton("GRAVITY", editStyle);
		gravityWell.setPosition(cam.viewportWidth - 100, cam.viewportHeight - 300);
		spawnPoint = new TextButton("SPAWN", editStyle);
		spawnPoint.setPosition(cam.viewportWidth - 100, cam.viewportHeight - 400);
		back = new TextButton("BACK", style);
		back.setPosition(cam.viewportWidth - 100, cam.viewportHeight - cam.viewportHeight);
		stage.addActor(worldBorder);
		stage.addActor(asteroid);
		stage.addActor(gravityWell);
		stage.addActor(spawnPoint);
		stage.addActor(back);
		
		worldBorder.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				addScreen(new LevelSizePrompt());
			}
		});
		
		back.addListener(new ClickListener(){
            @Override 
            public void clicked(InputEvent event, float x, float y){
            	GameStateManager.setScreen(0);
            }
        });
		
		batch = new SpriteBatch();
		
		pixmap = new Pixmap(10, 10, Format.RGBA8888);
		pixmap.setColor(Color.BLACK);
		pixmap.fill();
		pixmap.setColor(Color.GREEN);
		pixmap.drawRectangle(0, 0, 10, 10);
		
		vacantSquare = new Sprite(new Texture(pixmap));
		occupiedSquare = new Sprite(new Texture(Gdx.files.internal("images/occupiedGrid.png")));
		occupiedSquare.setBounds(0, 0, GRID_SQUARE_SIZE, GRID_SQUARE_SIZE);
		
		consoleCam = new OrthographicCamera();
		consoleViewport = new ScalingViewport(Scaling.stretch, 1280, 720, consoleCam);
		consoleViewport.apply();
	}
	
	@Override
	public void render(float delta) {
		if(active){
		  Gdx.gl.glClearColor(0, 0, 0, 1);
		  Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		  
		  renderGrid();
		  
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

	public void renderGrid() {
		batch.begin();
		for (int  x = 0; x < GRID_MAX_X; x++) {
			for (int y = 0; y < GRID_MAX_Y; y++) {
				vacantSquare.setPosition(x * GRID_SQUARE_SIZE, y * GRID_SQUARE_SIZE);
				vacantSquare.draw(batch);
			}
		}
		batch.end();
	}
	
	private class LevelSizePrompt implements Screen {

		private final Stage stage;
		
		private Pixmap prompt;
		private Sprite sprite;
		
		private final int PROMPT_WIDTH = 500;
		private final int PROMPT_HEIGHT = 500;
		
		private InputProcessor previousInputProcessor = Gdx.input.getInputProcessor();
		
		public LevelSizePrompt() {
			prompt = new Pixmap(PROMPT_WIDTH, PROMPT_HEIGHT, Format.RGBA8888);
			prompt.setColor(Color.RED);
			prompt.fill();
			prompt.setColor(Color.BLACK);
			prompt.drawRectangle(0, 0, PROMPT_WIDTH, PROMPT_HEIGHT);
			
			sprite = new Sprite(new Texture(prompt));

			this.stage = new Stage(viewport);
			Gdx.input.setInputProcessor(this.stage);
			
			this.stage.clear();
			this.stage.addListener(new InputListener() {
				public boolean keyUp(InputEvent event, int keyCode) {
					switch (keyCode) {
					case Input.Keys.BACK:
						cancel();
						break;
					}
					
					return super .keyUp(event, keyCode);
				}
			});
			
		}
		
		@Override
		public void show() {
			Gdx.input.setInputProcessor(this.stage);
			active = true;
		}

		@Override
		public void render(float delta) {
			if (active) {
				batch.begin();
				this.sprite.draw(batch);
				batch.end();
				
				stage.act();
				stage.draw();
				cam.update();
			}
		}

		public void cancel() {
			LevelEdit.this.removeScreen(LevelSizePrompt.this);
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
		public void hide() {
			active = false;
		}

		@Override
		public void dispose() {
			Gdx.input.setInputProcessor(previousInputProcessor);
		}
		
	}
	
}

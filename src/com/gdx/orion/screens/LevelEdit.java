package com.gdx.orion.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.TextInputListener;
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
	
	private int GRID_MAX_X = 100;
	private int GRID_MAX_Y = 100;
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
		pixmap.drawLine(0, 0, VIEWPORT_WIDTH, 0);
		pixmap.drawLine(0, 0, 0, VIEWPORT_HEIGHT);
		
		vacantSquare = new Sprite(new Texture(pixmap));
		
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
		  
		  super.render(delta);
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
		if (active) {
			batch.begin();
			for (int  x = 0; x < GRID_MAX_X; x++) {
				for (int y = 0; y < GRID_MAX_Y; y++) {
					vacantSquare.setPosition(x * GRID_SQUARE_SIZE, y * GRID_SQUARE_SIZE);
					vacantSquare.draw(batch);
				}
			}
			batch.end();
		}
	}
	
	private class LevelSizePrompt implements Screen {

		private boolean active = true;
		
		private boolean activePop = false;
		
		private final Stage stage;
		private SpriteBatch batch;
		
		private Pixmap prompt;
		private Sprite sprite;
		
		private final int PROMPT_WIDTH = (int) (cam.viewportWidth/2);
		private final int PROMPT_HEIGHT = (int) (cam.viewportHeight/2);
		
		private final BitmapFont selectionText = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Exo2-Regular.ttf"))
	    .generateFont(new FreeTypeFontParameter() {{
	    	size = 42;
	    	color = Color.RED;
	    	shadowOffsetX = 3;
	    	shadowOffsetY = 3;
		}});
		
		private final BitmapFont editorText = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Exo2-Regular.ttf"))
	    .generateFont(new FreeTypeFontParameter() {{
	    	size = 38;
	    	color = Color.WHITE;
	    	shadowOffsetX = 3;
	    	shadowOffsetY = 3;
		}});
		
		private TextButton x;
		private TextButton y;
		
		private TextButton exit;
		
		private InputProcessor previousInputProcessor = Gdx.input.getInputProcessor();
		
		public LevelSizePrompt() {
			this.stage = new Stage(viewport);
			Gdx.input.setInputProcessor(this.stage);
			
			this.batch = new SpriteBatch();
			
			this.prompt = new Pixmap(PROMPT_WIDTH, PROMPT_HEIGHT, Format.RGBA8888);
			this.prompt.setColor(Color.BLACK);
			this.prompt.fill();
			this.prompt.setColor(Color.GREEN);
			this.prompt.drawRectangle(0, 0, PROMPT_WIDTH, PROMPT_HEIGHT);
			
			this.sprite = new Sprite(new Texture(prompt));
			
			this.stage.clear();
			this.stage.addListener(new InputListener() {
				public boolean keyUp(InputEvent event, int keyCode) {
					switch (keyCode) {
					case Input.Keys.BACK: cancel(); break;
					case Input.Keys.ESCAPE: cancel(); break;
					}
					
					return super .keyUp(event, keyCode);
				}
			});
			
			TextButtonStyle select = new TextButtonStyle();
			select.font = selectionText;
			select.up = skin.newDrawable("white", Color.BLACK);
			x = new TextButton(LevelEdit.this.GRID_MAX_X + "", select);
			y = new TextButton(LevelEdit.this.GRID_MAX_Y + "", select);
			exit = new TextButton("X", select);
			x.setBounds(PROMPT_WIDTH/3, PROMPT_HEIGHT-225, 50, 50);
			y.setBounds(PROMPT_WIDTH/3, PROMPT_HEIGHT-400, 50, 50);
			exit.setBounds(1, PROMPT_HEIGHT-101, 50, 50);
			
			x.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (!activePop)
					Gdx.input.getTextInput(new NumberPrompt(true), "Width Value", "", null);
				}
			});
			
			y.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (!activePop)
					Gdx.input.getTextInput(new NumberPrompt(false), "Height Value", "", null);
				}
			});
			
			exit.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					LevelSizePrompt.this.cancel();
				}
			});
			
			stage.addActor(x);
			stage.addActor(y);
			stage.addActor(exit);
			
			this.sprite.setSize(PROMPT_WIDTH, PROMPT_HEIGHT);
			this.sprite.setPosition(0, 0);
		}
		
		@Override
		public void show() {
			Gdx.input.setInputProcessor(this.stage);
			this.active = true;
		}

		@Override
		public void render(float delta) {
			if (this.active) {
				this.batch.begin();
				this.sprite.draw(batch);
				this.editorText.draw(batch, "World Dimensions", PROMPT_WIDTH/5, PROMPT_HEIGHT - 50);
				this.editorText.draw(batch, "Width", PROMPT_WIDTH/5, PROMPT_HEIGHT - 175);
				this.editorText.draw(batch, "Height", PROMPT_WIDTH/5, PROMPT_HEIGHT - 350);
				this.batch.end();
				
				this.stage.act();
				this.stage.draw();
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
		
		
		private class NumberPrompt implements TextInputListener {
			
			private boolean x;
			
			public NumberPrompt(boolean x) {
				this.x = x;
				LevelSizePrompt.this.activePop = true;
			}
			
			public void input(String text) {
				int i = -1;
				try {
				if ((i = Integer.parseInt(text)) != -1) {
					if (x) {
						GRID_MAX_X = i;
						LevelSizePrompt.this.x.setText(i + "");
					} else {
						GRID_MAX_Y = i;
						LevelSizePrompt.this.y.setText(i + "");
					}
				}
				LevelSizePrompt.this.activePop = false;
				return;
				} catch (NumberFormatException ez) {
					LevelSizePrompt.this.activePop = false;
				}
			}

			public void canceled() {
				LevelSizePrompt.this.activePop = false;
			}
			
		}
		
	}
	
}

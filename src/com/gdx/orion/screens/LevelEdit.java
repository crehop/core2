package com.gdx.orion.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.InputMultiplexer;
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
import com.badlogic.gdx.math.Vector3;
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

public class LevelEdit extends GameState implements Screen, InputProcessor {
	public static Game game;
	public static OrthographicCamera cam;
	public static ScalingViewport viewport;  
	public static OrthographicCamera gridCam;
	
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
	
	private InputMultiplexer im;
	
	private OrthographicCamera consoleCam;
	private ScalingViewport consoleViewport;
	
	public static Object[][] data;
	
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
		editStyle.up = skin.newDrawable("white", Color.BLACK);
		editStyle.down = skin.newDrawable("white", Color.BLACK);
		editStyle.checked = skin.newDrawable("white", Color.RED);
		editStyle.over = skin.newDrawable("white", Color.GRAY);
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
		back.setPosition(cam.viewportWidth - 100 + viewport.getScreenX(), cam.viewportHeight - cam.viewportHeight + viewport.getScreenY());
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
		
		gridCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		gridCam.position.set(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
		
		batch = new SpriteBatch();
		batch.setProjectionMatrix(gridCam.combined);
		
		pixmap = new Pixmap(10, 10, Format.RGBA8888);
		pixmap.setColor(Color.BLACK);
		pixmap.fill();
		pixmap.setColor(Color.GREEN);
		pixmap.drawLine(0, 0, VIEWPORT_WIDTH, 0);
		pixmap.drawLine(0, 0, 0, VIEWPORT_HEIGHT);
		
		vacantSquare = new Sprite(new Texture(pixmap));
		
		im = new InputMultiplexer();
		im.addProcessor(stage);
		im.addProcessor(this);
		
		data = new Object[GRID_MAX_X][GRID_MAX_Y];
		for (int x = 0; x < GRID_MAX_X; x++) {
			for (int y = 0; y < GRID_MAX_Y; y++) {
				data[x][y] = null;
			}
		}
		
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
		  gridCam.update();
		  cam.update();
		  
		  Console.setLine2("SCREEN:" + Gdx.graphics.getWidth() + "/" + Gdx.graphics.getHeight());
		  Console.setLine3("CAM:"+ cam.viewportWidth + "/" + cam.viewportHeight);
		  Console.setLine4("VIEWPORT:"+ viewport.getScreenWidth() + "/" + viewport.getScreenHeight());
		  Console.setLine5("CONSOLE:" + (((Gdx.input.getX() - Gdx.graphics.getWidth()/2) * gridCam.zoom)) + "/" 
		  + (((Gdx.input.getY() - Gdx.graphics.getHeight() /2) * gridCam.zoom)));
		  Console.setLine1("FPS : " + Gdx.graphics.getFramesPerSecond());
		  Console.setLine6("GRIDCAM: " + gridCam.position.x + " / " + gridCam.position.y);
		  consoleCam.update();
		  Console.render(consoleCam);
		  
		  super.render(delta);
		}
	}
	
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
		stage.getViewport().apply();
		viewport.update(width, height, true);
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
		Gdx.input.setInputProcessor(im);
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
			batch.setProjectionMatrix(gridCam.combined);
			for (int  x = 0; x < GRID_MAX_X; x++) {
				for (int y = 0; y < GRID_MAX_Y; y++) {
					batch.draw(vacantSquare, x * GRID_SQUARE_SIZE, y * GRID_SQUARE_SIZE);
				}
			}
			batch.end();
		}
	}
	
	public boolean isOnGrid(int x, int y) {
		if (x + gridCam.position.x/2 > 0 && y + gridCam.viewportHeight + gridCam.position.x/2 > 0 && x < GRID_MAX_X * GRID_SQUARE_SIZE && y < GRID_MAX_Y * GRID_SQUARE_SIZE) {
			return true;
		}
		return false;
	}
	
	public void resetButtons() {
		worldBorder.setChecked(false);
		asteroid.setChecked(false);
		gravityWell.setChecked(false);
		spawnPoint.setChecked(false);
		back.setChecked(false);
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
			LevelEdit.this.resetButtons();
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
					data = new Object[GRID_MAX_X][GRID_MAX_Y];
					for (int x = 0; x < GRID_MAX_X; x++) {
						for (int y = 0; y < GRID_MAX_Y; y++) {
							if (data[x][y] != null) {
								if (!(data[x][y] instanceof Object)) {
									data[x][y] = null;
								}
							}
						}
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

	/**
	 * Input processor stuff for the editor only
	 */
	private Vector3 tp = new Vector3();
	private boolean dragging;
	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (asteroid.isChecked()) {

			return true;
		}
        if (button != Input.Buttons.LEFT || pointer > 0) return false;
        dragging = true;
        return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.LEFT || pointer > 0) return false;
        dragging = false;
        return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
	    if (!dragging) return false;
	      gridCam.translate(-Gdx.input.getDeltaX(), Gdx.input.getDeltaY());
	      return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		if(!(LevelEdit.gridCam.zoom + amount < 1)){
			LevelEdit.gridCam.zoom += amount;
			Console.setLine8(""+LevelEdit.cam.zoom);
		}
		if(LevelEdit.gridCam.zoom < 0){
			LevelEdit.gridCam.zoom = 1;
		}
		return false;
	}

	public Vector3 getTp() {
		return tp;
	}

	public void setTp(Vector3 tp) {
		this.tp = tp;
	}
	
}

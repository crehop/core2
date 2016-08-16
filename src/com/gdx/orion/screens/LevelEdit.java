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
import com.badlogic.gdx.math.Vector2;
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
import com.gdx.orion.data.GridData;
import com.gdx.orion.data.GridData.Type;
import com.gdx.orion.utils.Console;
import com.gdx.orion.utils.Scene2dUtils;

public class LevelEdit extends GameState implements Screen, InputProcessor {
	public static Game game;
	public static OrthographicCamera cam;
	public static ScalingViewport viewport;  
	public static OrthographicCamera gridCam;
	
	private final SpriteBatch batch;
	private final Sprite vacantSquare;
	private final Sprite occupiedSquare;
	
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
	
	private Vector3 clickPos = new Vector3(0,0,0);
	
	private OrthographicCamera consoleCam;
	private ScalingViewport consoleViewport;
	
	public static GridData[][] data;
	
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
		gridCam.position.set(0, 0, 0);
		
		batch = new SpriteBatch();
		batch.setProjectionMatrix(gridCam.combined);
		
		pixmap = new Pixmap(10, 10, Format.RGBA8888);
		pixmap.setColor(Color.BLACK);
		pixmap.fill();
		pixmap.setColor(Color.GREEN);
		pixmap.drawRectangle(0, 0, 10, 10);
		
		vacantSquare = new Sprite(new Texture(pixmap));
		
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		pixmap.setColor(Color.RED);
		pixmap.drawRectangle(0, 0, 10, 10);
		
		occupiedSquare = new Sprite(new Texture(pixmap));
		
		im = new InputMultiplexer();
		im.addProcessor(stage);
		im.addProcessor(this);
		
		data = new GridData[GRID_MAX_X][GRID_MAX_Y];
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

		  Console.setLine1("FPS : " + Gdx.graphics.getFramesPerSecond());
		  Console.setLine2("SCREEN:" + Gdx.graphics.getWidth() + "/" + Gdx.graphics.getHeight());
		  Console.setLine3("CAM:"+ cam.viewportWidth + "/" + cam.viewportHeight);
		  Console.setLine4("VIEWPORT:"+ viewport.getScreenWidth() + "/" + viewport.getScreenHeight());
		  clickPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		  gridCam.unproject(clickPos);
		  Console.setLine5("CONSOLE:" + clickPos.x + "/" + clickPos.y);
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
					if (data[x][y] == null) {
						batch.draw(vacantSquare, x * GRID_SQUARE_SIZE, y * GRID_SQUARE_SIZE);
					} else {
						batch.draw(occupiedSquare, x * GRID_SQUARE_SIZE, y * GRID_SQUARE_SIZE);
					}
				}
			}
			batch.end();
		}
	}
	
	public boolean isOnGrid(int x, int y) {
		if (x > 0 && y > 0 && x < GRID_MAX_X * GRID_SQUARE_SIZE && y < GRID_MAX_Y * GRID_SQUARE_SIZE) {
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
			clickPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			gridCam.unproject(clickPos);
			if (isOnGrid((int)clickPos.x, (int)clickPos.y)) {
				Object o = data[(int)clickPos.x/GRID_SQUARE_SIZE][(int)clickPos.y/GRID_SQUARE_SIZE];
				if (o == null) {
					GridData gd = new GridData(Type.ASTEROID);
					addScreen(new AsteroidEditPrompt(gd, (int)clickPos.x/GRID_SQUARE_SIZE, (int)clickPos.y/GRID_SQUARE_SIZE));
					return true;
				} else {
					data[(int)clickPos.x/GRID_SQUARE_SIZE][(int)clickPos.y/GRID_SQUARE_SIZE] = null;
				}
			}
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
	
	
	
	private class AsteroidEditPrompt implements Screen {
		private boolean active = true;
		private boolean activePop = false;
		
		private final int P_H = (int) cam.viewportHeight/2;
		private final int P_W = (int) cam.viewportWidth; 
		
		private final Stage stage;
		
		private SpriteBatch batch;
		
		private Pixmap pixmap;
		private Sprite promptBackground;
		
		private TextButton velocity1, velocity2, density, size;
		
		private final BitmapFont editorText = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Exo2-Regular.ttf"))
	    .generateFont(new FreeTypeFontParameter() {{
	    	size = 38;
	    	color = Color.WHITE;
	    	shadowOffsetX = 3;
	    	shadowOffsetY = 3;
		}});
		
		private final BitmapFont selectionText = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Exo2-Regular.ttf"))
	    .generateFont(new FreeTypeFontParameter() {{
	    	size = 42;
	    	color = Color.RED;
	    	shadowOffsetX = 3;
	    	shadowOffsetY = 3;
		}});
		
		private InputProcessor previousInputProcessor = Gdx.input.getInputProcessor();
		
		private GridData gd;
		private int gridX, gridY;
		
		public AsteroidEditPrompt(GridData gd, int gridX, int gridY) {
			this.stage = new Stage(viewport);
			Gdx.input.setInputProcessor(this.stage);
			
			this.gd = gd;
			this.gridX = gridX;
			this.gridY = gridY;
			
			this.batch = new SpriteBatch();
			
			this.pixmap = new Pixmap(P_W, P_H, Format.RGBA8888);
			this.pixmap.setColor(Color.BLACK);
			this.pixmap.fill();
			this.pixmap.setColor(Color.GREEN);
			this.pixmap.drawRectangle(0, 0, P_W, P_H);
			
			this.promptBackground = new Sprite(new Texture(pixmap));
			
			this.stage.clear();
			this.stage.addListener(new InputListener() {
				public boolean keyUp(InputEvent event, int keyCode) {
					switch (keyCode) {
					case Input.Keys.BACK: cancelAndPlace(); break;
					case Input.Keys.ESCAPE: cancelAndPlace(); break;
					}
					
					return super .keyUp(event, keyCode);
				}
			});
			
			TextButtonStyle select = new TextButtonStyle();
			select.font = selectionText;
			select.up = skin.newDrawable("white", Color.BLACK);
			
			this.velocity1 = new TextButton(0 + "", select);
			this.velocity2 = new TextButton(0 + "", select);
			this.density = new TextButton(1 + "", select);
			this.size = new TextButton(1 + "", select);
			
			this.velocity1.setBounds(0, 0, 100, 100);
			this.velocity2.setBounds(0, 0, 100, 100);
			this.density.setBounds(0, 0, 100, 100);
			this.size.setBounds(0, 0, 100, 100);
			
			this.velocity1.setPosition(P_W/4, P_H - 275);
			this.velocity2.setPosition(P_W/3, P_H - 275);
			this.density.setPosition(P_W/3, P_H - 375);
			this.size.setPosition(P_W/3, P_H - 475);
			
			this.velocity1.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (!activePop)
					Gdx.input.getTextInput(new AsteroidNumberPrompt(velocity1), "X Velocity", "", null);
				}
			});
			
			this.velocity2.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (!activePop)
					Gdx.input.getTextInput(new AsteroidNumberPrompt(velocity2), "Y Velocity", "", null);
				}
			});
			
			this.density.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (!activePop)
					Gdx.input.getTextInput(new AsteroidNumberPrompt(density), "Density", "", null);
				}
			});
			
			this.size.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (!activePop)
					Gdx.input.getTextInput(new AsteroidNumberPrompt(size), "Size", "", null);
				}
			});
			
			this.stage.addActor(velocity1);
			this.stage.addActor(velocity2);
			this.stage.addActor(density);
			this.stage.addActor(size);
		}
		public void show() {
			Gdx.input.setInputProcessor(this.stage);
			this.active = true;
		}
		public void cancelAndPlace() {
			gd.setPosition(new Vector2(gridX,gridY));
			gd.setForce(new Vector2(Float.parseFloat(velocity1.getText().toString()), Float.parseFloat(velocity2.getText().toString())));
			gd.setDensity(Float.parseFloat(density.getText().toString()));
			gd.setSize(Integer.parseInt((size.getText().toString())));
			LevelEdit.data[gridX][gridY] = gd;
			LevelEdit.this.removeScreen(AsteroidEditPrompt.this);
		}
		public void render(float delta) {
			if (active) {
				this.batch.begin();
				this.batch.draw(promptBackground, 0, 0);
				this.editorText.draw(batch, "Asteroid Creation", P_W/3, P_H - 100);
				this.editorText.draw(batch, "Velocity Vector", P_W/5, P_H - 200);
				this.editorText.draw(batch, "Density", P_W/5, P_H - 300);
				this.editorText.draw(batch, "Size", P_W/5, P_H - 400);
				this.batch.end();
				
				this.stage.act();
				this.stage.draw();
			}
		}
		public void resize(int width, int height) {
			
		}
		public void pause() {
			
		}
		public void resume() {
			
		}
		public void hide() {
			active = false;
		}
		public void dispose() {
			Gdx.input.setInputProcessor(previousInputProcessor);
		}
		
		private class AsteroidNumberPrompt implements TextInputListener {
			
			private TextButton b;
			
			public AsteroidNumberPrompt(TextButton b) {
				this.b = b;
				AsteroidEditPrompt.this.activePop = true;
			}
			
			public void input(String text) {
				int i = -1;
				try {
				if ((i = Integer.parseInt(text)) != -1) {
					b.setText(i + "");
				}
				AsteroidEditPrompt.this.activePop = false;
				return;
				} catch (NumberFormatException ez) {
					AsteroidEditPrompt.this.activePop = false;
				}
			}

			public void canceled() {
				AsteroidEditPrompt.this.activePop = false;
			}
			
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
					data = new GridData[GRID_MAX_X][GRID_MAX_Y];
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

}

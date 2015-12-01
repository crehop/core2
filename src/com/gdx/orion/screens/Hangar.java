package com.gdx.orion.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * This is the screen for the Hangar.  Player can repair, upgrade, and reconfigure
 * the ship in the hangar.
 * 
 * @author jau
 */
public class Hangar extends GameState implements Screen {
	private final Game game;
	private final OrthographicCamera camera;
	private final Viewport viewport;
	private final Sprite spriteShip;
	private final Sprite spriteBackground;
	private final SpriteBatch batch;
	private final Stage stage;
	private final ImageButton btnEnginePortLeft;
	private final ImageButton btnEnginePortRight;
	private static final float SHIP_SCALING_FACTOR = 0.85f;
	
	/**
	 * Sole constructor
	 * 
	 * @param game
	 * @param ID
	 */
	public Hangar(final Game game, final int ID) {
		super(GameStateManager.HANGAR);
		
		this.game = game;
		camera = new OrthographicCamera();
		viewport = new ScalingViewport(Scaling.fit, VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera);
		viewport.apply();
		batch = new SpriteBatch();
		stage = new Stage(viewport);
		spriteBackground = new Sprite(new Texture(Gdx.files.internal("images/Hangar_background.png")));
		spriteShip = new Sprite(new Texture(Gdx.files.internal("images/PlayerShip-base-1920.png")));
		
		btnEnginePortLeft = new ImageButton(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("images/EngineDockPort-left.png")))));
		btnEnginePortLeft.setPosition(550, shipBaseYPosition());
		btnEnginePortLeft.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				addScreen(new EnginePopupScreen());
			}
		});
		
		btnEnginePortRight = new ImageButton(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("images/EngineDockPort-right.png")))));
		btnEnginePortRight.setPosition(1240, shipBaseYPosition());
		btnEnginePortRight.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				addScreen(new EnginePopupScreen());
			}
		});
		
		stage.clear();
		stage.addActor(btnEnginePortLeft);
		stage.addActor(btnEnginePortRight);
		stage.addListener(new InputListener() {
			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				switch (keycode) {
					case Input.Keys.ESCAPE:
						GameStateManager.setScreen(GameStateManager.PAUSE);
						break;
					}
			
				return super.keyUp(event, keycode);
			}
		});
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		setActive(true);
	}

	@Override
	public void render(float delta) {
		if (active) {
			Gdx.gl.glClearColor(0, 0, 0, 0);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			batch.begin();
			batch.setProjectionMatrix(camera.combined);
			// Hangar background
			batch.draw(
					spriteBackground.getTexture(),
					0,
					0,
					camera.viewportWidth,
					camera.viewportHeight
					);
			// The ship itself centered horizontally and offset a bit up to allow room for engines
			// which extend from the back of the ship
			batch.draw(
					spriteShip.getTexture(),
					(camera.viewportWidth - spriteShip.getWidth() * SHIP_SCALING_FACTOR) / 2.0f,  // horizontally centered (why is scaling factor needed here?)
					shipBaseYPosition(),
					camera.viewportWidth * SHIP_SCALING_FACTOR,
					camera.viewportHeight * SHIP_SCALING_FACTOR
					);
			batch.end();
			
			stage.act();
			stage.draw();
			camera.update();
			
			super.render(delta);
		}
	}
	
	private float shipBaseYPosition() {
		return camera.viewportHeight / 7.2f;
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
		stage.getViewport().apply();
		
		super.resize(width, height);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {
		setActive(false);
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
		return active;
	}
	
	/**
	 * A stackable popup screen for engine selection
	 * 
	 * TODO: This class needs to read the ship configuration from the ShipConfig
	 * 
	 * @author jau
	 */
	private class EnginePopupScreen implements Screen {
		private static final float BORDER_LINE_WIDTH = 4.0f;
		private static final float INSET_PERCENTAGE = 0.05f;
		private boolean active = true;
		private final Color COLOR_BACKGROUND = new Color(0.5f, 0.5f, 0.5f, 0.45f);
		private final Color COLOR_BORDER = Color.NAVY;
		private final Sprite spriteEngine;
		private final SpriteBatch batch;
		private final Stage stage;
		private final ShapeRenderer shapeRenderer = new ShapeRenderer();
		private final GlyphLayout gryphLayoutEngineTitle = new GlyphLayout(); // Used for computing width of engine title font glyps text for centering
		private final BitmapFont fontScreenTitle = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Exo2-Black.ttf"))
									    .generateFont(new FreeTypeFontParameter() {{
									    	size = 40;
									    	color = new Color(0, 0.8f, 0.8f, 1);
									    	shadowOffsetX = 2;
									    	shadowOffsetY = 2;
										}});
		private final BitmapFont fontEngineTitle = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Exo2-Regular.ttf"))
									    .generateFont(new FreeTypeFontParameter() {{
									    	size = 38;
									    	color = Color.WHITE;
									    	shadowOffsetX = 3;
									    	shadowOffsetY = 3;
										}});
		private final BitmapFont fontDescription = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Exo2-Light.ttf"))
									    .generateFont(new FreeTypeFontParameter() {{
									    	size = 32;
									    	color = Color.WHITE;
									    	shadowOffsetX = 1;
									    	shadowOffsetY = 1;
										}});
		private final Label lblEngineDescription = new Label("...", new LabelStyle(fontDescription, fontDescription.getColor()));
		
		/**
		 * Sole constructor
		 */
		public EnginePopupScreen() {
			spriteEngine = new Sprite(new Texture(Gdx.files.internal("images/EngineChemical-left.png")));
			batch = new SpriteBatch();
			stage = new Stage(viewport);
			
			stage.clear();
			stage.addListener(new InputListener() {
				@Override
				public boolean keyUp(InputEvent event, int keycode) {
					switch (keycode){
						case Input.Keys.ESCAPE:
							Hangar.this.removeScreen(EnginePopupScreen.this);
							break;
						}
				
					return super.keyUp(event, keycode);
				}
			});
			
			// TODO: Load from ShipConfig
			lblEngineDescription.setText("This is the stock engine that uses impulse from a chemical reaction. This was the only type of engine available during early days of space exploration.  It is essentially a modernized rocket engine.  Its design operates best in planetary atmosphere conditions so provides a degraded level of performance in the frigid vacuum of space.  The chemical engine is largely superseded by Neutrino engines that perform much better in space.");
			lblEngineDescription.setSize(camera.viewportWidth * 0.85f, 188f);
			lblEngineDescription.setWrap(true);
		}
		
		@Override
		public void show() {
			Gdx.input.setInputProcessor(stage);
			active = true;
		}

		@Override
		public void render(float delta) {
			if (active) {
			    // Frame box dimensions is expressed as a parameter of INSET_PERCENTAGE
				// TODO: Can this be moved outside of render()?  Other parts of the code can use it.
			    final float boxX = INSET_PERCENTAGE * camera.viewportWidth;
			    final float boxY = INSET_PERCENTAGE * camera.viewportHeight;
			    final float boxWidth = (1 - 2 * INSET_PERCENTAGE) * camera.viewportWidth;
			    final float boxHeight = (1 - 2 * INSET_PERCENTAGE) * camera.viewportHeight;

			    shapeRenderer.setProjectionMatrix(camera.combined);
			    
			    // Frame outer box border
			    batch.begin();
			    shapeRenderer.begin(ShapeType.Filled);
			    shapeRenderer.setColor(COLOR_BORDER);
			    // left border
			    shapeRenderer.rectLine(boxX, boxY, boxX, boxY + boxHeight, BORDER_LINE_WIDTH); 
			    // top border
			    shapeRenderer.rectLine(boxX - BORDER_LINE_WIDTH / 2, boxY + boxHeight, boxX + boxWidth + BORDER_LINE_WIDTH / 2, boxY + boxHeight, BORDER_LINE_WIDTH); 
			    // right border
			    shapeRenderer.rectLine(boxX + boxWidth, boxY, boxX + boxWidth, boxY + boxHeight, BORDER_LINE_WIDTH); 
			    // bottom border
			    shapeRenderer.rectLine(boxX - BORDER_LINE_WIDTH / 2, boxY, boxX + boxWidth + BORDER_LINE_WIDTH / 2, boxY, BORDER_LINE_WIDTH);
			    shapeRenderer.end();
			    batch.end();
				
			    // Frame filled rectangle alpha blended
			    Gdx.gl.glEnable(GL20.GL_BLEND);
			    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			    batch.begin();
				shapeRenderer.begin(ShapeType.Filled);
				shapeRenderer.setColor(COLOR_BACKGROUND);
				shapeRenderer.box(
						boxX,       // x
						boxY,       // y 
						0.0f,       // z
						boxWidth,   // width
						boxHeight,  // height
						0.0f);      // depth
				shapeRenderer.end();
				batch.end();
				
				// Draw engines -- TEST -- this should be a setable scrollable content box that
				// can contain any content like engines, shields, power cores, etc.
				batch.begin();
				batch.setProjectionMatrix(stage.getViewport().getCamera().combined);
				final float engineBaseY = 1.25f * (camera.viewportHeight - spriteEngine.getHeight()) / 2;
				batch.draw(
						spriteEngine, 
						(camera.viewportWidth - spriteEngine.getWidth()) / 2, 
						engineBaseY
						);
				
				gryphLayoutEngineTitle.setText(fontEngineTitle, "Chemical Engine");
				
				// Screen title
				fontScreenTitle.draw(batch, "Select your engine", boxX + 10f, boxY + boxHeight - 5.0f);
				fontEngineTitle.draw(batch, "Chemical Engine",
						(camera.viewportWidth - spriteEngine.getWidth()) / 2 + gryphLayoutEngineTitle.width / 4,
						engineBaseY + spriteEngine.getHeight() + fontEngineTitle.getLineHeight());
				
				// Engine description blurb
				// TODO: Need to wrap in scrollable pane in case text overflows the screen
				lblEngineDescription.setPosition(boxX + 25f, engineBaseY - spriteEngine.getHeight() / 2.0f + lblEngineDescription.getMinHeight() / 2.0f);
				lblEngineDescription.draw(batch, 1.0f);
				
				batch.end();
				
				stage.act();
				stage.draw();
				
				camera.update();
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
		public void hide() {
			this.active = false;
			
		}

		@Override
		public void dispose() {
//			stage.dispose();
		}
	}
}

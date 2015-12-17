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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gdx.orion.gamemodel.Campaign;
import com.gdx.orion.gamemodel.engines.ChemicalEngine;
import com.gdx.orion.gamemodel.engines.Engine;
import com.gdx.orion.gamemodel.engines.NeutrinoEngine;
import com.gdx.orion.utils.Scene2dUtils;

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
	private final Button btnEnginePortLeft;
	private final Button btnEnginePortRight;
	private static final float SHIP_SCALING_FACTOR = 0.85f;
	
	/**
	 * The current engine to render.  Can be null if no engine is currently configured.
	 * The engine is implement as a button because it can be clicked on by user for
	 * configuration.
	 */
	private Button btnEngineLeft = null;
	private Button btnEngineRight = null;
	
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
		if (btnEngineLeft != null) stage.addActor(btnEngineLeft);
		if (btnEngineRight != null) stage.addActor(btnEngineRight);
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
		
		mvcUpdateView();
	}
	
	/**
	 * Configures the ship's view to match the campaign configuration.  This method
	 * can and should be called after any ship configuration is made to update the view
	 * with the model.
	 */
	private void mvcUpdateView() {
		final Engine currentEngine = Campaign.getInstance().getShipModel().getCurrentEngine();
		
		// Use Java reflection to determine type of engine (if any)
		if (currentEngine != null) {
			if (currentEngine instanceof ChemicalEngine) {
				// Left Engine
				btnEngineLeft = new ImageButton(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("images/EngineChemical-left.png")))));
				btnEngineLeft.setName("EngineChemical"); // Naming is important for removal from stage
				btnEngineLeft.setSize(250, 384);
				btnEngineLeft.setPosition(500, shipBaseYPosition() - 100f);
				
				stage.addActor(btnEngineLeft);
				
				// Right Engine
				btnEngineRight = new ImageButton(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("images/EngineChemical-left.png")))));
				btnEngineRight.setName("EngineChemical"); // Naming is important for removal from stage
				btnEngineRight.setSize(250, 384);
				btnEngineRight.setPosition(1175, shipBaseYPosition() - 100f);
				
				stage.addActor(btnEngineRight);
			} else {
				// Left Engine
				btnEngineLeft = new ImageButton(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("images/EngineNeutrino.png")))));
				btnEngineLeft.setName("EngineNeutrino"); // Naming is important for removal from stage
				btnEngineLeft.setSize(250, 384);
				btnEngineLeft.setPosition(500, shipBaseYPosition() - 100f);
				
				stage.addActor(btnEngineLeft);
				
				// Right Engine
				btnEngineRight = new ImageButton(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("images/EngineNeutrino.png")))));
				btnEngineRight.setName("EngineNeutrino"); // Naming is important for removal from stage
				btnEngineRight.setSize(250, 384);
				btnEngineRight.setPosition(1175, shipBaseYPosition() - 100f);
				
				stage.addActor(btnEngineRight);
			}
		} else {
			for (final Actor actor : stage.getActors()) {
				if (actor != null && actor instanceof ImageButton) {
					if (actor.getName() != null && actor.getName().startsWith("Engine")) {
						// Remove the engine since nothing was 
						actor.remove();
					}
				}
			}
		}
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
		private static final float FRAME_INSET_PERCENTAGE = 0.05f;
		private static final float BUTTON_INSET_PERCENTAGE = 1.06f;
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
		private final BitmapFont fontEngineDescription = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Exo2-Light.ttf"))
									    .generateFont(new FreeTypeFontParameter() {{
									    	size = 32;
									    	color = Color.WHITE;
									    	shadowOffsetX = 1;
									    	shadowOffsetY = 1;
										}});
		ScrollPane scrollPaneEngineSelection;
		
		
		// Popup's outer frame dimensions are expressed as a parameter of FRAME_INSET_PERCENTAGE
	    private final float boxX = FRAME_INSET_PERCENTAGE * camera.viewportWidth;
	    private final float boxY = FRAME_INSET_PERCENTAGE * camera.viewportHeight;
	    private final float boxWidth = (1 - 2 * FRAME_INSET_PERCENTAGE) * camera.viewportWidth;
	    private final float boxHeight = (1 - 2 * FRAME_INSET_PERCENTAGE) * camera.viewportHeight;
	    
	    /**
	     * Remember the previous input handler so that control can be returned to
	     * it on dispose
	     * 
	     * @see {@link #dispose()}
	     */
	    private InputProcessor previousInputProcessor = Gdx.input.getInputProcessor();
	    
		/**
		 * Sole constructor
		 */
		public EnginePopupScreen() {
			spriteEngine = new Sprite(new Texture(Gdx.files.internal("images/EngineChemical-left.png")));
			batch = new SpriteBatch();
			this.stage = new Stage(viewport);  // Must use the outer classes viewport!
			Gdx.input.setInputProcessor(this.stage);
			
			final Skin skin = new Skin();
			final Pixmap pixmap = new Pixmap(200, 100, Format.RGBA8888);
			pixmap.setColor(Color.GOLD);
			pixmap.fill();
			skin.add("white", new Texture(pixmap));
			skin.add("default", new FreeTypeFontGenerator(Gdx.files.internal("fonts/Exo2-Thin.ttf")).generateFont(new FreeTypeFontParameter()));
			final TextButton.TextButtonStyle style = Scene2dUtils.createTextButtonStyle(skin, "default");
			final Button btnOk = new TextButton("OK", style);
			btnOk.setPosition(boxX + boxWidth - btnOk.getWidth() * BUTTON_INSET_PERCENTAGE, boxY * BUTTON_INSET_PERCENTAGE);
			
			this.stage.clear();
			this.stage.addListener(new InputListener() {
				@Override
				public boolean keyUp(InputEvent event, int keycode) {
					switch (keycode) {
						case Input.Keys.ESCAPE:
						case Input.Keys.BACK:
							cancel();
							break;
						}
				
					return super.keyUp(event, keycode);
				}
			});
			btnOk.addListener(new ClickListener() {
	            @Override 
	            public void clicked(InputEvent event, float x, float y) {
	            	ok();
	            }
	        });
			
			
			// Main table layout
			final float tableInsetPercentage = 0.88f;
			final Table tblOuter = new Table();
			// Main container table is centered and slightly smaller than the viewport
			tblOuter.setBounds(
					(camera.viewportWidth - camera.viewportWidth * tableInsetPercentage) / 2, 
					(camera.viewportHeight - camera.viewportHeight * tableInsetPercentage) / 2, 
					camera.viewportWidth * tableInsetPercentage, 
					camera.viewportHeight * tableInsetPercentage);
			tblOuter.debug(); // Draws lines for easier debugging of layout
			
			final Table tblContent = new Table();
			// Set each engine to be the width of the panel as they should occupy the view entirely until scrolled to 
			// the next engine
			tblContent.add(createChemicalEngineLayout(tblOuter.getWidth() * .98f)).fill().minWidth(tblOuter.getWidth());
			tblContent.add(createNeutrinoEngineLayout(tblOuter.getWidth() * .98f)).fill().minWidth(tblOuter.getWidth());
			
			scrollPaneEngineSelection = new ScrollPane(tblContent);
			scrollPaneEngineSelection.setScrollBarPositions(true, false);
			scrollPaneEngineSelection.setScrollbarsOnTop(true);
			tblOuter.add(new Label("Choose Your Engine", new LabelStyle(fontScreenTitle, fontScreenTitle.getColor()))).align(Align.left).top().pad(0, 0, 8, 0);
			tblOuter.row();
			tblOuter.add(scrollPaneEngineSelection).center().fill().expand();
			tblOuter.row();
			tblOuter.add(btnOk).align(Align.right).pad(8, 0, 0, 0);
						
			stage.addActor(tblOuter);
		}
		
		/**
		 * Cancels and close this popup screen from view and perform no action
		 */
		public void cancel() {
			Hangar.this.removeScreen(EnginePopupScreen.this);
		}
		
		public void ok() {
			if (scrollPaneEngineSelection.getScrollPercentX() <= 0.5f) {
				Campaign.getInstance().getShipModel().setCurrentEngine(new ChemicalEngine());  // TODO: should get engine from inventory
			} else {
				Campaign.getInstance().getShipModel().setCurrentEngine(new NeutrinoEngine());  // TODO: should get engine from inventory
			}
			
			Hangar.this.removeScreen(EnginePopupScreen.this);
			mvcUpdateView();
		}
		
		@Override
		public void show() {
			Gdx.input.setInputProcessor(this.stage);
			active = true;
		}

		@Override
		public void render(float delta) {
			if (active) {
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
			Gdx.input.setInputProcessor(previousInputProcessor);
		}
		
		public Table createChemicalEngineLayout(final float maxWidth) {
			final Table table = new Table();
			final ImageButton imageButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("images/EngineChemical-left.png")))));
			final Label lblEngineDescription = new Label("...", new LabelStyle(fontEngineDescription, fontEngineDescription.getColor()));
			// TODO: Load from ShipConfig
			lblEngineDescription.setText("This is the stock engine that uses impulse from a chemical reaction. This was the only type of engine available during early days of space exploration.  It is essentially a modernized rocket engine.  Its design operates best in planetary atmosphere conditions so provides a degraded level of performance in the frigid vacuum of space.  The chemical engine is largely superseded by Neutrino engines that perform much better in space.");
			lblEngineDescription.setSize(camera.viewportWidth * 0.85f, 188f);
			lblEngineDescription.setWrap(true);
						
			table.debug();
			table.add(new Label("Chemical Engine", new LabelStyle(fontEngineTitle, fontEngineTitle.getColor()))).align(Align.center).pad(0, 0, 8, 0);
			table.row();
			table.add(imageButton).bottom().fill().expand();
			table.row();
			table.add(lblEngineDescription).align(Align.center).fill().expandY().bottom();

	        return table;
		}
		
		public Table createNeutrinoEngineLayout(final float maxWidth) {
			final Table table = new Table();
			final ImageButton imageButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("images/EngineNeutrino.png")))));
			final Label lblEngineDescription = new Label("...", new LabelStyle(fontEngineDescription, fontEngineDescription.getColor()));
			// TODO: Load from ShipConfig
			lblEngineDescription.setText("This modern space engine provides much greater efficiency and thrust over the old chemical engine design.  The Neutrino drive is the engine of choice for high performance sublight maneuvering in the vacuum of space.  It integrates advanced R&D concepts that physicists only theorized about at the dawn of the \"space age\".");
			lblEngineDescription.setSize(camera.viewportWidth * 0.85f, 188f);
			lblEngineDescription.setWrap(true);
						
			table.debug();
			table.add(new Label("Neutrino Engine", new LabelStyle(fontEngineTitle, fontEngineTitle.getColor()))).align(Align.center).pad(0, 0, 8, 0);
			table.row();
			table.add(imageButton).bottom().fill().expand();
			table.row();
			table.add(lblEngineDescription).align(Align.center).fill().expandY().bottom();

	        return table;
		}
	}
}

package com.gdx.orion.screens;

import java.util.Iterator;

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
import com.gdx.orion.gamemodel.engines.HiggsInhibitorEngine;
import com.gdx.orion.gamemodel.engines.NeutrinoEngine;
import com.gdx.orion.utils.PagedScrollPane;
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
		
		mvcUpdateView();
	}
	
	/**
	 * Configures the ship's view to match the campaign configuration.  This method
	 * can and should be called after any ship configuration is made to update the view
	 * with the model.
	 */
	private void mvcUpdateView() {
		final Engine currentEngine = Campaign.getInstance().getShipModel().getCurrentEngine();
		
		// Remove all engines from the stage as they will be updated to the new configuration
		final Iterator<Actor> iterator = stage.getActors().iterator();
		while (iterator.hasNext()) {
			final Actor actor = iterator.next();
			
			if (actor != null && actor instanceof ImageButton) {
				if (actor.getName() != null && actor.getName().startsWith("Engine")) {
					iterator.remove();
				}
			}
		}
		
		// Use Java reflection to determine type of engine (if any)
		if (currentEngine != null) {
			if (currentEngine instanceof ChemicalEngine) {
				// Left Engine
				final Button btnEngineLeft = new ImageButton(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("images/EngineChemical-left.png")))));
				btnEngineLeft.setName("EngineChemical"); // Naming is important for removal from stage
				btnEngineLeft.setSize(250, 384);
				btnEngineLeft.setPosition(500, shipBaseYPosition() - 100f);
				btnEngineLeft.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						addScreen(new EnginePopupScreen());
					}
				});
						
				stage.addActor(btnEngineLeft);
				
				// Right Engine
				final Button btnEngineRight = new ImageButton(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("images/EngineChemical-right.png")))));
				btnEngineRight.setName("EngineChemical"); // Naming is important for removal from stage
				btnEngineRight.setSize(250, 384);
				btnEngineRight.setPosition(1175, shipBaseYPosition() - 100f);
				btnEngineRight.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						addScreen(new EnginePopupScreen());
					}
				});
				
				stage.addActor(btnEngineRight);
			} else if (currentEngine instanceof NeutrinoEngine) {
				// Left Engine
				final Button btnEngineLeft = new ImageButton(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("images/EngineNeutrino-left.png")))));
				btnEngineLeft.setName("EngineNeutrino"); // Naming is important for removal from stage
				btnEngineLeft.setSize(250, 384);
				btnEngineLeft.setPosition(500, shipBaseYPosition() - 100f);
				btnEngineLeft.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						addScreen(new EnginePopupScreen());
					}
				});
				
				stage.addActor(btnEngineLeft);
				
				// Right Engine
				final Button btnEngineRight = new ImageButton(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("images/EngineNeutrino-right.png")))));
				btnEngineRight.setName("EngineNeutrino"); // Naming is important for removal from stage
				btnEngineRight.setSize(250, 384);
				btnEngineRight.setPosition(1175, shipBaseYPosition() - 100f);
				btnEngineRight.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						addScreen(new EnginePopupScreen());
					}
				});
				
				stage.addActor(btnEngineRight);
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
		private final SpriteBatch batch;
		private final Stage stage;
		private final ShapeRenderer shapeRenderer = new ShapeRenderer();
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
		PagedScrollPane scrollPaneEngineSelection;
		
		
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
			new Sprite(new Texture(Gdx.files.internal("images/EngineChemical-left.png")));
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
			//tblOuter.debug(); // Draws lines for easier debugging of layout
			
			final Table tblContent = new Table();
			// Set each engine to be the width of the panel as they should occupy the view entirely until scrolled to 
			// the next engine
			tblContent.add(createChemicalEngineLayout()).fill().minWidth(tblOuter.getWidth());
			tblContent.add(createNeutrinoEngineLayout()).fill().minWidth(tblOuter.getWidth());
			tblContent.add(createHiggsEngineLayout()).fill().minWidth(tblOuter.getWidth());
			
			scrollPaneEngineSelection = new PagedScrollPane();
			scrollPaneEngineSelection.addPage(createChemicalEngineLayout(), tblOuter.getWidth());
			scrollPaneEngineSelection.addPage(createNeutrinoEngineLayout(), tblOuter.getWidth());
			scrollPaneEngineSelection.addPage(createHiggsEngineLayout(), tblOuter.getWidth());
			scrollPaneEngineSelection.setScrollBarPositions(true, false);
			scrollPaneEngineSelection.setForceScroll(true, false);
			scrollPaneEngineSelection.setFlingTime(0.15f);  // Set an appropriate fling time: too fast and motion will be jerky, too slow and motion is heavy
			
			tblOuter.add(new Label("Choose Your Engine", new LabelStyle(fontScreenTitle, fontScreenTitle.getColor()))).align(Align.left).top().pad(0, 0, 8, 0);
			tblOuter.row();
			tblOuter.add(scrollPaneEngineSelection).center().fill().expand();
			tblOuter.row();
			tblOuter.add(btnOk).align(Align.right).pad(8, 0, 0, 0);
						
			stage.addActor(tblOuter);
			
			// Set the scrollpane to the currently selected engine
			//
			final Engine currentEngine = Campaign.getInstance().getShipModel().getCurrentEngine();
			if (currentEngine != null) {
				scrollPaneEngineSelection.forceScrollTo(currentEngine.getClass().getSimpleName());
			}
		}
		
		/**
		 * Cancels and close this popup screen from view and perform no action
		 */
		public void cancel() {
			Hangar.this.removeScreen(EnginePopupScreen.this);
		}
		
		public void ok() {
			// TODO: Add Higgs Inhibitor engine when its ready
			if (ChemicalEngine.class.getSimpleName().equals(scrollPaneEngineSelection.getCurrentPage().getName())) {
				Campaign.getInstance().getShipModel().setCurrentEngine(new ChemicalEngine());
			} else if (NeutrinoEngine.class.getSimpleName().equals(scrollPaneEngineSelection.getCurrentPage().getName())) {
				Campaign.getInstance().getShipModel().setCurrentEngine(new NeutrinoEngine());
			} else {
				Campaign.getInstance().getShipModel().setCurrentEngine(null);
			}
			
			// Remove the screen from view and notify views that model has changed
			Hangar.this.removeScreen(EnginePopupScreen.this);
			mvcUpdateView();
			GameStateManager.play.getPlayerShip().updateShipImage();  // TODO: this is a temporary hack since game flow isn't set up fully yet
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
		
		public Table createChemicalEngineLayout() {
			final Table table = new Table();
			final ImageButton imageButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("images/EngineChemical-left.png")))));
			final Label lblEngineDescription = new Label("...", new LabelStyle(fontEngineDescription, fontEngineDescription.getColor()));
			// TODO: Load from ShipConfig
			lblEngineDescription.setText("This is the stock engine that uses impulse from a chemical reaction. This was the only type of engine available during early days of space exploration.  It is essentially a modernized rocket engine.  Its design operates best in planetary atmosphere conditions so provides a degraded level of performance in the frigid vacuum of space.  The chemical engine is largely superseded by Neutrino engines that perform much better in space.");
			lblEngineDescription.setSize(camera.viewportWidth * 0.85f, 188f);
			lblEngineDescription.setWrap(true);
						
			//table.debug();
			table.add(new Label("Chemical Engine", new LabelStyle(fontEngineTitle, fontEngineTitle.getColor()))).align(Align.center).pad(0, 0, 8, 0);
			table.row();
			table.add(imageButton).bottom().fill().expand();
			table.row();
			table.add(lblEngineDescription).align(Align.center).fill().expandY().bottom();

			// Helps identify which engine this layout represents when OK is selected
			table.setName(ChemicalEngine.class.getSimpleName());
			
	        return table;
		}
		
		public Table createNeutrinoEngineLayout() {
			final Table table = new Table();
			final ImageButton imageButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("images/EngineNeutrino-left.png")))));
			final Label lblEngineDescription = new Label("...", new LabelStyle(fontEngineDescription, fontEngineDescription.getColor()));
			// TODO: Load from ShipConfig
			lblEngineDescription.setText("This modern space engine provides much greater efficiency and thrust over the old chemical engine design.  The Neutrino drive is the engine of choice for high performance sublight maneuvering in the vacuum of space.  It integrates advanced R&D concepts that physicists only theorized about at the dawn of the \"space age\".");
			lblEngineDescription.setSize(camera.viewportWidth * 0.85f, 188f);
			lblEngineDescription.setWrap(true);
						
			//table.debug();
			table.add(new Label("Neutrino Engine", new LabelStyle(fontEngineTitle, fontEngineTitle.getColor()))).align(Align.center).pad(0, 0, 8, 0);
			table.row();
			table.add(imageButton).bottom().fill().expand();
			table.row();
			table.add(lblEngineDescription).align(Align.center).fill().expandY().bottom();

			// Helps identify which engine this layout represents when OK is selected
			table.setName(NeutrinoEngine.class.getSimpleName());
			
	        return table;
		}
		
		public Table createHiggsEngineLayout() {
			final Table table = new Table();
			final ImageButton imageButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("images/Locked.png")))));
			final Label lblEngineDescription = new Label("...", new LabelStyle(fontEngineDescription, fontEngineDescription.getColor()));
			// TODO: Load from ShipConfig
			lblEngineDescription.setText("This is the most advanced sublight speed engine available and represents the absolute bleeding edge of humanity's progress. This engine's most fascinating feature is that it confers onto the craft an \"inertialess\" state where the craft ceases to have apparent mass and inertia to the outside observer. This allows the craft to be unaffected by gravity or inertia.  Acceleration and deceleration are near instant with no build up of momentum providing a \"UFO\"-like of maneuvering never seen before. It's hoped that further research into this type of drive will lead to a safe and efficient method of faster-than-light travel but that remains to be seen!");
			lblEngineDescription.setSize(camera.viewportWidth * 0.85f, 188f);
			lblEngineDescription.setWrap(true);
						
			//table.debug();
			table.add(new Label("Higgs Inhibitor Engine", new LabelStyle(fontEngineTitle, fontEngineTitle.getColor()))).align(Align.center).pad(0, 0, 8, 0);
			table.row();
			table.add(imageButton).bottom().fill().expand();
			table.row();
			table.add(lblEngineDescription).align(Align.center).fill().bottom();

			// Helps identify which engine this layout represents when OK is selected
			table.setName(HiggsInhibitorEngine.class.getSimpleName());
			
	        return table;
		}
	}
}

package com.gdx.orion.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gdx.orion.entities.voxel.VoxelType;
import com.gdx.orion.entities.voxel.types.Air;
import com.gdx.orion.entities.voxel.types.Grass;
import com.gdx.orion.entities.voxel.types.Stone;
import com.gdx.orion.utils.Scene2dUtils;

public class LevelEdit extends GameState implements Screen, InputProcessor {
	
	private static final int gridSquareSize = 10;
	
	private InputMultiplexer im;
	
	private OrthographicCamera editorCamera;
	private OrthographicCamera gridCamera;
	
	private Viewport editorViewport;
	
	private Stage stage;
	
	private SpriteBatch sb;
	
	private final Sprite background;
	private final Sprite vacantSquare;
	
	private int gridMaxX, gridMaxY;
	
	private VoxelType[][] voxelTypes;
	
	private boolean dragging = false;
	
	public LevelEdit() {
		super(GameStateManager.LEVELEDIT);
		
		im = new InputMultiplexer();
		im.addProcessor(this);
		
		editorCamera = new OrthographicCamera();
		gridCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		gridCamera.position.set(0 + gridCamera.viewportWidth/2, 0 + gridCamera.viewportHeight/2, 0);
		
		editorViewport = new ScalingViewport(Scaling.stretch, 1000, 1000, editorCamera);
		editorViewport.apply();
		
		stage = new Stage(editorViewport);
		
		sb = new SpriteBatch();
		sb.setProjectionMatrix(gridCamera.combined);
		
		background = new Sprite(new Texture(Gdx.files.internal("images/stars.png")));
		background.setSize(editorCamera.viewportWidth*2, editorCamera.viewportHeight*2);
		background.setPosition(0 - editorCamera.viewportWidth / 2, 0 - editorCamera.viewportHeight / 2);
		
		vacantSquare = new Sprite(new Texture(Gdx.files.internal("images/vacantSquare.png")));
		vacantSquare.setSize(gridSquareSize, gridSquareSize);
		
		Pixmap bp = new Pixmap(240, 200, Format.RGBA8888);
		bp.setColor(Color.GRAY);
		bp.fill();
		bp.setColor(Color.BLACK);
		bp.drawRectangle(0, 0, 240, 200);
		
		Skin skin = Scene2dUtils.createDefaultSkin(new Texture(bp));

		TextButtonStyle style = Scene2dUtils.createTextButtonStyle(skin, "default");
		
		final int buttonXOffset = 200, buttonYOffset = 100;
		
		final Button selectVoxel = new TextButton("Select Voxel", style);
		selectVoxel.setPosition(editorCamera.viewportWidth-buttonXOffset, editorCamera.viewportHeight-buttonYOffset);
		selectVoxel.setWidth(buttonXOffset);
		selectVoxel.setHeight(buttonYOffset);
		
		selectVoxel.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y)
			{
				addScreen(new VoxelSelector());
			}
		});
		
		stage.addActor(selectVoxel);
		
		im.addProcessor(stage);
		Gdx.input.setInputProcessor(im);
		
		resetGrid();
	}
	
	/*
	 * Custom Methods
	 */
	private void resetGrid() {
		gridMaxX = 40;
		gridMaxY = 40;
		
		voxelTypes = new VoxelType[gridMaxX][gridMaxY];
		
		for (int x = 0; x < gridMaxX; x++) {
			 for (int y = 0; y < gridMaxY; y++) {
				 voxelTypes[x][y] = VoxelType.AIR;
			 }
		}
	}
	
	private void renderGrid(SpriteBatch sb) {
		for (int x = 0; x < gridMaxX; x++) {
			for (int y = 0; y < gridMaxY; y++) {
				if (voxelTypes[x][y].equals(VoxelType.AIR)) {
					sb.draw(vacantSquare, (x * gridSquareSize), (y * gridSquareSize));
				}
			}
		}
	}
	
	private boolean isOnGrid(int x, int y) {
		if (x > 0 && y > 0 && x < gridMaxX * gridSquareSize && y < gridMaxY * gridSquareSize) {
			return true;
		}
		return false;
	}
	
	/*
	 * GDX Methods 
	 */
	public void show() {
		Gdx.input.setInputProcessor(im);
		this.setActive(true);
	}

	public void render(float delta) {
		if (active) {
			Gdx.gl.glClearColor(0,0,0,1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
			stage.act();
			
			sb.begin();
			
			// Layer 1
			sb.setProjectionMatrix(editorCamera.combined);
			
			background.draw(sb);
			
			// Layer 2
			sb.setProjectionMatrix(gridCamera.combined);
			
			renderGrid(sb);
			
			// Layer 3
			sb.setProjectionMatrix(editorCamera.combined);
			
			sb.end();
			
			stage.draw();
		
			editorCamera.update();
			gridCamera.update();
			
			super.render(delta);
		}
	}

	public void resize(int width, int height) {
		editorViewport.update(width, height);
		editorViewport.apply();
	}

	public void pause() {
		
	}

	public void resume() {
		
	}

	public void hide() {
		
	}

	public void dispose() {
		sb.dispose();
		stage.dispose();
	}

	public void setActive(boolean active) {
		super.active = active;
	}

	public boolean isActive() {
		return super.active;
	}
	
	/*
	 * Input processor methods 
	 */
	public boolean keyDown(int keycode) {
		return false;
	}

	public boolean keyUp(int keycode) {
		return false;
	}

	public boolean keyTyped(char character) {
		return false;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button != Input.Buttons.LEFT || pointer > 0) return false;
		dragging = true;
		return true;
	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (button != Input.Buttons.LEFT || pointer > 0) return false;
		dragging = false;
		return true;
	}

	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (!dragging) return false;
		gridCamera.translate(-Gdx.input.getDeltaX(), Gdx.input.getDeltaY());
		return true;
	}

	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	public boolean scrolled(int amount) {
		if (!(this.gridCamera.zoom + amount < 1)) {
			this.gridCamera.zoom += amount;
		} else if (this.gridCamera.zoom < 0) {
			this.gridCamera.zoom = 1;
		}
		return false;
	}
	
	/*
	 * Voxel Selector Class
	 */
	private class VoxelSelector implements Screen
	{

		private boolean active = true;
		private Stage stage;
		
		private SpriteBatch sb;
		
		private InputProcessor previousInputProcessor = Gdx.input.getInputProcessor();
		
		private Texture[][] imageGrid = { {Grass.getTexture(), Stone.getTexture()} };
		
		public VoxelSelector()
		{
			stage = new Stage();
			Gdx.input.setInputProcessor(this.stage);
			
			sb = new SpriteBatch();
			
			this.stage.clear();
			this.stage.addListener(new InputListener() {
				public boolean keyUp(InputEvent event, int keycode)
				{
					switch(keycode)
					{
						case Keys.ESCAPE: cancel(); break;
					}
					
					return super.keyUp(event, keycode);
				}
			});
		}
		
		public void show() 
		{
			Gdx.input.setInputProcessor(this.stage);
			active = true;
		}

		public void render(float delta) 
		{
			if (active)
			{
				sb.begin();
				drawImageGrid();
				sb.end();
				stage.draw();
				stage.act();
				editorCamera.update();
			}
		}

		public void resize(int width, int height) 
		{
			
		}

		public void pause() 
		{
			
		}
		
		public void resume() 
		{
			
		}

		public void hide() 
		{
			this.active = false;
		}

		public void dispose() 
		{
			Gdx.input.setInputProcessor(previousInputProcessor);
		}
		
		public void cancel()
		{
			LevelEdit.this.removeScreen(this);
		}
		
		public void drawImageGrid()
		{
			for (int x = 0; x < imageGrid.length; x++)
			{
				for (int y = 0; y < imageGrid.length; y++)
				{
					sb.draw(imageGrid[x][y], x * 100, y * 100, 100, 100);
				}
			}
		}
		
	}
	
}

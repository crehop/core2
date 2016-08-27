package com.gdx.orion.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gdx.orion.entities.voxel.VoxelType;
import com.gdx.orion.object.CustomButton;

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
	
	private final Texture vsnormal;
	private final Texture vspressed;
	
	private CustomButton selectVoxel;
	
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
		
		vsnormal = new Texture(Gdx.files.internal("buttons/editor/BlockSelect/sbnormal.png"));
		vspressed = new Texture(Gdx.files.internal("buttons/editor/BlockSelect/sbpressed.png"));
		
		selectVoxel = new CustomButton(vsnormal, vspressed, editorCamera.viewportWidth - 200, editorCamera.viewportHeight - 100, (float)200, (float)100);
		
		selectVoxel.addListener(new ClickListener() {
			@Override
		    public void clicked(InputEvent event, float x, float y) {
		    	LevelEdit.this.selectVoxel.switchImage();
		    	System.out.println("Press");
			}
		});
		
		stage.addActor(selectVoxel);
		
		im.addProcessor(stage);
		
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

	
	
}

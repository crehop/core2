package com.gdx.orion.screens;

import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Disposable;

/**
 * The screen service class that handles layering of separate screens
 */
public class ScreenLayerStack implements Disposable, Screen {
	/**
	 * Actual storage of screens in their respective order
	 * 
	 * @see #addScreen(Screen)
	 */
    private final Stack<Screen> screenStack = new Stack<Screen>();

    /**
     * Sole constructor
     */
    public ScreenLayerStack() {}

    /**
     * Adds the specified screen to stack.  If screen is already in the stack then
     * the screen is ignored.
     *
     * @param screen a screen to add to the layer.  Ordering is important!  Last screen
     * added is "on top".
     * @see #removeScreen(Screen)        
     */
    public void addScreen(final Screen screen) {
        // Add the screen to the layer stack but only if it does not already exist in the stack
        if (!screenStack.contains(screen)) {
        	screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        	screenStack.push(screen);
        } else {
        	System.out.printf("Screen %s already added.  Not adding screen again.%n", screen);
        }
    }

    /** Releases all resources of this object. */
    @Override
    public void dispose() {
        for (final Screen screen : screenStack) {
            if (screen == null) {
            	// Guard against potential null screens in the stack
            	continue;
            }
            
            screen.dispose();
        }
        screenStack.clear();
    }

    /**
     * Draws all screens in the service.
     */
//    public void draw() {
//        for(Screen screen : screenStack) {
//            if (screen == null) {
//            	// Guard against potential null screens in the stack
//            	continue;
//            }
//            
//            screen.draw();
//        }
//    }

    /**
     * Pauses all screens in the service.
     */
    public void pause() {
        for(Screen screen : screenStack) {
            if (screen == null) {
            	// Guard against potential null screens in the stack
            	continue;
            }
            
            screen.pause();
        }
    }

    /**
     * Called when the game window is resized.
     *
     * @param width  The new game window width (in pixels).
     * @param height The new game window height (in pixels).
     */
    public void resize(final int width, final int height) {
        for (final Screen screen : screenStack) {
            if(screen == null) {
            	continue;
            }
            
            screen.resize(width, height);
        }
    }

    /**
     * Resumes all screens in the service.
     */
    public void resume() {
        for (final Screen screen : screenStack) {
            if (screen == null) {
            	continue;
            }
            screen.resume();
        }
    }

    /**
     * Updates the screen service.
     *
     * @param tt The total amount of time, in seconds, since the game started.
     * @param dt The total amount of time, in seconds, since the last update.
     */
    public void update(final float tt, final float dt) {
        
    }

    /**
     * Removes the specified screen from the service.
     *
     * @param screen 
     * @see #addScreen(Screen)
     */
    public void removeScreen(final Screen screen) {
        screen.dispose();
        screenStack.remove(screen);
    }

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
        for(int i = screenStack.size() - 1; i >= 0; i--) {
            Screen screen = screenStack.get(i);
            if(screen == null) {
                screenStack.remove(i);
                continue;
            }
            screen.render(delta);
        }
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
}

package com.bomberman.java;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Bomberman extends Game {
    public SpriteBatch batch;
    public FitViewport viewport;
    public BitmapFont font;
    public OrthographicCamera camera;
    public float worldWidth;
    public float worldHeight;
    
    public void create() {
    	// Sprite Batch to render all images at once for performance
    	batch = new SpriteBatch();
    	
    	// Viewport to set up the ratio
    	viewport = new FitViewport(3, 3);
    	
    	// Font to be able to draw text to screen
    	font = new BitmapFont();
    	
    	// setting world width / height in game units (16x16 = 1 unit)
    	// map is 20x20 tiles with each tile being 16x16
    	worldWidth = 20;
    	worldHeight = 20;
    	
    	// Camera to render map
    	camera = new OrthographicCamera();
    	// Use position 20, 20 as in game units (16x16 = 1 unit)
    	camera.setToOrtho(false, worldWidth, worldHeight);
    	// set camera to middle of map
    	camera.position.set(10, 10, 0);
    	camera.update();
    	
    	// Settings for font to set their scale relative to screen size
    	font.setUseIntegerPositions(false);
    	font.getData().setScale((viewport.getWorldHeight() / Gdx.graphics.getWidth()) * 2);
    	
    	// Creating and showing new main menu screen
    	this.setScreen(new MainMenuScreen(this));
    }
    
    public void render() {
    	// use render method from game class (important)
    	super.render();
    }
    
    public void dispose() {
    	// disposing items to free up memory and prevent memory leaks
    	batch.dispose();
    	font.dispose();
    }
}

package com.bomberman.java;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen{
	
	final Bomberman game;
	
	public MainMenuScreen(final Bomberman game) {
		// Passing game to main menu to use batch and font
		this.game = game;
	}
	
	@Override
	public void render(float delta) {
		// Clearing screen
		ScreenUtils.clear(Color.BLACK);
		
		game.viewport.apply();
		
		// tell batch to use viewport to draw
		game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
		
		// Initialize sprite batch
		game.batch.begin();
		
		game.font.draw(game.batch, "Click anywhere to begin", 1, 1.5f);
		
		// End sprite batch
		game.batch.end();
		
		// if clicked or pressed
		if (Gdx.input.isTouched()) {
			game.setScreen(new GameScreen(game));
			dispose();
		}
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// Always have to set the resize incase the screen is resized
		game.viewport.update(width, height, true);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}

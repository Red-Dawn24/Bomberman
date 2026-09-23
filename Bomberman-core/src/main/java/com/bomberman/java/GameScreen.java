package com.bomberman.java;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen{
	final Bomberman game;
	
	public TiledMap map;
	public OrthogonalTiledMapRenderer mapRenderer;
	float unitScale;
	Texture ghostTexture;
	Enemy ghost;
	
	public GameScreen(final Bomberman game) {
		this.game = game;
		// 16x16 = 1 world unit
		unitScale = (1f / 16f);
		ghostTexture = new Texture("ghost.png");
		ghost = new Enemy(ghostTexture);
	}

	@Override
	public void show() {
		// Load map and map renderer
		map = new TmxMapLoader().load("FirstMap.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map, unitScale);
		
		//float worldWidth = map.getProperties().get("width", Integer.class) * map.getProperties().get("tilewidth", Integer.class) * unitScale;
		//float worldHeight = map.getProperties().get("height", Integer.class) * map.getProperties().get("tileheight", Integer.class) * unitScale;
		//game.camera.position.set(worldWidth / 2f, worldHeight / 2f, 0);
	}
	
	public void draw() {
		// draw to screen
		ScreenUtils.clear(Color.CYAN);
		// render map
		mapRenderer.render();
		// combine batch with new camera to properly draw
		game.batch.setProjectionMatrix(game.camera.combined);
		
		game.batch.begin();
		// draw objects here
		ghost.draw(game.batch);
		
		game.batch.end();
	}
	
	public void logic(float delta) {
		// anything logic that needs to constantly be rendered goes here
		game.camera.update();
		mapRenderer.setView(game.camera);
		ghost.logic(delta);
	}
	
	public void input(float delta) {
		// anything relating to input goes here
	}

	@Override
	public void render(float delta) {
		input(delta);
		logic(delta);
		draw();
	}

	@Override
	public void resize(int width, int height) {
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
		map.dispose();
		mapRenderer.dispose();
		ghostTexture.dispose();
	}

}

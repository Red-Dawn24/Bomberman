package com.bomberman.java;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen{
	final Bomberman game;
	
	public TiledMap map;
	public OrthogonalTiledMapRenderer mapRenderer;
	float unitScale;
	Texture ghostTexture;
	Enemy ghost;
	Player player1 = new Player(0, 0);
	Array<Bomb> bombs;
	
	public GameScreen(final Bomberman game) {
		this.game = game;
		// 16x16 = 1 world unit
		unitScale = (1f / 16f);
		ghostTexture = new Texture("ghost.png");
		ghost = new Enemy(ghostTexture);
		bombs = new Array<>();
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
		player1.render(game.batch);
		ghost.draw(game.batch);
		for (Bomb bomb : bombs) {
			bomb.draw(game.batch);
		}
		
		game.batch.end();
	}
	
	public void logic(float delta) {
		// anything logic that needs to constantly be rendered goes here
		game.camera.update();
		mapRenderer.setView(game.camera);
		mapRenderer.render();
		for (int i = bombs.size - 1; i >= 0; i--) {
			Bomb bomb = bombs.get(i);
			bomb.logic(delta);
			
			if (!bomb.bombTimerStart) {
				player1.bombStorage += 1;
				bombs.removeIndex(i);
			}
		}
		ghost.logic(delta, map);
		player1.update(delta);
	}
	
	public void input(float delta) {
		// anything relating to input goes here
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			if (player1.bombStorage == 0) return;
            Bomb bomb = new Bomb(map, Math.round(player1.x), Math.round(player1.y));
            bombs.add(bomb);
            player1.bombStorage -= 1;
		}
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

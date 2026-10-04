package com.bomberman.java;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;


public class Bomb {
	final static int[][] BOMB_DIRECTIONS = {
			{0, 1},
			{0, -1},
			{1, 0},
			{-1, 0}
	};
	TiledMap map;
	int bombX;
	int bombY;
	boolean bombTimerStart;
	float bombTimer;
	int bombRadius;
	float bombWidth;
	float bombHeight;
	Texture texture;
	Sprite sprite;
	
	public Bomb(TiledMap map, int x, int y) {
		bombTimer = 0;
		bombRadius = 1;
		bombX = x;
		bombY = y;
		bombWidth = 1;
		bombHeight = 1;
		texture = new Texture("bomb.png");
		sprite = new Sprite(texture);
		sprite.setSize(bombWidth, bombHeight);
		this.map = map;
		
		spawnBomb();
	}
	
	// when bomb is spawned, set position and timer
	public void spawnBomb() {
		sprite.setPosition(bombX, bombY);
		bombTimerStart = true;
	}
	
	// adds delta to bombTimer, when it reaches 3 or more, bombExplode is activated
	public void bombTimerManager(float delta) {
		if (bombTimer >= 3) {
			bombExplode(map);
			return;
		}
		bombTimer += delta;
		System.out.println(bombTimer);
	}
	
	// for now, it simply sets timer to false, which then gets deleted in gamescreen
	public void bombExplode(TiledMap map) {
		for (int i = 0; i < BOMB_DIRECTIONS.length; i++) {
			// get direction x and y and iterate through each one
			int nextX = bombX + BOMB_DIRECTIONS[i][0];
			int nextY = bombY + BOMB_DIRECTIONS[i][1];
			
			if (isBrick(map, nextX, nextY)) {
				breakBrick(map, nextX, nextY);
			}
		}
		bombTimerStart = false;
	}
	
	public boolean isBrick(TiledMap map, int x, int y) {
		for (MapLayer layer : map.getLayers()) {
			if (!(layer instanceof TiledMapTileLayer)) {
				continue;
			}
			
			TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;
			TiledMapTileLayer.Cell cell = tileLayer.getCell(x, y);
			
			// if layer is not floor and cell exists in that layer, return false
			if (cell != null && cell.getTile() != null && tileLayer.getName().equals("Bricks")) {
				return true;
			}
		}
		
		return false;
	}
	
	public void breakBrick(TiledMap map, int nextX, int nextY) {
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("Bricks");
		layer.setCell(nextX, nextY, null);
	}
	
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}
	
	public void logic(float delta) {
		if (bombTimerStart) {
			bombTimerManager(delta);
		}
	}
	
	public void dispose() {
		texture.dispose();
	}
	
}

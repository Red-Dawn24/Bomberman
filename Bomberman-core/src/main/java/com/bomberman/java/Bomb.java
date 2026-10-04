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
	
	boolean bombExplosion;
	float bombExplosionTimer;
	// temporary layer for collision purposes
	TiledMapTileLayer bombLayer;
	
	float bombWidth;
	float bombHeight;
	Texture texture;
	Sprite sprite;
	
	public Bomb(TiledMap map, int x, int y) {
		bombTimer = 0;
		bombRadius = 0;
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
			bombExplode(map, bombRadius);
			return;
		}
		bombTimer += delta;
		System.out.println(bombTimer);
	}
	
	// gets the tiles and checks if brick, if so, set it to a bomb explosion tile and delete brick
	public void bombExplode(TiledMap map) {
			for (int i = 0; i < BOMB_DIRECTIONS.length; i++) {
				// get direction x and y and iterate through each one	
				int nextX = bombX + BOMB_DIRECTIONS[i][0];
				int nextY = bombY + BOMB_DIRECTIONS[i][1];
				
				if (isBrick(map, nextX, nextY)) {
					breakBrick(map, nextX, nextY);
					setBombTiles(map, nextX, nextY);
				}
			}
		
		bombTimerStart = false;
		bombExplosion = true;
	}
	
	public void bombExplode(TiledMap map, int radius) {
		for (int k = 0; k <= radius; k++) {
			for (int i = 0; i < BOMB_DIRECTIONS.length; i++) {
				// get direction x and y and iterate through each one	
				int nextX = bombX + BOMB_DIRECTIONS[i][0];
				int nextY = bombY + BOMB_DIRECTIONS[i][1];
				
				if (nextX < bombX) {
					nextX -= k;
				} else if (nextX > bombX) {
					nextX += k;
				}
				
				if (nextY < bombY) {
					nextY -= k;
				} else if(nextY > bombY) {
					nextY += k;
				}
				
				if (isBrick(map, nextX, nextY)) {
					breakBrick(map, nextX, nextY);
					setBombTiles(map, nextX, nextY);
				}
			}
			
			bombTimerStart = false;
			bombExplosion = true;
		}
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
	
	public void setBombTiles(TiledMap map, int nextX, int nextY) {
		// will be replaced with bomb explosion tiles
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("Walls");
		TiledMapTileLayer.Cell cell;
		
		// if nextY is the direction, the tile selected will be a vertical explosion tile
		if (nextY > 0 || nextY < 0) {
			cell = (TiledMapTileLayer.Cell) layer.getCell(0, 0);
		} else if (nextX > 0 || nextX < 0) {
			// if nextX is the direction of the explosion, it will be a horizontal explosion tile
			cell = (TiledMapTileLayer.Cell) layer.getCell(0, 0);
		} else {
			// if nextX and nextY are (0, 0), it will be the center explosion tile
			cell = (TiledMapTileLayer.Cell) layer.getCell(0, 0);
		}
		
		layer.setCell(nextX, nextY, cell);
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

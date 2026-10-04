package com.bomberman.java;

import java.util.Arrays;
import java.util.stream.IntStream;

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
	// bomb only goes 4 directions
	final static int[][] BOMB_DIRECTIONS = {
			{0, 1},
			{0, -1},
			{1, 0},
			{-1, 0}
	};
	TiledMap map;
	// bomb pos on map
	int bombX;
	int bombY;
	
	// bomb variables for timer and if its active
	boolean bombTimerStart;
	float bombTimer;
	
	// radius, change it to make bomb go further or less distance
	int bombRadius;
	
	// will be used to show bomb explosion tiles for a second
	boolean bombExplosion;
	float bombExplosionTimer;
	
	// if true, explosion will go through soft blocks
	boolean bombPower;
	
	float bombWidth;
	float bombHeight;
	Texture texture;
	Sprite sprite;
	
	public Bomb(TiledMap map, int x, int y) {
		bombTimer = 0;
		bombRadius = 2;
		bombPower = false;
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
	
	// used to check if a direction has already hit a brick
	public boolean isIntInList(int[] list, int x) {
		for (int i = 0; i < list.length; i++) {
			if (x == list[i]) return true;
		}
		
		return false;
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
	}
	
	// gets the tiles and checks if brick, if so, set it to a bomb explosion tile and delete brick
	public void bombExplode(TiledMap map, int radius) {
		// create an array of length 4 and initialize numbers inside to 100
		int[] selectedDirections = new int[]{ 100, 100, 100, 100 };
		int count = 0;
		
		for (int k = 0; k <= radius; k++) {
			for (int i = 0; i < BOMB_DIRECTIONS.length; i++) {
				// get direction x and y and iterate through each one	
				
				// checks if int is in selected directions and if bomb isn't powered up
				if (isIntInList(selectedDirections, i)) continue;
				
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
				
				if (isWall(map, nextX, nextY)) {
					selectedDirections[count++] = i;
				}
				
				if (isBrick(map, nextX, nextY)) {
					breakBrick(map, nextX, nextY);
					
					// if bomb power active, ignore selected directions and convert tiles to bomb explosion tiles
					if (!bombPower) {
						selectedDirections[count++] = i;
					}
				}
				
				//setBombTiles(map, nextX, nextY);
			}
		}
		
		bombTimerStart = false;
		bombExplosion = true;
	}
	
	// checks and returns if a tile is a brick
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
	
	// checks if tile is wall, if so, stop explosion in that direction
	public boolean isWall(TiledMap map, int x, int y) {
		for (MapLayer layer : map.getLayers()) {
			if (!(layer instanceof TiledMapTileLayer)) {
				continue;
			}
			
			TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;
			TiledMapTileLayer.Cell cell = tileLayer.getCell(x, y);
			
			// if layer is not floor and cell exists in that layer, return false
			if (cell != null && cell.getTile() != null && tileLayer.getName().equals("Walls")) {
				return true;
			}
		}
		
		return false;
	}
	
	// sets brick to null, effectively deleting it
	public void breakBrick(TiledMap map, int nextX, int nextY) {
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("Bricks");
		layer.setCell(nextX, nextY, null);
	}
	
	// used to set tiles to appropriate bomb tiles
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

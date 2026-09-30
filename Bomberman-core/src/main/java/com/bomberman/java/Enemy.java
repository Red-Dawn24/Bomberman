package com.bomberman.java;

import java.util.Random;

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

public class Enemy {
	Random random;
	float enemyWidth;
	float enemyHeight;
	float enemyX;
	float enemyY;
	Rectangle rectangle;
	float speed;
	Texture texture;
	Sprite sprite;
	int directionX;
	int directionY;
	int reverseX;
	int reverseY;
	boolean changeDir;
	
	//temporary
	float maxX;
	float minX;
	
	// can only move 4 directions
	private final static int[][] DIRECTIONS = {
			{1, 0}, //right
			{-1, 0}, //left
			{0, 1}, // up
			{0, -1} // down
	};
	
	public Enemy(Texture texture) {
		random = new Random();
		// max x pos on map based of game units
		maxX = 20;
		minX = 1;
		// setting up sprite size / position
		enemyWidth = 1f;
		enemyHeight = 1f;
		enemyX = 1f;
		enemyY = 11f;
		rectangle = new Rectangle();
		// fast speeds can break the movement !!!
		speed = 3f;
		directionX = 1;
		directionY = 0;
		reverseX = 0;
		reverseY = 0;
		this.texture = texture;
		sprite = new Sprite(texture);
		sprite.setSize(enemyWidth, enemyHeight);
		sprite.setPosition(enemyX, enemyY);
	}
	
	// check if tile is walkable by getting the layer
	public boolean isWalkable(TiledMap map, int x, int y) {
		for (MapLayer layer : map.getLayers()) {
			if (!(layer instanceof TiledMapTileLayer)) {
				continue;
			}
			
			TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;
			TiledMapTileLayer.Cell cell = tileLayer.getCell(x, y);
			
			// if layer is not floor and cell exists in that layer, return false
			if (cell != null && cell.getTile() != null && !tileLayer.getName().equals("Floor")) {
				return false;
			}
		}
		
		// return true if x and y are both over 0 and under 20 in case character flys off screen
		return x > 0 && y > 0 && x < 20 && y < 20;
	}
	
	// sets random direction to go in after checking is walkable
	public void setRandomDirection(TiledMap map) {
		int[] validDirections = new int[4];
		int count = 0;
		
		int currentX = Math.round(sprite.getX());
		int currentY = Math.round(sprite.getY());
		
		for (int i = 0; i < DIRECTIONS.length; i++) {
			// get direction x and y and iterate through each one
			int nextX = currentX + DIRECTIONS[i][0];
			int nextY = currentY + DIRECTIONS[i][1];
			
			//if it is walkable, add to valid directions
			if (isWalkable(map, nextX, nextY)) {
				validDirections[count++] = i;
			}
			
		}
		
		// if no direction is walkable, do not move it to prevent breaking game
		if (count == 0) {
			directionX = 0;
			directionY = 0;
			return;
		}
		
		// select a random valid direction to make the enemy go towards
		int selected = validDirections[random.nextInt(count)];
		
		// set directionX and directionY to seleccted direction
		directionX = DIRECTIONS[selected][0];
		directionY = DIRECTIONS[selected][1];
	}
	
	//checks tile to determine whether to switch directions
	public void checkTile(TiledMap map) {
		int currentX = Math.round(sprite.getX());
		int currentY = Math.round(sprite.getY());
		
		// if the sprite position is between tiles, do not check to change direction. This prevents game breaks
		if (Math.abs(sprite.getX() - currentX) > 0.05f || Math.abs(sprite.getY() - currentY) > 0.05f) {
		        return;
		    }
		
		// check next x and y using directionX and directionY set in setRandomDirection.
		int nextX = currentX + directionX;
		int nextY = currentY + directionY;
		
		// check if next tile is walkable, if not change direction
		if (!isWalkable(map, nextX, nextY)) {
			setRandomDirection(map);
		}
	}
	
	public void checkHitbox(Rectangle playerRec){
		
	}
	
	public void move(float delta) {
		enemyX += speed * delta * directionX;
		enemyY += speed * delta * directionY;
		sprite.setX(MathUtils.clamp(enemyX, minX, maxX));
		sprite.setY(MathUtils.clamp(enemyY, minX, maxX));
	}
	
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}
	
	public void logic(float delta, TiledMap map) {
		rectangle.set(sprite.getX(), sprite.getY(), enemyWidth, enemyHeight);
		checkTile(map);
		move(delta);
	}
}

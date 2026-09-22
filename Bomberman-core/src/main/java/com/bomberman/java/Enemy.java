package com.bomberman.java;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Enemy {
	float enemyWidth;
	float enemyHeight;
	float enemyX;
	float enemyY;
	Rectangle rectangle;
	float speed;
	Texture texture;
	Sprite sprite;
	
	//temporary
	float maxX;
	float minX;
	boolean xAxis;
	
	public Enemy(Texture texture) {
		// max x pos on map based of game units
		maxX = 18;
		minX = 1;
		xAxis = true;
		// setting up sprite size / position
		enemyWidth = 1f;
		enemyHeight = 1f;
		enemyX = 1f;
		enemyY = 10f;
		rectangle = new Rectangle();
		speed = 10f;
		this.texture = texture;
		sprite = new Sprite(texture);
		sprite.setSize(enemyWidth, enemyHeight);
		sprite.setPosition(enemyX, enemyY);
	}
	
	// not used yet
	public void MoveOneTile(TiledMap map, boolean direction, float delta) {
		int nextX = 0;
		int nextY = 0;
		boolean solid = false;
		// if direction true, enemy moves on x axis
		if (direction) {
			// get next x pos to check if rectangle collides, divide by 16 to correlate with tiled map cordinates
			nextX = (int)((sprite.getX() + speed) / 16);
		} else {
			nextY = (int)((sprite.getY() + speed) / 16);
		}
		
		// Access tile layer
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("Tile Layer 1");
		TiledMapTileLayer.Cell cell = layer.getCell(nextX, nextY);
		
		// check if cell / tile is valid
		if (cell != null && cell.getTile() != null) {
			TiledMapTile tile = cell.getTile();
			MapProperties properties = tile.getProperties();
			
			// check if its solid property is true
			try {
				solid = properties.get("solid", Boolean.class);
			} catch (Exception e){
				solid = false;
			}
		}
		
		// game logic if hit a solid wall
		
		if (solid) {
			speed = -speed;
			if (direction) {
				enemyX = sprite.getX() + (speed * delta);
			} else {
				enemyY = sprite.getY() + (speed * delta);
			}
		} else {
			if (direction) {
				enemyX = sprite.getX() + (speed * delta);
			} else {
				enemyY = sprite.getY() + (speed * delta);
			}
		}
		
		sprite.setPosition(enemyX, enemyY);
	}
	
	// move enemy back and forth on x axis
	public void move(float delta) {
		enemyX += speed * delta;
		sprite.setPosition(enemyX, enemyY);
		if (sprite.getX() >= maxX || sprite.getX() <= minX) {
			speed = -speed;
		}
	}
	
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}
	
	public void logic(float delta) {
		// clamp enemy x axis to limit it
		sprite.setX(MathUtils.clamp(sprite.getX(), minX, maxX));
		rectangle.set(sprite.getX(), sprite.getY(), enemyWidth, enemyHeight);
		move(delta);
	}
}

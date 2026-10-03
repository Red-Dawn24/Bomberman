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
	int bombX;
	int bombY;
	boolean bombTimerStart;
	float bombTimer;
	int bombRadius;
	float bombWidth;
	float bombHeight;
	
	Texture texture;
	Sprite sprite;
	
	public Bomb(int x, int y) {
		bombTimer = 0;
		bombRadius = 1;
		bombX = x;
		bombY = y;
		bombWidth = 1;
		bombHeight = 1;
		texture = new Texture("bomb.png");
		sprite = new Sprite(texture);
		sprite.setSize(bombWidth, bombHeight);
		
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
			bombExplode();
			return;
		}
		bombTimer += delta;
		System.out.println(bombTimer);
	}
	
	// for now, it simply sets timer to false, which then gets deleted in gamescreen
	public void bombExplode() {
		bombTimerStart = false;
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

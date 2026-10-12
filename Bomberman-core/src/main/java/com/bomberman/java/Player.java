package com.bomberman.java;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Sprite;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Player{
    Texture image = new Texture(Gdx.files.internal("player.png"));
    //TextureRegion frame1 = new TextureRegion(image, 0,0, 16, 16);

    //This splits the texture for the animation
    TextureRegion[][] frames = TextureRegion.split(image, 16, 16);
    //This defines what the animations are made of
    Animation<TextureRegion> idleRight = new Animation<>(0.2f, frames[0][0], frames[0][1], frames[0][2], frames[0][3]);
    Animation<TextureRegion> idleLeft = new Animation<>(0.2f, frames[0][4], frames[0][5], frames[0][6], frames[0][7]);
    Animation<TextureRegion> idleDown = new Animation<>(0.2f, frames[0][8], frames[0][9], frames[0][10], frames[0][11]);
    Animation<TextureRegion> idleUp = new Animation<>(0.2f, frames[0][12], frames[0][13], frames[0][14], frames[0][15]);
    Animation<TextureRegion> walkRight  = new Animation<>(0.2f, frames[0][16], frames[0][17], frames[0][18], frames[0][19]);
    Animation<TextureRegion> walkLeft = new Animation<>(0.2f, frames[0][20], frames[0][21], frames[0][22], frames[0][23]);
    Animation<TextureRegion> walkDown = new Animation<>(0.2f, frames[0][24], frames[0][25], frames[0][26], frames[0][27]);
    Animation<TextureRegion> walkUp = new Animation<>(0.2f, frames[0][28], frames[0][29], frames[0][30], frames[0][31]);
    Animation<TextureRegion> deadAnimation = new Animation<>(0.5f, frames[0][32], frames[0][33], frames[0][34], frames[0][35]);
         
     Animation<TextureRegion> currentAnimation = idleRight;
    float stateTime = 0f;
    
    //Variables for the player. Pretty obvious what they are gonna do
    float x;
    float y; 
    float speed = 3;
    boolean moving;
    Vector2 direction = new Vector2(x, y);
    String lastDirection = "";
    int bombStorage;
    Sprite sprite;
    
    public Player(int x, int y){
        this.x = x;
        this.y = y;
        bombStorage = 1;
        // we need to state that the animations would loop
        idleRight.setPlayMode(Animation.PlayMode.LOOP);
        idleLeft.setPlayMode(Animation.PlayMode.LOOP);
        idleDown.setPlayMode(Animation.PlayMode.LOOP);
        idleUp.setPlayMode(Animation.PlayMode.LOOP);
        walkUp.setPlayMode(Animation.PlayMode.LOOP);
        walkDown.setPlayMode(Animation.PlayMode.LOOP);
        walkLeft.setPlayMode(Animation.PlayMode.LOOP);
        walkRight.setPlayMode(Animation.PlayMode.LOOP);
        deadAnimation.setPlayMode(Animation.PlayMode.NORMAL);
        sprite = new Sprite(image);
        sprite.setSize(1f,1f);
        sprite.setPosition(x, y);
        
    }

    // public void initialize(){
    //     TextureRegion[][] temp = TextureRegion.split(image, image.getWidth()/FRAME_COLS, image.getHeight()/FRAME_ROWS);
    //     int index = 0;

    // }
	public boolean isWalkable(TiledMap map, float x, float y) {
		for (MapLayer layer : map.getLayers()) {
			if (!(layer instanceof TiledMapTileLayer)) {
				continue;
			}
            
            TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;
			TiledMapTileLayer.Cell cell = tileLayer.getCell(Math.round(x + 0.3f*direction.x), Math.round(y+ 0.3f*direction.y));
			
			// if layer is not floor and cell exists in that layer, return false
			if (cell != null && cell.getTile() != null && !tileLayer.getName().equals("Floor")) {
				return false;
			}
		}
		
		// return true if x and y are both over 0 and under 20 in case character flys off screen
		return x > 0 && y > 0 && x < 20 && y < 20;
	}
    
    public void update(float delta, TiledMap map){
        moving = false;

        // input with up and down being superior
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            direction.x = -1;
            currentAnimation = walkLeft;
            lastDirection = "left";
            moving = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            direction.x = 1;
            currentAnimation = walkRight;
            lastDirection = "right";
            moving = true;
        } else {
            direction.x = 0;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            direction.y = 1;
            currentAnimation = walkUp;
            lastDirection = "up";
            moving = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            direction.y = -1;
            currentAnimation = walkDown;
            lastDirection = "down";
            moving = true;
        } else {
            direction.y = 0;
        }
        //Makes player idle animation
        if (!moving){
            if (lastDirection.equals("left")){
                currentAnimation = idleLeft;
            } else if (lastDirection.equals("right")){
                currentAnimation = idleRight;
            } else if (lastDirection.equals("up")){
                currentAnimation = idleUp;
            } else if (lastDirection.equals("down")){
                currentAnimation = idleDown;
            }
        }
        if (direction.x != 0 || direction.y != 0){
            direction = direction.nor(); //Heh normalize, I did this manually on my other code
        }

        stateTime += delta;
        
        float nextX = x + direction.x * speed * delta;
        float nextY = y + direction.y * speed * delta;

        if (isWalkable(map, nextX, nextY)){
            x = nextX;
            y = nextY;
        }
        


    } 
    public void render(SpriteBatch batch){
        //Pretty much tells batch to use the currect frame with stateTime telling the code how long the animation was running
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime);
        batch.draw(currentFrame, x, y, 1f, 1f);
    }
}
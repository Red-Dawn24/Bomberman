package com.bomberman.java;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Player{
    Texture image = new Texture(Gdx.files.internal("player.png"));
    //TextureRegion frame1 = new TextureRegion(image, 0,0, 16, 16);

    //This splits the texture for the animation
    TextureRegion[][] frames = TextureRegion.split(image, 16, 16);
    //This defines what the animations are made of
    Animation<TextureRegion> idleRight = new Animation<>(0.2f, frames[0][0], frames[0][1], frames[0][2], frames[0][3]);
    Animation<TextureRegion> idleLeft = new Animation<>(0.2f, frames[0][4], frames[0][5], frames[0][6], frames[0][7]);
    Animation<TextureRegion> walkDown = new Animation<>(0.2f, frames[0][8], frames[0][9], frames[0][10], frames[0][11]);
    Animation<TextureRegion> walkUp = new Animation<>(0.2f, frames[0][12], frames[0][13], frames[0][14], frames[0][15]);
    Animation<TextureRegion> walkRight  = new Animation<>(0.2f, frames[0][16], frames[0][17], frames[0][18], frames[0][19]);
    Animation<TextureRegion> walkLeft = new Animation<>(0.2f, frames[0][20], frames[0][21], frames[0][22], frames[0][23]);
        
    Animation<TextureRegion> currentAnimation = idleRight;
    float stateTime = 0f;
    
    //Variables for the player. Pretty obvious what they are gonna do
    float x;
    float y; 
    float speed = 3;
    boolean moving;
    String lastDirection = "";
    
    public Player(int x, int y){
        this.x = x;
        this.y = y;
        // we need to state that the animations would loop
        idleRight.setPlayMode(Animation.PlayMode.LOOP);
        idleLeft.setPlayMode(Animation.PlayMode.LOOP);
        walkUp.setPlayMode(Animation.PlayMode.LOOP);
        walkDown.setPlayMode(Animation.PlayMode.LOOP);
        walkLeft.setPlayMode(Animation.PlayMode.LOOP);
        walkRight.setPlayMode(Animation.PlayMode.LOOP);
    }

    // public void initialize(){
    //     TextureRegion[][] temp = TextureRegion.split(image, image.getWidth()/FRAME_COLS, image.getHeight()/FRAME_ROWS);
    //     int index = 0;

    // }

    public void update(float delta){
        moving = false;

        // input with up and down being superior
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.x += -1 * this.speed * delta;
            currentAnimation = walkLeft;
            lastDirection = "left";
            moving = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.x += 1 * this.speed * delta;
            currentAnimation = walkRight;
            lastDirection = "right";
            moving = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            this.y += 1 * this.speed * delta;
            currentAnimation = walkUp;
            moving = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            this.y += -1 * this.speed * delta;
            currentAnimation = walkDown;
            moving = true;
        }

        //Makes player idle animation
        if (!moving){
            //Sick ternary operator. My personal favorite way to define a variable without the big if
            currentAnimation = (lastDirection.equals("right")) ? idleRight : idleLeft;
        }
        stateTime += delta;

    } 
    public void render(SpriteBatch batch){
        //Pretty much tells batch to use the currect frame with stateTime telling the code how long the animation was running
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime);
        batch.draw(currentFrame, x, y, 1f, 1f);
    }
}
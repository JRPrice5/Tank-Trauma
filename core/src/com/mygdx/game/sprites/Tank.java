package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import java.util.Random;

public class Tank {
    private Vector3 position;
    private Vector3 velocity;
    private int movementSpeed;
    private float rotation;
    private int rotationSpeed;
    private float normalisedRotation;
    private int directionX;
    private int directionY;
    private Texture tank;
    private Texture turret;
    
    public Tank(int x, int y) {
        Random rand = new Random();
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);
        movementSpeed = 100;
        rotation = 0; 
        rotationSpeed = 2;
        tank = new Texture("tankBody_green.png");
        turret = new Texture("gameover.png");
    }
    
    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Keys.A)) {
            rotation -= rotationSpeed;
        }
        if (Gdx.input.isKeyPressed(Keys.D)) {
            rotation += rotationSpeed;
        }
        if (rotation > 359) {
            rotation -= 360;
        } else if (rotation < 0) {
            rotation += 360;
        }
        if (rotation < 90) {
            normalisedRotation = (float) Math.toRadians(rotation);
            directionX = 1;
            directionY = 1;
        } else if (rotation < 180) {
            normalisedRotation = (float) Math.toRadians(180 - rotation); 
            directionX = 1;
            directionY = -1;
        } else if (rotation < 270) {
            normalisedRotation = (float) Math.toRadians(rotation - 180);
            directionX = -1;
            directionY = -1;
        } else if (rotation < 360) {
            normalisedRotation = (float) Math.toRadians(360 - rotation);
            directionX = -1;
            directionY = 1;                   
        }
        if (Gdx.input.isKeyPressed(Keys.W)) {
            velocity.add(((float) (-directionX * movementSpeed * java.lang.Math.sin(normalisedRotation))), ((float) (-directionY * movementSpeed * java.lang.Math.cos(normalisedRotation))), 0);
        }
        if (Gdx.input.isKeyPressed(Keys.S)) {
            velocity.add((float) (directionX * movementSpeed * java.lang.Math.sin(normalisedRotation)), (float) (directionY * movementSpeed * java.lang.Math.cos(normalisedRotation)), 0);            
        }
        velocity.scl(dt);
        position.add(velocity.x, velocity.y, 0);
        if (position.y < 0)
            position.y = 0;
        if (position.x < 0)
            position.x = 0;
        velocity.scl(1/dt);
        velocity.scl(dt);
    }
    
    public Vector3 getPosition() {
        return position;
    }

    public Texture getTexture() {
        return tank;
    }
    
    public float getRotation() {
        return rotation;
    }
}

package com.mygdx.game.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class Bullet {
    private Texture texture;
    private Vector3 position;
    private int rotation;
    private int speed;
    private Vector3 velocity;
    private int lifeSpan;
    private int timeAlive;

    public Bullet(float x, float y, int rotation, String colour, float normaliserX, float normaliserY) {
        texture = new Texture("bullet"
                +colour.substring(0,1).toUpperCase()
                +colour.substring(1)
                +"1.png");
        position = new Vector3(x, y, 0);
        this.rotation = rotation;
        speed = 140;
        velocity = new Vector3(normaliserX * speed, normaliserY * speed, 0);
        lifeSpan = 15;
        timeAlive = 0;
    }
    
    public void update(float dt, int rotation) {
        this.rotation = rotation;
        velocity.scl(dt);
        position.add(velocity.x, velocity.y, 0);
        velocity.scl(1/dt);
        velocity.scl(dt);
        
        timeAlive += (dt/1000);
        if (timeAlive >= lifeSpan) {
            texture.dispose();
        }
    }
    
    public Texture getTexture() {
        return texture;
    }
    
    public Vector3 getPosition() {
        return position;
    }
    
    public int getRotation() {
        return rotation;
    }
    
    public Vector3 getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocityX, float velocityY) {
        velocity = new Vector3(velocityX, velocityY, 0);
    }
    
    public int getSpeed() {
        return speed;
    }

    public int getLifeSpan() {
        return lifeSpan;
    }

    public int getTimeAlive() {
        return timeAlive;
    }
}

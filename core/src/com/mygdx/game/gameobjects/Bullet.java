package com.mygdx.game.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class Bullet {
    private Texture texture;
    private Vector3 position;
    private float rotation;
    private int speed;
    private Vector3 velocity;
    private float lifeSpan;

    public Bullet(float x, float y, String colour, float rotation, float normaliserX, float normaliserY) {
        texture = new Texture("bullet"+colour+".png");
        position = new Vector3(x, y, 0);
        this.rotation = rotation;
        speed = 400;
        velocity = new Vector3(normaliserX * speed, normaliserY * speed, 0);
        lifeSpan = 8;
    }
    
    public void update(float dt) {
        velocity.scl(dt);
        position.add(velocity.x, velocity.y, 0);
        velocity.scl(1/dt);
        lifeSpan -= dt;
    }
    
    public void dispose() {
        texture.dispose();
    }
    
    public Texture getTexture() {
        return texture;
    }
    
    public Vector3 getPosition() {
        return position;
    }
    
    public float getRotation() {
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

    public float getLifeSpan() {
        return lifeSpan;
    }
}

package com.mygdx.game.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;

public class TankBody {
    private Texture texture;
    private float rotationSpeed;
    private Body body;

    public TankBody(int x, int y, String colour, Body body) {
        this.texture = new Texture("tankBody_"+colour+".png");
        this.rotationSpeed = 1.7f;
    }
    
    public void dispose() {
        texture.dispose();
    }

    public Body getBody() {
        return body;
    }
    
    public float getRotationSpeed() {
        return rotationSpeed;
    }
    
    public Texture getTexture() {
        return texture;
    }
}

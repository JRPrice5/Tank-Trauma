package com.mygdx.game.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class TankBody {
    private Texture texture;
    private Vector3 position;
    private int rotation;
    private int rotationSpeed;

    public TankBody(int x, int y, String colour) {
        this.texture = new Texture("tankBody_"+colour+".png");
        this.position = new Vector3(x, y, 0);
        this.rotation = 0;
        this.rotationSpeed = 2;

    }

    public Vector3 getPosition() {
        return position;
    }
    
    public void appendPosition(float x, float y) {
        position.add(x, y, 0);
    }
    
    public void setPositionX(float value) {
        position.x = value;
    }
    
    public void setPositionY(float value) {
        position.y = value;
    }
    
    public int getRotation() {
        return rotation;
    }

    public void appendRotation(int value) {
        rotation += value;
    }
    
    public int getRotationSpeed() {
        return rotationSpeed;
    }

    public Texture getTexture() {
        return texture;
    }
}

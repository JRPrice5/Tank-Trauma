package com.mygdx.game.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class TankBody {
    private Texture texture;
    private Vector3 position;
    private float rotation;
    private float resolvedRotation;
    private float rotationSpeed;
    private byte directionX;
    private byte directionY;

    public TankBody(int x, int y, String colour) {
        this.texture = new Texture("tankBody_"+colour+".png");
        this.position = new Vector3(x, y, 0);
        this.rotation = 0;
        this.rotationSpeed = 1.5f;
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
    
    public float getRotation() {
        return rotation;
    }

    public void appendRotation(float value) {
        rotation += value;
    }

    public float getResolvedRotation() {
        return resolvedRotation;
    }

    public void setResolvedRotation(float resolvedRotation) {
        this.resolvedRotation = resolvedRotation;
    }
    
    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public Texture getTexture() {
        return texture;
    }

    public byte getDirectionX() {
        return directionX;
    }

    public void setDirectionX(byte directionX) {
        this.directionX = directionX;
    }

    public byte getDirectionY() {
        return directionY;
    }

    public void setDirectionY(byte directionY) {
        this.directionY = directionY;
    }
    
}

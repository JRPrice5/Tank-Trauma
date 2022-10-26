package com.mygdx.game.prefabs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class TankTurret {
    private Texture texture;
    private Vector3 position;
    private int rotation;
    private int rotationSpeed;

    public TankTurret(Texture body, int x, int y, String colour) {
        this.texture = new Texture("tank"+colour+"_barrel1.png");
        this.position = new Vector3(x + (body.getWidth() - texture.getWidth()) / 2, y - 1, 0);
        this.rotation = 0;
        this.rotationSpeed = 3;
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

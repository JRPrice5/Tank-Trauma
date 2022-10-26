package com.mygdx.game.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import java.util.LinkedList;

public class TankTurret {
    private Texture texture;
    private Vector3 position;
    private int rotation;
    private int rotationSpeed;
    private String colour;
    private boolean onShoot;
    private LinkedList<Bullet> bullets;
    private float reload;

    public TankTurret(Texture body, int x, int y, String colour) {
        this.colour = colour;
        texture = new Texture("tank"+colour+"_barrel1.png");
        position = new Vector3(x + (body.getWidth() - texture.getWidth()) / 2, y - 1, 0);
        rotation = 0;
        rotationSpeed = 3;
        bullets = new LinkedList();
        reload = 0;
    }
    
    public void shoot(float normaliserX, float normaliserY) {
        if (reload <= 0) {
            Bullet bullet = new Bullet(position.x + ((texture.getWidth() - (new Texture("bulletGreen1.png").getWidth())) / 2), position.y + 8, rotation, colour, normaliserX, normaliserY);
            bullets.add(bullet);
            reload = 1;
        }
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
    
    public LinkedList<Bullet> getBullets() {
        return bullets;
    }
    
    public float getReload() {
        return reload;
    }
    
    public void reduceReload(float value) {
        reload -= value;
    }
    
    public void setReload(float value) {
        reload = value;
    }
}

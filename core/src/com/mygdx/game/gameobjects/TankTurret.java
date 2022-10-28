package com.mygdx.game.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import java.util.LinkedList;

public class TankTurret {
    private static final int BARREL_LENGTH = 34;
    private Texture turretTexture;
    private Texture bulletTexture;
    private Vector3 position;
    private Vector3 barrel;
    private float rotation;
    private float resolvedRotation;
    private float rotationSpeed;
    private byte turretDirectionX;
    private byte turretDirectionY;
    private String colour;
    private LinkedList<Bullet> bullets;
    private float reload;
    private byte barrelDirectionX;
    private byte barrelDirectionY;

    public TankTurret(Texture body, int x, int y, String colour) {
        this.colour = colour;
        turretTexture = new Texture("tank"+colour+"_barrel1.png");
        bulletTexture = new Texture("bulletGreen1.png");
        position = new Vector3(x + (body.getWidth() - turretTexture.getWidth()) / 2, y - 1, 0);
        barrel = new Vector3((position.x + (turretTexture.getWidth() / 2)), position.y + 44, 0);
        rotation = 0;
        rotationSpeed = 1.5f;
        bullets = new LinkedList();
        reload = 0;
    }
    
    public void update(float dt) {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.update(dt);
            if (bullet.getLifeSpan() <= 0) {
                bullet.dispose();
                bullets.remove(i);
            }
        }
        if (reload > 0) {
            reduceReload(dt);
        }
        
        // resolved rotation is causing an issue
        float distanceX = (float) ((turretDirectionX * BARREL_LENGTH * java.lang.Math.sin(resolvedRotation))
                + position.x + (turretTexture.getWidth() / 2) - barrel.x);
        float distanceY = (float) ((turretDirectionY * BARREL_LENGTH * java.lang.Math.cos(resolvedRotation))
                + position.y + 44 - barrel.y);
        barrel.add(distanceX, distanceY, 0);
    }
    
    public void shoot(float normaliserX, float normaliserY) {
        if (rotation < 45) {
            setBarrelDirectionX((byte) -1);
            setBarrelDirectionY((byte) 1);
        } else if (rotation < 90) {
            setBarrelDirectionX((byte) 1);
            setBarrelDirectionY((byte) -1);
        } else if (rotation < 180) {
            setBarrelDirectionX((byte) -1);
            setBarrelDirectionY((byte) -1);           
        } else if (rotation < 225) {
            setBarrelDirectionX((byte) -1);
            setBarrelDirectionY((byte) 1);
        } else if (rotation < 270) {
            setBarrelDirectionX((byte) 1);
            setBarrelDirectionY((byte) -1);
        } else if (rotation < 360) {
            setBarrelDirectionX((byte) -1);
            setBarrelDirectionY((byte) -1);
        } 
        
        if (reload <= 0) {
            Bullet bullet = new Bullet(
                    (float) (barrel.x
                            - (bulletTexture.getWidth() * java.lang.Math.cos(resolvedRotation) / 2)
                            + (turretDirectionX * bulletTexture.getHeight() * java.lang.Math.sin(resolvedRotation) / 2)),
                    (float) (barrel.y
                            - (bulletTexture.getWidth() * java.lang.Math.sin(resolvedRotation))
                            + (turretDirectionY * bulletTexture.getHeight() * java.lang.Math.cos(resolvedRotation) / 2)),
                    rotation,
                    colour,
                    normaliserX,
                    normaliserY);
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
    
    public Vector3 getBarrel() {
        return barrel;
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

    public Texture getTurretTexture() {
        return turretTexture;
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

    public byte getTurretDirectionX() {
        return turretDirectionX;
    }

    public void setTurretDirectionX(byte turretDirectionX) {
        this.turretDirectionX = turretDirectionX;
    }

    public byte getTurretDirectionY() {
        return turretDirectionY;
    }

    public void setTurretDirectionY(byte turretDirectionY) {
        this.turretDirectionY = turretDirectionY;
    }

    public byte getBarrelDirectionX() {
        return barrelDirectionX;
    }

    public void setBarrelDirectionX(byte barrelDirectionX) {
        this.barrelDirectionX = barrelDirectionX;
    }

    public byte getBarrelDirectionY() {
        return barrelDirectionY;
    }

    public void setBarrelDirectionY(byte barrelDirectionY) {
        this.barrelDirectionY = barrelDirectionY;
    }
}

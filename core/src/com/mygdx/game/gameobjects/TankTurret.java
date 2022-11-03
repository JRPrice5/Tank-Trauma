package com.mygdx.game.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import java.util.LinkedList;

public class TankTurret {
    private int barrelLength;
    private String colour;
    private String turretFile;
    private Texture texture;
    private Texture bulletTexture;
    private Vector3 position;
    private Vector3 barrel;
    private float rotation;
    private float resolvedRotation;
    private float rotationSpeed;
    private byte turretDirectionX;
    private byte turretDirectionY;
    private LinkedList<Bullet> bullets;
    private float reload;
    private byte barrelAdjustmentX;
    private byte barrelAdjustmentY;

    public TankTurret(Texture body, int x, int y, String colour) {
        barrelLength = 44;
        this.colour = colour;
        turretFile = "tank"+colour+"_barrel1.png";
        texture = new Texture(turretFile);
        bulletTexture = new Texture("bulletDark1.png");
        position = new Vector3(x + (body.getWidth() - texture.getWidth()) / 2, y - 1, 0);
        barrel = new Vector3((position.x + (texture.getWidth() / 2)), position.y + barrelLength, 0);
        rotation = 0;
        rotationSpeed = 1.2f;
        bullets = new LinkedList();
        reload = 0;
    }
    
    public void update(float dt) {
//        if () {
//            
//        }
        
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
        float distanceX = (float) ((turretDirectionX * barrelLength * java.lang.Math.sin(resolvedRotation))
                + position.x + (texture.getWidth() / 2) - barrel.x);
        float distanceY = (float) ((turretDirectionY * barrelLength * java.lang.Math.cos(resolvedRotation))
                + position.y + barrelLength - barrel.y);
        barrel.add(distanceX, distanceY, 0);
    }
    
    public void shoot(float normaliserX, float normaliserY) {
        if (rotation < 90) {
            setBarrelAdjustmentX((byte) -1);
            setBarrelAdjustmentY((byte) 1);
        } else if (rotation < 180) {
            setBarrelAdjustmentX((byte) 1);
            setBarrelAdjustmentY((byte) 1);           
        } else if (rotation < 270) {
            setBarrelAdjustmentX((byte) 1);
            setBarrelAdjustmentY((byte) -1);
        } else if (rotation < 360) {
            setBarrelAdjustmentX((byte) -1);
            setBarrelAdjustmentY((byte) -1);
        } 
        
        if (reload <= 0) {
            Bullet bullet = new Bullet(
                    (float) (barrel.x
                            + (barrelAdjustmentX * java.lang.Math.cos(resolvedRotation) * bulletTexture.getWidth() / 2)
                            - (turretDirectionX * java.lang.Math.sin(resolvedRotation) * bulletTexture.getHeight() / 2)),
                    (float) (barrel.y
                            + (barrelAdjustmentY * java.lang.Math.sin(resolvedRotation) * bulletTexture.getWidth() / 2)
                            - (turretDirectionY * java.lang.Math.cos(resolvedRotation) * bulletTexture.getHeight() / 2)),
                    rotation,
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

    public byte getBarrelAdjustmentX() {
        return barrelAdjustmentX;
    }

    public void setBarrelAdjustmentX(byte barrelAdjustmentX) {
        this.barrelAdjustmentX = barrelAdjustmentX;
    }

    public byte getBarrelAdjustmentY() {
        return barrelAdjustmentY;
    }

    public void setBarrelAdjustmentY(byte barrelAdjustmentY) {
        this.barrelAdjustmentY = barrelAdjustmentY;
    }
    
    public int getBarrelLength() {
        return barrelLength;
    }
}

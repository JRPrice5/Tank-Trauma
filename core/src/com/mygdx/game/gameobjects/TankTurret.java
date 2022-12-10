package com.mygdx.game.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import java.util.LinkedList;

public class TankTurret {
    private String colour;
    private String turret;
    private Texture texture;
    private float barrelLength;
    private Texture bulletTexture;
    private Vector2 turretPosition;
    private Vector2 barrelPosition;
    private float rotation;
    private float resolvedRotation;
    private float rotationSpeed;
    private byte turretDirectionX;
    private byte turretDirectionY;
    private LinkedList<Bullet> bullets;
    private float reload;
    private byte barrelAdjustmentX;
    private byte barrelAdjustmentY;

    public TankTurret(Texture body, String colour, Body physicsBody) {
        this.colour = colour;
        turret = "tank"+colour+"_barrel.png";
        texture = new Texture(turret);
        barrelLength = 44;
        bulletTexture = new Texture("bullet"+colour+".png");
        turretPosition = new Vector2(physicsBody.getWorldCenter().x - texture.getWidth() / 2, physicsBody.getWorldCenter().y - 13);
        barrelPosition = new Vector2(turretPosition.x + texture.getWidth() / 2, turretPosition.y + barrelLength - 13);
        rotation = 0;
        rotationSpeed = 1.3f;
        bullets = new LinkedList();
        reload = 0;
    }
    
    public void update(float dt) {
        if (turret.contains("_barrel")) {
            barrelLength = 44;
        } else if (turret.contains("specialBarrel1") 
                || turret.contains("specialBarrel2")) {
            barrelLength = 36;
        } else if (turret.contains("specialBarrel3")) {
            barrelLength = 46;
        } else if (turret.contains("specialBarrel4")) {
            barrelLength = 57;
        } else if (turret.contains("specialBarrel5") 
                || turret.contains("specialBarrel6") 
                || turret.contains("specialBarrel7")) {
            barrelLength = 47;
        } 
        
        // try to remove this update loop
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

        float distanceX = (float) ((turretDirectionX * barrelLength * java.lang.Math.sin(resolvedRotation))
                + turretPosition.x - texture.getWidth() + 2 - barrelPosition.x);
        float distanceY = (float) ((turretDirectionY * barrelLength * java.lang.Math.cos(resolvedRotation))
                + turretPosition.y - barrelPosition.y);
        barrelPosition.add(distanceX, distanceY);
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
                    (float) (barrelPosition.x
                            + (barrelAdjustmentX * java.lang.Math.cos(resolvedRotation) * bulletTexture.getWidth() / 2)
                            - (turretDirectionX * java.lang.Math.sin(resolvedRotation) * bulletTexture.getHeight() / 2)),
                    (float) (barrelPosition.y
                            + (barrelAdjustmentY * java.lang.Math.sin(resolvedRotation) * bulletTexture.getWidth() / 2)
                            - (turretDirectionY * java.lang.Math.cos(resolvedRotation) * bulletTexture.getHeight() / 2)), 
                    colour,
                    rotation,
                    normaliserX,
                    normaliserY);
            bullets.add(bullet);
            reload = 1;
        }
    }
    
    public void dispose() {
        texture.dispose();
    }

    public Vector2 getTurretPosition() {
        return turretPosition;
    }
    
    public void appendPosition(float x, float y) {
        turretPosition.add(x, y);
    }
    
    public void setTurretPosition(Vector2 newPosition) {
        turretPosition = newPosition;
    }
    
    public Vector2 getBarrelPosition() {
        return barrelPosition;
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
    
    public float getBarrelLength() {
        return barrelLength;
    }
    
    public Texture getBulletTexture() {
        return bulletTexture;
    }
}

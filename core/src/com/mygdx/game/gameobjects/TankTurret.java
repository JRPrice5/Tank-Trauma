package com.mygdx.game.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import java.util.LinkedList;
import com.badlogic.gdx.physics.box2d.World;

public class TankTurret {
    private final int BARREL_OFFSET = 3;
    private World world;
    private String colour;
    private String turret;
    private Texture texture;
    private float BARREL_LENGTH;
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

    public TankTurret(Texture body, String colour, Body tankRigidBody, World world) {
        this.world = world;
        this.colour = colour;
        turret = "tank"+colour+"_barrel.png";
        texture = new Texture(turret);
        BARREL_LENGTH = 44;
        bulletTexture = new Texture("cannonBall"+colour+"Small.png");
        turretPosition = new Vector2(tankRigidBody.getWorldCenter().x, tankRigidBody.getWorldCenter().y);
        barrelPosition = new Vector2(turretPosition.x, turretPosition.y + BARREL_LENGTH);
        rotation = 0;
        rotationSpeed = 1.3f;
        bullets = new LinkedList();
        reload = 0;
    }
    
    public void update(float dt) {
        if (turret.contains("_barrel")) {
            BARREL_LENGTH = 44;
        } else if (turret.contains("specialBarrel1") 
                || turret.contains("specialBarrel2")) {
            BARREL_LENGTH = 36;
        } else if (turret.contains("specialBarrel3")) {
            BARREL_LENGTH = 46;
        } else if (turret.contains("specialBarrel4")) {
            BARREL_LENGTH = 57;
        } else if (turret.contains("specialBarrel5") 
                || turret.contains("specialBarrel6") 
                || turret.contains("specialBarrel7")) {
            BARREL_LENGTH = 47;
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

        float distanceX = (float) ((turretDirectionX * BARREL_LENGTH * java.lang.Math.sin(resolvedRotation))
                - barrelPosition.x + turretPosition.x);
        float distanceY = (float) ((turretDirectionY * BARREL_LENGTH * java.lang.Math.cos(resolvedRotation))
                - barrelPosition.y + turretPosition.y - BARREL_OFFSET);
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
                    normaliserY,
                    world);
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
        return BARREL_LENGTH;
    }
    
    public Texture getBulletTexture() {
        return bulletTexture;
    }
}

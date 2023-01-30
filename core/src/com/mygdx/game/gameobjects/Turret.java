package com.mygdx.game.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import java.util.LinkedList;
import com.badlogic.gdx.physics.box2d.World;
import static com.mygdx.game.screens.PlayScreen.UNIT_SCALE;

public class Turret {
    private final short BARREL_OFFSET = 3;
    private short barrelLength = 44;
    private World world;
    private String colour;
    private Body tankRigidBody;
    private String turret;
    private Texture texture;
    private Texture bulletTexture;
    private Vector2 barrelPosition;
    private Body rigidBody;
    private float rotation;
    private float resolvedRotation;
    private float rotationSpeed;
    private byte directionX;
    private byte directionY;
    private Vector2 direction;
    private LinkedList<Projectile> projectiles;
    private float reloadTime;
    private byte barrelAdjustmentX;
    private byte barrelAdjustmentY;

    public Turret(Texture body, String colour, Body tankRigidBody, World world) {
        this.world = world;
        this.colour = colour;
        this.tankRigidBody = tankRigidBody;
        turret = "tank"+colour+"_barrel.png";
        texture = new Texture(turret);
        bulletTexture = new Texture("cannonBall"+colour+"Small.png");
        barrelPosition = new Vector2(tankRigidBody.getWorldCenter().x / UNIT_SCALE, tankRigidBody.getWorldCenter().y / UNIT_SCALE + barrelLength);
        rotation = 0;
        direction = new Vector2((float)java.lang.Math.sin(Math.toRadians(rotation)), (float)java.lang.Math.cos(Math.toRadians(rotation)));
        reloadTime = 0;
        projectiles = new LinkedList<>();
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
        
        rotation = (float) Math.toDegrees(tankRigidBody.getAngle());

        direction.x = (float)java.lang.Math.sin(Math.toRadians(rotation));
        direction.y = (float)java.lang.Math.cos(Math.toRadians(rotation));
        
        // try to remove this update loop
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile projectile = projectiles.get(i);
            projectile.update(dt);
            if (projectile.getLifeSpan() <= 0) {
                projectile.dispose();
                projectiles.remove(i);
            }
        }
        if (reloadTime > 0) {
            reduceReloadTime(dt);
        }

        float distanceX = (float) ((directionX * barrelLength * java.lang.Math.sin(resolvedRotation))
                - barrelPosition.x + tankRigidBody.getWorldCenter().x / UNIT_SCALE);
        float distanceY = (float) ((directionY * barrelLength * java.lang.Math.cos(resolvedRotation))
                - barrelPosition.y + tankRigidBody.getWorldCenter().y / UNIT_SCALE - BARREL_OFFSET);
        barrelPosition.add(distanceX, distanceY);
    }
    
    public void shoot() {
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
        
        if (reloadTime <= 0) {
            CannonBall cannonBall = new CannonBall(
                    (float) (barrelPosition.x
//                            + (barrelAdjustmentX * java.lang.Math.cos(resolvedRotation) * bulletTexture.getWidth() / 2)
                            - (directionX * java.lang.Math.sin(resolvedRotation) * 3 * bulletTexture.getHeight() / 8)),
                    (float) (barrelPosition.y
//                            + (barrelAdjustmentY * java.lang.Math.sin(resolvedRotation) * bulletTexture.getWidth() / 2)
                            - (directionY * java.lang.Math.cos(resolvedRotation) * 3 * bulletTexture.getHeight() / 8)), 
                    colour,
                    rotation,
                    resolvedRotation,
                    world,
                    barrelAdjustmentX,
                    barrelAdjustmentY,
                    directionX,
                    directionY,
                    direction);
            projectiles.add(cannonBall);
            reloadTime = 1;
        }
    }
    
    public void dispose() {
        texture.dispose();
    }

    public Vector2 getBarrelPosition() {
        return barrelPosition;
    }
    
    public float getRotation() {
        return rotation;
    }
    
    public void appendRotation(float angle) {
        rotation += angle;
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
    
    public LinkedList<Projectile> getProjectiles() {
        return projectiles;
    }
    
    public float getReloadTime() {
        return reloadTime;
    }
    
    public void reduceReloadTime(float value) {
        reloadTime -= value;
    }
    
    public void setReloadTime(float value) {
        reloadTime = value;
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
    
    public short getBarrelLength() {
        return barrelLength;
    }
    
    public Texture getBulletTexture() {
        return bulletTexture;
    }
}

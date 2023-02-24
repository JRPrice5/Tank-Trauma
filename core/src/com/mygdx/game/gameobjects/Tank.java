package com.mygdx.game.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import static com.mygdx.game.screens.GameScreen.UNIT_SCALE;
import java.util.LinkedList;


public class Tank {
    private final short BARREL_OFFSET = 3;
    private short barrelLength = 44;
    private Body rigidBody;
    private int forwardSpeed;
    private int backwardSpeed;
    private Vector2 direction;
    private float rotation;
    private boolean isAlive;
    private String colour;
    private Texture bodyTexture;
    private Texture turretTexture;
    private Texture bulletTexture;
    private float rotationSpeed;
    private World world;
    private Vector2 barrelPosition;
    private float reloadTime;
    private LinkedList projectiles;
    private byte directionX;
    private byte directionY;
    private float resolvedRotation;
    
    public Tank(String colour, Body rigidBody, World world) {
        this.rigidBody = rigidBody;
        rigidBody.getFixtureList().get(2).setUserData(colour);
        forwardSpeed = 160;
        backwardSpeed = 120;
        direction = new Vector2(rigidBody.getLocalVector(rigidBody.getLocalCenter()).x, -rigidBody.getLocalVector(rigidBody.getLocalCenter()).y);
        isAlive = true;
        this.colour = colour;
        bodyTexture = new Texture("tankBody_"+colour+".png");
        turretTexture = new Texture("tank"+colour+"_barrel.png");
        bulletTexture = new Texture("cannonBall"+colour+"Small.png");
        rotationSpeed = 2f;
        this.world = world;
        barrelPosition = new Vector2(rigidBody.getWorldCenter().x / UNIT_SCALE, rigidBody.getWorldCenter().y / UNIT_SCALE + barrelLength);
        reloadTime = 0;
        projectiles = new LinkedList<>();
    }
    
    public void update(float dt) {
        if (isAlive) {
            direction.x = rigidBody.getLocalVector(rigidBody.getLocalCenter()).x;
            direction.y = -rigidBody.getLocalVector(rigidBody.getLocalCenter()).y;
            rotation = (float) Math.toDegrees(rigidBody.getAngle());
            // try to remove this update loop
            for (int i = 0; i < projectiles.size(); i++) {
                Projectile projectile = (Projectile) projectiles.get(i);
                projectile.update(dt);
                if (projectile.getLifeSpan() <= 0) {
                    projectile.dispose();
                    projectiles.remove(i);
                }
            }
            
            if (reloadTime > 0) {
                reloadTime -= dt;
            }

            float distanceX = (float) ((barrelLength * -java.lang.Math.sin(Math.toRadians(rotation)))
                    - barrelPosition.x + rigidBody.getWorldCenter().x / UNIT_SCALE);
            float distanceY = (float) ((barrelLength * java.lang.Math.cos(Math.toRadians(rotation)))
                    - barrelPosition.y + rigidBody.getWorldCenter().y / UNIT_SCALE - BARREL_OFFSET);
            barrelPosition.add(distanceX, distanceY);
        } else {
            rigidBody.setTransform(-10, -10, 0);
        }
    }
    
    public void shoot() {
        if (reloadTime <= 0) {
            CannonBall cannonBall = new CannonBall(
                    (float) (barrelPosition.x
                            - (directionX * java.lang.Math.sin(resolvedRotation) * BARREL_OFFSET * bulletTexture.getHeight() / 8)),
                    (float) (barrelPosition.y
                            - (directionY * java.lang.Math.cos(resolvedRotation) * BARREL_OFFSET * bulletTexture.getHeight() / 8)), 
                    colour,
                    rotation,
                    world,
                    direction,
                    rigidBody);
            projectiles.add(cannonBall);
            reloadTime = 1;
        }
    }
    
    public void dispose() {
        rigidBody.setActive(false);
        rigidBody.getFixtureList().clear();
    }
    
    public int getForwardSpeed() {
        return forwardSpeed;
    }

    public int getBackwardSpeed() {
        return backwardSpeed;
    }
    
    public Vector2 getDirection() {
        return direction;
    }
    
    public boolean getIsAlive() {
        return isAlive;
    }
    
    public void setIsAlive(boolean value) {
        isAlive = value;
    }
    
    public Body getRigidBody() {
        return rigidBody;
    }
    
    public String getColour() {
        return colour;
    }
    
    public float getReloadTime() {
        return reloadTime;
    } 
    
    public void setReloadTime(float value) {
        reloadTime = value;
    }
    
    public Texture getBodyTexture() {
        return bodyTexture;
    }
    
    public float getRotationSpeed() {
        return rotationSpeed;
    }
    
    public short getBarrelLength() {
        return barrelLength;
    }
    
    public Texture getBulletTexture() {
        return bulletTexture;
    }

    public Texture getTurretTexture() {
        return turretTexture;
    }

    public LinkedList getProjectiles() {
        return projectiles;
    }
}

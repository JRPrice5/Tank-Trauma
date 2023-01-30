package com.mygdx.game.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;


public class Tank {
    private Body rigidBody;
    private TankBody body;
    private Turret turret;
    private int forwardSpeed;
    private int backwardSpeed;
    private Vector2 direction;
    private boolean isAlive;
    private String colour;
    
    public Tank(String colour, Body tankRigidBody, World world) {
        rigidBody = tankRigidBody;
        rigidBody.getFixtureList().get(2).setUserData(colour);
        body = new TankBody(0, 0, colour, tankRigidBody);
        turret = new Turret(body.getTexture(), colour, tankRigidBody, world);
        forwardSpeed = 140;
        backwardSpeed = 110;
        direction = new Vector2(tankRigidBody.getLocalVector(tankRigidBody.getLocalCenter()).x, -tankRigidBody.getLocalVector(tankRigidBody.getLocalCenter()).y);
        isAlive = true;
        this.colour = colour;
    }
    
    public void update(float dt) {
        turret.update(dt);
        if (isAlive) {
            direction.x = rigidBody.getLocalVector(rigidBody.getLocalCenter()).x;
            direction.y = -rigidBody.getLocalVector(rigidBody.getLocalCenter()).y;

            float turretRotation = turret.getRotation();

            // Correct rotations that are greater than 360 or smaller than 0
            if (turretRotation > 359) {
                turret.appendRotation(-360);
            } else if (turretRotation < 0) {
                turret.appendRotation(360);
            }

            turretAngleResolved(turretRotation);
        } else {
            rigidBody.setTransform(100, 100, 0);
        }
    }
    
    public void turretAngleResolved(float turretRotation) {
        if (turretRotation < 90) {
            turret.setResolvedRotation((float) Math.toRadians(360 - turretRotation));
            turret.setDirectionX((byte) 1);
            turret.setDirectionY((byte) 1);
        } else if (turretRotation < 180) {
            turret.setResolvedRotation((float) Math.toRadians(360 - (180 - turretRotation)));
            turret.setDirectionX((byte) 1);
            turret.setDirectionY((byte) -1);
        } else if (turretRotation < 270) {
            turret.setResolvedRotation((float) Math.toRadians(360 - (turretRotation - 180)));
            turret.setDirectionX((byte) -1);
            turret.setDirectionY((byte) -1);
        } else if (turretRotation < 360) {
            turret.setResolvedRotation((float) Math.toRadians(360 - (360 - turretRotation)));
            turret.setDirectionX((byte) -1);
            turret.setDirectionY((byte) 1);     
        }
    }
    
    public void dispose() {
        turret.dispose();
        body.dispose();
        rigidBody.setActive(false);
        rigidBody.getFixtureList().clear();
    }
    
    public Turret getTurret() {
        return turret;
    }
    
    public TankBody getBody() {
        return body;
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
    
    public boolean getAlive() {
        return isAlive;
    }
    
    public void setAlive(boolean value) {
        isAlive = value;
    }
    
    public Body getRigidBody() {
        return rigidBody;
    }
    
    public String getColour() {
        return colour;
    }
}

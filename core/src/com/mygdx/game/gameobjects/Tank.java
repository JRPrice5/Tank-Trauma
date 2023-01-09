package com.mygdx.game.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.screens.PlayScreen;
import com.badlogic.gdx.physics.box2d.World;


public class Tank {
    private Body rigidBody;
    private TankBody body;
    private TankTurret turret;
    private int forwardSpeed;
    private int backwardSpeed;
    private Vector2 direction;

    public Vector2 getDirection() {
        return direction;
    }
    
    public Tank(String colour, int mapSizeX, int mapSizeY, Body tankRigidBody, World world) {
        rigidBody = tankRigidBody;
        body = new TankBody(0, 0, colour, tankRigidBody);
        turret = new TankTurret(body.getTexture(), colour, tankRigidBody, world);
        forwardSpeed = 140;
        backwardSpeed = 110;
        direction = new Vector2(tankRigidBody.getLocalVector(tankRigidBody.getLocalCenter()).x, -tankRigidBody.getLocalVector(tankRigidBody.getLocalCenter()).y);
    }
    
    public void update(float dt) {
        turret.setTurretPosition(rigidBody.getPosition().scl(1 / PlayScreen.UNIT_SCALE));
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
        
        turret.update(dt);
    }
    
    public void turretAngleResolved(float turretRotation) {
        if (turretRotation < 90) {
            turret.setResolvedRotation((float) Math.toRadians(turretRotation));
            turret.setTurretDirectionX((byte) 1);
            turret.setTurretDirectionY((byte) 1);
        } else if (turretRotation < 180) {
            turret.setResolvedRotation((float) Math.toRadians(180 - turretRotation));
            turret.setTurretDirectionX((byte) 1);
            turret.setTurretDirectionY((byte) -1);
        } else if (turretRotation < 270) {
            turret.setResolvedRotation((float) Math.toRadians(turretRotation - 180));
            turret.setTurretDirectionX((byte) -1);
            turret.setTurretDirectionY((byte) -1);
        } else if (turretRotation < 360) {
            turret.setResolvedRotation((float) Math.toRadians(360 - turretRotation));
            turret.setTurretDirectionX((byte) -1);
            turret.setTurretDirectionY((byte) 1);     
        }
        
    }
    
    public void dispose() {
        turret.dispose();
        body.dispose();
    }
    
    public TankTurret getTurret() {
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
}

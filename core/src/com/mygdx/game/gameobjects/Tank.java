package com.mygdx.game.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.screens.PlayScreen;

public class Tank {
    private Body physicsBody;
    private TankBody body;
    private TankTurret turret;
    private int forwardSpeed;
    private int backwardSpeed;
    private Vector2 direction;

    public Vector2 getDirection() {
        return direction;
    }
    
    public Tank(String colour, int mapSizeX, int mapSizeY, Body physicsBody) {
        this.physicsBody = physicsBody;
        body = new TankBody(0, 0, colour, physicsBody);
        turret = new TankTurret(body.getTexture(), colour, physicsBody);
        forwardSpeed = 150;
        backwardSpeed = 110;
        direction = new Vector2(physicsBody.getLocalVector(physicsBody.getLocalCenter()).x, -physicsBody.getLocalVector(physicsBody.getLocalCenter()).y);
    }
    
    public void update(float dt) {
        turret.setTurretPosition(physicsBody.getPosition().scl(1 / PlayScreen.UNIT_SCALE));
        direction.x = physicsBody.getLocalVector(physicsBody.getLocalCenter()).x;
        direction.y = -physicsBody.getLocalVector(physicsBody.getLocalCenter()).y;
        
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

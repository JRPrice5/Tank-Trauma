package com.mygdx.game.gameobjects;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;

public class Tank {
    private TankBody body;
    private TankTurret turret;
    private Vector3 velocity;
    private int forwardVelocity;
    private int backwardVelocity;
    
    public Tank(String colour, int mapSizeX, int mapSizeY, Body hitBox) {
        body = new TankBody(0, 0, colour, hitBox);
        turret = new TankTurret(body.getTexture(), colour);
        velocity = new Vector3(0, 0, 0);
        forwardVelocity = 110;
        backwardVelocity = -80;
    }
    
    public void update(float dt) {
        // Calculate tank position vectors
        velocity.scl(dt);
//        if ((body.getPosition().y <= 0) && (body.getPosition().x > 0) && velocity.y <= 0) {
//            body.appendPosition(velocity.x, 0);
//            turret.appendPosition(velocity.x, 0);               
//        } else if ((body.getPosition().x <= 0) && (body.getPosition().y > 0) && velocity.x <= 0) {
//            body.appendPosition(0, velocity.y);
//            turret.appendPosition(0, velocity.y);
//        } else if ((body.getPosition().x <= 0) && (body.getPosition().y <= 0) && velocity.x < 0 && velocity.y >= 0) {
//            body.appendPosition(0, velocity.y);
//            turret.appendPosition(0, velocity.y);
//        } else if ((body.getPosition().x <= 0) && (body.getPosition().y <= 0) && velocity.x >= 0 && velocity.y < 0) {
//            body.appendPosition(velocity.x, 0);
//            turret.appendPosition(velocity.x, 0);  
//        } else if ((body.getPosition().x > 0) && (body.getPosition().y > 0)){
//            body.appendPosition(velocity.x, velocity.y);
//            turret.appendPosition(velocity.x, velocity.y);
//        } 
        body.appendPosition(velocity.x, velocity.y);
        turret.appendPosition(velocity.x, velocity.y);
        velocity.scl(1/dt);
        velocity.scl(dt);
        
        
        float bodyRotation = body.getRotation();
        float turretRotation = turret.getRotation();

        // Correct rotations that are greater than 360 or smaller than 0
        if (bodyRotation > 359) {
            body.appendRotation(bodyRotation - 360);
        } else if (bodyRotation < 0) {
            body.appendRotation(bodyRotation + 360);
        }
        if (turretRotation > 359) {
            turret.appendRotation(-360);
        } else if (turretRotation < 0) {
            turret.appendRotation(360);
        }
        
        bodyAngleResolved(bodyRotation);
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
    
    public void bodyAngleResolved(float bodyRotation) {
        if (bodyRotation < 90) {
            body.setResolvedRotation((float) Math.toRadians(bodyRotation));
            body.setDirectionX((byte) 1);
            body.setDirectionY((byte) 1);
        } else if (bodyRotation < 180) {
            body.setResolvedRotation((float) Math.toRadians(180 - bodyRotation)); 
            body.setDirectionX((byte) 1);
            body.setDirectionY((byte) -1);
        } else if (bodyRotation < 270) {
            body.setResolvedRotation((float) Math.toRadians(bodyRotation - 180));
            body.setDirectionX((byte) -1);
            body.setDirectionY((byte) -1);
        } else if (bodyRotation < 360) {
            body.setResolvedRotation((float) Math.toRadians(360 - bodyRotation));
            body.setDirectionX((byte) -1);
            body.setDirectionY((byte) 1);                  
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

    public Vector3 getVelocity() {
        return velocity;
    }
    
    public void appendVelocity(float x, float y) {
        velocity.add(x, y, 0);
    }
    
    public int getForwardVelocity() {
        return forwardVelocity;
    }

    public int getBackwardVelocity() {
        return backwardVelocity;
    }
}

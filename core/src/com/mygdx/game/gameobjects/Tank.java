package com.mygdx.game.gameobjects;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.screens.PlayScreen;

public class Tank {
    private Body physicsBody;
    private TankBody body;
    private TankTurret turret;
    private int forwardSpeed;
    private int backwardSpeed;
    
    public Tank(String colour, int mapSizeX, int mapSizeY, Body physicsBody) {
        this.physicsBody = physicsBody;
        body = new TankBody(0, 0, colour, physicsBody);
        turret = new TankTurret(body.getTexture(), colour, physicsBody);
        forwardSpeed = 150;
        backwardSpeed = 110;
    }
    
    public void update(float dt) {
        // Calculate tank position vectors
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
//        body.appendPosition(physicsBody.getLinearVelocity().x, physicsBody.getLinearVelocity().y);
//        turret.appendPosition(physicsBody.getLinearVelocity().x, physicsBody.getLinearVelocity().y);
        turret.setTurretPosition(physicsBody.getPosition().scl(1 / PlayScreen.UNIT_SCALE));
        
        
        float bodyRotation = body.getRotation();
        float turretRotation = turret.getRotation();

        // Correct rotations that are greater than 360 or smaller than 0
        if (bodyRotation > 359) {
            body.appendRotation(- 360);
        } else if (bodyRotation < 0) {
            body.appendRotation(360);
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

    public int getForwardSpeed() {
        return forwardSpeed;
    }

    public int getBackwardSpeed() {
        return backwardSpeed;
    }
}

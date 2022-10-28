package com.mygdx.game.gameobjects;

import com.badlogic.gdx.math.Vector3;

public class Tank {
    private TankBody body;
    private TankTurret turret;
    private Vector3 velocity;
    private int forwardVelocity;
    private int backwardVelocity;
    
    public Tank(int x, int y, String colour) {
        body = new TankBody(x, y, colour);
        turret = new TankTurret(body.getTexture(), x, y, colour);
        velocity = new Vector3(0, 0, 0);
        forwardVelocity = 140;
        backwardVelocity = -100;
    }
    
    public void update(float dt) {
        // Calculate tank position vectors
        velocity.scl(dt);
        if ((body.getPosition().y <= 0) && (body.getPosition().x > 0) && velocity.y < 0) {
            body.appendPosition(velocity.x, 0);
            turret.appendPosition(velocity.x, 0);               
        } else if ((body.getPosition().x <= 0) && (body.getPosition().y > 0) && velocity.x < 0) {
            body.appendPosition(0, velocity.y);
            turret.appendPosition(0, velocity.y);
        } else {
            body.appendPosition(velocity.x, velocity.y);
            turret.appendPosition(velocity.x, velocity.y);
        }
        velocity.scl(1/dt);
        velocity.scl(dt);
        
        turret.update(dt);
        
        float bodyRotation = body.getRotation();
        float turretRotation = turret.getRotation();

        // Correct rotations that are greater than 360 or smaller than 0
        if (bodyRotation > 359) {
            body.appendRotation(-360);
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
        
        // Prevent tank from moving off the screen
        if (body.getPosition().y < 0) {
            body.setPositionY(0);
            turret.setPositionY(-1);
        } 
//        else if (body.getPosition().y > viewport.getScreenHeight()) {
//            body.setPositionY(viewport.getScreenHeight());
//            turret.setPositionY(viewport.getScreenHeight() + 1);
//        }
        if (body.getPosition().x < 0) {
            body.setPositionX(0);
            turret.setPositionX((body.getTexture().getWidth() - turret.getTurretTexture().getWidth()) / 2);
        } 
//        else if (body.getPosition().x > viewport.getScreenWidth()) {
//            body.setPositionX(viewport.getScreenWidth());
//            turret.setPositionX(viewport.getScreenWidth() - (body.getTexture().getWidth() - turret.getTurretTexture().getWidth()) / 2);
//        }
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

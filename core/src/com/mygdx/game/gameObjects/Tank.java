package com.mygdx.game.gameObjects;

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
        forwardVelocity = 100;
        backwardVelocity = -80;
    }
    
    public void update(float dt) {
        // Calculate tank position vectors
        velocity.scl(dt);
        body.appendPosition(velocity.x, velocity.y);
        turret.appendPosition(velocity.x, velocity.y);
        velocity.scl(1/dt);
        velocity.scl(dt);
        
        for (int i = 0; i < turret.getBullets().size(); i++) {
            Bullet bullet = turret.getBullets().get(i);
            bullet.update(dt, turret.getRotation());
            if (bullet.getTimeAlive() >= bullet.getLifeSpan()) {
                turret.getBullets().remove(i);
            }
        }
        if (turret.getReload() > 0) {
            turret.reduceReload(dt);
        }
        
        // Prevent tank from moving off the screen
        if (body.getPosition().y < 0) {
            body.setPositionY(0);
            turret.setPositionY(-1);
        }
        if (body.getPosition().x < 0) {
            body.setPositionX(0);
            turret.setPositionX((body.getTexture().getWidth() - turret.getTexture().getWidth()) / 2);
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

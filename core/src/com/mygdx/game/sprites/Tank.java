package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import java.util.Random;

public class Tank {
    private Vector3 bodyPosition;
    private Vector3 turretPosition;
    private Vector3 velocity;
    private int forwardSpeed;
    private int backwardSpeed;
    private int bodyRotation;
    private int turretRotation;
    private int bodyRotationSpeed;
    private int turretRotationSpeed;
    private Texture body;
    private Texture turret;
    
    public Tank(int x, int y) {
        Random rand = new Random();
        bodyPosition = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);
        forwardSpeed = 100;
        backwardSpeed = 80;
        bodyRotation = 0; 
        turretRotation = 0;
        bodyRotationSpeed = 2;
        turretRotationSpeed = 3;
        body = new Texture("tankBody_green.png");
        turret = new Texture("tankGreen_barrel1.png");
        turretPosition = new Vector3(x + (body.getWidth() - turret.getWidth()) / 2, y - 1, 0);
//        turretPosition = new Vector3(x + (body.getWidth() - turret.getWidth()) / 2, y + (body.getHeight() + turret.getHeight()) / 2, 0);
//        DistanceJointDef defJoint = new DistanceJointDef();
//        defJoint.length = 3;
//        defJoint.initialize(
//                turret,
//                body,
//                new Vector2(turretPosition.x + (turret.getWidth() / 2), turretPosition.y + 39),
//                new Vector2(bodyPosition.x + (body.getWidth() / 2 ), bodyPosition.y + (body.getHeight() / 2)));
    }
    
    public void update(float dt) {
        
        // Control turret and body rotation speeds, depending on user input
        if (Gdx.input.isKeyPressed(Keys.A) && Gdx.input.isKeyPressed(Keys.DPAD_LEFT)) {
            bodyRotation -= bodyRotationSpeed;
            turretRotation -= turretRotationSpeed;
        } else if (Gdx.input.isKeyPressed(Keys.A)) {
            bodyRotation -= bodyRotationSpeed;
            turretRotation -= bodyRotationSpeed;
        } else if (Gdx.input.isKeyPressed(Keys.DPAD_LEFT)) {
            turretRotation -= turretRotationSpeed;
        }
        if (Gdx.input.isKeyPressed(Keys.D) && Gdx.input.isKeyPressed(Keys.DPAD_RIGHT)) {
            bodyRotation += bodyRotationSpeed;
            turretRotation += turretRotationSpeed;
        } else if (Gdx.input.isKeyPressed(Keys.D)) {
            bodyRotation += bodyRotationSpeed;
            turretRotation += bodyRotationSpeed;
        } else if (Gdx.input.isKeyPressed(Keys.DPAD_RIGHT)) {
            turretRotation += turretRotationSpeed;
        }
        
        // Correct rotations that are greater than 360 or smaller than 0
        if (bodyRotation > 359) {
            bodyRotation -= 360;
        } else if (bodyRotation < 0) {
            bodyRotation += 360;
        }
        if (turretRotation > 359) {
            turretRotation -= 360;
        } else if (turretRotation < 0) {
            turretRotation += 360;
        }
        
        byte directionX = 0;
        byte directionY = 0;
        float resolvedBodyRotation = 0;
        if (bodyRotation < 90) {
            resolvedBodyRotation = (float) Math.toRadians(bodyRotation);
            directionX = 1;
            directionY = 1;
        } else if (bodyRotation < 180) {
            resolvedBodyRotation = (float) Math.toRadians(180 - bodyRotation); 
            directionX = 1;
            directionY = -1;
        } else if (bodyRotation < 270) {
            resolvedBodyRotation = (float) Math.toRadians(bodyRotation - 180);
            directionX = -1;
            directionY = -1;
        } else if (bodyRotation < 360) {
            resolvedBodyRotation = (float) Math.toRadians(360 - bodyRotation);
            directionX = -1;
            directionY = 1;                   
        }
        
        // Apply normalised x and y velocities if W or S is pressed
        if (Gdx.input.isKeyPressed(Keys.W)) {
            velocity.add(
                    (float) (directionX * forwardSpeed * java.lang.Math.sin(resolvedBodyRotation)),
                    (float) (directionY * forwardSpeed * java.lang.Math.cos(resolvedBodyRotation)),
                    0);
        }
        if (Gdx.input.isKeyPressed(Keys.S)) {
            velocity.add(
                    -(float) (directionX * backwardSpeed * java.lang.Math.sin(resolvedBodyRotation)),
                    -(float) (directionY * backwardSpeed * java.lang.Math.cos(resolvedBodyRotation)),
                    0);
        }
        
        // Calculate tank position vectors with velocity and time between frames.
        velocity.scl(dt);
        bodyPosition.add(velocity.x, velocity.y, 0);
        turretPosition.add(velocity.x, velocity.y, 0);
        velocity.scl(1/dt);
        velocity.scl(dt);
//        if (position.y < 0) 
//            position.y = 0;
//        if (position.x < 0)
//            position.x = 0;
    }
    
    public Vector3 getBodyPosition() {
        return bodyPosition;
    }
    
    public Vector3 getTurretPosition() {
        return turretPosition;
    }

    public Texture getBody() {
        return body;
    }
    
    public Texture getTurret() {
        return turret;
    }
    
    public int getBodyRotation() {
        return bodyRotation;
    }
    
    public int getTurretRotation() {
        return turretRotation;
    }
}

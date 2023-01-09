package com.mygdx.game.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.math.Vector2;
import static com.mygdx.game.screens.PlayScreen.UNIT_SCALE;


public class Bullet {
    private Body body;
    private Texture texture;
    private Vector3 position;
    private float rotation;
    private int speed;
    private Vector3 velocity;
    private float lifeSpan;

    public Bullet(float x, float y, String colour, float rotation, float normaliserX, float normaliserY, World world) {
        createRigidBody(x, y, world);
        texture = new Texture("cannonBall"+colour+"Small.png");
        position = new Vector3(x, y, 0);
        this.rotation = rotation;
        speed = 400;
        velocity = new Vector3(normaliserX * speed, normaliserY * speed, 0);
        lifeSpan = 8;
    }
    
    public void update(float dt) {
        velocity.scl(dt);
        position.add(velocity.x, velocity.y, 0);
        velocity.scl(1/dt);
        lifeSpan -= dt;
    }
    
    public void createRigidBody(float x, float y, World world) {
        Body bullet;
        BodyDef def = new BodyDef();
        
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x * UNIT_SCALE, y * UNIT_SCALE);
        bullet = world.createBody(def);
        
        CircleShape shape = new CircleShape();
        shape.setRadius(0.05f);
        
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 1;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = 1;
        fixtureDef.filter.maskBits = 1;
        
        // gives body the shape and a density
        bullet.createFixture(fixtureDef);
        shape.dispose();
        body = bullet;
    }
    
    public void dispose() {
        texture.dispose();
    }
    
    public Texture getTexture() {
        return texture;
    }
    
    public Vector3 getPosition() {
        return position;
    }
    
    public float getRotation() {
        return rotation;
    }
    
    public Vector3 getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocityX, float velocityY) {
        velocity = new Vector3(velocityX, velocityY, 0);
    }
    
    public int getSpeed() {
        return speed;
    }

    public float getLifeSpan() {
        return lifeSpan;
    }
}

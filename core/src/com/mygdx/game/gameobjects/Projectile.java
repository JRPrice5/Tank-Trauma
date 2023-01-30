package com.mygdx.game.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Projectile {
    private Texture texture;
    private Body body;
    private Vector2 position;
    private Vector2 offset;
    private float rotation;
    private float lifeSpan;
    
    public abstract void update(float dt);
    public abstract Body createRigidBody(float x, float y, World world);
    public abstract void dispose();
    public abstract Body getBody();
    public abstract float getRotation();
    public abstract Texture getTexture();
    public abstract Vector2 getPosition();
    public abstract float getLifeSpan();
    public abstract int getCollisionCount();
    public abstract void incrementCollisionCount();
}

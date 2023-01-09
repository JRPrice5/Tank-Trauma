package com.mygdx.game.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.math.Vector2;
import static com.mygdx.game.screens.PlayScreen.UNIT_SCALE;


public class Bullet {
    private Texture texture;
    private Body body;
    private Vector2 position;
    private float resolvedRotation;
    private Vector2 offset;
    private float rotation;
    private float lifeSpan;

    public Bullet(float x, float y, String colour, float rotation, float resolvedRotation, World world, byte barrelAdjustmentX, byte barrelAdjustmentY, byte turretDirectionX, byte turretDirectionY) {
        texture = new Texture("cannonBall"+colour+"Small.png");
        createRigidBody(x, y, world);
        offset = new Vector2((float)((barrelAdjustmentX * java.lang.Math.cos(resolvedRotation) * texture.getWidth() / 2) 
                                    - (turretDirectionX * java.lang.Math.sin(resolvedRotation) * texture.getHeight() / 2)),
                                (float)((-turretDirectionY * java.lang.Math.cos(resolvedRotation) * texture.getHeight() / 2)
                                    + (barrelAdjustmentY * java.lang.Math.sin(resolvedRotation) * texture.getWidth() / 2)));
        position = new Vector2(x + offset.x, y + offset.y);
        this.resolvedRotation = resolvedRotation;
        this.rotation = rotation;
        lifeSpan = 8;
    }
    
    public void update(float dt) {
//        velocity.scl(dt);
//        position.add(velocity.x, velocity.y, 0);
//        velocity.scl(1/dt);
        position.add(body.getWorldCenter().x / UNIT_SCALE + offset.x - position.x, body.getWorldCenter().y / UNIT_SCALE + offset.y - position.y);
        lifeSpan -= dt;
    }
    
    public void createRigidBody(float x, float y, World world) {
        Body bullet;
        BodyDef def = new BodyDef();
        
        def.type = BodyDef.BodyType.DynamicBody;
//        def.position.set((float) (x + texture.getWidth() / 2 * java.lang.Math.cos(resolvedRotation) * barrelAdjustmentX) * UNIT_SCALE,
//                        (float) (y + texture.getWidth() / 2 * java.lang.Math.sin(resolvedRotation) * barrelAdjustmentY) * UNIT_SCALE);
        def.position.set(x * UNIT_SCALE, y * UNIT_SCALE);
        bullet = world.createBody(def);
        
        CircleShape shape = new CircleShape();
        shape.setRadius(0.05f);
        
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 1;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = 2;
        fixtureDef.filter.maskBits = 3;
        
        // gives body the shape and a density
        bullet.createFixture(fixtureDef);
        shape.dispose();
        body = bullet;
    }
    
    public void dispose() {
        texture.dispose();
        body.setActive(false);
    }
    
    public Body getBody() {
        return body;
    }
    
    public float getRotation() {
        return rotation;
    }
    
    public Texture getTexture() {
        return texture;
    }
    
    public Vector2 getPosition() {
        return position;
    }

    public float getLifeSpan() {
        return lifeSpan;
    }
}

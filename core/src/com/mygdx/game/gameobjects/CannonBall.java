package com.mygdx.game.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.math.Vector2;
import static com.mygdx.game.screens.PlayScreen.UNIT_SCALE;


public class CannonBall extends Projectile {
    private final short SPEED = 250;
    private Texture texture;
    private Body body;
    private Vector2 position;
    private Vector2 offset;
    private float rotation;
    private float lifeSpan;

    public CannonBall(float x, float y, String colour, float rotation,
            float resolvedRotation, World world, byte barrelAdjustmentX,
            byte barrelAdjustmentY, byte turretDirectionX, byte turretDirectionY,
            Vector2 turretDirection) {
        texture = new Texture("cannonBall"+colour+"Small.png");
        body = createRigidBody(x, y, world);
        offset = new Vector2(-texture.getWidth() / 2, -texture.getHeight() / 2);
        position = new Vector2(x + offset.x, y + offset.y);
        body.setLinearVelocity((turretDirection.x * SPEED / turretDirection.len()) * UNIT_SCALE,
                    (turretDirection.y * SPEED / turretDirection.len()) * UNIT_SCALE);
        lifeSpan = 12;
    }
    
    @Override
    public void update(float dt) {
        position.x = body.getWorldCenter().x / UNIT_SCALE + offset.x;
        position.y = body.getWorldCenter().y / UNIT_SCALE + offset.y;
        lifeSpan -= dt;
    }
    
    @Override
    public Body createRigidBody(float x, float y, World world) {
        Body bullet;
        BodyDef def = new BodyDef();
        
        def.type = BodyDef.BodyType.DynamicBody;
//        def.position.set((float) (x + texture.getWidth() / 2 * java.lang.Math.cos(resolvedRotation) * barrelAdjustmentX) * UNIT_SCALE,
//                        (float) (y + texture.getWidth() / 2 * java.lang.Math.sin(resolvedRotation) * barrelAdjustmentY) * UNIT_SCALE);
        def.position.set(x * UNIT_SCALE, y * UNIT_SCALE);
        bullet = world.createBody(def);
        bullet.setFixedRotation(false);
        
        CircleShape shape = new CircleShape();
        shape.setRadius(0.05f);
        
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 1;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = 0x0008;
        fixtureDef.filter.maskBits = 0x0001;
        
        // gives body the shape and a density
        bullet.createFixture(fixtureDef);
        bullet.setBullet(true);
        shape.dispose();
        return bullet;
    }
    
    @Override
    public void dispose() {
        texture.dispose();
        body.setActive(false);
        body.destroyFixture(body.getFixtureList().peek());
    }
    
    @Override
    public Body getBody() {
        return body;
    }
    
    @Override
    public float getRotation() {
        return rotation;
    }
    
    @Override
    public Texture getTexture() {
        return texture;
    }
    
    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public float getLifeSpan() {
        return lifeSpan;
    }
}

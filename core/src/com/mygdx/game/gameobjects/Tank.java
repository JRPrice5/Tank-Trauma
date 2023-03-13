package com.mygdx.game.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import static com.mygdx.game.screens.GameScreen.UNIT_SCALE;
import java.util.LinkedList;


public class Tank {
    private final short BARREL_OFFSET = 3;
    private short barrelLength = 44;
    private Body body;
    private int forwardSpeed;
    private int backwardSpeed;
    private Vector2 direction;
    private float rotation;
    private boolean isAlive;
    private String colour;
    private Texture bodyTexture;
    private Texture turretTexture;
    private Texture bulletTexture;
    private float rotationSpeed;
    private World world;
    private Vector2 barrelPosition;
    private float reloadTime;
    private LinkedList projectiles;
    private byte directionX;
    private byte directionY;
    private float resolvedRotation;
    
    public Tank(String colour, Vector2 position, World world) {
        this.world = world;
        body = createRigidBody(position);
        body.getFixtureList().get(2).setUserData(colour);
        forwardSpeed = 160;
        backwardSpeed = 120;
        direction = new Vector2(body.getLocalVector(body.getLocalCenter()).x, -body.getLocalVector(body.getLocalCenter()).y);
        isAlive = true;
        this.colour = colour;
        bodyTexture = new Texture("tankBody_"+colour+".png");
        turretTexture = new Texture("tank"+colour+"_barrel.png");
        bulletTexture = new Texture("cannonBall"+colour+"Small.png");
        rotationSpeed = 2f;
        barrelPosition = new Vector2(body.getWorldCenter().x / UNIT_SCALE, body.getWorldCenter().y / UNIT_SCALE + barrelLength);
        reloadTime = 0;
        projectiles = new LinkedList<>();
    }
    
    public void update(float dt) {
        if (isAlive) {
            direction.x = body.getLocalVector(body.getLocalCenter()).x;
            direction.y = -body.getLocalVector(body.getLocalCenter()).y;
            rotation = (float) Math.toDegrees(body.getAngle());
            // try to remove this update loop
            for (int i = 0; i < projectiles.size(); i++) {
                Projectile projectile = (Projectile) projectiles.get(i);
                projectile.update(dt);
                if (projectile.getLifeSpan() <= 0) {
                    projectile.dispose();
                    projectiles.remove(i);
                }
            }
            
            if (reloadTime > 0) {
                reloadTime -= dt;
            }

            float distanceX = (float) ((barrelLength * -java.lang.Math.sin(Math.toRadians(rotation)))
                    - barrelPosition.x + body.getWorldCenter().x / UNIT_SCALE);
            float distanceY = (float) ((barrelLength * java.lang.Math.cos(Math.toRadians(rotation)))
                    - barrelPosition.y + body.getWorldCenter().y / UNIT_SCALE - BARREL_OFFSET);
            barrelPosition.add(distanceX, distanceY);
        } else {
            body.setTransform(-10, -10, 0);
        }
    }
    
    public void shoot() {
        if (reloadTime <= 0) {
            Bullet cannonBall = new Bullet(
                    (float) (barrelPosition.x
                            - (directionX * java.lang.Math.sin(resolvedRotation) * BARREL_OFFSET * bulletTexture.getHeight() / 2)),
                    (float) (barrelPosition.y
                            - (directionY * java.lang.Math.cos(resolvedRotation) * BARREL_OFFSET * bulletTexture.getHeight() / 2)), 
                    colour,
                    rotation,
                    world,
                    direction,
                    body);
            projectiles.add(cannonBall);
            reloadTime = 1;
        }
    }
    
    public Body createRigidBody(Vector2 position) {
        Body body;
        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(position.x, position.y);
        def.fixedRotation = true;
        // initializes the body and puts it into the box2d world
        // with the body definition properties
        body = world.createBody(def);

        Vector2[] localVertices = {new Vector2(-30 * UNIT_SCALE, -30 * UNIT_SCALE), new Vector2(-23 * UNIT_SCALE, -34 * UNIT_SCALE), 
            new Vector2(23 * UNIT_SCALE, -34 * UNIT_SCALE), new Vector2(30 * UNIT_SCALE, -30 * UNIT_SCALE), new Vector2(30 * UNIT_SCALE, 30 * UNIT_SCALE), 
            new Vector2(23 * UNIT_SCALE, 34 * UNIT_SCALE), new Vector2(-23 * UNIT_SCALE, 34 * UNIT_SCALE), new Vector2(-30 * UNIT_SCALE, 30 * UNIT_SCALE)};
        PolygonShape shape1 = new PolygonShape();
        shape1.set(localVertices);
        FixtureDef fixtureDef1 = new FixtureDef();
        fixtureDef1.density = 1;
        fixtureDef1.friction = 0;
        fixtureDef1.shape = shape1;
        
        PolygonShape shape2 = new PolygonShape();
        shape2.setAsBox(22.5f * UNIT_SCALE, 20f * UNIT_SCALE, new Vector2(0, -10 * UNIT_SCALE), 0);
        FixtureDef fixtureDef2 = new FixtureDef();
        fixtureDef2.density = 1;
        fixtureDef2.friction = 0;
        fixtureDef2.shape = shape2;
        fixtureDef2.filter.maskBits = 2;

        FixtureDef fixtureDef3 = new FixtureDef();
        fixtureDef3.shape = shape1;
        fixtureDef3.isSensor = true;
        fixtureDef3.filter.maskBits = 8;
        fixtureDef3.filter.categoryBits = 2;
        
        // gives body the shape and a density
        body.createFixture(fixtureDef1);
        body.createFixture(fixtureDef2);
        body.createFixture(fixtureDef3);
        shape1.dispose();
        return body;
    }
    
    public void dispose() {
        body.setActive(false);
        body.getFixtureList().clear();
    }
    
    public int getForwardSpeed() {
        return forwardSpeed;
    }

    public int getBackwardSpeed() {
        return backwardSpeed;
    }
    
    public Vector2 getDirection() {
        return direction;
    }
    
    public boolean getIsAlive() {
        return isAlive;
    }
    
    public void setIsAlive(boolean value) {
        isAlive = value;
    }
    
    public Body getBody() {
        return body;
    }
    
    public String getColour() {
        return colour;
    }
    
    public float getReloadTime() {
        return reloadTime;
    } 
    
    public void setReloadTime(float value) {
        reloadTime = value;
    }
    
    public Texture getBodyTexture() {
        return bodyTexture;
    }
    
    public float getRotationSpeed() {
        return rotationSpeed;
    }
    
    public short getBarrelLength() {
        return barrelLength;
    }
    
    public Texture getBulletTexture() {
        return bulletTexture;
    }

    public Texture getTurretTexture() {
        return turretTexture;
    }

    public LinkedList getProjectiles() {
        return projectiles;
    }
}

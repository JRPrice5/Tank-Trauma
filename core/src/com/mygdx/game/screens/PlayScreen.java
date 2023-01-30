package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.TankTrauma;
import com.mygdx.game.gameobjects.Projectile;
import com.mygdx.game.gameobjects.Tank;
import com.mygdx.game.gameobjects.TankBody;
import com.mygdx.game.utils.MapGenerator;
import com.mygdx.game.gameobjects.Turret;
import com.mygdx.game.utils.Listener;
import com.mygdx.game.utils.MazeHitboxParser;
import java.util.LinkedList;
import java.util.Random;

public class PlayScreen implements Screen {
    public static final float UNIT_SCALE = 1 / 128f;
    
    private final TankTrauma game;
    
    private int mapSizeX;
    private final int maxWidth;
    private final int minWidth;
    
    private int mapSizeY;
    private final int maxHeight;
    private final int minHeight;
    
    private OrthographicCamera cam;
    private Viewport viewport;
    
    private final Tank tank1;
    private final TankBody body1;
    private final Turret turret1;
    private Projectile projectile;
    
    private final Listener contactListener;
    private final Box2DDebugRenderer b2dr;
    private final World world;

    private final Body tank1RigidBody;
    
    private MapGenerator map;
    private final MazeHitboxParser mazeHitboxParser;
    
    private OrthogonalTiledMapRenderer groundRenderer;
    private OrthogonalTiledMapRenderer mazeRenderer;
    
    public PlayScreen(TankTrauma game, int maxWidth, int minWidth, int maxHeight, int minHeight) {
        this.game = game;
        
        Random random = new Random();
        
        mapSizeX = random.nextInt(maxWidth - minWidth + 1) + minWidth;
        this.maxWidth = maxWidth;
        this.minWidth = minWidth;
        
        mapSizeY = random.nextInt(maxHeight - minHeight + 1) + minHeight;
        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
        
        cam = new OrthographicCamera();
        viewport = new FitViewport(mapSizeX + 0.25f, mapSizeY + 0.25f, cam);
        
        b2dr = new Box2DDebugRenderer();
        world = new World(new Vector2(0, 0), false);
        tank1RigidBody = createTankBody(4.5f, 4.5f);
        tank1RigidBody.setSleepingAllowed(false);
        
        tank1 = new Tank("green", tank1RigidBody, world);
        body1 = tank1.getBody();
        turret1 = tank1.getTurret();
        tank1.getTurret().getProjectiles();
        
        contactListener = new Listener(tank1, turret1.getProjectiles());
        
        map = new MapGenerator(mapSizeX, mapSizeY);
        map.generateGround();
        map.generateMaze();
        mazeHitboxParser = new MazeHitboxParser();
        mazeHitboxParser.parseMapLayers(world, map);
        groundRenderer = new OrthogonalTiledMapRenderer(map.getGroundMap(), UNIT_SCALE);
        mazeRenderer = new OrthogonalTiledMapRenderer(map.getMazeMap(), UNIT_SCALE);
    }

    public void handleInput() {
        float bodyRotationSpeed = body1.getRotationSpeed();
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
        
        tank1RigidBody.setAngularVelocity(0);
        
        // Control turret and body rotation speeds, depending on user input
        if (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
            tank1RigidBody.setAngularVelocity(tank1RigidBody.getAngularVelocity() + bodyRotationSpeed);
        } else if (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
            tank1RigidBody.setAngularVelocity(tank1RigidBody.getAngularVelocity() + bodyRotationSpeed);
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            tank1RigidBody.setAngularVelocity(tank1RigidBody.getAngularVelocity() + bodyRotationSpeed);
        }
        
        if (Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
            tank1RigidBody.setAngularVelocity(tank1RigidBody.getAngularVelocity() - bodyRotationSpeed);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
            tank1RigidBody.setAngularVelocity(tank1RigidBody.getAngularVelocity() - bodyRotationSpeed);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            tank1RigidBody.setAngularVelocity(tank1RigidBody.getAngularVelocity() - bodyRotationSpeed);
        }
        
        tank1RigidBody.setLinearVelocity(0, 0);
        
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            tank1RigidBody.setLinearVelocity((tank1.getDirection().x * tank1.getForwardSpeed() / tank1.getDirection().len()) * UNIT_SCALE,
                    (tank1.getDirection().y * tank1.getForwardSpeed() / tank1.getDirection().len()) * UNIT_SCALE);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            tank1RigidBody.setLinearVelocity((-tank1.getDirection().x * tank1.getBackwardSpeed() / tank1.getDirection().len()) * UNIT_SCALE,
                    (-tank1.getDirection().y * tank1.getBackwardSpeed() / tank1.getDirection().len()) * UNIT_SCALE);
        }
        
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            turret1.shoot();
        }
    }

    public void update(float dt) {
        world.step(1 / 60f, 6, 2);
        handleInput();
        tank1.update(dt);
        if (!tank1.getAlive()) {
            tank1.setAlive(true);
            newRound();
        }
    }

    @Override
    public void render(float dt) {
        update(dt);
        
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.sb.setProjectionMatrix(cam.combined);
        
        int tankWidth = body1.getTexture().getWidth();
        int tankHeight = body1.getTexture().getHeight();
        int turretWidth = turret1.getTexture().getWidth();
        int turretHeight = turret1.getTexture().getHeight();
       
        // Render maze and ground with adjustments for odd and even map sizes
        int[] layer1 = {0};
        int[] layer2 = {1};
        int[] layer3 = {2};
        
        float xOffset = (mapSizeX % 2 == 0) ? 0 : 0.5f;
        float yOffset = (mapSizeY % 2 == 0) ? 0 : 0.5f;
        
        cam.position.set((mapSizeX / 2) + xOffset, (mapSizeY / 2) + yOffset, 0);
        cam.update();
        groundRenderer.setView(cam);
        groundRenderer.render();
        cam.position.set((mapSizeX / 2) + 0.5f + xOffset, (mapSizeY / 2) + 0.5f + yOffset, 0);
        cam.update();
        mazeRenderer.setView(cam);
        mazeRenderer.render(layer1);
        cam.position.set((mapSizeX / 2) + 0.5f + xOffset, (mapSizeY / 2) + yOffset, 0);
        cam.update();
        mazeRenderer.setView(cam);
        mazeRenderer.render(layer2);
        cam.position.set((mapSizeX / 2) + xOffset, (mapSizeY / 2) + 0.5f + yOffset, 0);
        cam.update();
        mazeRenderer.setView(cam);
        mazeRenderer.render(layer3);
        
        // Sets camera position to centre and sets the viewport dimensions
        cam.position.set(mapSizeX / 2, mapSizeY / 2, 0);
        cam.update();
        
        // Render tank
        game.sb.begin();
        game.sb.draw(body1.getTexture(),
                tank1RigidBody.getWorldCenter().x - ((body1.getTexture().getWidth() / 2) * UNIT_SCALE),
                tank1RigidBody.getWorldCenter().y + ((5 - (body1.getTexture().getHeight() / 2)) * UNIT_SCALE),
                (tankWidth / 2) * UNIT_SCALE,
                ((tankHeight / 2) - 5) * UNIT_SCALE,
                tankWidth * UNIT_SCALE,
                tankHeight * UNIT_SCALE,
                1,
                1, 
                (float) (Math.toDegrees(tank1RigidBody.getAngle())), 
                0,
                0,
                tankWidth,
                tankHeight,
                false,
                false);
        
        for (int i = 0; i < turret1.getProjectiles().size(); i++) {
            projectile = turret1.getProjectiles().get(i);
            if (projectile.getBody().isAwake()) {
                int bulletWidth = projectile.getTexture().getWidth();
                int bulletHeight = projectile.getTexture().getHeight();
                game.sb.draw(
                    projectile.getTexture(),
                    projectile.getPosition().x * UNIT_SCALE,
                    projectile.getPosition().y * UNIT_SCALE,
                    0,
                    0,
                    bulletWidth * UNIT_SCALE,
                    bulletHeight * UNIT_SCALE,
                    1,
                    1,
                    -projectile.getRotation(),
                    0,
                    0,
                    bulletWidth,
                    bulletHeight,
                    false,
                    false);
            }
        }
        
        game.sb.draw(turret1.getTexture(),
                tank1RigidBody.getWorldCenter().x - (turret1.getTexture().getWidth() / 2 * UNIT_SCALE), 
                tank1RigidBody.getWorldCenter().y - ((turret1.getTexture().getHeight() - turret1.getBarrelLength()) * UNIT_SCALE),
                (turretWidth / 2) * UNIT_SCALE,
                (turret1.getTexture().getHeight() - turret1.getBarrelLength()) * UNIT_SCALE,
                turretWidth * UNIT_SCALE,
                turretHeight * UNIT_SCALE,
                1,
                1, (float) (Math.toDegrees(tank1.getRigidBody().getAngle())),
                0,
                0,
                turretWidth,
                turretHeight,
                false,
                false);        
        
        game.sb.draw(
                turret1.getBulletTexture(),
                (turret1.getBarrelPosition().x - turret1.getBulletTexture().getWidth() / 2) * UNIT_SCALE, 
                (turret1.getBarrelPosition().y - turret1.getBulletTexture().getHeight() / 2) * UNIT_SCALE, 
                turret1.getBulletTexture().getWidth() * UNIT_SCALE,
                turret1.getBulletTexture().getHeight() * UNIT_SCALE);  
        
        game.sb.end();
        
        b2dr.render(world, cam.combined);
    }
    
    public void newRound() {
        Random random = new Random();
        mapSizeX = random.nextInt(maxWidth - minWidth + 1) + minWidth;
        mapSizeY = random.nextInt(maxHeight - minHeight + 1) + minHeight;
        cam.viewportWidth = mapSizeX + 0.25f;
        cam.viewportHeight = mapSizeY + 0.25f;
        cam.update();
//        viewport.update((int)(mapSizeX / UNIT_SCALE), (int)(mapSizeY / UNIT_SCALE), true);
        viewport = new FitViewport(mapSizeX + 0.25f, mapSizeY + 0.25f, cam);
        map = new MapGenerator(mapSizeX, mapSizeY);
        map.generateGround();
        map.generateMaze();
        LinkedList projectiles = turret1.getProjectiles();
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile projectile = (Projectile) projectiles.get(i);
            projectile.dispose();
            projectiles.remove(i);
        }
        mazeHitboxParser.dispose();
        mazeHitboxParser.parseMapLayers(world, map);
        groundRenderer = new OrthogonalTiledMapRenderer(map.getGroundMap(), UNIT_SCALE);
        mazeRenderer = new OrthogonalTiledMapRenderer(map.getMazeMap(), UNIT_SCALE);
        tank1.getRigidBody().setTransform(4.5f, 4.5f, 0);
    }
    
    public Body createTankBody(float x, float y) {
        Body pBody;
        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x, y);
        def.fixedRotation = true;
        // initializes the body and puts it into the box2d world
        // with the body definition properties
        pBody = world.createBody(def);

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
        pBody.createFixture(fixtureDef1);
        pBody.createFixture(fixtureDef2);
        pBody.createFixture(fixtureDef3);
        shape1.dispose();
        return pBody;
    }
    
    @Override
    public void show() {
        world.setContactListener(contactListener);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        world.dispose();
        groundRenderer.dispose();
        mazeRenderer.dispose();
        tank1.dispose();
    }
    
    public Viewport getViewport() {
        return viewport;
    }

    public World getWorld() {
        return world;
    }
}

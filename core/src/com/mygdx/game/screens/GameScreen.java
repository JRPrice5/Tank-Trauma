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
import com.mygdx.game.utils.MazeGenerator;
import com.mygdx.game.utils.MazeHitboxParser;
import com.mygdx.game.utils.CollisionListener;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameScreen implements Screen {
    public static final float UNIT_SCALE = 1 / 128f;
    
    private final TankTrauma game;

    private int mazeSizeX;
    private final int maxWidth;
    private final int minWidth;
   
    private int mazeSizeY;
    private final int maxHeight;
    private final int minHeight;
    
    private final OrthographicCamera cam;
    private Viewport viewport;
    
    private final Tank tank1;
    
    private final Tank tank2;
    
    private Projectile projectile;
    
    private final CollisionListener contactListener;
    private final Box2DDebugRenderer b2dr;
    private final World world;

    private final Body tank1RigidBody;
    private final Body tank2RigidBody;
    
    private MazeGenerator map;
    private final MazeHitboxParser hitboxParser;
    
    private OrthogonalTiledMapRenderer groundRenderer;
    private OrthogonalTiledMapRenderer mazeRenderer;
    
    public GameScreen(TankTrauma game, int maxWidth, int minWidth, int maxHeight, int minHeight) {
        this.game = game;
        
        Random random = new Random();
        
        mazeSizeX = random.nextInt(maxWidth - minWidth + 1) + minWidth;
        this.maxWidth = maxWidth;
        this.minWidth = minWidth;
        
        mazeSizeY = random.nextInt(maxHeight - minHeight + 1) + minHeight;
        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
        
        cam = new OrthographicCamera();
        viewport = new FitViewport(mazeSizeX + 0.25f, mazeSizeY + 0.25f, cam);
        
        b2dr = new Box2DDebugRenderer();
        world = new World(new Vector2(0, 0), false);
        Vector2 tank1Spawn = randomSpawn();
        tank1RigidBody = createTankBody(tank1Spawn);
        tank1RigidBody.setSleepingAllowed(false);
        
        tank1 = new Tank("sand", tank1RigidBody, world);
        
        Vector2 tank2Spawn = randomSpawn();
        while (tank2Spawn.x == tank1Spawn.x && tank2Spawn.y == tank1Spawn.y) {
            tank2Spawn = randomSpawn();
        }
        tank2RigidBody = createTankBody(tank2Spawn);
        tank2RigidBody.setSleepingAllowed(false);
        
        tank2 = new Tank("dark", tank2RigidBody, world);
        
        contactListener = new CollisionListener(tank1, tank2);
        
        map = new MazeGenerator((int)mazeSizeX, (int)mazeSizeY);
        map.generateGround();
        map.generateMaze();
        hitboxParser = new MazeHitboxParser();
        hitboxParser.parseMaze(world, map);
        groundRenderer = new OrthogonalTiledMapRenderer(map.getGroundMap(), UNIT_SCALE);
        mazeRenderer = new OrthogonalTiledMapRenderer(map.getMazeMap(), UNIT_SCALE);
    }
    
    public Vector2 randomSpawn() {
        Random random = new Random();
        float xPos = 0;
        float yPos = 0;
        if (mazeSizeX % 2 == 0) {
            xPos = random.nextInt((int) mazeSizeX) + 0.5f;
        } else {
            xPos = random.nextInt((int) mazeSizeX);
        }
        
        if (mazeSizeY % 2 == 0) {
            yPos = random.nextInt((int) mazeSizeY) + 0.5f;
        } else {
            yPos = random.nextInt((int) mazeSizeY);
        }
        return new Vector2(xPos, yPos);
    }

    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
        
        tank1RigidBody.setAngularVelocity(0);
        tank2RigidBody.setAngularVelocity(0);
        
        // Control turret and body rotation speeds, depending on user input
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            tank1RigidBody.setAngularVelocity(tank1RigidBody.getAngularVelocity() + tank1.getRotationSpeed());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
            tank2RigidBody.setAngularVelocity(tank2RigidBody.getAngularVelocity() + tank2.getRotationSpeed());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            tank1RigidBody.setAngularVelocity(tank1RigidBody.getAngularVelocity() - tank1.getRotationSpeed());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
            tank2RigidBody.setAngularVelocity(tank2RigidBody.getAngularVelocity() - tank2.getRotationSpeed());
        }
        
        tank1RigidBody.setLinearVelocity(0, 0);
        tank2RigidBody.setLinearVelocity(0, 0);
        
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            tank1RigidBody.setLinearVelocity((tank1.getDirection().x * tank1.getForwardSpeed() / tank1.getDirection().len()) * UNIT_SCALE,
                    (tank1.getDirection().y * tank1.getForwardSpeed() / tank1.getDirection().len()) * UNIT_SCALE);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) {
            tank2RigidBody.setLinearVelocity((tank2.getDirection().x * tank2.getForwardSpeed() / tank2.getDirection().len()) * UNIT_SCALE,
                    (tank2.getDirection().y * tank2.getForwardSpeed() / tank2.getDirection().len()) * UNIT_SCALE);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            tank1RigidBody.setLinearVelocity((-tank1.getDirection().x * tank1.getBackwardSpeed() / tank1.getDirection().len()) * UNIT_SCALE,
                    (-tank1.getDirection().y * tank1.getBackwardSpeed() / tank1.getDirection().len()) * UNIT_SCALE);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)) {
            tank2RigidBody.setLinearVelocity((-tank2.getDirection().x * tank2.getBackwardSpeed() / tank2.getDirection().len()) * UNIT_SCALE,
                    (-tank2.getDirection().y * tank2.getBackwardSpeed() / tank2.getDirection().len()) * UNIT_SCALE);
        }
        
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            tank1.shoot();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT) || Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT)) {
            tank2.shoot();
        }
    }
    
    public void update(float dt) throws InterruptedException {
        if (!tank1.getIsAlive() || !tank2.getIsAlive()) {
            tank1.setIsAlive(true);
            tank2.setIsAlive(true);
            newRound();
        }
        
        world.step(1 / 60f, 6, 2);
        handleInput();
        
        tank1.update(dt);
        tank2.update(dt);
    }

    @Override
    public void render(float dt) {
        try {
            update(dt);
        } catch (InterruptedException ex) {
            Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.sb.setProjectionMatrix(cam.combined);
        
        int tankWidth = tank1.getBodyTexture().getWidth();
        int tankHeight = tank1.getBodyTexture().getHeight();
        int turretWidth = tank1.getTurretTexture().getWidth();
        int turretHeight = tank1.getTurretTexture().getHeight();
       
        // Render maze and ground with adjustments for odd and even map sizes
        int[] layer1 = {0};
        int[] layer2 = {1};
        int[] layer3 = {2};
        
        float xOffset = (mazeSizeX % 2 == 0) ? 0 : 0.5f;
        float yOffset = (mazeSizeY % 2 == 0) ? 0 : 0.5f;
        
        cam.position.set((mazeSizeX / 2) + xOffset, (mazeSizeY / 2) + yOffset, 0);
        cam.update();
        groundRenderer.setView(cam);
        groundRenderer.render();
        cam.position.set((mazeSizeX / 2) + 0.5f + xOffset, (mazeSizeY / 2) + 0.5f + yOffset, 0);
        cam.update();
        mazeRenderer.setView(cam);
        mazeRenderer.render(layer1);
        cam.position.set((mazeSizeX / 2) + 0.5f + xOffset, (mazeSizeY / 2) + yOffset, 0);
        cam.update();
        mazeRenderer.setView(cam);
        mazeRenderer.render(layer2);
        cam.position.set((mazeSizeX / 2) + xOffset, (mazeSizeY / 2) + 0.5f + yOffset, 0);
        cam.update();
        mazeRenderer.setView(cam);
        mazeRenderer.render(layer3);
        
        // Sets camera position to centre and sets the viewport dimensions
        cam.position.set(mazeSizeX / 2, mazeSizeY / 2, 0);
        cam.update();
        
        // Render tank
        game.sb.begin();
        game.sb.draw(tank1.getBodyTexture(),
                tank1RigidBody.getWorldCenter().x - ((tank1.getBodyTexture().getWidth() / 2) * UNIT_SCALE),
                tank1RigidBody.getWorldCenter().y + ((5 - (tank1.getBodyTexture().getHeight() / 2)) * UNIT_SCALE),
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
        
        game.sb.draw(tank2.getBodyTexture(),
                tank2RigidBody.getWorldCenter().x - ((tank2.getBodyTexture().getWidth() / 2) * UNIT_SCALE),
                tank2RigidBody.getWorldCenter().y + ((5 - (tank2.getBodyTexture().getHeight() / 2)) * UNIT_SCALE),
                (tankWidth / 2) * UNIT_SCALE,
                ((tankHeight / 2) - 5) * UNIT_SCALE,
                tankWidth * UNIT_SCALE,
                tankHeight * UNIT_SCALE,
                1,
                1, 
                (float) (Math.toDegrees(tank2RigidBody.getAngle())), 
                0,
                0,
                tankWidth,
                tankHeight,
                false,
                false);
        
        for (int i = 0; i < tank1.getProjectiles().size(); i++) {
            projectile = (Projectile) tank1.getProjectiles().get(i);
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
        
        for (int i = 0; i < tank2.getProjectiles().size(); i++) {
            projectile = (Projectile) tank2.getProjectiles().get(i);
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
        
        game.sb.draw(tank1.getTurretTexture(),
                tank1RigidBody.getWorldCenter().x - (tank1.getTurretTexture().getWidth() / 2 * UNIT_SCALE), 
                tank1RigidBody.getWorldCenter().y - ((tank1.getTurretTexture().getHeight() - tank1.getBarrelLength()) * UNIT_SCALE),
                (turretWidth / 2) * UNIT_SCALE,
                (tank1.getTurretTexture().getHeight() - tank1.getBarrelLength()) * UNIT_SCALE,
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
        
        game.sb.draw(tank2.getTurretTexture(),
                tank2RigidBody.getWorldCenter().x - (tank2.getTurretTexture().getWidth() / 2 * UNIT_SCALE), 
                tank2RigidBody.getWorldCenter().y - ((tank2.getTurretTexture().getHeight() - tank2.getBarrelLength()) * UNIT_SCALE),
                (turretWidth / 2) * UNIT_SCALE,
                (tank2.getTurretTexture().getHeight() - tank2.getBarrelLength()) * UNIT_SCALE,
                turretWidth * UNIT_SCALE,
                turretHeight * UNIT_SCALE,
                1,
                1, (float) (Math.toDegrees(tank2.getRigidBody().getAngle())),
                0,
                0,
                turretWidth,
                turretHeight,
                false,
                false);  
        
        game.sb.end();
        
//        b2dr.render(world, cam.combined);
    }
    
    public void newRound() throws InterruptedException {
        Thread.sleep(1000);
        Random random = new Random();
        mazeSizeX = random.nextInt(maxWidth - minWidth + 1) + minWidth;
        mazeSizeY = random.nextInt(maxHeight - minHeight + 1) + minHeight;
        cam.viewportWidth = mazeSizeX + 0.25f;
        cam.viewportHeight = mazeSizeY + 0.25f;
        cam.update();
//        viewport.update((int)(mapSizeX / UNIT_SCALE), (int)(mapSizeY / UNIT_SCALE), true);
        viewport = new FitViewport(mazeSizeX + 0.25f, mazeSizeY + 0.25f, cam);
        Gdx.graphics.setWindowedMode(1920, 1080);
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        map = new MazeGenerator((int)mazeSizeX, (int)mazeSizeY);
        map.generateGround();
        map.generateMaze();
        LinkedList projectiles1 = tank1.getProjectiles();
        for (int i = 0; i < projectiles1.size(); i++) {
            Projectile projectile = (Projectile) projectiles1.get(i);
            projectile.dispose();
            projectiles1.remove(i);
        }
        LinkedList projectiles2 = tank2.getProjectiles();
        for (int i = 0; i < projectiles2.size(); i++) {
            Projectile projectile = (Projectile) projectiles2.get(i);
            projectile.dispose();
            projectiles2.remove(i);
        }
        hitboxParser.dispose();
        hitboxParser.parseMaze(world, map);
        groundRenderer = new OrthogonalTiledMapRenderer(map.getGroundMap(), UNIT_SCALE);
        mazeRenderer = new OrthogonalTiledMapRenderer(map.getMazeMap(), UNIT_SCALE);
        Vector2 tank1Spawn = randomSpawn();
        tank1.getRigidBody().setTransform(tank1Spawn, 0);
        Vector2 tank2Spawn = randomSpawn();
        while (tank2Spawn.x == tank1Spawn.x && tank2Spawn.y == tank1Spawn.y) {
            tank2Spawn = randomSpawn();
        }
        tank2.getRigidBody().setTransform(tank2Spawn, 0);
    }
    
    public Body createTankBody(Vector2 position) {
        Body pBody;
        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(position.x, position.y);
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

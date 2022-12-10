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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.TankTrauma;
import com.mygdx.game.gameobjects.Bullet;
import com.mygdx.game.gameobjects.Tank;
import com.mygdx.game.gameobjects.TankBody;
import com.mygdx.game.utils.MapGenerator;
import com.mygdx.game.gameobjects.TankTurret;
import com.mygdx.game.utils.MazeCollisionParser;
import java.util.LinkedList;

public class PlayScreen implements Screen {
    public static final float UNIT_SCALE = 1 / 128f;
    
    private final TankTrauma game;
    
    private final OrthographicCamera cam;
    private final Viewport viewport;
    
    private int mapSizeX;
    private int mapSizeY;
    
    private final Tank tank;
    private final TankBody body;
    private TankTurret turret;
    private Bullet bullet;
    private LinkedList<Bullet> playerBullets;
    
    private Box2DDebugRenderer b2dr;
    private World world;

    private Body player;
    
    private final MapGenerator map;
    
    private final OrthogonalTiledMapRenderer groundRenderer;
    private final OrthogonalTiledMapRenderer mazeRenderer;
    
    public PlayScreen(TankTrauma game, int mapSizeX, int mapSizeY) {
        this.game = game;
        
        cam = new OrthographicCamera();
        viewport = new FitViewport(((mapSizeX + 0.25f) * 16) / 9, mapSizeY + 0.25f, cam);
        
        this.mapSizeX = mapSizeX;
        this.mapSizeY = mapSizeY;
        
        b2dr = new Box2DDebugRenderer();
        world = new World(new Vector2(0, 0), false);
        player = createBox(5, 5, 76, 72, false);
        player.setSleepingAllowed(false);
        
        tank = new Tank("red", mapSizeX, mapSizeY, player);
        body = tank.getBody();
        turret = tank.getTurret();
        playerBullets = tank.getTurret().getBullets();
        
        map = new MapGenerator(mapSizeX, mapSizeY);
        map.generateGround();
        map.generateMaze();
        
        MazeCollisionParser.parseMapLayers(world, map);
        
        groundRenderer = new OrthogonalTiledMapRenderer(map.getGroundMap(), UNIT_SCALE);
        mazeRenderer = new OrthogonalTiledMapRenderer(map.getMazeMap(), UNIT_SCALE);
    }

    public void handleInput() {
        float bodyRotationSpeed = body.getRotationSpeed();
        float turretRotationSpeed = turret.getRotationSpeed();
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
        
        // Control turret and body rotation speeds, depending on user input
        if (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
//            player.setTransform(player.getLinearVelocity(), -bodyRotationSpeed);                  
            body.appendRotation(-bodyRotationSpeed);
            turret.appendRotation(-turretRotationSpeed - bodyRotationSpeed);
        } else if (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
//            player.setTransform(player.getLinearVelocity(), -bodyRotationSpeed);                  
            body.appendRotation(-bodyRotationSpeed);
            turret.appendRotation(turretRotationSpeed - bodyRotationSpeed);
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
//            player.setTransform(player.getLinearVelocity(), -bodyRotationSpeed);               
            body.appendRotation(-bodyRotationSpeed);
            turret.appendRotation(-bodyRotationSpeed);
        } else if ((Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) && !(Gdx.input.isKeyPressed(Input.Keys.D))) {
            turret.appendRotation(-turretRotationSpeed);
        } 
        
        // Use player for tankBody, define player as body inside tank body and apply all physics to the body instead of tankbody
        
        if (Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
//            player.setTransform(player.getLinearVelocity(), bodyRotationSpeed);            
            body.appendRotation(bodyRotationSpeed);
            turret.appendRotation(turretRotationSpeed + bodyRotationSpeed);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
//            player.setTransform(player.getLinearVelocity(), bodyRotationSpeed);            
            body.appendRotation(bodyRotationSpeed);
            turret.appendRotation(-turretRotationSpeed + bodyRotationSpeed);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
//            player.setTransform(player.getLinearVelocity(), bodyRotationSpeed);
            body.appendRotation(bodyRotationSpeed);
            turret.appendRotation(bodyRotationSpeed);
        } else if ((Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) && !(Gdx.input.isKeyPressed(Input.Keys.A))) {
            turret.appendRotation(turretRotationSpeed);
        }
        
        float resolvedBodyRotation = body.getResolvedRotation();
        
        player.setLinearVelocity(0, 0);
        
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            player.setLinearVelocity(
                    (float) (body.getDirectionX() * tank.getForwardSpeed() * java.lang.Math.sin(resolvedBodyRotation) * UNIT_SCALE), 
                    (float) (body.getDirectionY() * tank.getForwardSpeed() * java.lang.Math.cos(resolvedBodyRotation) * UNIT_SCALE));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            player.setLinearVelocity(
                    (float) (body.getDirectionX() * -tank.getBackwardSpeed() * java.lang.Math.sin(resolvedBodyRotation) * UNIT_SCALE), 
                    (float) (body.getDirectionY() * -tank.getBackwardSpeed() * java.lang.Math.cos(resolvedBodyRotation) * UNIT_SCALE));
        }
        
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) {
            turret.shoot(
                    (float) (turret.getTurretDirectionX() * java.lang.Math.sin(turret.getResolvedRotation())),
                    (float) (turret.getTurretDirectionY() * java.lang.Math.cos(turret.getResolvedRotation())));
        }
    }

    public void update(float dt) {
        world.step(1 / 60f, 6, 2);
        handleInput();
        tank.update(dt);
    }

    @Override
    public void render(float dt) {
        update(dt);
        
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.sb.setProjectionMatrix(cam.combined);
        
        int tankWidth = body.getTexture().getWidth();
        int tankHeight = body.getTexture().getHeight();
        int turretWidth = turret.getTexture().getWidth();
        int turretHeight = turret.getTexture().getHeight();
       
        // Render maze and ground with adjustments for odd and even map sizes
        int[] layer1 = {0};
        int[] layer2 = {1};
        int[] layer3 = {2};
        
        float xOffset = (map.getMapSizeX() % 2 == 0) ? 0 : 0.5f;
        float yOffset = (map.getMapSizeY() % 2 == 0) ? 0 : 0.5f;
        
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
        viewport.setCamera(cam);
        
        // Render tank
        game.sb.begin();
        game.sb.draw(
                body.getTexture(),
                player.getWorldCenter().x - ((body.getTexture().getWidth() / 2) * UNIT_SCALE),
                player.getWorldCenter().y - ((body.getTexture().getHeight() / 2) * UNIT_SCALE),
                (tankWidth / 2) * UNIT_SCALE,
                ((tankHeight / 2) - 5) * UNIT_SCALE,
                tankWidth * UNIT_SCALE,
                tankHeight * UNIT_SCALE,
                1,
                1,
                360 - body.getRotation(), 
                /* 360 - player.getAngle(), */
                0,
                0,
                tankWidth,
                tankHeight,
                false,
                false);
        
        for (int i = 0; i < turret.getBullets().size(); i++) {
            bullet = turret.getBullets().get(i);
            int bulletWidth = bullet.getTexture().getWidth();
            int bulletHeight = bullet.getTexture().getHeight();
            game.sb.draw(
                bullet.getTexture(),
                bullet.getPosition().x * UNIT_SCALE,
                bullet.getPosition().y * UNIT_SCALE,
                0, 
                0,
                bulletWidth * UNIT_SCALE,
                bulletHeight * UNIT_SCALE,
                1,
                1,
                -bullet.getRotation(),
                0,
                0,
                bulletWidth,
                bulletHeight,
                false,
                false);
        }
        
        game.sb.draw(
                turret.getTexture(),
                player.getWorldCenter().x - (turret.getTexture().getWidth() / 2 * UNIT_SCALE), 
                player.getWorldCenter().y - (13 * UNIT_SCALE),
                (turretWidth / 2) * UNIT_SCALE,
                (turret.getTexture().getHeight() - turret.getBarrelLength()) * UNIT_SCALE,
                turretWidth * UNIT_SCALE,
                turretHeight * UNIT_SCALE,
                1,
                1,
                360 - turret.getRotation(),
                0,
                0,
                turretWidth,
                turretHeight,
                false,
                false);        
        
        game.sb.draw(
                turret.getBulletTexture(),
                turret.getBarrelPosition().x * UNIT_SCALE, 
                turret.getBarrelPosition().y * UNIT_SCALE, 
                turret.getBulletTexture().getWidth() * UNIT_SCALE,
                turret.getBulletTexture().getHeight() * UNIT_SCALE);  
        
        game.sb.end();
        
        b2dr.render(world, cam.combined);
    }
    
    public Body createBox(float x, float y, int width, int height, boolean isStatic) {
        Body pBody;
        BodyDef def = new BodyDef();

        def.type = isStatic ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
        def.position.set(x, y);
        def.fixedRotation = true;
        // initializes the body and puts it into the box2d world
        // with the body definition properties
        pBody = world.createBody(def);

        // makes a 32 x 32 box (measure w and h from center)
        // setting positions and stuff -> / PPM
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 * UNIT_SCALE, height / 2 * UNIT_SCALE, new Vector2(0 , -5 * UNIT_SCALE), 0);

        // gives body the shape and a density
        pBody.createFixture(shape, 1);
        shape.dispose();
        return pBody;
    }
    
    @Override
    public void show() {
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
        tank.dispose();
    }
    
    public Viewport getViewport() {
        return viewport;
    }

    public World getWorld() {
        return world;
    }
}

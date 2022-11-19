package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.TankTrauma;
import com.mygdx.game.gameobjects.Bullet;
import com.mygdx.game.gameobjects.Tank;
import com.mygdx.game.gameobjects.TankBody;
import com.mygdx.game.gameobjects.MapGenerator;
import com.mygdx.game.gameobjects.TankTurret;
import java.util.LinkedList;

public class PlayScreen implements Screen {
    private static final float UNIT_SCALE = 1 / 128f;
    private final TankTrauma game;
    private final OrthographicCamera cam;
    private final Viewport viewport;
    private final Tank player;
    private final TankBody body;
    private TankTurret turret;
    private final OrthogonalTiledMapRenderer groundRenderer;
    private final OrthogonalTiledMapRenderer mazeRenderer;
    private final MapGenerator Map;
    private LinkedList<Bullet> playerBullets;
    private Bullet bullet;
    private int mapSize;
    
    public PlayScreen(TankTrauma game, int mapSize) {
        this.game = game;
        cam = new OrthographicCamera();
        viewport = new FitViewport(((mapSize + 1) * 16) / 9, mapSize + 1, cam);
        player = new Tank(50, 400, "green");
        body = player.getBody();
        turret = player.getTurret();
        Map = new MapGenerator(mapSize);
        Map.generateGround();
        Map.generateMaze();
        groundRenderer = new OrthogonalTiledMapRenderer(Map.getGroundMap(), UNIT_SCALE);
        mazeRenderer = new OrthogonalTiledMapRenderer(Map.getMazeMap(), UNIT_SCALE);
        playerBullets = player.getTurret().getBullets();
        this.mapSize = mapSize;
    }

    public void handleInput() {
        float bodyRotationSpeed = body.getRotationSpeed();
        float turretRotationSpeed = turret.getRotationSpeed();
        
        // Control turret and body rotation speeds, depending on user input
        if (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
            body.appendRotation(-bodyRotationSpeed);
            turret.appendRotation(-turretRotationSpeed - bodyRotationSpeed);
        } else if (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
            body.appendRotation(-bodyRotationSpeed);
            turret.appendRotation(turretRotationSpeed - bodyRotationSpeed);
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            body.appendRotation(-bodyRotationSpeed);
            turret.appendRotation(-bodyRotationSpeed);
        } else if ((Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) && !(Gdx.input.isKeyPressed(Input.Keys.D))) {
            turret.appendRotation(-turretRotationSpeed);
        } 
        
        if (Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
            body.appendRotation(bodyRotationSpeed);
            turret.appendRotation(turretRotationSpeed + bodyRotationSpeed);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
            body.appendRotation(bodyRotationSpeed);
            turret.appendRotation(-turretRotationSpeed + bodyRotationSpeed);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            body.appendRotation(bodyRotationSpeed);
            turret.appendRotation(bodyRotationSpeed);
        } else if ((Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) && !(Gdx.input.isKeyPressed(Input.Keys.A))) {
            turret.appendRotation(turretRotationSpeed);
        }
        
        float resolvedBodyRotation = body.getResolvedRotation();
        
        // Apply normalised x and y velocities if W or S is pressed
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            player.appendVelocity(
                    (float) (body.getDirectionX() * player.getForwardVelocity() * java.lang.Math.sin(resolvedBodyRotation)),
                    (float) (body.getDirectionY() * player.getForwardVelocity() * java.lang.Math.cos(resolvedBodyRotation)));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            player.appendVelocity(
                    (float) (body.getDirectionX() * player.getBackwardVelocity() * java.lang.Math.sin(resolvedBodyRotation)),
                    (float) (body.getDirectionY() * player.getBackwardVelocity() * java.lang.Math.cos(resolvedBodyRotation)));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) {
            turret.shoot(
                    (float) (turret.getTurretDirectionX() * java.lang.Math.sin(turret.getResolvedRotation())),
                    (float) (turret.getTurretDirectionY() * java.lang.Math.cos(turret.getResolvedRotation())));
        }
        
        // Prevent tank from moving off the screen
//        if (body.getPosition().y < 4) {
//            body.setPositionY(4);
//            turret.setPositionY(3);
//        } else if (body.getPosition().y > 7 / UNIT_SCALE) {
//            body.setPositionY(7 / UNIT_SCALE);
//            turret.setPositionY((7 / UNIT_SCALE) - 1);
//        }
//        if (body.getPosition().x < 10) {
//            body.setPositionX(10);
//            turret.setPositionX(10 + ((body.getTexture().getWidth() - turret.getTurretTexture().getWidth()) / 2));
//        } 
////        else if (body.getPosition().x > viewport.getScreenWidth()) {
////            body.setPositionX(viewport.getScreenWidth());
////            turret.setPositionX(viewport.getScreenWidth() - (body.getTexture().getWidth() - turret.getTurretTexture().getWidth()) / 2);
////        }
    }

    public void update(float dt) {
        handleInput();
        player.update(dt);
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
        

        // Render ground
        cam.position.set((float)mapSize / 2, (float)mapSize / 2, 0);
        cam.update();
        groundRenderer.setView(cam);
        groundRenderer.render();
        
        // Render maze
//        cam.position.set((float)((mapSize / 2) - 64), (float)((mapSize / 2) + 64), 0);
//        mazeRenderer.setView(cam);
//        mazeRenderer.getBatch().begin();
//        mazeRenderer.renderTileLayer(Map.getVerticalLayer());
        
//        mazeRenderer.setView(cam);
//        mazeRenderer.renderTileLayer(Map.getHorizontalLayer());
        
//        mazeRenderer.renderTileLayer(Map.getDotLayer());
//        mazeRenderer.getBatch().end();
        int[] layer1 = {0};
        int[] layer2 = {1};
        int[] layer3 = {2};
        
        cam.position.set((float)(mapSize / 2) + (128 * UNIT_SCALE), (float)(mapSize / 2) + (128 * UNIT_SCALE), 0);
        cam.update();
        mazeRenderer.setView(cam);
        mazeRenderer.render(layer1);
        cam.position.set((float)(mapSize / 2) + (128 * UNIT_SCALE), (float)(mapSize / 2) + (64 * UNIT_SCALE), 0);
        cam.update();
        mazeRenderer.setView(cam);
        mazeRenderer.render(layer2);
        cam.position.set((float)(mapSize / 2) + (64 * UNIT_SCALE), (float)(mapSize / 2) + (128 * UNIT_SCALE), 0);
        cam.update();
        mazeRenderer.setView(cam);
        mazeRenderer.render(layer3);
        
        // Reset camera position to centre
        cam.position.set((float)mapSize / 2, (float)mapSize / 2, 0);
        
        // Render tank
        game.sb.begin();
        game.sb.draw(
                body.getTexture(),
                body.getPosition().x * UNIT_SCALE,
                body.getPosition().y * UNIT_SCALE,
                (tankWidth / 2) * UNIT_SCALE,
                ((tankHeight / 2) + 7) * UNIT_SCALE,
                tankWidth * UNIT_SCALE,
                tankHeight * UNIT_SCALE,
                1,
                1,
                180-body.getRotation(), 
                0,
                0,
                tankWidth,
                tankHeight,
                false,
                false);
        
        
        game.sb.draw(turret.getTexture(),
                turret.getPosition().x * UNIT_SCALE,
                turret.getPosition().y * UNIT_SCALE,
                (turretWidth / 2) * UNIT_SCALE,
                turret.getBarrelLength() * UNIT_SCALE,
                turretWidth * UNIT_SCALE,
                turretHeight * UNIT_SCALE,
                1,
                1,
                180-turret.getRotation(),
                0,
                0,
                turretWidth,
                turretHeight,
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
        game.sb.end();
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
    }
    
    public Viewport getViewport() {
        return viewport;
    }

            
}

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
import com.mygdx.game.gameobjects.Ground;
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
    private final OrthogonalTiledMapRenderer renderer;
    private final Ground tilemap;
    private LinkedList<Bullet> playerBullets;
    private Bullet bullet;
    
    public PlayScreen(TankTrauma game) {
        this.game = game;
        cam = new OrthographicCamera();
        viewport = new FitViewport(128 / 9, 8, cam);
        player = new Tank(50, 400, "red");
        body = player.getBody();
        turret = player.getTurret();
        tilemap = new Ground();
        tilemap.generateTilemap();
        renderer = new OrthogonalTiledMapRenderer(tilemap.getTileMap(), UNIT_SCALE);
        playerBullets = player.getTurret().getBullets();
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
        int turretWidth = turret.getTurretTexture().getWidth();
        int turretHeight = turret.getTurretTexture().getHeight();
        
        // Render ground tilemap
        cam.position.set(UNIT_SCALE * viewport.getScreenWidth() / 2, UNIT_SCALE * viewport.getScreenHeight() / 2, 0);
        renderer.setView(cam);
        renderer.render();
        
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
        
        for (int i = 0; i < turret.getBullets().size(); i++) {
            bullet = turret.getBullets().get(i);
            int bulletWidth = bullet.getTexture().getWidth();
            int bulletHeight = bullet.getTexture().getHeight();
            game.sb.draw(
                bullet.getTexture(),
                bullet.getPosition().x * UNIT_SCALE,
                bullet.getPosition().y * UNIT_SCALE,
                (bulletWidth / 2) * UNIT_SCALE, 
                (bulletHeight / 2) * UNIT_SCALE,
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
        
        game.sb.draw(turret.getTurretTexture(),
                turret.getPosition().x * UNIT_SCALE,
                turret.getPosition().y * UNIT_SCALE,
                (turretWidth / 2) * UNIT_SCALE,
                44 * UNIT_SCALE,
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

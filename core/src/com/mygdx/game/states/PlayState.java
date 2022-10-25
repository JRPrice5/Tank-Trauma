package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.sprites.Tank;
import com.mygdx.game.sprites.Tilemap;

public class PlayState extends State {
    private static float unitScale = 1 / 128f;
    private int tankWidth;
    private int tankHeight;
    private int turretWidth;
    private int turretHeight;
    private Tank player;
    private OrthogonalTiledMapRenderer renderer;
    private Tilemap tilemap;
    private Viewport viewport;
    
    public PlayState(GameStateManager gsm, Viewport viewport) {
        super(gsm);
        player = new Tank(50, 400);
        tankWidth = player.getBody().getWidth();
        tankHeight = player.getBody().getHeight();
        turretWidth = player.getTurret().getWidth();
        turretHeight = player.getTurret().getHeight();
        cam.setToOrtho(false, 8, 8);
        this.viewport = viewport;
        tilemap = new Tilemap();
        tilemap.randomiseTilemap();
        renderer = new OrthogonalTiledMapRenderer(tilemap.getGround(), unitScale);
    }

//    @Override
//    public void resize(int width, int height) {
//        viewport.update(width, height);
//        cam.position.set(viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2, 0);
//    }
    
    @Override
    public void handleInput() {
        // Control turret and body rotation speeds, depending on user input
        if (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
            player.appendBodyRotation(-player.getBodyRotationSpeed());
            player.appendTurretRotation(-player.getTurretRotationSpeed());
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.appendBodyRotation(-player.getBodyRotationSpeed());
            player.appendTurretRotation(-player.getBodyRotationSpeed());
        } else if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
            player.appendTurretRotation(-player.getTurretRotationSpeed());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
            player.appendBodyRotation(player.getBodyRotationSpeed());
            player.appendTurretRotation(player.getTurretRotationSpeed());
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.appendBodyRotation(player.getBodyRotationSpeed());
            player.appendTurretRotation(player.getBodyRotationSpeed());
        } else if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
            player.appendTurretRotation(player.getTurretRotationSpeed());
        }
        
        // Correct rotations that are greater than 360 or smaller than 0
        if (player.getBodyRotation() > 359) {
            player.appendBodyRotation(-360);
        } else if (player.getBodyRotation() < 0) {
            player.appendBodyRotation(360);
        }
        if (player.getTurretRotation() > 359) {
            player.appendTurretRotation(-360);
        } else if (player.getTurretRotation() < 0) {
            player.appendTurretRotation(360);
        }
        
        byte directionX = 0;
        byte directionY = 0;
        float resolvedBodyRotation = 0;
        if (player.getBodyRotation() < 90) {
            resolvedBodyRotation = (float) Math.toRadians(player.getBodyRotation());
            directionX = 1;
            directionY = 1;
        } else if (player.getBodyRotation() < 180) {
            resolvedBodyRotation = (float) Math.toRadians(180 - player.getBodyRotation()); 
            directionX = 1;
            directionY = -1;
        } else if (player.getBodyRotation() < 270) {
            resolvedBodyRotation = (float) Math.toRadians(player.getBodyRotation() - 180);
            directionX = -1;
            directionY = -1;
        } else if (player.getBodyRotation() < 360) {
            resolvedBodyRotation = (float) Math.toRadians(360 - player.getBodyRotation());
            directionX = -1;
            directionY = 1;                   
        }
        
        // Apply normalised x and y velocities if W or S is pressed
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            player.appendVelocity(
                    (float) (directionX * player.getForwardSpeed() * java.lang.Math.sin(resolvedBodyRotation)),
                    (float) (directionY * player.getForwardSpeed() * java.lang.Math.cos(resolvedBodyRotation)));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            player.appendVelocity(
                    -(float) (directionX * player.getForwardSpeed() * java.lang.Math.sin(resolvedBodyRotation)),
                    -(float) (directionY * player.getForwardSpeed() * java.lang.Math.cos(resolvedBodyRotation)));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        player.update(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
//        resize(1920, 1080);
        renderer.setView(cam);
        sb.setProjectionMatrix(cam.combined);
        renderer.render();
        sb.begin();
        sb.draw(
                player.getBody(),
                player.getBodyPosition().x * unitScale,
                player.getBodyPosition().y * unitScale,
                tankWidth * unitScale / 2,
                ((tankHeight / 2) + 7) * unitScale,
                tankWidth * unitScale,
                tankHeight * unitScale,
                1,
                1,
                180-player.getBodyRotation(), 
                0,
                0,
                tankWidth,
                tankHeight,
                false,
                false);
        sb.draw(
                player.getTurret(),
                player.getTurretPosition().x * unitScale,
                player.getTurretPosition().y * unitScale,
                (turretWidth / 2) * unitScale,
//                (tankHeight / 2) * unitScale,
                44 * unitScale,
                turretWidth * unitScale,
                turretHeight * unitScale,
                1,
                1,
                180-player.getTurretRotation(),
                0,
                0,
                turretWidth,
                turretHeight,
                false,
                false);
        sb.end();
    }

    @Override
    public void dispose() {
    }
}

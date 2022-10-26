package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.prefabs.Tank;
import com.mygdx.game.prefabs.TankBody;
import com.mygdx.game.prefabs.Ground;
import com.mygdx.game.prefabs.TankTurret;

public class PlayState extends State {
    private static float unitScale = 1 / 128f;
    private Tank player;
    private TankBody body;
    private TankTurret turret;
    private OrthogonalTiledMapRenderer renderer;
    private Ground tilemap;
    private Viewport viewport;
    
    public PlayState(GameStateManager gsm, Viewport viewport) {
        super(gsm);
        player = new Tank(50, 400, "green");
        body = player.getBody();
        turret = player.getTurret();
        cam.setToOrtho(false, 8, 8);
        this.viewport = viewport;
        tilemap = new Ground();
        tilemap.generateTilemap();
        renderer = new OrthogonalTiledMapRenderer(tilemap.getTileMap(), unitScale);
    }

//    @Override
//    public void resize(int width, int height) {
//        viewport.update(width, height);
//        cam.position.set(viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2, 0);
//    }
    
    @Override
    public void handleInput() {
        int bodyRotationSpeed = body.getRotationSpeed();
        int turretRotationSpeed = turret.getRotationSpeed();
        
        // Control turret and body rotation speeds, depending on user input
        if (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
            body.appendRotation(-bodyRotationSpeed);
            turret.appendRotation(-turretRotationSpeed);
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            body.appendRotation(-bodyRotationSpeed);
            turret.appendRotation(-bodyRotationSpeed);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
            turret.appendRotation(-turretRotationSpeed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
            body.appendRotation(bodyRotationSpeed);
            turret.appendRotation(turretRotationSpeed);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            body.appendRotation(bodyRotationSpeed);
            turret.appendRotation(bodyRotationSpeed);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
            turret.appendRotation(turretRotationSpeed);
        }
        
        int bodyRotation = body.getRotation();
        int turretRotation = turret.getRotation();
        
        // Correct rotations that are greater than 360 or smaller than 0
        if (bodyRotation > 359) {
            body.appendRotation(-360);
        } else if (bodyRotation < 0) {
            body.appendRotation(360);
        }
        if (turretRotation > 359) {
            turret.appendRotation(-360);
        } else if (turretRotation < 0) {
            turret.appendRotation(360);
        }
        
        byte directionX = 0;
        byte directionY = 0;
        float resolvedBodyRotation = 0;
        if (bodyRotation < 90) {
            resolvedBodyRotation = (float) Math.toRadians(bodyRotation);
            directionX = 1;
            directionY = 1;
        } else if (bodyRotation < 180) {
            resolvedBodyRotation = (float) Math.toRadians(180 - bodyRotation); 
            directionX = 1;
            directionY = -1;
        } else if (bodyRotation < 270) {
            resolvedBodyRotation = (float) Math.toRadians(bodyRotation - 180);
            directionX = -1;
            directionY = -1;
        } else if (bodyRotation < 360) {
            resolvedBodyRotation = (float) Math.toRadians(360 - bodyRotation);
            directionX = -1;
            directionY = 1;                   
        }
        
        int forwardVelocity = player.getForwardVelocity();
        int backwardVelocity = player.getBackwardVelocity();
        
        // Apply normalised x and y velocities if W or S is pressed
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            player.appendVelocity(
                    (float) (directionX * forwardVelocity * java.lang.Math.sin(resolvedBodyRotation)),
                    (float) (directionY * forwardVelocity * java.lang.Math.cos(resolvedBodyRotation)));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            player.appendVelocity(
                    (float) (directionX * backwardVelocity * java.lang.Math.sin(resolvedBodyRotation)),
                    (float) (directionY * backwardVelocity * java.lang.Math.cos(resolvedBodyRotation)));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        player.update(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        int tankWidth = body.getTexture().getWidth();
        int tankHeight = body.getTexture().getHeight();
        int turretWidth = turret.getTexture().getWidth();
        int turretHeight = turret.getTexture().getHeight();
        
        // Render ground tilemap
        renderer.setView(cam);
        sb.setProjectionMatrix(cam.combined);
        renderer.render();
        
        // Render tank
        sb.begin();
        sb.draw(
                body.getTexture(),
                body.getPosition().x * unitScale,
                body.getPosition().y * unitScale,
                tankWidth * unitScale / 2,
                ((tankHeight / 2) + 7) * unitScale,
                tankWidth * unitScale,
                tankHeight * unitScale,
                1,
                1,
                180-player.getBody().getRotation(), 
                0,
                0,
                tankWidth,
                tankHeight,
                false,
                false);
        sb.draw(
                player.getTurret().getTexture(),
                player.getTurret().getPosition().x * unitScale,
                player.getTurret().getPosition().y * unitScale,
                (turretWidth / 2) * unitScale,
//                (tankHeight / 2) * unitScale,
                44 * unitScale,
                turretWidth * unitScale,
                turretHeight * unitScale,
                1,
                1,
                180-player.getTurret().getRotation(),
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

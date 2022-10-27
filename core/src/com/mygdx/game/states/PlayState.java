package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.gameObjects.Bullet;
import com.mygdx.game.gameObjects.Tank;
import com.mygdx.game.gameObjects.TankBody;
import com.mygdx.game.gameObjects.Ground;
import com.mygdx.game.gameObjects.TankTurret;
import java.util.LinkedList;

public class PlayState extends State {
    private static float unitScale = 1 / 128f;
    private Tank player;
    private TankBody body;
    private TankTurret turret;
    private OrthogonalTiledMapRenderer renderer;
    private Ground tilemap;
    private static Viewport viewport;
    private LinkedList<Bullet> playerBullets;
    private Bullet bullet;
    
    public PlayState(GameStateManager gsm, Viewport viewport) {
        super(gsm);
        player = new Tank(50, 400, "red");
        body = player.getBody();
        turret = player.getTurret();
        cam.setToOrtho(false, 8, 8);
        this.viewport = viewport;
        tilemap = new Ground();
        tilemap.generateTilemap();
        renderer = new OrthogonalTiledMapRenderer(tilemap.getTileMap(), unitScale);
        playerBullets = player.getTurret().getBullets();
    }

//    @Override
//    public void resize(int width, int height) {
//        viewport.update(width, height);
//        cam.position.set(viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2, 0);
//    }
    
    @Override
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

    @Override
    public void update(float dt) {
        handleInput();
        player.update(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        int tankWidth = body.getTexture().getWidth();
        int tankHeight = body.getTexture().getHeight();
        int turretWidth = turret.getTurretTexture().getWidth();
        int turretHeight = turret.getTurretTexture().getHeight();
        
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
                (tankWidth / 2) * unitScale,
                ((tankHeight / 2) + 7) * unitScale,
                tankWidth * unitScale,
                tankHeight * unitScale,
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
            sb.draw(
                bullet.getTexture(),
                bullet.getPosition().x * unitScale,
                bullet.getPosition().y * unitScale,
                (bulletWidth / 2) * unitScale, 
                (bulletHeight / 2) * unitScale,
                bulletWidth * unitScale,
                bulletHeight * unitScale,
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
        
        sb.draw(turret.getTurretTexture(),
                turret.getPosition().x * unitScale,
                turret.getPosition().y * unitScale,
                (turretWidth / 2) * unitScale,
                44 * unitScale,
                turretWidth * unitScale,
                turretHeight * unitScale,
                1,
                1,
                180-turret.getRotation(),
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
    
    public static Viewport getViewport() {
        return viewport;
    }
            
}

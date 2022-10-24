package com.mygdx.game.states;

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
    private Tank tank;
    private OrthogonalTiledMapRenderer renderer;
    private Tilemap tilemap;
    private Viewport viewport;
    
    public PlayState(GameStateManager gsm, Viewport viewport) {
        super(gsm);
        tank = new Tank(50, 400);
        tankWidth = tank.getBody().getWidth();
        tankHeight = tank.getBody().getHeight();
        turretWidth = tank.getTurret().getWidth();
        turretHeight = tank.getTurret().getHeight();
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
    }

    @Override
    public void update(float dt) {
        tank.update(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
//        resize(1920, 1080);
        renderer.setView(cam);
        sb.setProjectionMatrix(cam.combined);
        renderer.render();
        sb.begin();
        sb.draw(
                tank.getBody(),
                tank.getBodyPosition().x * unitScale,
                tank.getBodyPosition().y * unitScale,
                tankWidth * unitScale / 2,
                ((tankHeight / 2) + 7) * unitScale,
                tankWidth * unitScale,
                tankHeight * unitScale,
                1,
                1,
                180-tank.getBodyRotation(), 
                0,
                0,
                tankWidth,
                tankHeight,
                false,
                false);
        sb.draw(
                tank.getTurret(),
                tank.getTurretPosition().x * unitScale,
                tank.getTurretPosition().y * unitScale,
                (turretWidth / 2) * unitScale,
//                (tankHeight / 2) * unitScale,
                44 * unitScale,
                turretWidth * unitScale,
                turretHeight * unitScale,
                1,
                1,
                180-tank.getTurretRotation(),
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

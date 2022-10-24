package com.mygdx.game.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.sprites.Tank;
import com.mygdx.game.sprites.Tilemap;

public class PlayState extends State {
    private float unitScale = 1 / 128f;
    private int TWidth;
    private int THeight;
    private Tank tank;
    private OrthogonalTiledMapRenderer renderer;
    private Tilemap tilemap;
    private Viewport viewport;
    
    public PlayState(GameStateManager gsm, Viewport viewport) {
        super(gsm);
        tank = new Tank(50, 400);
        TWidth = tank.getTexture().getWidth();
        THeight = tank.getTexture().getHeight();
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
        sb.draw(tank.getTexture(), tank.getPosition().x * unitScale, tank.getPosition().y * unitScale, TWidth * unitScale / 2,
                THeight * unitScale / 2, TWidth * unitScale, THeight * unitScale,
                1, 1, -tank.getRotation(), 0, 0, TWidth , THeight, false, false);
        sb.end();
    }

    @Override
    public void dispose() {
    }
}

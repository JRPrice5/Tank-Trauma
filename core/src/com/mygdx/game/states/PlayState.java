package com.mygdx.game.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.TankDemo;
import com.mygdx.game.sprites.Tank;
import com.mygdx.game.sprites.Tilemap;

public class PlayState extends State {
    private float unitScale = 1 / 256f;
    private Tank tank;
    private OrthogonalTiledMapRenderer renderer;
    private Tilemap tilemap;
    private Viewport viewport;
    
    public PlayState(GameStateManager gsm, Viewport viewport) {
        super(gsm);
        tank = new Tank(50, 300);
        cam.setToOrtho(false, 8, 8);
        this.viewport = viewport;
        tilemap = new Tilemap();
        tilemap.randomiseTilemap();
        renderer = new OrthogonalTiledMapRenderer(tilemap.getGround(), unitScale);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        cam.position.set(cam.viewportWidth / 2,cam.viewportHeight / 2, 0);
    }
    
    @Override
    public void handleInput() {
        // if(Gdx.input.justTouched())
            // tank.move();
    }

    @Override
    public void update(float dt) {
//        handleInput();
        tank.update(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        renderer.setView(cam);
        sb.setProjectionMatrix(cam.combined);
        renderer.render();
    }

    @Override
    public void dispose() {
    }
}

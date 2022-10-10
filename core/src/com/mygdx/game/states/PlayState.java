package com.mygdx.game.states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.mygdx.game.TankDemo;
import com.mygdx.game.sprites.Tank;
import com.mygdx.game.sprites.Tilemap;

public class PlayState extends State {
    private final float unitScale = 1 / 16f;
    private Tank tank;
    private Texture bg;
    private OrthogonalTiledMapRenderer renderer;
    private Tilemap tilemap;
    
    public PlayState(GameStateManager gsm) {
        super(gsm);
        tank = new Tank(50, 300);
        cam.setToOrtho(false, TankDemo.WIDTH / 16, TankDemo.HEIGHT / 16);
        tilemap = new Tilemap();
        renderer = new OrthogonalTiledMapRenderer(tilemap.getGround(), unitScale);
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
        sb.setProjectionMatrix(cam.combined);
        renderer.render();
    }

    @Override
    public void dispose() {
        bg.dispose();
    }
}

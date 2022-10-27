package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.TankDemo;

public class MenuState extends State {
    private Texture background;
    private Texture playBtn;
    private Viewport viewport;
    
    public MenuState(GameStateManager gsm) {
        super(gsm);
        background = new Texture("bg.png");
        playBtn = new Texture("playBtn.png");
        viewport = new FitViewport(TankDemo.WIDTH, TankDemo.HEIGHT, cam);
    }
    
//    @Override
//    public void resize(int width, int height) {
//        viewport.update(width, height);
//        cam.position.set(viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2, 0);
//    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            gsm.set(new PlayState(gsm, viewport));
            dispose();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
//        resize(600, 600);
//        cam.update();
        sb.setProjectionMatrix(cam.combined);
//        sb.draw(background, 0, 0, 400, 800);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        playBtn.dispose();
    }
}

package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.TankTrauma;

public class MenuScreen implements Screen {
    private TankTrauma game;
    private OrthographicCamera cam;
    private Viewport viewport;
    private Texture background;
    private int maxWidth;
    private int minWidth;
    private int maxHeight;
    private int minHeight;
    
    public MenuScreen(TankTrauma game) {
        this.game = game;
        cam = new OrthographicCamera();
        viewport = new FitViewport(TankTrauma.WIDTH, TankTrauma.HEIGHT, cam);
        background = new Texture("bg.png");
        maxWidth = 2;
        minWidth = 2;
        maxHeight = 2;
        minHeight = 2;
    }
    
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            game.setScreen(new GameScreen(game, maxWidth, minWidth, maxHeight, minHeight));
            dispose();
        }
    }

    public void update() {
        handleInput();
    }

    @Override
    public void render(float dt) {
        update();
        
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.sb.setProjectionMatrix(cam.combined);
        
//        game.sb.begin();
//        sb.draw(gui etc);
//        game.sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
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
}

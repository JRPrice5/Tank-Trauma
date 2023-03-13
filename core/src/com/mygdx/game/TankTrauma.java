package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.screens.MenuScreen;

public class TankTrauma extends Game {
        public static final String TITLE = "Tank Trauma";
        public static final int WIDTH = 1920;
        public static final int HEIGHT = 1080;
	public SpriteBatch sb;
	
        @Override
	public void create () {
		sb = new SpriteBatch();
                Gdx.gl.glClearColor(1, 1, 1, 1);
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                setScreen(new MenuScreen(this));
	}

        @Override
	public void render () {
            super.render();
	}
        
        @Override
        public void dispose() {
            sb.dispose();
        }
}

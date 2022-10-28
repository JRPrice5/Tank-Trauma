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
	
	public void create () {
		sb = new SpriteBatch();
                setScreen(new MenuScreen(this));
                Gdx.gl.glClearColor(0, 0, 0, 1);
	}

	public void render () {
            super.render();
	}
}

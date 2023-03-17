package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.screens.MenuScreen;

public class TankTrauma extends Game {
        public static final String TITLE = "Tank Trauma";
        public static final int WIDTH = 1920;
        public static final int HEIGHT = 1080;
	public SpriteBatch sb;
	
        @Override
	public void create () {
            Preferences prefs = Gdx.app.getPreferences("My Preferences");
            prefs.putInteger("minWidth", 10);
            prefs.putInteger("maxWidth", 10);
            prefs.putInteger("minHeight", 10);
            prefs.putInteger("maxHeight", 10);
            prefs.putString("tileType", "random");
            prefs.putString("tank1Colour", "red");
            prefs.putString("tank2Colour", "dark");
            prefs.flush();
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

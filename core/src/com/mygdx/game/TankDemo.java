package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.states.GameStateManager;
import com.mygdx.game.states.MenuState;

public class TankDemo extends ApplicationAdapter {
        public static final String TITLE = "Tank Trauma";
        public static final int WIDTH = 480;
        public static final int HEIGHT = 800;
        private GameStateManager gsm;
	private SpriteBatch sb;
	
	@Override
	public void create () {
		sb = new SpriteBatch();
                gsm = new GameStateManager();
                Gdx.gl.glClearColor(0, 0, 0, 1);
                gsm.push(new MenuState(gsm));
	}

	@Override
	public void render () {
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            // Wipes the screen before each render
            gsm.update(Gdx.graphics.getDeltaTime());
            gsm.render(sb);
	}
}

package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.TankTrauma;

public class MenuScreen implements Screen {
    private TankTrauma game;
    private Skin skin;
    private Stage stage;
    private Table table;
    private Viewport viewport;
    private OrthographicCamera cam;
    private Texture background;
    
    private final TextButton startButton;
    private final TextButton settingsButton;
    private final TextButton quitButton;
    
    public MenuScreen(final TankTrauma game) {
        this.game = game;
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        cam = new OrthographicCamera();
        viewport = new ScreenViewport(cam);
        stage = new Stage(viewport);
        background = new Texture("background1.png");
        
        startButton = new TextButton("START", skin);
        settingsButton = new TextButton("SETTINGS", skin);
        quitButton = new TextButton("QUIT", skin);
        
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(new GameScreen(game));
            }
        });
        
        settingsButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               game.setScreen(new SettingsScreen(game));
           }
        });
        
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        
        table = new Table();
        
        table.add(startButton).padBottom(40).minSize(330, 80);
        table.row();
        table.add(settingsButton).padBottom(40).minSize(330, 80);
        table.row();
        table.add(quitButton).minSize(330, 80);
        
        table.padLeft(40);
        table.setWidth(stage.getWidth());
        table.align(Align.left|Align.top);
        table.setPosition(0, Gdx.graphics.getHeight() * 0.75f);
        
        stage.addActor(table);
        
        Gdx.input.setInputProcessor(stage);
    }
    
    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.sb.setProjectionMatrix(cam.combined);
        
        game.sb.begin();
        game.sb.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.sb.end();      
        
        stage.act(dt);
        stage.draw();
    }

    @Override
    public void dispose() {
        background.dispose();
        skin.dispose();
        stage.dispose();
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

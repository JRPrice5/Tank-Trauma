package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.TankTrauma;

public class SettingsScreen implements Screen {
    private TankTrauma game;
    private Skin skin;
    private Stage stage;
    private Table buttonTable;
    private Table settingsTable;
    private Viewport viewport;
    private OrthographicCamera cam;
    private Texture background;
    
//    private final TextButton audioButton;
    private final TextButton gameplayButton;
    private final TextButton backButton;
    
    public SettingsScreen(final TankTrauma game) {
        this.game = game;
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        cam = new OrthographicCamera();
        viewport = new ScreenViewport(cam);
        stage = new Stage(viewport);
        background = new Texture("background1.png");
        
//        audioButton = new TextButton("AUDIO", skin);
        gameplayButton = new TextButton("GAMEPLAY", skin);
        backButton = new TextButton("BACK", skin);
        
//        audioButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                
//            }
//        });
        
        gameplayButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               CheckBox test = new CheckBox("test", skin);
               settingsTable.add(test).pad(100);
           }
        });
        
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });
        
        buttonTable = new Table();
        settingsTable = new Table();
        
        settingsTable.align(Align.center);
        settingsTable.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        stage.addActor(settingsTable);
        
//        table.add(audioButton).padBottom(40).minSize(330, 80);
//        table.row();
        buttonTable.add(gameplayButton).padBottom(40).minSize(330, 80);
        buttonTable.row();
        buttonTable.add(backButton).minSize(330, 80);
        
        buttonTable.padLeft(40);
        buttonTable.setWidth(stage.getWidth());
        buttonTable.align(Align.left|Align.top);
        buttonTable.setPosition(0, Gdx.graphics.getHeight() * 0.75f);
        
        stage.addActor(buttonTable);
        
        Gdx.input.setInputProcessor(stage);
    }
    
    @Override
    public void show() {
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
    public void resize(int i, int i1) {
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

    @Override
    public void dispose() {
        background.dispose();
        skin.dispose();
        stage.dispose();
    }
}

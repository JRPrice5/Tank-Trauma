package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.TankTrauma;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SettingsScreen implements Screen {
    private TankTrauma game;
    private Skin skin;
    private Stage stage;
    private Table rootTable;
    private Table settingsTable;
    private Table buttonTable;
    private Viewport viewport;
    private OrthographicCamera cam;
    private Texture background;
    private Boolean validated;
    
    private final TextButton gameplayButton;
    private final TextButton backButton;
    private final TextButton saveButton;
    
    public SettingsScreen(final TankTrauma game) {
        this.game = game;
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        cam = new OrthographicCamera();
        viewport = new ScreenViewport(cam);
        stage = new Stage(viewport);
        background = new Texture("background1.png");
        validated = true;
        
        gameplayButton = new TextButton("GAMEPLAY", skin);
        backButton = new TextButton("BACK", skin);
        saveButton = new TextButton("SAVE", skin);
        final Dialog dialog = new Dialog("Width and/or height values are invalid. ", skin);
        
        Label minWidthLabel = new Label("Maze Width (2 - 15) anywhere from:", skin);
        final TextField minWidthField = new TextField("", skin);
        Label maxWidthLabel = new Label("to:", skin);
        final TextField maxWidthField = new TextField("", skin);
        Label minHeightLabel = new Label("Maze Height (2 - 15) anywhere from:", skin);
        final TextField minHeightField = new TextField("", skin);
        Label maxHeightLabel = new Label("to:", skin);
        final TextField maxHeightField = new TextField("", skin);
        
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                TextField minWidthInput = (TextField) settingsTable.getChild(1);
                TextField maxWidthInput = (TextField) settingsTable.getChild(3);
                TextField minHeightInput = (TextField) settingsTable.getChild(5);
                TextField maxHeightInput = (TextField) settingsTable.getChild(7);
                
                int minWidth = -1;
                try {
                    minWidth = (minWidthInput.getText().equals("")) ? 0 : Integer.parseInt(minWidthInput.getText());
                } catch(NumberFormatException e) {
                    minWidth = 0;
                }
                
                int maxWidth = -1;
                try {
                    maxWidth = (minWidthInput.getText().equals("")) ? 0 : Integer.parseInt(maxWidthInput.getText());
                } catch(NumberFormatException e) {
                    maxWidth = 0;
                }
                
                int minHeight = -1;
                try {
                    minHeight = (minHeightInput.getText().equals("")) ? 0 : Integer.parseInt(minHeightInput.getText());
                } catch(NumberFormatException e) {
                    minHeight = 0;
                }
                
                int maxHeight = -1;
                try {
                    maxHeight = (maxHeightInput.getText().equals("")) ? 0 : Integer.parseInt(maxHeightInput.getText());
                } catch(NumberFormatException e) {
                    maxHeight = 0;
                }

                if (minWidth >= 2 && minWidth <= 15 && minWidth <= maxWidth) {
                    Gdx.app.getPreferences("My Preferences").putInteger("minWidth", minWidth);
                } else if (minWidth == 0) {
                    validated = false; 
                }
                
                if (maxWidth >= 2 && maxWidth <= 15 && maxWidth >= minWidth) {
                    Gdx.app.getPreferences("My Preferences").putInteger("maxWidth", maxWidth);
                } else if (maxWidth == 0) {
                    validated = false;
                }
                
                if (minHeight >= 2 && minHeight <= 15 && minHeight <= maxHeight) {
                    Gdx.app.getPreferences("My Preferences").putInteger("minHeight", minHeight);
                } else if (minHeight == 0) {
                    validated = false;
                }
                
                if (maxHeight >= 2 && maxHeight <= 15 && maxHeight >= minHeight) {
                    Gdx.app.getPreferences("My Preferences").putInteger("maxHeight", maxHeight);
                } else if (maxHeight == 0) {
                    validated = false;
                }
                
                if (validated) {
                    Gdx.app.getPreferences("My Preferences").flush();
                    minWidthField.setText("");
                    maxWidthField.setText("");
                    minHeightField.setText("");
                    maxHeightField.setText("");
                } else {
                    dialog.show(stage);
                    Timer.schedule(new Timer.Task() {
                        @Override 
                        public void run() {
                            dialog.hide();
                        }
                    }, 4);
                }
                
            }
        });

        gameplayButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               settingsTable.setVisible(true);
           }
        });
        
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });
        
        rootTable = new Table();
        settingsTable = new Table();
        buttonTable = new Table();
        
        settingsTable.setVisible(false);
        
        settingsTable.add(minWidthLabel);
        settingsTable.add(minWidthField).width(50);
        settingsTable.add(maxWidthLabel);
        settingsTable.add(maxWidthField).width(50);
        settingsTable.row();
        settingsTable.add(minHeightLabel);
        settingsTable.add(minHeightField).width(50);
        settingsTable.add(maxHeightLabel);
        settingsTable.add(maxHeightField).width(50);
        settingsTable.row();
        settingsTable.add(saveButton);
        
        buttonTable.add(gameplayButton).padBottom(40).minSize(330, 80);
        buttonTable.row();
        buttonTable.add(backButton).minSize(330, 80);
        
        rootTable.add(buttonTable).padRight(250);
        rootTable.add(settingsTable);
        
        rootTable.padLeft(40);
        rootTable.setWidth(stage.getWidth());
        rootTable.align(Align.left|Align.top);
        rootTable.setPosition(0, Gdx.graphics.getHeight() * 0.75f);
        
        stage.addActor(rootTable);
        
        Gdx.input.setInputProcessor(stage);
    }
    
    public void update() {

    }
    
    @Override
    public void show() {
    }

    @Override
    public void render(float dt) {
        update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.sb.setProjectionMatrix(cam.combined);
        
//        game.sb.begin();
//        game.sb.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        game.sb.end();      
        
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

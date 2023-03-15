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

public class SettingsScreen implements Screen {
    private TankTrauma game;
    private Skin skin;
    private Stage stage;
    private Table rootTable;
    private Table settingsTable;
    private Table buttonTable;
    private Table tileTable;
    private Viewport viewport;
    private OrthographicCamera cam;
    private Texture background;
    private Boolean mazeValidated;
    private Boolean colourValidated;
    
    private String tileType;
    
    private String tank1Colour;
    private String tank2Colour;
    
    private Table colourTable1;
    private Table colourTable2;
    
    public SettingsScreen(final TankTrauma game) {
        this.game = game;
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        cam = new OrthographicCamera();
        viewport = new ScreenViewport(cam);
        stage = new Stage(viewport);
        background = new Texture("background1.png");
        mazeValidated = true;
        colourValidated = true;
        
        TextButton gameplayButton = new TextButton("GAMEPLAY", skin);
        TextButton backButton = new TextButton("BACK", skin);
        TextButton saveButton = new TextButton("SAVE", skin);
        final Dialog mazeDialog = new Dialog("Width and/or height values are invalid.", skin);
        
        Label minWidthLabel = new Label("Random Maze Width (2 - 15) from:", skin);
        final TextField minWidthField = new TextField(Gdx.app.getPreferences("My Preferences").getString("minWidth"), skin);
        Label maxWidthLabel = new Label("to: ", skin);
        final TextField maxWidthField = new TextField(Gdx.app.getPreferences("My Preferences").getString("maxWidth"), skin);
        Label minHeightLabel = new Label("Random Maze Height (2 - 15) from:", skin);
        final TextField minHeightField = new TextField(Gdx.app.getPreferences("My Preferences").getString("minHeight"), skin);
        Label maxHeightLabel = new Label("to: ", skin);
        final TextField maxHeightField = new TextField(Gdx.app.getPreferences("My Preferences").getString("maxHeight"), skin);
        
        Label tileTypeLabel = new Label("Maze Tile Type:", skin);
        TextButton sandButton = new TextButton("SAND", skin);
        
        TextButton grassButton = new TextButton("GRASS", skin);
        TextButton randomButton = new TextButton("RANDOM", skin);
        
        Label colourLabel1 = new Label("Player 1 Colour: ", skin);
        Label colourLabel2 = new Label("Player 2 Colour: ", skin);
        
        TextButton beachButton1 = new TextButton("BEACH", skin); 
        TextButton beachButton2 = new TextButton("BEACH", skin); 
        
        TextButton greenButton1 = new TextButton("GREEN", skin); 
        TextButton greenButton2 = new TextButton("GREEN", skin); 
        
        TextButton redButton1 = new TextButton("RED", skin); 
        TextButton redButton2 = new TextButton("RED", skin); 
        
        TextButton blackButton1 = new TextButton("BLACK", skin); 
        TextButton blackButton2 = new TextButton("BLACK", skin); 
        
        final Dialog colourDialog = new Dialog("Player 1 cannnot have the same colour as player 2.", skin);
        colourDialog.setPosition(500, 0);
        
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                TextField minWidthInput = (TextField) settingsTable.getChild(1);
                TextField maxWidthInput = (TextField) settingsTable.getChild(3);
                TextField minHeightInput = (TextField) settingsTable.getChild(5);
                TextField maxHeightInput = (TextField) settingsTable.getChild(7);
                
                int minWidth = 0;
                try {
                    if (!minWidthInput.getText().equals("")) {
                        minWidth = Integer.parseInt(minWidthInput.getText());
                    }
                } catch(NumberFormatException e) {
                    minWidth = -1;
                }
                
                int maxWidth = 0;
                try {
                    if (!maxWidthInput.getText().equals("")) {
                        maxWidth = Integer.parseInt(maxWidthInput.getText());
                    }
                } catch(NumberFormatException e) {
                    maxWidth = -1;
                }
                
                int minHeight = 0;
                try {
                    if (!minHeightInput.getText().equals("")) {
                        minHeight = Integer.parseInt(minHeightInput.getText());
                    }
                } catch(NumberFormatException e) {
                    minHeight = -1;
                }
                
                int maxHeight = 0;
                try {
                    if (!maxHeightInput.getText().equals("")) {
                        maxHeight = Integer.parseInt(maxHeightInput.getText());
                    }
                } catch(NumberFormatException e) {
                    maxHeight = -1;
                }

                if (minWidth >= 2 && minWidth <= 15 && (minWidth <= maxWidth || maxWidth == 0) &&
                        (maxWidth != 0 || minWidth <= Integer.parseInt(Gdx.app.getPreferences("My Preferences").getString("maxWidth")))) {
                    Gdx.app.getPreferences("My Preferences").putInteger("minWidth", minWidth);
                } else if (minWidth != 0) {
                    mazeValidated = false; 
                }
                
                if (maxWidth >= 2 && maxWidth <= 15 && maxWidth >= minWidth && (minWidth != 0 ||
                        maxWidth >= Integer.parseInt(Gdx.app.getPreferences("My Preferences").getString("minWidth")))) {
                    Gdx.app.getPreferences("My Preferences").putInteger("maxWidth", maxWidth);
                } else if (maxWidth != 0) {
                    mazeValidated = false;
                }
                
                if (minHeight >= 2 && minHeight <= 15 && (minHeight <= maxHeight || maxHeight == 0) &&
                        (maxHeight != 0 || minHeight <= Integer.parseInt(Gdx.app.getPreferences("My Preferences").getString("maxHeight")))) {
                    Gdx.app.getPreferences("My Preferences").putInteger("minHeight", minHeight);
                } else if (minHeight != 0) {
                    mazeValidated = false;
                }
                
                if (maxHeight >= 2 && maxHeight <= 15 && maxHeight >= minHeight && (minHeight != 0 ||
                        maxHeight >= Integer.parseInt(Gdx.app.getPreferences("My Preferences").getString("minHeight")))) {
                    Gdx.app.getPreferences("My Preferences").putInteger("maxHeight", maxHeight);
                } else if (maxHeight != 0) {
                    mazeValidated = false;
                }
                
                if (tileType != null) {
                    Gdx.app.getPreferences("My Preferences").putString("tileType", tileType);
                }
                
                if (tank1Colour != null) {
                    Gdx.app.getPreferences("My Preferences").putString("tank1Colour", tank1Colour);
                }
                
                if (tank2Colour != null) {
                    Gdx.app.getPreferences("My Preferences").putString("tank2Colour", tank2Colour);
                }
                
                if (tank1Colour != null && tank2Colour != null) {
                    colourValidated = !(Gdx.app.getPreferences("My Preferences").getString("tank1Colour").equals(tank2Colour) ||
                            Gdx.app.getPreferences("My Preferences").getString("tank2Colour").equals(tank1Colour) ||
                            tank1Colour.equals(tank2Colour));
                } 
                
                if (mazeValidated && colourValidated) {
                    Gdx.app.getPreferences("My Preferences").flush();
                    minWidthField.setText("");
                    maxWidthField.setText("");
                    minHeightField.setText("");
                    maxHeightField.setText("");
                } else if (!mazeValidated && colourValidated) {
                    mazeDialog.show(stage);
                    Timer.schedule(new Timer.Task() {
                        @Override 
                        public void run() {
                            mazeDialog.hide();
                        }
                    }, 1.5f);
                } else if (!colourValidated && mazeValidated) {
                    colourDialog.show(stage);
                    Timer.schedule(new Timer.Task() {
                        @Override 
                        public void run() {
                            colourDialog.hide();
                        }
                    }, 1.5f);
                } else {
                    mazeDialog.show(stage);
                    Timer.schedule(new Timer.Task() {
                        @Override 
                        public void run() {
                            mazeDialog.hide();
                        }
                    }, 2.5f);   
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
        
        sandButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               tileType = "sand";
           }
        });
        
        grassButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               tileType = "grass";
           }
        });
        
        randomButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               tileType = "random";
           }
        });
        
        beachButton1.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               tank1Colour = "sand";
           }
        });
        
        greenButton1.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               tank1Colour = "green";
           }
        });
        
        redButton1.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               tank1Colour = "red";
           }
        });
        
        blackButton1.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               tank1Colour = "dark";
           }
        });
        
        beachButton2.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               tank2Colour = "sand";
           }
        });
        
        greenButton2.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               tank2Colour = "green";
           }
        });
        
        redButton2.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               tank2Colour = "red";
           }
        });
        
        blackButton2.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               tank2Colour = "dark";
           }
        });
        
        rootTable = new Table();
        settingsTable = new Table();
        buttonTable = new Table();
        tileTable = new Table();
        colourTable1 = new Table();
        colourTable2 = new Table();
        
        settingsTable.setVisible(false);
        
        settingsTable.add(minWidthLabel);
        settingsTable.add(minWidthField).width(50);
        settingsTable.add(maxWidthLabel).padRight(40);
        settingsTable.add(maxWidthField).width(50);
        settingsTable.row();
        settingsTable.add(minHeightLabel).padTop(20);
        settingsTable.add(minHeightField).padTop(20).width(50);
        settingsTable.add(maxHeightLabel).padTop(20).padRight(40);
        settingsTable.add(maxHeightField).padTop(20).width(50);
        settingsTable.row().padBottom(100);
        tileTable.add(sandButton).padRight(40);
        tileTable.add(grassButton).padRight(40);
        tileTable.add(randomButton);
        settingsTable.add(tileTypeLabel).padTop(50);
        settingsTable.add(tileTable).padTop(50);
        settingsTable.row();
        settingsTable.add(colourLabel1);
        colourTable1.add(beachButton1).padRight(40);
        colourTable1.add(greenButton1).padRight(40);
        colourTable1.add(redButton1).padRight(40);
        colourTable1.add(blackButton1);
        settingsTable.add(colourTable1);
        settingsTable.row();
        settingsTable.add(colourLabel2);
        colourTable2.add(beachButton2).padRight(40);
        colourTable2.add(greenButton2).padRight(40);
        colourTable2.add(redButton2).padRight(40);
        colourTable2.add(blackButton2);
        settingsTable.add(colourTable2).padTop(20);
        settingsTable.row();
        settingsTable.add(saveButton).padTop(20);
        
        
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

package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.TankTrauma;
import com.mygdx.game.gameobjects.Projectile;
import com.mygdx.game.gameobjects.Tank;
import com.mygdx.game.utils.MazeGenerator;
import com.mygdx.game.utils.MazeHitboxParser;
import com.mygdx.game.utils.CollisionListener;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class GameScreen implements Screen {
    public static float UNIT_SCALE = 1 / 128f;
    
    private final TankTrauma game;
    
    private int mazeSizeX;
    private final int maxWidth;
    private final int minWidth;
   
    private int mazeSizeY;
    private final int maxHeight;
    private final int minHeight;
    
    private OrthographicCamera cam;
    private Viewport viewport;
    
    private final Tank tank1;
    private final Tank tank2;
    
    private Projectile projectile;
    
    private final CollisionListener contactListener;
    private final Box2DDebugRenderer b2dr;
    private final World world;

    private MazeGenerator map;
    private final MazeHitboxParser hitboxParser;
    
    private OrthogonalTiledMapRenderer groundRenderer;
    private OrthogonalTiledMapRenderer mazeRenderer;
    
    private boolean paused;
    private final Skin skin;
    private final Stage stage;
    private final Table table;
    private final TextButton menuButton;
    private final TextButton resumeButton;
    private final TextButton restartButton;
    private final TextButton quitButton;
    
    public GameScreen(final TankTrauma game, final int maxWidth, final int minWidth, final int maxHeight, final int minHeight) {
        this.game = game;
        
        Random random = new Random();
        
        mazeSizeX = random.nextInt(maxWidth - minWidth + 1) + minWidth;
        this.maxWidth = maxWidth;
        this.minWidth = minWidth;
        
        mazeSizeY = random.nextInt(maxHeight - minHeight + 1) + minHeight;
        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
        
        cam = new OrthographicCamera();
        viewport = new FitViewport(mazeSizeX + 0.25f, mazeSizeY + 0.25f, cam);
        
        b2dr = new Box2DDebugRenderer();
        world = new World(new Vector2(0, 0), false);
        Vector2 tank1Spawn = randomSpawn();
        
        tank1 = new Tank("sand", tank1Spawn, world);
        
        Vector2 tank2Spawn = randomSpawn();
        while (tank2Spawn.x == tank1Spawn.x && tank2Spawn.y == tank1Spawn.y) {
            tank2Spawn = randomSpawn();
        }
        
        tank2 = new Tank("dark", tank2Spawn, world);
        
        contactListener = new CollisionListener(tank1, tank2);
        
        map = new MazeGenerator((int)mazeSizeX, (int)mazeSizeY);
        map.generateGround();
        map.generateMaze();
        hitboxParser = new MazeHitboxParser();
        hitboxParser.parseMaze(world, map);
        groundRenderer = new OrthogonalTiledMapRenderer(map.getGroundMap(), UNIT_SCALE);
        mazeRenderer = new OrthogonalTiledMapRenderer(map.getMazeMap(), UNIT_SCALE);
        
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.None);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage();
        
        menuButton = new TextButton("MAIN MENU", skin);
        restartButton = new TextButton("RESTART", skin);
        resumeButton = new TextButton("RESUME", skin);
        quitButton = new TextButton("QUIT", skin);
        
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(new MenuScreen(game));
           }
        });
        
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                restart();
           }
        });
        
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                resume();
            }
        });
        
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        
        table = new Table();
        
        table.add(menuButton).padBottom(40).minSize(330, 80);
        table.row();
        table.add(resumeButton).padBottom(40).minSize(330, 80);
        table.row();
        table.add(restartButton).padBottom(40).minSize(330, 80);
        table.row();
        table.add(quitButton).minSize(330, 80);
        
//        table.padRight(40);
        table.setWidth(stage.getWidth());
        table.align(Align.left|Align.top);
        
        stage.addActor(table);
        
        Gdx.input.setInputProcessor(stage);
    }
    
    public Vector2 randomSpawn() {
        Random random = new Random();
        float xPos = 0;
        float yPos = 0;
        if (mazeSizeX % 2 == 0) {
            xPos = random.nextInt((int) mazeSizeX) + 0.5f;
        } else {
            xPos = random.nextInt((int) mazeSizeX);
        }
        
        if (mazeSizeY % 2 == 0) {
            yPos = random.nextInt((int) mazeSizeY) + 0.5f;
        } else {
            yPos = random.nextInt((int) mazeSizeY);
        }
        return new Vector2(xPos, yPos);
    }
    
    public void newRound() throws InterruptedException {
        Thread.sleep(1000);
        Random random = new Random();
        mazeSizeX = random.nextInt(maxWidth - minWidth + 1) + minWidth;
        mazeSizeY = random.nextInt(maxHeight - minHeight + 1) + minHeight;
//        cam.viewportWidth = mazeSizeX + 0.25f;
//        cam.viewportHeight = mazeSizeY + 0.25f;
//        cam.update();
        viewport = new FitViewport(mazeSizeX + 0.25f, mazeSizeY + 0.25f, cam);
        Gdx.graphics.setWindowedMode(1920, 1080);
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        map = new MazeGenerator((int)mazeSizeX, (int)mazeSizeY);
        map.generateGround();
        map.generateMaze();
        LinkedList projectiles1 = tank1.getProjectiles();
        for (int i = 0; i < projectiles1.size(); i++) {
            Projectile projectile = (Projectile) projectiles1.get(i);
            projectile.dispose();
            projectiles1.remove(i);
        }
        LinkedList projectiles2 = tank2.getProjectiles();
        for (int i = 0; i < projectiles2.size(); i++) {
            Projectile projectile = (Projectile) projectiles2.get(i);
            projectile.dispose();
            projectiles2.remove(i);
        }
        hitboxParser.dispose();
        hitboxParser.parseMaze(world, map);
        groundRenderer = new OrthogonalTiledMapRenderer(map.getGroundMap(), UNIT_SCALE);
        mazeRenderer = new OrthogonalTiledMapRenderer(map.getMazeMap(), UNIT_SCALE);
        Vector2 tank1Spawn = randomSpawn();
        tank1.getBody().setTransform(tank1Spawn, 0);
        Vector2 tank2Spawn = randomSpawn();
        while (tank2Spawn.x == tank1Spawn.x && tank2Spawn.y == tank1Spawn.y) {
            tank2Spawn = randomSpawn();
        }
        tank2.getBody().setTransform(tank2Spawn, 0);
    }

    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) pause();
        
        tank1.getBody().setAngularVelocity(0);
        tank2.getBody().setAngularVelocity(0);
        
        // Control turret and body rotation speeds, depending on user input
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            tank1.getBody().setAngularVelocity(tank1.getBody().getAngularVelocity() + tank1.getRotationSpeed());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
            tank2.getBody().setAngularVelocity(tank2.getBody().getAngularVelocity() + tank2.getRotationSpeed());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            tank1.getBody().setAngularVelocity(tank1.getBody().getAngularVelocity() - tank1.getRotationSpeed());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
            tank2.getBody().setAngularVelocity(tank2.getBody().getAngularVelocity() - tank2.getRotationSpeed());
        }
        
        tank1.getBody().setLinearVelocity(0, 0);
        tank2.getBody().setLinearVelocity(0, 0);
        
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            tank1.getBody().setLinearVelocity((tank1.getDirection().x * tank1.getForwardSpeed() / tank1.getDirection().len()) * UNIT_SCALE,
                    (tank1.getDirection().y * tank1.getForwardSpeed() / tank1.getDirection().len()) * UNIT_SCALE);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) {
            tank2.getBody().setLinearVelocity((tank2.getDirection().x * tank2.getForwardSpeed() / tank2.getDirection().len()) * UNIT_SCALE,
                    (tank2.getDirection().y * tank2.getForwardSpeed() / tank2.getDirection().len()) * UNIT_SCALE);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            tank1.getBody().setLinearVelocity((-tank1.getDirection().x * tank1.getBackwardSpeed() / tank1.getDirection().len()) * UNIT_SCALE,
                    (-tank1.getDirection().y * tank1.getBackwardSpeed() / tank1.getDirection().len()) * UNIT_SCALE);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)) {
            tank2.getBody().setLinearVelocity((-tank2.getDirection().x * tank2.getBackwardSpeed() / tank2.getDirection().len()) * UNIT_SCALE,
                    (-tank2.getDirection().y * tank2.getBackwardSpeed() / tank2.getDirection().len()) * UNIT_SCALE);
        }
        
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            tank1.shoot();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT) || Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT)) {
            tank2.shoot();
        }
    }
    
    public void update(float dt) throws InterruptedException {
        if (!tank1.getIsAlive() || !tank2.getIsAlive()) {
            tank1.setIsAlive(true);
            tank2.setIsAlive(true);
            newRound();
        }
        
        world.step(1 / 60f, 6, 2);
        handleInput();
        
        tank1.update(dt);
        tank2.update(dt);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.sb.setProjectionMatrix(cam.combined);

        if (!paused) {
            try {
                update(dt);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            int tankWidth = tank1.getBodyTexture().getWidth();
            int tankHeight = tank1.getBodyTexture().getHeight();
            int turretWidth = tank1.getTurretTexture().getWidth();
            int turretHeight = tank1.getTurretTexture().getHeight();

            // Render maze and ground with adjustments for odd and even map sizes
            int[] layer1 = {0};
            int[] layer2 = {1};
            int[] layer3 = {2};

            float xOffset = (mazeSizeX % 2 == 0) ? 0 : 0.5f;
            float yOffset = (mazeSizeY % 2 == 0) ? 0 : 0.5f;

            cam.position.set((mazeSizeX / 2) + xOffset, (mazeSizeY / 2) + yOffset, 0);
            cam.update();
            groundRenderer.setView(cam);
            groundRenderer.render();
            cam.position.set((mazeSizeX / 2) + 0.5f + xOffset, (mazeSizeY / 2) + 0.5f + yOffset, 0);
            cam.update();
            mazeRenderer.setView(cam);
            mazeRenderer.render(layer1);
            cam.position.set((mazeSizeX / 2) + 0.5f + xOffset, (mazeSizeY / 2) + yOffset, 0);
            cam.update();
            mazeRenderer.setView(cam);
            mazeRenderer.render(layer2);
            cam.position.set((mazeSizeX / 2) + xOffset, (mazeSizeY / 2) + 0.5f + yOffset, 0);
            cam.update();
            mazeRenderer.setView(cam);
            mazeRenderer.render(layer3);

            // Sets camera position to centre and sets the viewport dimensions
            cam.position.set(mazeSizeX / 2, mazeSizeY / 2, 0);
            cam.update();

            // Render tank
            game.sb.begin();
            game.sb.draw(tank1.getBodyTexture(),
                    tank1.getBody().getWorldCenter().x - ((tank1.getBodyTexture().getWidth() / 2) * UNIT_SCALE),
                    tank1.getBody().getWorldCenter().y + ((5 - (tank1.getBodyTexture().getHeight() / 2)) * UNIT_SCALE),
                    (tankWidth / 2) * UNIT_SCALE,
                    ((tankHeight / 2) - 5) * UNIT_SCALE,
                    tankWidth * UNIT_SCALE,
                    tankHeight * UNIT_SCALE,
                    1,
                    1, 
                    (float) (Math.toDegrees(tank1.getBody().getAngle())), 
                    0,
                    0,
                    tankWidth,
                    tankHeight,
                    false,
                    false);

            game.sb.draw(tank2.getBodyTexture(),
                    tank2.getBody().getWorldCenter().x - ((tank2.getBodyTexture().getWidth() / 2) * UNIT_SCALE),
                    tank2.getBody().getWorldCenter().y + ((5 - (tank2.getBodyTexture().getHeight() / 2)) * UNIT_SCALE),
                    (tankWidth / 2) * UNIT_SCALE,
                    ((tankHeight / 2) - 5) * UNIT_SCALE,
                    tankWidth * UNIT_SCALE,
                    tankHeight * UNIT_SCALE,
                    1,
                    1, 
                    (float) (Math.toDegrees(tank2.getBody().getAngle())), 
                    0,
                    0,
                    tankWidth,
                    tankHeight,
                    false,
                    false);

            for (int i = 0; i < tank1.getProjectiles().size(); i++) {
                projectile = (Projectile) tank1.getProjectiles().get(i);
                if (projectile.getBody().isAwake()) {
                    int bulletWidth = projectile.getTexture().getWidth();
                    int bulletHeight = projectile.getTexture().getHeight();
                    game.sb.draw(
                        projectile.getTexture(),
                        projectile.getPosition().x * UNIT_SCALE,
                        projectile.getPosition().y * UNIT_SCALE,
                        0,
                        0,
                        bulletWidth * UNIT_SCALE,
                        bulletHeight * UNIT_SCALE,
                        1,
                        1,
                        -projectile.getRotation(),
                        0,
                        0,
                        bulletWidth,
                        bulletHeight,
                        false,
                        false);
                }
            }

            for (int i = 0; i < tank2.getProjectiles().size(); i++) {
                projectile = (Projectile) tank2.getProjectiles().get(i);
                if (projectile.getBody().isAwake()) {
                    int bulletWidth = projectile.getTexture().getWidth();
                    int bulletHeight = projectile.getTexture().getHeight();
                    game.sb.draw(
                        projectile.getTexture(),
                        projectile.getPosition().x * UNIT_SCALE,
                        projectile.getPosition().y * UNIT_SCALE,
                        0,
                        0,
                        bulletWidth * UNIT_SCALE,
                        bulletHeight * UNIT_SCALE,
                        1,
                        1,
                        -projectile.getRotation(),
                        0,
                        0,
                        bulletWidth,
                        bulletHeight,
                        false,
                        false);
                }
            }

            game.sb.draw(tank1.getTurretTexture(),
                    tank1.getBody().getWorldCenter().x - (tank1.getTurretTexture().getWidth() / 2 * UNIT_SCALE), 
                    tank1.getBody().getWorldCenter().y - ((tank1.getTurretTexture().getHeight() - tank1.getBarrelLength()) * UNIT_SCALE),
                    (turretWidth / 2) * UNIT_SCALE,
                    (tank1.getTurretTexture().getHeight() - tank1.getBarrelLength()) * UNIT_SCALE,
                    turretWidth * UNIT_SCALE,
                    turretHeight * UNIT_SCALE,
                    1,
                    1, (float) (Math.toDegrees(tank1.getBody().getAngle())),
                    0,
                    0,
                    turretWidth,
                    turretHeight,
                    false,
                    false);      

            game.sb.draw(tank2.getTurretTexture(),
                    tank2.getBody().getWorldCenter().x - (tank2.getTurretTexture().getWidth() / 2 * UNIT_SCALE), 
                    tank2.getBody().getWorldCenter().y - ((tank2.getTurretTexture().getHeight() - tank2.getBarrelLength()) * UNIT_SCALE),
                    (turretWidth / 2) * UNIT_SCALE,
                    (tank2.getTurretTexture().getHeight() - tank2.getBarrelLength()) * UNIT_SCALE,
                    turretWidth * UNIT_SCALE,
                    turretHeight * UNIT_SCALE,
                    1,
                    1, (float) (Math.toDegrees(tank2.getBody().getAngle())),
                    0,
                    0,
                    turretWidth,
                    turretHeight,
                    false,
                    false);  

            game.sb.end();

    //        b2dr.render(world, cam.combined);
        } else {
            stage.act(dt);
            stage.draw();
        }
    }
    
    public void restart() {
        game.setScreen(new GameScreen(game, maxWidth, minWidth, maxHeight, minHeight));
    }
    
    @Override
    public void show() {
        world.setContactListener(contactListener);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        viewport = new ScreenViewport(cam);
        viewport.update(TankTrauma.WIDTH, TankTrauma.HEIGHT);
        table.setPosition(-Gdx.graphics.getWidth() * 0.7175f, Gdx.graphics.getHeight() * 0.225f);
//        table.setPosition(0, Gdx.graphics.getHeight() / 2);
        stage.setViewport(viewport);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        paused = true;
    }

    @Override
    public void resume() {
        viewport = new FitViewport(mazeSizeX + 0.25f, mazeSizeY + 0.25f, cam);
        Gdx.graphics.setWindowedMode(1920, 1080);
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.None);
        paused = false;
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        groundRenderer.dispose();
        mazeRenderer.dispose();
        tank1.dispose();
        tank2.dispose();
        stage.dispose();
        skin.dispose();
    }
    
    public Viewport getViewport() {
        return viewport;
    }

    public World getWorld() {
        return world;
    }
}

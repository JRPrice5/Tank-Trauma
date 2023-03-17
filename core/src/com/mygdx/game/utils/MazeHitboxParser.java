package com.mygdx.game.utils;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.mygdx.game.screens.GameScreen.UNIT_SCALE;
import java.util.LinkedList;

public class MazeHitboxParser {
    private LinkedList<Body> walls;
    
    public MazeHitboxParser() {
        walls = new LinkedList();
    }
    
    public void parseMaze(World world, MazeGenerator map) {
        String[][] tileCornerStates = map.getTileCornerStates();
        for (int i = 0; i < map.getMazeMap().getLayers().getCount(); i++) {
            TiledMapTileLayer layer = (TiledMapTileLayer) map.getMazeMap().getLayers().get(i);
            
            for (int y = 0; y < layer.getHeight(); y++) {
                for (int x = 0; x < layer.getWidth(); x++) {
                    if ("vertical-layer".equals(layer.getName()) && !tileCornerStates[y][x].contains("v") 
                            || "horizontal-layer".equals(layer.getName()) && !tileCornerStates[y][x].contains("h") 
                            || "dot-layer".equals(layer.getName()) && !tileCornerStates[y][x].contains("d")) 
                    {
                        continue;
                    }
                    
                    BodyDef def = new BodyDef();
                    def.type = BodyDef.BodyType.StaticBody;
                    def.fixedRotation = true;
                    
                    int width = 0;
                    int height = 0;
                    
                    float xOffset = (map.getMazeSizeX() % 2 == 0) ? 0 : -0.5f;
                    float yOffset = (map.getMazeSizeY() % 2 == 0) ? 0 : -0.5f;
                    
                    switch (layer.getName()) {
                        case "vertical-layer":
                            width = 12;
                            height = 116;
                            def.position.set(x + xOffset, y + 0.5f + yOffset);
                            break;
                        case "horizontal-layer":
                            width = 116;
                            height = 12;
                            def.position.set(x + 0.5f + xOffset, y + yOffset);
                            break;
                        case "dot-layer":
                            width = 12;
                            height = 12;
                            def.position.set(x + xOffset, y + yOffset);
                            break;
                        }
 
                    Body body = world.createBody(def);
                    PolygonShape shape = new PolygonShape();
                    shape.setAsBox((width / 2) * UNIT_SCALE, (height / 2) * UNIT_SCALE);
                    
                    FixtureDef fixtureDef = new FixtureDef();
                    fixtureDef.density = 1;
                    fixtureDef.friction = 0;
                    fixtureDef.restitution = 1;
                    fixtureDef.shape = shape;
                    fixtureDef.filter.categoryBits = 2;
                    
                    // gives body the shape and a density
                    body.createFixture(fixtureDef);
                    walls.add(body);
                    shape.dispose();
                }
            }
        }
    }
    
    public void dispose() {
        for (Body body : walls) {
            body.destroyFixture(body.getFixtureList().first());
        }
        walls.clear();
    }
}

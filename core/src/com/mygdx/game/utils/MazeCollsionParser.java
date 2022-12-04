package com.mygdx.game.utils;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.mygdx.game.screens.PlayScreen.UNIT_SCALE;

public class MazeCollsionParser {
    // make sure to integrate unit scale into this
    // maybe merge unit scale with pixels per meter or do research using forums
    public static void parseMapLayers(World world, MapGenerator map) {
        String[][] tileCornerStates = map.getTileCornerStates();
        for (int i = 0; i < map.getMazeMap().getLayers().getCount(); i++) {
            TiledMapTileLayer layer = (TiledMapTileLayer) map.getMazeMap().getLayers().get(i);
            
            for (int y = 0; y < layer.getHeight(); y++) {
                for (int x = 0; x < layer.getWidth(); x++) {
                    if (i == 0 && !tileCornerStates[y][x].contains("d") 
                            || i == 1 && !tileCornerStates[y][x].contains("v") 
                            || i == 2 && !tileCornerStates[y][x].contains("h")) 
                    {
                        continue;
                    }
                    
                    BodyDef def = new BodyDef();
                    def.type = BodyDef.BodyType.StaticBody;
                    def.fixedRotation = true;
                    
                    int width = 0;
                    int height = 0;
                    switch (layer.getName()) {
                        case "vertical-layer":
                            width = 12;
                            height = 128;
                            def.position.set(x, map.getMapSizeY() - (y + 0.5f));
                            break;
                        case "horizontal-layer":
                            width = 128;
                            height = 12;
                            def.position.set(x + 0.5f, map.getMapSizeY() - y);
                            break;
                        case "dot-layer":
                            width = 12;
                            height = 12;
                            def.position.set(x, map.getMapSizeY() - y);
                            break;
                    }
 
                    Body body = world.createBody(def);
                    PolygonShape shape = new PolygonShape();
                    shape.setAsBox((width / 2) * UNIT_SCALE, (height / 2) * UNIT_SCALE);

                    // gives body the shape and a density
                    body.createFixture(shape, 1);
                    shape.dispose();
                }
            }
        }
    }
}

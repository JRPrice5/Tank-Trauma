package com.mygdx.game.utils;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.mygdx.game.screens.PlayScreen.UNIT_SCALE;

public class MapCollisionParser {
    // make sure to integrate unit scale into this
    // maybe merge unit scale with pixels per meter or do research using forums
    public static void parseMapLayers(World world, TiledMap maze) {
        for (int layer = 0; layer < maze.getLayers().getCount(); layer++) {
            TiledMapTileLayer currentLayer = (TiledMapTileLayer) maze.getLayers().get(layer);
            
            for (int y = 0; y < currentLayer.getHeight(); y++) {
                for (int x = 0; x < currentLayer.getWidth(); x++) {
//                    if (!(() || ())) {
//                        continue;
//                    }
                    TextureRegion texture = currentLayer.getCell(x, y).getTile().getTextureRegion();
                    int width = 0;
                    int height = 0;
                    switch (currentLayer.getName()) {
                        case "vertical-layer":
                            width = 12;
                            height = 128;
                            break;
                        case "horizontal-layer":
                            width = 128;
                            height = 12;
                            break;
                        case "dot-layer":
                            width = 12;
                            height = 12;
                            break;
                    }
                    BodyDef def = new BodyDef();
                    def.type = BodyDef.BodyType.StaticBody;
                    // change y coord to fit top left map generation
                    // origin is bottom left of map
                    // or change map generation to bottom left
                    // delete this after changes
                    def.position.set((x + 1) * 64 * UNIT_SCALE, (y + 1) * 64 * UNIT_SCALE);
                    def.fixedRotation = true;
                    Body body = world.createBody(def);
                    // makes a 32 x 32 box (measure w and h from center)
                    // setting positions and stuff -> / PPM
                    PolygonShape shape = new PolygonShape();
                    shape.setAsBox(width / 2 * UNIT_SCALE, height / 2 * UNIT_SCALE);

                    // gives body the shape and a density
                    body.createFixture(shape, 1);
                    shape.dispose();
                }
            }
        }
    }
}
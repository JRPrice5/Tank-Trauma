package com.mygdx.game.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import java.util.Random;

public class Ground {
    private TiledMap tileMap;
    private TiledMapTileLayer layer;
    private TextureRegion tileImage;
    
    public Ground(int mapSize) {
        tileMap = new TiledMap();
        layer = new TiledMapTileLayer(mapSize, mapSize, 128, 128);
        tileMap.getLayers().add(layer);
    }
    
    public void generateTilemap() {
        Random random = new Random();
        int tileMapType = random.nextInt(2);
        for (int h = 0; h < layer.getHeight(); h++) {
            for (int w = 0; w < layer.getWidth(); w++) {
                Cell cell = new Cell();
                String tile = "";
                    if (tileMapType == 0) {
                        if (random.nextInt(2) == 0) {
                            tile = "tileGrass1.png";
                        } else {
                            tile = "tileGrass2.png";
                        }
                    } else if (tileMapType == 1) {
                        if (random.nextInt(2) == 0) {
                            tile = "tileSand1.png";
                        } else {
                            tile = "tileSand2.png";
                        }
                    }
                cell.setTile(new StaticTiledMapTile(new TextureRegion(new Texture(tile))));
                layer.setCell(w, h, cell);
            }
        }
    }
    
    public TiledMap getTileMap() {
        return tileMap;
    } 

    public TiledMapTileLayer getLayer() {
        return layer;
    }
}

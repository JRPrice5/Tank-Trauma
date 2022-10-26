package com.mygdx.game.prefabs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

public class Ground {
    private TiledMap tileMap;
    private TiledMapTileLayer layer;
    private TextureRegion tileImage;
    
    public Ground() {
        tileMap = new TiledMap();
        layer = new TiledMapTileLayer(8, 8, 128, 128);
        tileMap.getLayers().add(layer);
        tileImage = new TextureRegion(new Texture("tileGrass1.png"));
        
//        groundLayer = (TiledMapTileLayer) ground.getLayers().get("GroundLayer");
//        cell = groundLayer.getCell(0, 0);
//        TextureRegion textureRegion = new TextureRegion(tileImage, 64, 64);
//        cell.setTile(new StaticTiledMapTile(textureRegion));
//        groundLayer.setCell(0, 0, cell);
    }
    
    public void generateTilemap() {
        for (int h = 0; h < layer.getHeight(); h++) {
            for (int w = 0; w < layer.getWidth(); w++) {
                Cell cell = new Cell();
                cell.setTile(new StaticTiledMapTile(tileImage));
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

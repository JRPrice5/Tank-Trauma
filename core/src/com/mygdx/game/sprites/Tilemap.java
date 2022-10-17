package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.mygdx.game.TankDemo;

public class Tilemap {
    private TiledMap ground;
    private TiledMapTileLayer groundLayer;
    private TextureRegion tileImage;
    private Map walls;
    
    public Tilemap() {
        ground = new TiledMap();
        groundLayer = new TiledMapTileLayer(8, 8, 256, 256);
        ground.getLayers().add(groundLayer);
        tileImage = new TextureRegion(new Texture("badlogic.jpg"));
        
//        groundLayer = (TiledMapTileLayer) ground.getLayers().get("GroundLayer");
//        cell = groundLayer.getCell(0, 0);
//        TextureRegion textureRegion = new TextureRegion(tileImage, 64, 64);
//        cell.setTile(new StaticTiledMapTile(textureRegion));
//        groundLayer.setCell(0, 0, cell);
    }
    
    public void randomiseTilemap() {
        for (int h = 0; h < groundLayer.getHeight(); h++) {
            for (int w = 0; w < groundLayer.getWidth(); w++) {
                Cell cell = new Cell();
                cell.setTile(new StaticTiledMapTile(tileImage));
                groundLayer.setCell(w, h, cell);
            }
        }
    }
    
    public TiledMap getGround() {
        return ground;
    } 

    public TiledMapTileLayer getGroundLayer() {
        return groundLayer;
    }
}

package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

public class Tilemap {
    private TiledMap ground;
    private TiledMapTileLayer groundLayer;
    private Map walls;
    private MapLayer wallsLayer;
    private Texture tileImage;
    private Cell cell;
    
    public Tilemap() {
        groundLayer = (TiledMapTileLayer) ground.getLayers().get(1);
        wallsLayer = ground.getLayers().get(2);
        tileImage = new Texture("badlogic.png");
        cell = groundLayer.getCell(0, 0);
        TextureRegion textureRegion = new TextureRegion(tileImage, 64, 64);
        cell.setTile(new StaticTiledMapTile(textureRegion));
        groundLayer.setCell(0, 0, cell);
    }
    
    public TiledMap getGround() {
        return ground;
    } 
}

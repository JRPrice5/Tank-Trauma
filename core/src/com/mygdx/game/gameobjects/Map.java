package com.mygdx.game.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import java.util.Random;

public class Map {
    private TiledMap groundMap;
    private TiledMap mazeMap;
    private int mapSize;
    private TiledMapTileLayer groundLayer;
    private TiledMapTileLayer dotLayer;
    private TiledMapTileLayer verticalLayer;
    private TiledMapTileLayer horizontalLayer;
    
    public Map(int mapSize) {
        groundMap = new TiledMap();
        mazeMap = new TiledMap();
        this.mapSize = mapSize;
        groundLayer = new TiledMapTileLayer(mapSize, mapSize, 128, 128);
        dotLayer = new TiledMapTileLayer(mapSize + 1, mapSize + 1, 128, 128);
        verticalLayer = new TiledMapTileLayer(mapSize + 1, mapSize + 1, 128, 128);
        horizontalLayer = new TiledMapTileLayer(mapSize + 1, mapSize + 1, 128, 128);
        groundMap.getLayers().add(groundLayer);
        mazeMap.getLayers().add(dotLayer);
        mazeMap.getLayers().add(verticalLayer);
        mazeMap.getLayers().add(horizontalLayer);
    }
    
    public void generateGround() {
        Random random = new Random();
        int tileMapType = random.nextInt(2);
        for (int y = 0; y < groundLayer.getHeight(); y++) {
            for (int x = 0; x < groundLayer.getWidth(); x++) {
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
                groundLayer.setCell(x, y, cell);
            }
        }
    }
    
    public void generateMaze() {
        Random random = new Random();
        for (int y = 0; y < verticalLayer.getHeight(); y++) {
            for (int x = 0; x < verticalLayer.getWidth(); x++) {
                Cell cell = new Cell();
                String vTile = "";
                String hTile = "";
                
                if (x == 0 || x == verticalLayer.getWidth() - 1) {
                    vTile = "vwall.png";
                } else {
                    int tileType = random.nextInt(2);
                    if (tileType == 1) {
                        vTile = "vwall.png";
                    }
                }
                if (vTile == "vwall.png") {
                    cell.setTile(new StaticTiledMapTile(new TextureRegion(new Texture(vTile))));
                } else {
                    cell.setTile(new StaticTiledMapTile(new TextureRegion()));
                }
                verticalLayer.setCell(x, y, cell);
                
                if (y == 0 || y == verticalLayer.getHeight() - 1) {
                    hTile = "hwall.png";
                } else {
                    int tileType = random.nextInt(2);
                    if (tileType == 1) {
                        hTile = "hwall.png";
                    }
                }
                if (hTile == "hwall.png") {
                    cell.setTile(new StaticTiledMapTile(new TextureRegion(new Texture(hTile))));
                } else {
                    cell.setTile(new StaticTiledMapTile(new TextureRegion()));
                }
                horizontalLayer.setCell(x, y, cell);
                
                // Create dot if a v or h wall is present
                // Adjust this algorithm and sizes of walls to work with dot layer
            }
        }
    }
    
    public TiledMap getGroundMap() {
        return groundMap;
    } 
    
    public TiledMap getMazeMap() {
        return mazeMap;
    } 

    public TiledMapTileLayer getDotLayer() {
        return dotLayer;
    }
    
    public TiledMapTileLayer getVerticalLayer() {
        return verticalLayer;
    }

    public TiledMapTileLayer getHorizontalLayer() {
        return horizontalLayer;
    }
}

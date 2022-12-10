package com.mygdx.game.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class MapGenerator {
    private TiledMap groundMap;
    private TiledMap mazeMap;
    
    private int mapSizeX;
    private int mapSizeY;
    
    private TiledMapTileLayer groundLayer;
    private TiledMapTileLayer dotLayer;
    private TiledMapTileLayer verticalLayer;
    private TiledMapTileLayer horizontalLayer;
    
    private String[][] tileCornerStates;
    private boolean[][] tilesAccessible;
    Queue<Vector2> tilesToSearch;
    
    public MapGenerator(int mapSizeX, int mapSizeY) {
        groundMap = new TiledMap();
        mazeMap = new TiledMap();
        
        this.mapSizeX = mapSizeX;
        this.mapSizeY = mapSizeY;
        
        groundLayer = new TiledMapTileLayer(mapSizeX, mapSizeY, 128, 128);
        verticalLayer = new TiledMapTileLayer(mapSizeX + 1, mapSizeY, 128, 128);
        horizontalLayer = new TiledMapTileLayer(mapSizeX, mapSizeY + 1, 128, 128);
        dotLayer = new TiledMapTileLayer(mapSizeX + 1, mapSizeY + 1, 128, 128);
        
        verticalLayer.setName("vertical-layer");
        horizontalLayer.setName("horizontal-layer");
        dotLayer.setName("dot-layer");
        
        groundMap.getLayers().add(groundLayer);
        mazeMap.getLayers().add(dotLayer);
        mazeMap.getLayers().add(verticalLayer);
        mazeMap.getLayers().add(horizontalLayer);
        
        tileCornerStates = new String[mapSizeY + 1][mapSizeX + 1];
        tilesAccessible = new boolean[mapSizeY][mapSizeX];
        tilesToSearch = new LinkedList<>();
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
        for (int y = 0; y < mapSizeY + 1; y++) {
            for (int x = 0; x < mapSizeX + 1; x++) {
                tileCornerStates[y][x] = "";
            }
        }
            
        for (int y = 0; y < verticalLayer.getHeight(); y++) {
            for (int x = 0; x < verticalLayer.getWidth(); x++) {
                
                if (x == 0 || x == verticalLayer.getWidth() - 1) {
                    Cell cell = new Cell();
                    cell.setTile(new StaticTiledMapTile(new TextureRegion(new Texture("vWall3.png"))));
                    tileCornerStates[y][x] = "v";
                    verticalLayer.setCell(x, y, cell);
                } else {
                    int tileType = random.nextInt(2);
                    if (tileType == 1) {
                        Cell cell = new Cell();
                        cell.setTile(new StaticTiledMapTile(new TextureRegion(new Texture("vWall3.png"))));
                        tileCornerStates[y][x] = "v";
                        verticalLayer.setCell(x, y, cell);
                    } 
                }
            }
        }
        
        for (int y = 0; y < horizontalLayer.getHeight(); y++) {
            for (int x = 0; x < horizontalLayer.getWidth(); x++) {
                if (y == 0 || y == horizontalLayer.getHeight() - 1) {
                    Cell cell = new Cell();
                    cell.setTile(new StaticTiledMapTile(new TextureRegion(new Texture("hWall3.png"))));
                    tileCornerStates[y][x] += "h";
                    horizontalLayer.setCell(x, y, cell);
                } else {
                    int tileType = random.nextInt(2);
                    if (tileType == 1) {
                        Cell cell = new Cell(); 
                        cell.setTile(new StaticTiledMapTile(new TextureRegion(new Texture("hWall3.png"))));
                        tileCornerStates[y][x] += "h";
                        horizontalLayer.setCell(x, y, cell);
                    } 
                }
            }
        }
        
        for (int y = 0; y < dotLayer.getHeight(); y++) {
            for (int x = 0; x < dotLayer.getWidth(); x++) {
                if (y == 0 
                        || y == dotLayer.getHeight() - 1 
                        || x == 0 
                        || x == dotLayer.getWidth() - 1 
                        ||!tileCornerStates[y][x].equals("") 
                        || tileCornerStates[y - 1][x].contains("v") 
                        || tileCornerStates[y][x - 1].contains("h")) 
                {
                    Cell cell = new Cell();
                    cell.setTile(new StaticTiledMapTile(new TextureRegion(new Texture("dot3.png"))));
                    dotLayer.setCell(x, y, cell);
                    tileCornerStates[y][x] += "d";
                }
            }
        }
        
        int x = random.nextInt(mapSizeX);
        int y = random.nextInt(mapSizeY);
        tilesAccessible[y][x] = true;
        for (int i = 0; i < mapSizeX * mapSizeY; i++) {
            // Retrieves and removes the coords of the first tile
            // from the queue
            if (i > 0) {  
                    x = (int) tilesToSearch.peek().x;
                    y = (int) tilesToSearch.poll().y;
            }

            // Determines whether adjacent tiles can be accessed, 
            // if so, they are added to tilesToSearch
            boolean northWall = (tileCornerStates[y][x].contains("h"));
            boolean eastWall = (tileCornerStates[y][x + 1].contains("v"));
            boolean southWall = (tileCornerStates[y + 1][x].contains("h"));
            boolean westWall = (tileCornerStates[y][x].contains("v"));

            if (!northWall && y > 0) {
                if (!tilesAccessible[y - 1][x]) {
                    tilesAccessible[y - 1][x] = true;
                    tilesToSearch.add(new Vector2(x, y - 1));
                }
            }
            if (!eastWall && x < mapSizeX - 1) {
                if (!tilesAccessible[y][x + 1]) {
                    tilesAccessible[y][x + 1] = true;
                    tilesToSearch.add(new Vector2(x + 1, y));
                }
            }
            if (!southWall && y < mapSizeY - 1) {
                if (!tilesAccessible[y + 1][x]) {
                    tilesAccessible[y + 1][x] = true;
                    tilesToSearch.add(new Vector2(x, y + 1));
                }
            }
            if (!westWall && x > 0) {
                if (!tilesAccessible[y][x - 1]) {
                    tilesAccessible[y][x - 1] = true;
                    tilesToSearch.add(new Vector2(x - 1, y));
                }
            }

            // If the adjacent tiles cannot be accessed, 
            // delete a wall from a random adjacent tile to continue the
            // search
            if (tilesToSearch.isEmpty()) {
                boolean tileAccessed;
                LinkedList blockingTiles = new LinkedList();
                
                // Stores each tile that blocks the current path
                for (y = 0; y < mapSizeY; y++) {
                    for (x = 0; x < mapSizeX; x++) {
                        tileAccessed = tilesAccessible[y][x];
                        if (!tileAccessed) {
                            continue;
                        }
                        boolean nTileAccessible = true;
                        boolean eTileAccessible = true;
                        boolean sTileAccessible = true;
                        boolean wTileAccessible = true;
                        if (y > 0) {
                            nTileAccessible = tilesAccessible[y - 1][x];
                        }
                        if (x < mapSizeX - 1) {
                            eTileAccessible = tilesAccessible[y][x + 1];
                        }
                        if (y < mapSizeY - 1) {
                            sTileAccessible = tilesAccessible[y + 1][x];
                        }
                        if (x > 0) {
                            wTileAccessible = tilesAccessible[y][x - 1];
                        } 
                        if (!(nTileAccessible && eTileAccessible && sTileAccessible && wTileAccessible)) {
                            blockingTiles.add(x);
                            blockingTiles.add(y);
                        } 
                    }
                }
                
                int wallsToDestroy;
                if (blockingTiles.isEmpty()) {
                    continue;
                } 
//                else if (blockingTiles.size() < 4) {
//                    wallsToDestroy = random.nextInt(blockingTiles.size());
//                } 
//                else {
//                    wallsToDestroy = random.nextInt(blockingTiles.size());
//                }
                wallsToDestroy = random.nextInt(blockingTiles.size());
                
                for (int p = 0; p <= wallsToDestroy; p++) {
                    if (blockingTiles.isEmpty()) {
                        continue;
                    } 
                    boolean nTileAccessible = true;
                    boolean eTileAccessible = true;
                    boolean sTileAccessible = true;
                    boolean wTileAccessible = true;
                    int index = random.nextInt(blockingTiles.size() / 2) * 2;
                    int xCoord = (int) blockingTiles.get(index);
                    int yCoord = (int) blockingTiles.get(index + 1);
                    blockingTiles.remove(index);
                    blockingTiles.remove(index);

                    if (yCoord > 0) {
                        nTileAccessible = tilesAccessible[yCoord - 1][xCoord];
                    } 
                    if (xCoord < mapSizeX - 1) {
                        eTileAccessible = tilesAccessible[yCoord][xCoord + 1];
                    }
                    if (yCoord < mapSizeY - 1) {
                        sTileAccessible = tilesAccessible[yCoord + 1][xCoord];
                    }
                    if (xCoord > 0) {
                        wTileAccessible = tilesAccessible[yCoord][xCoord - 1];
                    } 

                    if (!(nTileAccessible || eTileAccessible || wTileAccessible)) {
                        switch (random.nextInt(3)) {
                        case 0:
                            deleteNorthTile(xCoord, yCoord);
                            break;
                        case 1:
                            deleteEastTile(xCoord, yCoord);
                            break;
                        case 2:
                            deleteWestTile(xCoord, yCoord);
                            break;
                        }
                    }
                    else if (!(nTileAccessible || eTileAccessible || sTileAccessible)) {
                        switch (random.nextInt(3)) {
                        case 0:
                            deleteNorthTile(xCoord, yCoord);
                            break;
                        case 1:
                            deleteEastTile(xCoord, yCoord);
                            break;
                        case 2:
                            deleteSouthTile(xCoord, yCoord);
                            break;
                         }
                    }
                    else if (!(eTileAccessible || sTileAccessible || wTileAccessible)) {
                        switch (random.nextInt(3)) {
                        case 0:
                            deleteEastTile(xCoord, yCoord);
                            break;
                        case 1:
                            deleteSouthTile(xCoord, yCoord);
                            break;
                        case 2:
                            deleteWestTile(xCoord, yCoord);
                            break;
                        }
                    } 
                    else if (!(nTileAccessible || sTileAccessible || wTileAccessible)) {
                        switch (random.nextInt(3)) {
                        case 0:
                            deleteNorthTile(xCoord, yCoord);
                            break;
                        case 1:
                            deleteSouthTile(xCoord, yCoord);
                            break;
                        case 2:
                            deleteWestTile(xCoord, yCoord);
                            break;
                        }
                    }
                    else if (!(nTileAccessible || sTileAccessible)) {
                        switch (random.nextInt(2)) {
                        case 0:
                            deleteNorthTile(xCoord, yCoord);
                            break;
                        case 1:
                            deleteSouthTile(xCoord, yCoord);
                            break;
                        }
                    }
                    else if (!(eTileAccessible || wTileAccessible)) {
                        switch (random.nextInt(2)) {
                        case 0:
                            deleteEastTile(xCoord, yCoord);
                            break;
                        case 1:
                            deleteWestTile(xCoord, yCoord);
                            break;
                        }
                    } 
                    else if (!(nTileAccessible || wTileAccessible)) {
                        switch (random.nextInt(2)) {
                        case 0:
                            deleteNorthTile(xCoord, yCoord);
                            break;
                        case 1:
                            deleteWestTile(xCoord, yCoord);
                            break;
                        }
                    } 
                    else if (!(nTileAccessible || eTileAccessible)) {
                        switch (random.nextInt(2)) {
                        case 0:
                            deleteNorthTile(xCoord, yCoord);
                            break;
                        case 1:
                            deleteEastTile(xCoord, yCoord);
                            break;
                        }
                    } 
                    else if (!(eTileAccessible || sTileAccessible)) {
                        switch (random.nextInt(2)) {
                        case 0:
                            deleteEastTile(xCoord, yCoord);
                            break;
                        case 1:
                            deleteSouthTile(xCoord, yCoord);
                            break;
                        }
                    } 
                    else if (!(sTileAccessible || wTileAccessible)) {
                        switch (random.nextInt(2)) {
                        case 0:
                            deleteSouthTile(xCoord, yCoord);
                            break;
                        case 1:
                            deleteWestTile(xCoord, yCoord);
                            break;
                        }
                    } 
                    else if (!nTileAccessible) {
                        deleteNorthTile(xCoord, yCoord);
                    } 
                    else if (!eTileAccessible) {
                        deleteEastTile(xCoord, yCoord);
                    } 
                    else if (!sTileAccessible) {
                        deleteSouthTile(xCoord, yCoord);
                    } 
                    else if (!wTileAccessible) {
                        deleteWestTile(xCoord, yCoord);
                    }
                }
            }
        }
    }
    
    private void deleteNorthTile(int xCoord, int yCoord) {
        Cell cell = new Cell();
        horizontalLayer.setCell(xCoord, yCoord, cell);
        tileCornerStates[yCoord][xCoord] = tileCornerStates[yCoord][xCoord].replace("h", "");
        tilesAccessible[yCoord - 1][xCoord] = true;
        tilesToSearch.add(new Vector2(xCoord, yCoord - 1));
        if (xCoord > 0) {
            if (!tileCornerStates[yCoord][xCoord - 1].contains("h")) {
                dotLayer.setCell(xCoord, yCoord, cell);
            }
        }
        if (xCoord < mapSizeX - 1) {
            if (!tileCornerStates[yCoord][xCoord + 1].contains("h")) {
                dotLayer.setCell(xCoord + 1, yCoord, cell);
            }
        }
    }
    
    private void deleteEastTile(int xCoord, int yCoord) {
        Cell cell = new Cell();
        verticalLayer.setCell(xCoord + 1, yCoord, cell);
        tileCornerStates[yCoord][xCoord + 1] = tileCornerStates[yCoord][xCoord + 1].replace("v", "");
        tilesAccessible[yCoord][xCoord + 1] = true;
        tilesToSearch.add(new Vector2(xCoord + 1, yCoord));
        if (yCoord > 0) {
            if (!tileCornerStates[yCoord - 1][xCoord + 1].contains("v")) {
                dotLayer.setCell(xCoord + 1, yCoord, cell);
            }
        }
        if (yCoord < mapSizeY - 1) {
            if (!tileCornerStates[yCoord + 1][xCoord + 1].contains("v")) {
                dotLayer.setCell(xCoord + 1, yCoord + 1, cell);
            }
        }
    }
    
    private void deleteSouthTile(int xCoord, int yCoord) {
        Cell cell = new Cell();
        horizontalLayer.setCell(xCoord, yCoord + 1, cell);
        tileCornerStates[yCoord + 1][xCoord] = tileCornerStates[yCoord + 1][xCoord].replace("h", "");
        tilesAccessible[yCoord + 1][xCoord] = true;
        tilesToSearch.add(new Vector2(xCoord, yCoord + 1));
        if (xCoord > 0) {
            if (!tileCornerStates[yCoord + 1][xCoord - 1].contains("h")) {
                dotLayer.setCell(xCoord, yCoord + 1, cell);
            }
        }
        if (xCoord < mapSizeX - 1) {
            if (!tileCornerStates[yCoord + 1][xCoord + 1].contains("h")) {
                dotLayer.setCell(xCoord + 1, yCoord + 1, cell);
            }
        }
    }
    
    private void deleteWestTile(int xCoord, int yCoord) {
        Cell cell = new Cell();
        verticalLayer.setCell(xCoord, yCoord, cell);
        tileCornerStates[yCoord][xCoord] = tileCornerStates[yCoord][xCoord].replace("v", "");
        tilesAccessible[yCoord][xCoord - 1] = true;
        tilesToSearch.add(new Vector2(xCoord - 1, yCoord));
        if (yCoord > 0) {
            if (!tileCornerStates[yCoord - 1][xCoord].contains("v")) {
                dotLayer.setCell(xCoord, yCoord, cell);
            }
        }
        if (yCoord < mapSizeY - 1) {
            if (!tileCornerStates[yCoord + 1][xCoord].contains("v")) {
                dotLayer.setCell(xCoord, yCoord + 1, cell);
            }
        }
    }
    
    public int getMapSizeX() {
        return mapSizeX;
    }
    
    public int getMapSizeY() {
        return mapSizeY;
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
    
    public String[][] getTileCornerStates() {
        return tileCornerStates;
    }
}

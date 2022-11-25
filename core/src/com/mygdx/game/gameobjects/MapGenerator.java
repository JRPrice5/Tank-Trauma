package com.mygdx.game.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class MapGenerator {
    private TiledMap groundMap;
    private TiledMap mazeMap;
    private int mapSize;
    private TiledMapTileLayer groundLayer;
    private TiledMapTileLayer dotLayer;
    private TiledMapTileLayer verticalLayer;
    private TiledMapTileLayer horizontalLayer;
    
    public MapGenerator(int mapSize) {
        groundMap = new TiledMap();
        mazeMap = new TiledMap();
        this.mapSize = mapSize;
        groundLayer = new TiledMapTileLayer(mapSize, mapSize, 128, 128);
        dotLayer = new TiledMapTileLayer(mapSize + 1, mapSize + 1, 128, 128);
        verticalLayer = new TiledMapTileLayer(mapSize + 1, mapSize, 128, 128);
        horizontalLayer = new TiledMapTileLayer(mapSize, mapSize + 1, 128, 128);
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
        String[][] tileCornerState = new String[mapSize + 1][mapSize + 1];
        for (int y = 0; y < mapSize + 1; y++) {
            for (int x = 0; x < mapSize + 1; x++) {
                tileCornerState[y][x] = "";
            }
        }
            
        for (int y = 0; y < verticalLayer.getHeight(); y++) {
            for (int x = 0; x < verticalLayer.getWidth(); x++) {
                Cell cell = new Cell();
                
                if (x == 0 || x == verticalLayer.getWidth() - 1) {
                    cell.setTile(new StaticTiledMapTile(new TextureRegion(new Texture("vWall3.png"))));
                    tileCornerState[y][x] = "v";
                } else {
                    int tileType = random.nextInt(2);
                    if (tileType == 1) {
                        cell.setTile(new StaticTiledMapTile(new TextureRegion(new Texture("vWall3.png"))));
                        tileCornerState[y][x] = "v";
                    } 
                }
                verticalLayer.setCell(x, y, cell);  
            }
        }
        
        for (int y = 0; y < horizontalLayer.getHeight(); y++) {
            for (int x = 0; x < horizontalLayer.getWidth(); x++) {
                Cell cell = new Cell();

                if (y == 0 || y == horizontalLayer.getHeight() - 1) {
                    cell.setTile(new StaticTiledMapTile(new TextureRegion(new Texture("hWall3.png"))));
                    tileCornerState[y][x] += "h";
                } else {
                    int tileType = random.nextInt(2);
                    if (tileType == 1) {
                        cell.setTile(new StaticTiledMapTile(new TextureRegion(new Texture("hWall3.png"))));
                        tileCornerState[y][x] += "h";
                    } 
                }
                horizontalLayer.setCell(x, y, cell);
            }
        }
        
        for (int y = 0; y < dotLayer.getHeight(); y++) {
            for (int x = 0; x < dotLayer.getWidth(); x++) {
                Cell cell = new Cell();
                
                if (y == 0 || y == dotLayer.getHeight() - 1 || x == 0 || x == dotLayer.getWidth() - 1) {
                    cell.setTile(new StaticTiledMapTile(new TextureRegion(new Texture("dot3.png"))));
                }  else if (!tileCornerState[y][x].equals("")) {
                    cell.setTile(new StaticTiledMapTile(new TextureRegion(new Texture("dot3.png"))));
                } else if (tileCornerState[y - 1][x].contains("v") || tileCornerState[y][x - 1].contains("h")) {
                    cell.setTile(new StaticTiledMapTile(new TextureRegion(new Texture("dot3.png"))));
                }
                dotLayer.setCell(x, y, cell);
            }
        }
        
        
        Queue<Integer> tilesToSearch = new LinkedList<>();
        boolean[][] tilesAccessible = new boolean[mapSize][mapSize];
        tilesAccessible[0][0] = true;
        
        int x = 0;
        int y = 0;
        tilesAccessible[0][0] = true;
        for (int i = 0; i < mapSize * mapSize; i++) {
            // Retrieves and removes the coords of the first tile
            // from the queue
            if (i > 0) {  
                    x = (int) tilesToSearch.poll();
                    y = (int) tilesToSearch.poll();
            }

            // Determines whether adjacent tiles can be accessed, 
            // if so, they are added to tilesToSearch
            boolean northWall = (tileCornerState[y][x].contains("h"));
            boolean eastWall = (tileCornerState[y][x + 1].contains("v"));
            boolean southWall = (tileCornerState[y + 1][x].contains("h"));
            boolean westWall = (tileCornerState[y][x].contains("v"));

            if (!northWall && y > 0) {
                if (!tilesAccessible[y - 1][x]) {
                    tilesAccessible[y - 1][x] = true;
                    tilesToSearch.add(x);
                    tilesToSearch.add(y - 1);
                }
            }
            if (!eastWall && x < mapSize - 1) {
                if (!tilesAccessible[y][x + 1]) {
                    tilesAccessible[y][x + 1] = true;
                    tilesToSearch.add(x + 1);
                    tilesToSearch.add(y);
                }
            }
            if (!southWall && y < mapSize - 1) {
                if (!tilesAccessible[y + 1][x]) {
                    tilesAccessible[y + 1][x] = true;
                    tilesToSearch.add(x);
                    tilesToSearch.add(y + 1);
                }
            }
            if (!westWall && x > 0) {
                if (!tilesAccessible[y][x - 1]) {
                    tilesAccessible[y][x - 1] = true;
                    tilesToSearch.add(x - 1);
                    tilesToSearch.add(y);
                }
            }

            // If the adjacent tiles cannot be accessed, 
            // delete a wall from a random adjacent tile to continue the
            // search
            Cell cell = new Cell();
            if (tilesToSearch.isEmpty()) {
                boolean tileAccessed;
                LinkedList blockingTiles = new LinkedList();
                
                // Stores each tile that blocks the current path
                for (y = 0; y < mapSize; y++) {
                    for (x = 0; x < mapSize; x++) {
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
                        if (x < mapSize - 1) {
                            eTileAccessible = tilesAccessible[y][x + 1];
                        }
                        if (y < mapSize - 1) {
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
                
                boolean nTileAccessible = true;
                boolean eTileAccessible = true;
                boolean sTileAccessible = true;
                boolean wTileAccessible = true;
                
                if (blockingTiles.size() == 0) {
                    continue;
                }
                int index = random.nextInt(blockingTiles.size() / 2) * 2;
                int xCoord = (int) blockingTiles.get(index);
                int yCoord = (int) blockingTiles.get(index + 1);
                blockingTiles.clear();
                
                if (yCoord > 0) {
                    nTileAccessible = tilesAccessible[yCoord - 1][xCoord];
                } 
                if (xCoord < mapSize - 1) {
                    eTileAccessible = tilesAccessible[yCoord][xCoord + 1];
                }
                if (yCoord < mapSize - 1) {
                    sTileAccessible = tilesAccessible[yCoord + 1][xCoord];
                }
                if (xCoord > 0) {
                    wTileAccessible = tilesAccessible[yCoord][xCoord - 1];
                } 
                
                if (!(nTileAccessible || eTileAccessible || wTileAccessible)) {
                    switch (random.nextInt(3)) {
                    case 0:
                        horizontalLayer.setCell(xCoord, yCoord, cell);
                        tileCornerState[yCoord][xCoord].replace("h", "");
                        tilesAccessible[yCoord - 1][xCoord] = true;
                        tilesToSearch.add(xCoord);
                        tilesToSearch.add(yCoord - 1);
                        if (xCoord > 0) {
                            if (!tileCornerState[yCoord][xCoord - 1].contains("h")) {
                                dotLayer.setCell(xCoord, yCoord, cell);
                            }
                        }
                        break;
                    case 1:
                        verticalLayer.setCell(xCoord + 1, yCoord, cell);
                        tileCornerState[yCoord][xCoord + 1].replace("v", "");
                        tilesAccessible[yCoord][xCoord + 1] = true;
                        tilesToSearch.add(xCoord + 1);
                        tilesToSearch.add(yCoord);
                        if (yCoord > 0) {
                            if (!tileCornerState[yCoord - 1][xCoord + 1].contains("v")) {
                                dotLayer.setCell(xCoord + 1, yCoord, cell);
                            }
                        }
                        break;
                    case 2:
                        verticalLayer.setCell(xCoord, yCoord, cell);
                        tileCornerState[yCoord][xCoord].replace("v", "");
                        tilesAccessible[yCoord][xCoord - 1] = true;
                        tilesToSearch.add(xCoord - 1);
                        tilesToSearch.add(yCoord);
                        if (y > 0) {
                            if (!tileCornerState[yCoord - 1][xCoord].contains("v")) {
                                dotLayer.setCell(xCoord, yCoord, cell);
                            }
                        }
                        break;
                    }
                }
                else if (!(nTileAccessible || eTileAccessible || sTileAccessible)) {
                    switch (random.nextInt(3)) {
                    case 0:
                        horizontalLayer.setCell(xCoord, yCoord, cell);
                        tileCornerState[yCoord][xCoord].replace("h", "");
                        tilesAccessible[yCoord - 1][xCoord] = true;
                        tilesToSearch.add(xCoord);
                        tilesToSearch.add(yCoord - 1);
                        if (xCoord > 0) {
                            if (!tileCornerState[yCoord][xCoord - 1].contains("h")) {
                                dotLayer.setCell(xCoord, yCoord, cell);
                            }
                        }
                        break;
                    case 1:
                        verticalLayer.setCell(xCoord + 1, yCoord, cell);    
                        tileCornerState[yCoord][xCoord + 1].replace("v", "");
                        tilesAccessible[yCoord][xCoord + 1] = true;
                        tilesToSearch.add(xCoord + 1);
                        tilesToSearch.add(yCoord);
                        if (yCoord > 0) {
                            if (!tileCornerState[yCoord - 1][xCoord + 1].contains("v")) {
                                dotLayer.setCell(xCoord + 1, yCoord, cell);
                            }
                        }
                        break;
                    case 2:
                        horizontalLayer.setCell(xCoord, yCoord + 1, cell);
                        tileCornerState[yCoord + 1][xCoord].replace("h", "");
                        tilesAccessible[yCoord + 1][xCoord] = true;
                        tilesToSearch.add(xCoord);
                        tilesToSearch.add(yCoord + 1);
                        if (xCoord > 0) {
                            if (!tileCornerState[yCoord + 1][xCoord - 1].contains("h")) {
                                dotLayer.setCell(xCoord, yCoord + 1, cell);
                            }
                        }
                        break;
                     }
                }
                else if (!(eTileAccessible || sTileAccessible || wTileAccessible)) {
                    switch (random.nextInt(3)) {
                    case 0:
                        verticalLayer.setCell(xCoord + 1, yCoord, cell);
                        tileCornerState[yCoord][xCoord + 1].replace("v", "");
                        tilesToSearch.add(xCoord + 1);
                        tilesToSearch.add(yCoord);
                        if (yCoord > 0) {
                            if (!tileCornerState[yCoord - 1][xCoord + 1].contains("v")) {
                                dotLayer.setCell(xCoord + 1, yCoord, cell);
                            }
                        }
                        break;
                    case 1:
                        horizontalLayer.setCell(xCoord, yCoord + 1, cell);
                        tileCornerState[yCoord + 1][xCoord].replace("h", "");
                        tilesAccessible[yCoord + 1][xCoord] = true;
                        tilesToSearch.add(xCoord);
                        tilesToSearch.add(yCoord + 1);
                        if (xCoord > 0) {
                            if (!tileCornerState[yCoord + 1][xCoord - 1].contains("h")) {
                                dotLayer.setCell(xCoord, yCoord + 1, cell);
                            }
                        }
                        break;
                    case 2:
                        verticalLayer.setCell(xCoord, yCoord, cell);
                        tileCornerState[yCoord][xCoord].replace("v", "");
                        tilesAccessible[yCoord][xCoord - 1] = true;
                        tilesToSearch.add(xCoord - 1);
                        tilesToSearch.add(yCoord);
                        if (!tileCornerState[yCoord - 1][xCoord].contains("v")) {
                            dotLayer.setCell(xCoord, yCoord, cell);
                        }
                        break;
                    }
                } 
                else if (!(nTileAccessible || sTileAccessible || wTileAccessible)) {
                    switch (random.nextInt(3)) {
                    case 0:
                        horizontalLayer.setCell(xCoord, yCoord, cell);
                        tileCornerState[yCoord][xCoord].replace("h", "");
                        tilesAccessible[yCoord - 1][xCoord] = true;
                        tilesToSearch.add(xCoord);
                        tilesToSearch.add(yCoord - 1);
                        if (xCoord > 0) {
                            if (!tileCornerState[yCoord][xCoord - 1].contains("h")) {
                                dotLayer.setCell(xCoord, yCoord, cell);
                            }
                        }
                        break;
                    case 1:
                        horizontalLayer.setCell(xCoord, yCoord + 1, cell);
                        tileCornerState[yCoord + 1][xCoord].replace("h", "");
                        tilesAccessible[yCoord + 1][xCoord] = true;
                        tilesToSearch.add(xCoord);
                        tilesToSearch.add(yCoord + 1);
                        if (xCoord > 0) {
                            if (!tileCornerState[yCoord + 1][xCoord - 1].contains("h")) {
                                dotLayer.setCell(xCoord, yCoord + 1, cell);
                            }
                        }
                        break;
                    case 2:
                        verticalLayer.setCell(xCoord, yCoord, cell);
                        tileCornerState[yCoord][xCoord].replace("v", "");
                        tilesAccessible[yCoord][xCoord - 1] = true;
                        tilesToSearch.add(xCoord - 1);
                        tilesToSearch.add(yCoord);
                        if (yCoord > 0) {
                            if (!tileCornerState[yCoord - 1][xCoord].contains("v")) {
                                dotLayer.setCell(xCoord, yCoord, cell);
                            }
                        }
                        break;
                    }
                }
                else if (!(nTileAccessible || sTileAccessible)) {
                    switch (random.nextInt(2)) {
                    case 0:
                        horizontalLayer.setCell(xCoord, yCoord, cell);
                        tileCornerState[yCoord][xCoord].replace("h", "");
                        tilesAccessible[yCoord - 1][xCoord] = true;
                        tilesToSearch.add(xCoord);
                        tilesToSearch.add(yCoord - 1);
                        if (xCoord > 0) {
                            if (!tileCornerState[yCoord][xCoord - 1].contains("h")) {
                                dotLayer.setCell(xCoord, yCoord, cell);
                            }
                        }
                        break;
                    case 1:
                        horizontalLayer.setCell(xCoord, yCoord + 1, cell);
                        tileCornerState[yCoord + 1][xCoord].replace("h", "");
                        tilesAccessible[yCoord + 1][xCoord] = true;
                        tilesToSearch.add(xCoord);
                        tilesToSearch.add(yCoord + 1);
                        if (xCoord > 0) {
                            if (!tileCornerState[yCoord + 1][xCoord - 1].contains("h")) {
                                dotLayer.setCell(xCoord, yCoord + 1, cell);
                            }
                        }
                        break;
                    }
                }
                else if (!(eTileAccessible || wTileAccessible)) {
                    switch (random.nextInt(2)) {
                    case 0:
                        verticalLayer.setCell(xCoord + 1, yCoord, cell);
                        tileCornerState[yCoord][xCoord + 1].replace("v", "");
                        tilesAccessible[yCoord][xCoord + 1] = true;
                        tilesToSearch.add(xCoord + 1);
                        tilesToSearch.add(yCoord);
                        if (yCoord > 0) {
                            if (!tileCornerState[yCoord - 1][xCoord + 1].contains("v")) {
                                dotLayer.setCell(xCoord + 1, yCoord, cell);
                            }
                        }
                        break;
                    case 1:
                        verticalLayer.setCell(xCoord, yCoord, cell);
                        tileCornerState[yCoord][xCoord].replace("v", "");
                        tilesAccessible[yCoord][xCoord - 1] = true;
                        tilesToSearch.add(xCoord - 1);
                        tilesToSearch.add(yCoord);
                        if (yCoord > 0) {
                            if (!tileCornerState[yCoord - 1][xCoord].contains("v")) {
                                dotLayer.setCell(xCoord, yCoord, cell);
                            }
                        }
                        break;
                    }
                } 
                else if (!(nTileAccessible || wTileAccessible)) {
                    switch (random.nextInt(2)) {
                    case 0:
                        horizontalLayer.setCell(xCoord, yCoord, cell);
                        tileCornerState[yCoord][xCoord].replace("h", "");
                        tilesAccessible[yCoord - 1][xCoord] = true;
                        tilesToSearch.add(xCoord);
                        tilesToSearch.add(yCoord - 1);
                        if (!tileCornerState[yCoord][xCoord - 1].contains("h")) {
                            dotLayer.setCell(xCoord, yCoord, cell);
                        }
                        break;
                    case 1:
                        verticalLayer.setCell(xCoord, yCoord, cell);
                        tileCornerState[yCoord][xCoord].replace("v", "");
                        tilesAccessible[yCoord][xCoord - 1] = true;
                        tilesToSearch.add(xCoord - 1);
                        tilesToSearch.add(yCoord);
                        if (!tileCornerState[yCoord - 1][xCoord].contains("v")) {
                            dotLayer.setCell(xCoord, yCoord, cell);
                        }
                        break;
                    }
                } 
                else if (!(nTileAccessible || eTileAccessible)) {
                    switch (random.nextInt(2)) {
                    case 0:
                        horizontalLayer.setCell(xCoord, yCoord, cell);
                        tileCornerState[yCoord][xCoord].replace("h", "");
                        tilesAccessible[yCoord - 1][xCoord] = true;
                        tilesToSearch.add(xCoord);
                        tilesToSearch.add(yCoord - 1);
                        if (xCoord > 0) {
                            if (!tileCornerState[yCoord][xCoord - 1].contains("h")) {
                                dotLayer.setCell(xCoord, yCoord, cell);
                            }
                        }
                        break;
                    case 1:
                        verticalLayer.setCell(xCoord + 1, yCoord, cell);
                        tileCornerState[yCoord][xCoord + 1].replace("v", "");
                        tilesAccessible[yCoord][xCoord + 1] = true;
                        tilesToSearch.add(xCoord + 1);
                        tilesToSearch.add(yCoord);
                        if (!tileCornerState[yCoord - 1][xCoord + 1].contains("v")) {
                            dotLayer.setCell(xCoord + 1, yCoord, cell);
                        }
                        break;
                    }
                } 
                else if (!(eTileAccessible || sTileAccessible)) {
                    switch (random.nextInt(2)) {
                    case 0:
                        verticalLayer.setCell(xCoord + 1, yCoord, cell);
                        tileCornerState[yCoord][xCoord + 1].replace("v", "");
                        tilesAccessible[yCoord][xCoord + 1] = true;
                        tilesToSearch.add(xCoord + 1);
                        tilesToSearch.add(yCoord);
                        if (yCoord > 0) {
                            if (!tileCornerState[yCoord - 1][xCoord + 1].contains("v")) {
                                dotLayer.setCell(xCoord + 1, yCoord, cell);
                            }
                        }
                        break;
                    case 1:
                        horizontalLayer.setCell(xCoord, yCoord + 1, cell);
                        tileCornerState[yCoord + 1][xCoord].replace("h", "");
                        tilesAccessible[yCoord + 1][xCoord] = true;
                        tilesToSearch.add(xCoord);
                        tilesToSearch.add(yCoord + 1);
                        if (xCoord > 0) {
                            if (!tileCornerState[yCoord + 1][xCoord - 1].contains("h")) {
                                dotLayer.setCell(xCoord, yCoord + 1, cell);
                            }
                        }
                        break;
                    }
                } 
                else if (!(sTileAccessible || wTileAccessible)) {
                    switch (random.nextInt(2)) {
                    case 0:
                        horizontalLayer.setCell(xCoord, yCoord + 1, cell);
                        tileCornerState[yCoord + 1][xCoord].replace("h", "");
                        tilesAccessible[yCoord + 1][xCoord] = true;
                        tilesToSearch.add(xCoord);
                        tilesToSearch.add(yCoord + 1);
                        if (!tileCornerState[yCoord + 1][xCoord - 1].contains("h")) {
                            dotLayer.setCell(xCoord, yCoord + 1, cell);
                        }
                        break;
                    case 1:
                        verticalLayer.setCell(xCoord, yCoord, cell);
                        tileCornerState[yCoord][xCoord].replace("v", "");
                        tilesAccessible[yCoord][xCoord - 1] = true;
                        tilesToSearch.add(xCoord - 1);
                        tilesToSearch.add(yCoord);
                        if (yCoord > 0) {
                            if (!tileCornerState[yCoord - 1][xCoord].contains("v")) {
                                dotLayer.setCell(xCoord, yCoord, cell);
                            }
                        }
                        break;
                    }
                } 
                else if (!nTileAccessible) {
                    horizontalLayer.setCell(xCoord, yCoord, cell);
                    tileCornerState[yCoord][xCoord].replace("h", "");
                    tilesAccessible[yCoord - 1][xCoord] = true;
                    tilesToSearch.add(xCoord);
                    tilesToSearch.add(yCoord - 1);
                    if (xCoord > 0) {
                        if (!tileCornerState[yCoord][xCoord - 1].contains("h")) {
                            dotLayer.setCell(xCoord, yCoord, cell);
                        }
                    }
                } 
                else if (!eTileAccessible) {
                    verticalLayer.setCell(xCoord + 1, yCoord, cell);
                    tileCornerState[yCoord][xCoord + 1].replace("v", "");
                    tilesAccessible[yCoord][xCoord + 1] = true;
                    tilesToSearch.add(xCoord + 1);
                    tilesToSearch.add(yCoord);
                    if (yCoord > 0) {
                        if (!tileCornerState[yCoord - 1][xCoord + 1].contains("v")) {
                            dotLayer.setCell(xCoord + 1, yCoord, cell);
                        }
                    }
                } 
                else if (!sTileAccessible) {
                    horizontalLayer.setCell(xCoord, yCoord + 1, cell);
                    tileCornerState[yCoord + 1][xCoord].replace("h", "");
                    tilesAccessible[yCoord + 1][xCoord] = true;
                    tilesToSearch.add(xCoord);
                    tilesToSearch.add(yCoord + 1);
                    if (xCoord > 0) {
                        if (!tileCornerState[yCoord + 1][xCoord - 1].contains("h")) {
                            dotLayer.setCell(xCoord, yCoord + 1, cell);
                        }
                    }
                } 
                else if (!wTileAccessible) {
                    verticalLayer.setCell(xCoord, yCoord, cell);
                    tileCornerState[yCoord][xCoord].replace("v", "");
                    tilesAccessible[yCoord][xCoord - 1] = true;
                    tilesToSearch.add(xCoord - 1);
                    tilesToSearch.add(yCoord);
                    if (yCoord > 0) {
                        if (!tileCornerState[yCoord - 1][xCoord].contains("v")) {
                            dotLayer.setCell(xCoord, yCoord, cell);
                        }
                    }
                }
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

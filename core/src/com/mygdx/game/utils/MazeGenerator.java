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

public class MazeGenerator {
    private TiledMap groundMap;
    private TiledMap mazeMap;
    
    private int mazeSizeX;
    private int mazeSizeY;
    
    private TiledMapTileLayer groundLayer;
    private TiledMapTileLayer dotLayer;
    private TiledMapTileLayer verticalLayer;
    private TiledMapTileLayer horizontalLayer;
    
    private String[][] tileCornerStates;
    private boolean[][] accessibleTiles;
    Queue<Vector2> tilesToSearch;
    
    private TiledMapTileLayer mazeLayer;
    
    public MazeGenerator(int mazeSizeX, int mazeSizeY) {
        groundMap = new TiledMap();
        mazeMap = new TiledMap();
        
        this.mazeSizeX = mazeSizeX;
        this.mazeSizeY = mazeSizeY;
        
        groundLayer = new TiledMapTileLayer(mazeSizeX, mazeSizeY, 128, 128);
        verticalLayer = new TiledMapTileLayer(mazeSizeX + 1, mazeSizeY, 128, 128);
        horizontalLayer = new TiledMapTileLayer(mazeSizeX, mazeSizeY + 1, 128, 128);
        dotLayer = new TiledMapTileLayer(mazeSizeX + 1, mazeSizeY + 1, 128, 128);
        mazeLayer = new TiledMapTileLayer(mazeSizeX, mazeSizeY, 128, 128);
        
        verticalLayer.setName("vertical-layer");
        horizontalLayer.setName("horizontal-layer");
        dotLayer.setName("dot-layer");
        mazeLayer.setName("maze-layer");
        
        groundMap.getLayers().add(groundLayer);
        mazeMap.getLayers().add(dotLayer);
        mazeMap.getLayers().add(verticalLayer);
        mazeMap.getLayers().add(horizontalLayer);
        
        tileCornerStates = new String[mazeSizeY + 1][mazeSizeX + 1];
        accessibleTiles = new boolean[mazeSizeY][mazeSizeX];
        tilesToSearch = new LinkedList<>();
    }
    
    public void generateGround() {
        Random random = new Random();
//        int tileMapType = random.nextInt(2);
        int tileMapType = 0;
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
        for (int y = 0; y < mazeSizeY + 1; y++) {
            for (int x = 0; x < mazeSizeX + 1; x++) {
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
        
        int x = random.nextInt(mazeSizeX);
        int y = random.nextInt(mazeSizeY);
        accessibleTiles[y][x] = true;
        for (int i = 0; i < mazeSizeX * mazeSizeY; i++) {
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
                if (!accessibleTiles[y - 1][x]) {
                    accessibleTiles[y - 1][x] = true;
                    tilesToSearch.add(new Vector2(x, y - 1));
                }
            }
            if (!eastWall && x < mazeSizeX - 1) {
                if (!accessibleTiles[y][x + 1]) {
                    accessibleTiles[y][x + 1] = true;
                    tilesToSearch.add(new Vector2(x + 1, y));
                }
            }
            if (!southWall && y < mazeSizeY - 1) {
                if (!accessibleTiles[y + 1][x]) {
                    accessibleTiles[y + 1][x] = true;
                    tilesToSearch.add(new Vector2(x, y + 1));
                }
            }
            if (!westWall && x > 0) {
                if (!accessibleTiles[y][x - 1]) {
                    accessibleTiles[y][x - 1] = true;
                    tilesToSearch.add(new Vector2(x - 1, y));
                }
            }

            // If the adjacent tiles cannot be accessed then 
            // delete a wall from a random adjacent tile to continue the search
            if (tilesToSearch.isEmpty()) {
                boolean tileAccessed;
                LinkedList blockingTiles = new LinkedList();
                
                // Stores each tile that blocks the current path
                for (y = 0; y < mazeSizeY; y++) {
                    for (x = 0; x < mazeSizeX; x++) {
                        tileAccessed = accessibleTiles[y][x];
                        if (!tileAccessed) {
                            continue;
                        }
                        boolean nTileAccessible = true;
                        boolean eTileAccessible = true;
                        boolean sTileAccessible = true;
                        boolean wTileAccessible = true;
                        if (y > 0) {
                            nTileAccessible = accessibleTiles[y - 1][x];
                        }
                        if (x < mazeSizeX - 1) {
                            eTileAccessible = accessibleTiles[y][x + 1];
                        }
                        if (y < mazeSizeY - 1) {
                            sTileAccessible = accessibleTiles[y + 1][x];
                        }
                        if (x > 0) {
                            wTileAccessible = accessibleTiles[y][x - 1];
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
                wallsToDestroy = random.nextInt(blockingTiles.size() / 2);
                
                for (int n = 0; n <= wallsToDestroy; n++) {
                    if (blockingTiles.isEmpty()) {
                        break;
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
                        nTileAccessible = accessibleTiles[yCoord - 1][xCoord];
                    } 
                    if (xCoord < mazeSizeX - 1) {
                        eTileAccessible = accessibleTiles[yCoord][xCoord + 1];
                    }
                    if (yCoord < mazeSizeY - 1) {
                        sTileAccessible = accessibleTiles[yCoord + 1][xCoord];
                    }
                    if (xCoord > 0) {
                        wTileAccessible = accessibleTiles[yCoord][xCoord - 1];
                    } 

                    if (!(nTileAccessible || eTileAccessible || wTileAccessible)) {
                        switch (random.nextInt(3)) {
                        case 0:
                            deleteNorthWall(xCoord, yCoord);
                            break;
                        case 1:
                            deleteEastWall(xCoord, yCoord);
                            break;
                        case 2:
                            deleteWestWall(xCoord, yCoord);
                            break;
                        }
                    }
                    else if (!(nTileAccessible || eTileAccessible || sTileAccessible)) {
                        switch (random.nextInt(3)) {
                        case 0:
                            deleteNorthWall(xCoord, yCoord);
                            break;
                        case 1:
                            deleteEastWall(xCoord, yCoord);
                            break;
                        case 2:
                            deleteSouthWall(xCoord, yCoord);
                            break;
                         }
                    }
                    else if (!(eTileAccessible || sTileAccessible || wTileAccessible)) {
                        switch (random.nextInt(3)) {
                        case 0:
                            deleteEastWall(xCoord, yCoord);
                            break;
                        case 1:
                            deleteSouthWall(xCoord, yCoord);
                            break;
                        case 2:
                            deleteWestWall(xCoord, yCoord);
                            break;
                        }
                    } 
                    else if (!(nTileAccessible || sTileAccessible || wTileAccessible)) {
                        switch (random.nextInt(3)) {
                        case 0:
                            deleteNorthWall(xCoord, yCoord);
                            break;
                        case 1:
                            deleteSouthWall(xCoord, yCoord);
                            break;
                        case 2:
                            deleteWestWall(xCoord, yCoord);
                            break;
                        }
                    }
                    else if (!(nTileAccessible || sTileAccessible)) {
                        switch (random.nextInt(2)) {
                        case 0:
                            deleteNorthWall(xCoord, yCoord);
                            break;
                        case 1:
                            deleteSouthWall(xCoord, yCoord);
                            break;
                        }
                    }
                    else if (!(eTileAccessible || wTileAccessible)) {
                        switch (random.nextInt(2)) {
                        case 0:
                            deleteEastWall(xCoord, yCoord);
                            break;
                        case 1:
                            deleteWestWall(xCoord, yCoord);
                            break;
                        }
                    } 
                    else if (!(nTileAccessible || wTileAccessible)) {
                        switch (random.nextInt(2)) {
                        case 0:
                            deleteNorthWall(xCoord, yCoord);
                            break;
                        case 1:
                            deleteWestWall(xCoord, yCoord);
                            break;
                        }
                    } 
                    else if (!(nTileAccessible || eTileAccessible)) {
                        switch (random.nextInt(2)) {
                        case 0:
                            deleteNorthWall(xCoord, yCoord);
                            break;
                        case 1:
                            deleteEastWall(xCoord, yCoord);
                            break;
                        }
                    } 
                    else if (!(eTileAccessible || sTileAccessible)) {
                        switch (random.nextInt(2)) {
                        case 0:
                            deleteEastWall(xCoord, yCoord);
                            break;
                        case 1:
                            deleteSouthWall(xCoord, yCoord);
                            break;
                        }
                    } 
                    else if (!(sTileAccessible || wTileAccessible)) {
                        switch (random.nextInt(2)) {
                        case 0:
                            deleteSouthWall(xCoord, yCoord);
                            break;
                        case 1:
                            deleteWestWall(xCoord, yCoord);
                            break;
                        }
                    } 
                    else if (!nTileAccessible) {
                        deleteNorthWall(xCoord, yCoord);
                    } 
                    else if (!eTileAccessible) {
                        deleteEastWall(xCoord, yCoord);
                    } 
                    else if (!sTileAccessible) {
                        deleteSouthWall(xCoord, yCoord);
                    } 
                    else if (!wTileAccessible) {
                        deleteWestWall(xCoord, yCoord);
                    }
                }
            }
        }
    }
    
    private void deleteNorthWall(int xCoord, int yCoord) {
        Cell cell = new Cell();
        horizontalLayer.setCell(xCoord, yCoord, cell);
        tileCornerStates[yCoord][xCoord] = tileCornerStates[yCoord][xCoord].replace("h", "");
        accessibleTiles[yCoord - 1][xCoord] = true;
        tilesToSearch.add(new Vector2(xCoord, yCoord - 1));
        if (xCoord > 0) {
            if (!tileCornerStates[yCoord][xCoord - 1].contains("h")
                    && !tileCornerStates[yCoord - 1][xCoord].contains("v") 
                    && !tileCornerStates[yCoord][xCoord].contains("v")) {
                dotLayer.setCell(xCoord, yCoord, cell);
                tileCornerStates[yCoord][xCoord] = tileCornerStates[yCoord][xCoord].replace("d", "");
            }
        }
        if (xCoord < mazeSizeX - 1) {
            if (!tileCornerStates[yCoord][xCoord + 1].contains("h")
                    && !tileCornerStates[yCoord - 1][xCoord + 1].contains("v") 
                    && !tileCornerStates[yCoord][xCoord + 1].contains("v")) {
                dotLayer.setCell(xCoord + 1, yCoord, cell);
                tileCornerStates[yCoord][xCoord + 1] = tileCornerStates[yCoord][xCoord + 1].replace("d", "");
            }
        }
    }
    
    private void deleteEastWall(int xCoord, int yCoord) {
        Cell cell = new Cell();
        verticalLayer.setCell(xCoord + 1, yCoord, cell);
        tileCornerStates[yCoord][xCoord + 1] = tileCornerStates[yCoord][xCoord + 1].replace("v", "");
        accessibleTiles[yCoord][xCoord + 1] = true;
        tilesToSearch.add(new Vector2(xCoord + 1, yCoord));
        if (yCoord > 0) {
            if (!tileCornerStates[yCoord - 1][xCoord + 1].contains("v")
                    && !tileCornerStates[yCoord][xCoord].contains("h") 
                    && !tileCornerStates[yCoord][xCoord + 1].contains("h")) {
                dotLayer.setCell(xCoord + 1, yCoord, cell);
                tileCornerStates[yCoord][xCoord + 1] = tileCornerStates[yCoord][xCoord + 1].replace("d", "");
            }
        }
        if (yCoord < mazeSizeY - 1) {
            if (!tileCornerStates[yCoord + 1][xCoord + 1].contains("v")
                    && !tileCornerStates[yCoord + 1][xCoord].contains("h") 
                    && !tileCornerStates[yCoord + 1][xCoord + 1].contains("h")) {
                dotLayer.setCell(xCoord + 1, yCoord + 1, cell);
                tileCornerStates[yCoord + 1][xCoord + 1] = tileCornerStates[yCoord + 1][xCoord + 1].replace("d", "");
            }
        }
    }
    
    private void deleteSouthWall(int xCoord, int yCoord) {
        Cell cell = new Cell();
        horizontalLayer.setCell(xCoord, yCoord + 1, cell);
        tileCornerStates[yCoord + 1][xCoord] = tileCornerStates[yCoord + 1][xCoord].replace("h", "");
        accessibleTiles[yCoord + 1][xCoord] = true;
        tilesToSearch.add(new Vector2(xCoord, yCoord + 1));
        if (xCoord > 0) {
            if (!tileCornerStates[yCoord + 1][xCoord - 1].contains("h")
                    && !tileCornerStates[yCoord + 1][xCoord].contains("v") 
                    && !tileCornerStates[yCoord][xCoord].contains("v")) {
                dotLayer.setCell(xCoord, yCoord + 1, cell);
                tileCornerStates[yCoord + 1][xCoord] = tileCornerStates[yCoord + 1][xCoord].replace("d", "");
            }
        }
        if (xCoord < mazeSizeX - 1) {
            if (!tileCornerStates[yCoord + 1][xCoord + 1].contains("h")
                    && !tileCornerStates[yCoord + 1][xCoord + 1].contains("v") 
                    && !tileCornerStates[yCoord][xCoord + 1].contains("v")) {
                dotLayer.setCell(xCoord + 1, yCoord + 1, cell);
                tileCornerStates[yCoord + 1][xCoord + 1] = tileCornerStates[yCoord + 1][xCoord + 1].replace("d", "");
            }
        }
    }
    
    private void deleteWestWall(int xCoord, int yCoord) {
        Cell cell = new Cell();
        verticalLayer.setCell(xCoord, yCoord, cell);
        tileCornerStates[yCoord][xCoord] = tileCornerStates[yCoord][xCoord].replace("v", "");
        accessibleTiles[yCoord][xCoord - 1] = true;
        tilesToSearch.add(new Vector2(xCoord - 1, yCoord));
        if (yCoord > 0) {
            if (!tileCornerStates[yCoord - 1][xCoord].contains("v")
                    && !tileCornerStates[yCoord][xCoord - 1].contains("h") 
                    && !tileCornerStates[yCoord][xCoord].contains("h")) {
                dotLayer.setCell(xCoord, yCoord, cell);
                tileCornerStates[yCoord][xCoord] = tileCornerStates[yCoord][xCoord].replace("d", "");
            }
        }
        if (yCoord < mazeSizeY - 1) {
            if (!tileCornerStates[yCoord + 1][xCoord].contains("v")
                    && !tileCornerStates[yCoord + 1][xCoord - 1].contains("h") 
                    && !tileCornerStates[yCoord + 1][xCoord].contains("h")) {
                dotLayer.setCell(xCoord, yCoord + 1, cell);
                tileCornerStates[yCoord + 1][xCoord] = tileCornerStates[yCoord + 1][xCoord].replace("d", "");
            }
        }
    }
    
    public int getMazeSizeX() {
        return mazeSizeX;
    }
    
    public int getMazeSizeY() {
        return mazeSizeY;
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

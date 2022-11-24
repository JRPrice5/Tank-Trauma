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
//    private LinkedList<LinkedList> tilesToSearch;
    
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
        boolean[][] isTileAccessible = new boolean[mapSize][mapSize];
        isTileAccessible[0][0] = true;
        boolean FullyAccessible = false;
        
//        while (!FullyAccessible) {
            int x = 0;
            int y = 0;
            boolean[][] beenAccessed = new boolean[mapSize][mapSize];
            beenAccessed[0][0] = true;
            for (int i = 0; i < mapSize * mapSize; i++) {
                // Retrieves and removes the coords of the first tile
                // from the queue
                if (i > 0) {
                        x = (int) tilesToSearch.poll();
                        y = (int) tilesToSearch.poll();
                }
                
                // Determines whether adjacent tiles can be accessed, 
                // if so, they are added to tilesToSearch
//                if (x <= mapSize - 1 && y <= mapSize - 1){
                boolean northWall = (tileCornerState[y][x].contains("h"));
                boolean eastWall = (tileCornerState[y][x + 1].contains("v"));
                boolean southWall = (tileCornerState[y + 1][x].contains("h"));
                boolean westWall = (tileCornerState[y][x].contains("v"));
//                }
                
                if (!northWall && y >= 1) {
                    if (!beenAccessed[y - 1][x]) {
                        isTileAccessible[y - 1][x] = true;
                        beenAccessed[y - 1][x] = true;
                        tilesToSearch.add(x);
                        tilesToSearch.add(y - 1);
                    }
                }
                if (!eastWall && x < mapSize - 1) {
                    if (!beenAccessed[y][x + 1]) {
                        isTileAccessible[y][x + 1] = true;
                        beenAccessed[y][x + 1] = true;
                        tilesToSearch.add(x + 1);
                        tilesToSearch.add(y);
                    }
                }
                if (!southWall && y < mapSize - 1) {
                    if (!beenAccessed[y + 1][x]) {
                        isTileAccessible[y + 1][x] = true;
                        beenAccessed[y + 1][x] = true;
                        tilesToSearch.add(x);
                        tilesToSearch.add(y + 1);
                    }
                }
                if (!westWall && x >= 1) {
                    if (!beenAccessed[y][x - 1]) {
                        isTileAccessible[y][x - 1] = true;
                        beenAccessed[y][x - 1] = true;
                        tilesToSearch.add(x - 1);
                        tilesToSearch.add(y);
                    }
                }
                
                // If the adjacent tiles cannot be accessed, 
                // delete a wall from a random adjacent tile to continue the
                // search
                Cell cell = new Cell();
                if (tilesToSearch.isEmpty()) {
                    if (x >= 1 && x < mapSize - 1 && y >= 1 && y < mapSize - 1) {
                        switch (random.nextInt(4)) {
                        case 0:
                            verticalLayer.setCell(x, y, cell);
                            if (y >= 1) {
                                if (!tileCornerState[y - 1][x].contains("v")) {
                                    dotLayer.setCell(x, y, cell);
                                }
                            }
                            tileCornerState[y][x].replace("v", ""); 
                            tilesToSearch.add(x - 1);
                            tilesToSearch.add(y);
                            break;
                        case 1:
                            horizontalLayer.setCell(x, y, cell);
                            if (x >= 1) {
                                if (!tileCornerState[y][x - 1].contains("h")) {
                                    dotLayer.setCell(x, y, cell);
                                }
                            }
                            tileCornerState[y][x].replace("h", "");
                            tilesToSearch.add(x);
                            tilesToSearch.add(y - 1);
                            break;
                        case 2:
                            verticalLayer.setCell(x + 1, y, cell);
                            if (y >= 1) {
                                if (!tileCornerState[y - 1][x + 1].contains("v")) {
                                    dotLayer.setCell(x, y, cell);
                                }
                            }
                            tileCornerState[y][x + 1].replace("v", "");
                            tilesToSearch.add(x + 1);
                            tilesToSearch.add(y);
                            break;
                        case 3:
                            horizontalLayer.setCell(x, y + 1, cell);
                            if (x >= 1) {
                                if (!tileCornerState[y + 1][x - 1].contains("h")) {
                                    dotLayer.setCell(x, y, cell);
                                }
                            }
                            tileCornerState[y + 1][x].replace("h", "");
                            tilesToSearch.add(x);
                            tilesToSearch.add(y + 1);
                            break;
                        }
                    } else if (x == 0 && y >= 1 && y < mapSize - 1) {
                        switch (random.nextInt(3)) {
                        case 0:
                            horizontalLayer.setCell(x, y, cell);
                            tileCornerState[y][x].replace("v", "");
                            tilesToSearch.add(x);
                            tilesToSearch.add(y - 1);
                            break;
                        case 1:
                            verticalLayer.setCell(x + 1, y, cell);
                            if (!tileCornerState[y - 1][x + 1].contains("v")) {
                                    dotLayer.setCell(x, y, cell);
                            }
                            tileCornerState[y][x + 1].replace("v", "");
                            tilesToSearch.add(x + 1);
                            tilesToSearch.add(y);
                            break;
                        case 2:
                            horizontalLayer.setCell(x, y + 1, cell);
                            tileCornerState[y + 1][x].replace("h", "");
                            tilesToSearch.add(x);
                            tilesToSearch.add(y + 1);
                            break;
                        }
                    } else if (x == mapSize - 1 && y >= 1 && y < mapSize - 1) {
                        switch (random.nextInt(3)) {
                        case 0:
                            verticalLayer.setCell(x, y, cell);
                            if (!tileCornerState[y - 1][x].contains("v")) {
                                    dotLayer.setCell(x, y, cell);
                            }
                            tileCornerState[y][x].replace("v", "");
                            tilesToSearch.add(x - 1);
                            tilesToSearch.add(y);
                            break;
                        case 1:
                            horizontalLayer.setCell(x, y, cell);
                            if (!tileCornerState[y][x - 1].contains("h")) {
                                dotLayer.setCell(x, y, cell);
                            }
                            tileCornerState[y][x].replace("h", "");
                            tilesToSearch.add(x);
                            tilesToSearch.add(y - 1);
                            break;
                        case 2:
                            horizontalLayer.setCell(x, y + 1, cell);
                            if (!tileCornerState[y + 1][x - 1].contains("h")) {
                                dotLayer.setCell(x, y, cell);
                            }
                            tileCornerState[y + 1][x].replace("h", "");
                            tilesToSearch.add(x);
                            tilesToSearch.add(y + 1);
                            break;
                        }
                    } else if (x >= 1 && x < mapSize - 1 && y == 0) {
                        switch (random.nextInt(3)) {
                        case 0:
                            verticalLayer.setCell(x, y, cell);
                            tileCornerState[y][x].replace("h", "");
                            tilesToSearch.add(x - 1);
                            tilesToSearch.add(y);
                            break;
                        case 1:
                            verticalLayer.setCell(x + 1, y, cell);
                            tileCornerState[y][x + 1].replace("v", "");
                            tilesToSearch.add(x + 1);
                            tilesToSearch.add(y);
                            break;
                        case 2:
                            horizontalLayer.setCell(x, y + 1, cell);
                            if (!tileCornerState[y + 1][x - 1].contains("h")) {
                                dotLayer.setCell(x, y, cell);
                            }
                            tileCornerState[y + 1][x].replace("h", "");
                            tilesToSearch.add(x);
                            tilesToSearch.add(y + 1);
                            break;
                        }
                    } else if (x >= 1 && x < mapSize - 1 && y == mapSize - 1) {
                        switch (random.nextInt(3)) {
                        case 0:
                            verticalLayer.setCell(x, y, cell);
                            if (!tileCornerState[y - 1][x].contains("v")) {
                                    dotLayer.setCell(x, y, cell);
                            }                                
                            tileCornerState[y][x].replace("v", "");
                            tilesToSearch.add(x - 1);
                            tilesToSearch.add(y);
                            break;
                        case 1:
                            horizontalLayer.setCell(x, y, cell);
                            if (!tileCornerState[y][x - 1].contains("h")) {
                                    dotLayer.setCell(x, y, cell);
                            }
                            tileCornerState[y][x].replace("h", "");
                            tilesToSearch.add(x);
                            tilesToSearch.add(y - 1);
                            break;
                        case 2:
                            verticalLayer.setCell(x + 1, y, cell);
                            if (!tileCornerState[y - 1][x + 1].contains("v")) {
                                    dotLayer.setCell(x, y, cell);
                            }                                
                            tileCornerState[y][x + 1].replace("v", "");
                            tilesToSearch.add(x + 1);
                            tilesToSearch.add(y);
                            break;
                        }
                    } else if (x == 0 && y == 0) {
                        switch (random.nextInt(2)) {
                        case 0:
                            verticalLayer.setCell(x + 1, y, cell);
                            tileCornerState[y][x + 1].replace("v", "");
                            tilesToSearch.add(x + 1);
                            tilesToSearch.add(y);
                            break;
                        case 1:
                            horizontalLayer.setCell(x, y + 1, cell);
                            tileCornerState[y + 1][x].replace("h", "");
                            tilesToSearch.add(x);
                            tilesToSearch.add(y + 1);
                            break;
                        }
                    } else if (x == 0 && y == mapSize - 1) {
                        switch (random.nextInt(2)) {
                        case 0:
                            horizontalLayer.setCell(x, y, cell);
                            tileCornerState[y][x].replace("h", "");
                            tilesToSearch.add(x);
                            tilesToSearch.add(y - 1);
                            break;
                        case 1:
                            verticalLayer.setCell(x + 1, y, cell);
                            if (!tileCornerState[y - 1][x + 1].contains("v")) {
                                    dotLayer.setCell(x, y, cell);
                            } 
                            tileCornerState[y][x + 1].replace("v", "");
                            tilesToSearch.add(x + 1);
                            tilesToSearch.add(y);
                            break;
                        }
                    } else if (x == mapSize - 1 && y == 0) {
                        switch (random.nextInt(2)) {
                        case 0:
                            verticalLayer.setCell(x, y, cell);
                            tileCornerState[y][x].replace("v", ""); 
                            tilesToSearch.add(x - 1);
                            tilesToSearch.add(y);
                            break;
                        case 1:
                            horizontalLayer.setCell(x, y + 1, cell);
                            if (!tileCornerState[y + 1][x - 1].contains("h")) {
                                dotLayer.setCell(x, y, cell);
                            }
                            tileCornerState[y + 1][x].replace("h", "");
                            tilesToSearch.add(x);
                            tilesToSearch.add(y + 1);
                            break;
                        }                       
                    } else if (x == mapSize - 1 && y == mapSize - 1) {
                        switch (random.nextInt(2)) {
                        case 0:
                            verticalLayer.setCell(x, y, cell);
                            if (!tileCornerState[y - 1][x].contains("v")) {
                                    dotLayer.setCell(x, y, cell);
                            } 
                            tileCornerState[y][x].replace("v", ""); 
                            tilesToSearch.add(x - 1);
                            tilesToSearch.add(y);
                            break;
                        case 1:
                            horizontalLayer.setCell(x, y, cell);
                            if (!tileCornerState[y][x - 1].contains("h")) {
                                    dotLayer.setCell(x, y, cell);
                            }
                            tileCornerState[y][x].replace("h", "");
                            tilesToSearch.add(x);
                            tilesToSearch.add(y - 1);
                            break;
                        }                      
                    }
                }
            }
            FullyAccessible = true;
            for (boolean[] row : isTileAccessible) {
                for (boolean tile : row) {
                    if (tile = false) {
                        FullyAccessible = false;
                        System.out.print("0 ");
                    } else {
                        System.out.print("1 ");
                    }
                }
                System.out.println("");
            }
//        }
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

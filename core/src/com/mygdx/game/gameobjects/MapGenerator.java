package com.mygdx.game.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import java.util.LinkedList;
import java.util.Random;

public class MapGenerator {
    private TiledMap groundMap;
    private TiledMap mazeMap;
    private int mapSize;
    private TiledMapTileLayer groundLayer;
    private TiledMapTileLayer dotLayer;
    private TiledMapTileLayer verticalLayer;
    private TiledMapTileLayer horizontalLayer;
    private LinkedList<int[]> cellsToSearch;
    
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
        
        int[][] tiles = new int[mapSize][mapSize];
        for (int[] row : tiles) {
            for (int tile : row) {
                tile = 0;
            }
        }
        tiles[0][0] = 1;
        Boolean a = true;
        int[] cellToSearch = {0, 0};
        cellsToSearch.add(cellToSearch);
        for (int i = 0; i < mapSize * mapSize; i++) {
            int x = cellsToSearch.get(0)[1];
            int y = cellsToSearch.get(0)[0];
            int currentTile = tiles[y][x];
            String tileState = tileCornerState[y][x] 
                    + tileCornerState[y + 1][x].replace("v", "")
                    + tileCornerState[y][x + 1].replace("h", "");
//            tiles[y + 1][x] = currentTile + 1;
//            tiles[y][x + 1] = currentTile + 1;
            Cell cell = new Cell();
            
            // Checks whether tile is surrounded by walls
            if (tileState.contentEquals("vhvh")) {
                
                // Deletes a random wall from the tile and updates cellsToSearch
                switch (random.nextInt(4)) {
                    case 0:
                        verticalLayer.setCell(x, y, cell);
                        cellToSearch = {x - 1, y};
                        cellsToSearch.add(cellToSearch);
                        break;
                    case 1:
                        horizontalLayer.setCell(x, y, cell);
                        cellToSearch = {x, y - 1};
                        cellsToSearch.add(cellToSearch);
                        break;
                    case 2:
                        verticalLayer.setCell(x + 1, y, cell);
                        cellToSearch = {x + 1, y};
                        cellsToSearch.add(cellToSearch);
                        break;
                    case 3:
                        horizontalLayer.setCell(x, y + 1, cell);
                        cellToSearch = {x, y + 1};
                        cellsToSearch.add(cellToSearch);
                        break;
                }
            }                
//            if (x >= 0 && x <= horizontalLayer.getWidth() - 1 && y >= 0 && y <= verticalLayer.getHeight() - 1) {
//               
//            }
        }
    }
    
//        nodes[]
//        int x = 0;
//        int y = 0;
//        for (int i = 0; i < mapSize * mapSize; i++) {
//            String tileState = tileCornerState[y][x] 
//                    + tileCornerState[y + 1][x].replace("v", "")
//                    + tileCornerState[y][x + 1].replace("h", "");
//            if (tileState.contentEquals("vhvh")) {
//                
//            }
//            if (x >= 0 && x <= horizontalLayer.getWidth() - 1 && y >= 0 && y <= verticalLayer.getHeight() - 1) {
//               
//            }
//        }
    
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

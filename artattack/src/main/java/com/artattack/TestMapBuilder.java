package com.artattack;

import java.util.List;

public class TestMapBuilder implements MapBuilder {
    private Maps map;
    
    public TestMapBuilder(){
        this.map = new Maps();
    }

    public void reset() {
        this.map = new Maps();
    }

    @Override
    public void setPlayerOne(Player player) {
        this.map.setPlayerOne(player);
    }

    @Override
    public void setPlayerTwo(Player player) {
        this.map.setPlayerTwo(player);
    }

    @Override
    public void setInteractableElements(List<InteractableElement> elements) {
        this.map.setInteractableElements(elements);
    }

    @Override
    public void setEnemies(List<Enemy> enemies) {
        this.map.setEnemies(enemies);
    }

    @Override
    public void setDict(){
        this.map.setDict();
    }

    @Override
    public void setTurnQueue(){
        this.map.setTurnQueue();
    }

    @Override
    public void setDimension(int rows, int columns){
        this.map.setDimension(rows, columns);
    }

    @Override
    public void startMap() {
        int rows = this.map.getRows();
        int columns = this.map.getColumns();

        char[][] charMatrix = new char[rows][columns];

        // 1️⃣ Inizializza tutte le celle a '.'
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                charMatrix[i][j] = '.';
            }
        }

        // 2️⃣ Bordo superiore e inferiore
        for (int j = 0; j < columns; j++) {
            charMatrix[0][j] = '#';
            charMatrix[rows - 1][j] = '#';
        }

        // 3️⃣ Bordo sinistro e destro
        for (int i = 0; i < rows; i++) {
            charMatrix[i][0] = '#';
            charMatrix[i][columns - 1] = '#';
        }

        // 4️⃣ Ostacolo fisso
        for (int j = 5; j < 15 && j < columns; j++) {
            if (10 < rows) {
                charMatrix[10][j] = '#';
            }
        }

        // 5️⃣ Posiziona tutti gli elementi della mappa
        for (Coordinates key : this.map.getDict().keySet()) {
            MapElement element = this.map.getDict().get(key);
            int y = key.getY();
            int x = key.getX();

            if (y >= 0 && y < rows && x >= 0 && x < columns) {
                charMatrix[y][x] = element.getMapSymbol();
            } else {
                System.out.println("⚠️ Coordinate fuori mappa ignorate: " + key);
            }
        }

        // 6️⃣ Avvia la mappa con la matrice costruita
        this.map.startMap(charMatrix);
}


    @Override
    public Maps getResult() {
        Maps product = this.map;
        this.reset();
        return product;
    }

}
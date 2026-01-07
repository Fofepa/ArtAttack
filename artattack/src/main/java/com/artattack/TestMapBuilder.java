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
    public void startMap(){
        char[][] charMatrix = new char[this.map.getRows()][this.map.getColumns()];
        
        for (int i = 0; i < this.map.getRows(); i++) {
            for (int j = 0; j < this.map.getColumns(); j++) {
                charMatrix[i][j] = '.';
            }
        }
        
        for (int i = 0; i < this.map.getColumns(); i++) {
            charMatrix[0][i] = '#';
            charMatrix[this.map.getRows() - 1][i] = '#';
        }
        for (int i = 0; i < this.map.getRows(); i++) {
            charMatrix[i][0] = '#';
            charMatrix[i][this.map.getColumns() - 1] = '#';
        }
        
        for (int i = 5; i < 15; i++) {
            charMatrix[10][i] = '#';
        }
        
        for (Coordinates key : this.map.getDict().keySet()){
            MapElement element = this.map.getDict().get(key);
            charMatrix[key.getY()][key.getX()] = element.getMapSymbol();
        }

        this.map.startMap(charMatrix);
    }

    @Override
    public Maps getResult() {
        Maps product = this.map;
        this.reset();
        return product;
    }

}
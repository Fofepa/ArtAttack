package com.artattack.level;

import java.util.List;

import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.InteractableElement;
import com.artattack.mapelements.Player;

public class TutorialMapBuilder implements MapBuilder {
    private Maps map;

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
    public void setDimension(int rows, int columns) { //26 x 32
        this.map.setDimension(rows, columns);
    }

    @Override
    public void startMap() {
        char[][] charMatrix = new char[this.map.getRows()][this.map.getColumns()];
        for (int i = 0; i < charMatrix.length; i++) {
            charMatrix[0][i] = '#';
            charMatrix[charMatrix[0].length][i] = '#';
        }
        for (int i = 1; i < charMatrix[0].length - 1; i++) {
            charMatrix[i][0] = '#';
            charMatrix[i][charMatrix.length] = '#';
        }
        for (int i = 1; i < charMatrix.length - 1; i++) {
            for (int j = 1; i < charMatrix[0].length - 1; i++) {
                charMatrix[i][j] = '.';
            }
        }
        for (Coordinates key : this.map.getDict().keySet()) {
            charMatrix[key.getX()][key.getY()] = this.map.getDict().get(key).getMapSymbol();
        }
        this.map.startMap(charMatrix);
    }

    @Override
    public void setDict() {
        this.map.setDict();
    }

    @Override
    public void setTurnQueue() {
        this.map.setTurnQueue();
    }

    @Override
    public Maps getResult() {
        Maps product = this.map;
        this.reset();
        return product;
    }
    
}

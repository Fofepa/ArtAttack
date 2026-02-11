package com.artattack.level;

import java.util.List;

import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.InteractableElement;
import com.artattack.mapelements.Player;
import com.artattack.mapelements.TriggerGroup;

public class TutorialMapBuilder implements MapBuilder {
    private Maps map;
    private char[][] charMatrix;

    public TutorialMapBuilder() {
        this.map = new Maps();
    }

    public void reset() {
        this.map = new Maps();
    }

    @Override
    public void setID(int ID){
        this.map.setID(ID);
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
    public void setDimension(int rows, int columns) { 
        this.map.setDimension(rows, columns);
        this.charMatrix = new char[this.map.getWidth()][this.map.getHeight()];
    }

    @Override
    public void addTriggerGroup(TriggerGroup triggerGroup, Coordinates offset, int width, int height, String spritePath) {
        this.map.addTriggerGroup(triggerGroup, offset, width, height, spritePath);
    }

    @Override
    public void startMap() {
        if (this.charMatrix == null) {
            return;
        }
        for (int i = 0; i < this.charMatrix.length; i++) {
            this.charMatrix[i][0] = '#';
            this.charMatrix[i][this.charMatrix[0].length - 1] = '#';
        }
        for (int i = 1; i < this.charMatrix[0].length - 1; i++) {
            this.charMatrix[0][i] = '#';
            this.charMatrix[charMatrix.length - 1][i] = '#';
        }
        for (int i = 1; i < this.charMatrix.length - 1; i++) {
            for (int j = 1; j < this.charMatrix[0].length - 1; j++) {
                this.charMatrix[i][j] = '.';
            }
        }
        buildWall(new Coordinates(19, 21), 1, 4, '#');
        buildWall(new Coordinates(22, 20), 9, 1, '#');
        buildWall(new Coordinates(1, 19), 12, 2, '#');
        buildWall(new Coordinates(1, 15), 10, 1, '#');
        buildWall(new Coordinates(13, 1), 2, 3, '#');
        buildWall(new Coordinates(14, 12), 3, 2, '#');
        buildWall(new Coordinates(24, 11), 7, 1, '#');
        for (Coordinates key : this.map.getDict().keySet()) {
            this.charMatrix[key.getX()][key.getY()] = this.map.getDict().get(key).getMapSymbol();
        }
        this.map.startMap(charMatrix);
    }

    public void buildWall(Coordinates offset, int width, int height, char mapSymbol) {
        if (width + offset.getX() > this.map.getWidth() || height + offset.getY() > this.map.getHeight() ||
            width + offset.getX() < 0 || height + offset.getY() < 0) {
            return;
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                this.charMatrix[i + offset.getX()][j + offset.getY()] = mapSymbol;
            }
        }
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
    public void setTurnQueue(Player first, Player second) {
        this.map.setTurnQueue(first, second);
    }

    @Override
    public Maps getResult() {
        Maps product = this.map;
        this.reset();
        return product;
    }

    @Override
    public void buildBorder() {
       // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setSpawn(Coordinates p1spawn, Coordinates p2spawn) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setSpawn'");
    }
    
}

package com.artattack.level;

import java.util.List;

import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.InteractableElement;
import com.artattack.mapelements.Player;
import com.artattack.mapelements.TriggerGroup;

public class MapBuilderBossOne implements MapBuilder{
    private Maps map;
    private char[][] charMatrix;

    public MapBuilderBossOne() {
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
    public void setDimension(int width, int height) {
        this.map.setDimension(width, height);
        this.charMatrix = new char[this.map.getWidth()][this.map.getHeight()];
    }

    @Override
    public void addTriggerGroup(TriggerGroup triggerGroup, Coordinates offset, int width, int height) {
        this.map.addTriggerGroup(triggerGroup, offset, width, height);
    }

    @Override
    public void startMap() {
        try {
            for (Coordinates key : this.map.getDict().keySet()) {
                this.charMatrix[key.getX()][key.getY()] = this.map.getDict().get(key).getMapSymbol();
            }
            this.map.startMap(this.charMatrix);
        } catch (NullPointerException e) {
            System.err.println("dictionary and/or charMatrix has not been initialized ");
        }
    }

    @Override
    public void buildBorder() {
    try {
        int rows = 32;
        int cols = 40;

        // Fill with floor
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                charMatrix[i][j] = '.';
            }
        }

        // Outer border only
        for (int i = 0; i < rows; i++) {
            charMatrix[i][0] = '#';
            charMatrix[i][cols - 1] = '#';
        }
        for (int j = 0; j < cols; j++) {
            charMatrix[0][j] = '#';
            charMatrix[rows - 1][j] = '#';
        }

        // Create door gaps
        // Exit door at top
        charMatrix[0][19] = '.';
        charMatrix[0][20] = '.';
        
        // Entry door at bottom
        charMatrix[31][19] = '.';
        charMatrix[31][20] = '.';

    } catch (NullPointerException e) {
        System.err.println("charMatrix has not been initialized");
    }
}

    @Override
    public void buildWall(Coordinates offset, int width, int height, char mapSymbol) {
        try {
            if (width + offset.getX() > this.map.getWidth() || height + offset.getY() > this.map.getHeight() ||
                width + offset.getX() < 0 || height + offset.getY() < 0) {
                throw new ArithmeticException("Map dimensions and parameters mismatch");
            }
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    this.charMatrix[i + offset.getX()][j + offset.getY()] = mapSymbol;
                }
            }
        } catch (NullPointerException e) {
            System.err.println("charMatrix has not been initialized");
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
    public void setID(int ID) {
        this.map.setID(ID);
    }

    @Override
    public void setTurnQueue(Player currPlayer, Player otherPlayer) {
        this.map.setTurnQueue(currPlayer, otherPlayer);
    }

    @Override
    public void reset() {
        this.map = new Maps();
    }
    
    @Override
    public Maps getResult() {
        Maps product = this.map;
        this.reset();
        return product;
    }

    @Override
    public void setSpawn(Coordinates p1spawn, Coordinates p2spawn) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setSpawn'");
    }
}

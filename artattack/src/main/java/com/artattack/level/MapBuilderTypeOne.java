package com.artattack.level;

import java.util.List;

import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.InteractableElement;
import com.artattack.mapelements.Player;
import com.artattack.mapelements.TriggerGroup;

public class MapBuilderTypeOne implements MapBuilder {
    private Maps map;
    private char[][] charMatrix;

    public MapBuilderTypeOne() {
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
    public void addTriggerGroup(TriggerGroup triggerGroup, Coordinates offset, int width, int height, String spritePath) {
        this.map.addTriggerGroup(triggerGroup, offset, width, height, spritePath);
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
            for (char[] charMatrix1 : this.charMatrix) {
                charMatrix1[0] = '#';
                charMatrix1[this.charMatrix[0].length - 1] = '#';
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
        this.map.setSpawn(p1spawn, p2spawn);
    }
}

package com.artattack.level;

import java.util.List;

import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.InteractableElement;
import com.artattack.mapelements.Player;
import com.artattack.mapelements.TriggerGroup;

public class MapBuilderTypeOne implements MapBuilder {
    private Maps map;

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
    }

    @Override
    public void addTriggerGroup(TriggerGroup triggerGroup, Coordinates offset, int width, int height, String spritePath) {
        this.map.addTriggerGroup(triggerGroup, offset, width, height, spritePath);
    }

    @Override
    public void startMap() {
        this.map.startMap();
    }

    @Override
    public void buildBorder() {
        try {
            for (char[] charMatrix1 : this.map.getMapMatrix()) {
                charMatrix1[0] = '\u2588';
                charMatrix1[this.map.getMapMatrix()[0].length - 1] = '\u2588';
            }
            for (int i = 1; i < this.map.getMapMatrix()[0].length - 1; i++) {
                this.map.getMapMatrix()[0][i] = '\u2588';
                this.map.getMapMatrix()[this.map.getMapMatrix().length - 1][i] = '\u2588';
            }
            for (int i = 1; i < this.map.getMapMatrix().length - 1; i++) {
                for (int j = 1; j < this.map.getMapMatrix()[0].length - 1; j++) {
                    this.map.getMapMatrix()[i][j] = '\u25EA';
                }
            }
        } catch (NullPointerException e) {
            System.err.println("charMatrix has not been initialized");
        }
    }

    @Override
    public void buildBorder(char wall, char floor) {
        try {
            for (char[] charMatrix1 : this.map.getMapMatrix()) {
                charMatrix1[0] = wall;
                charMatrix1[this.map.getMapMatrix()[0].length - 1] = wall;
            }
            for (int i = 1; i < this.map.getMapMatrix()[0].length - 1; i++) {
                this.map.getMapMatrix()[0][i] = wall;
                this.map.getMapMatrix()[this.map.getMapMatrix().length - 1][i] = wall;
            }
            for (int i = 1; i < this.map.getMapMatrix().length - 1; i++) {
                for (int j = 1; j < this.map.getMapMatrix()[0].length - 1; j++) {
                    this.map.getMapMatrix()[i][j] = floor;
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
                    this.map.getMapMatrix()[i + offset.getX()][j + offset.getY()] = mapSymbol;
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

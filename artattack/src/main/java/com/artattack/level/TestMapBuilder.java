package com.artattack.level;

import java.util.List;

import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.InteractableElement;
import com.artattack.mapelements.MapElement;
import com.artattack.mapelements.Player;
import com.artattack.mapelements.TriggerGroup;

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
    public void setTurnQueue(Player first, Player second) {
        this.map.setTurnQueue(first, second);
    }

    @Override
    public void setDimension(int width, int height){
        this.map.setDimension(width, height);
    }

    @Override
    public void addTriggerGroup(TriggerGroup triggerGroup, Coordinates offset, int width, int height) {
        this.map.addTriggerGroup(triggerGroup, offset, width, height);
    }

    @Override
    public void startMap() {
        int height = this.map.getHeight();
        int width = this.map.getWidth();

        char[][] charMatrix = new char[width][height];

        // Sets all the cells with the "." character
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                charMatrix[i][j] = '.';
            }
        }

        // Coverage of the border with the # character
        for (int j = 0; j < height; j++) {
            charMatrix[0][j] = '#';
            charMatrix[width - 1][j] = '#';
        }

        for (int i = 0; i < width; i++) {
            charMatrix[i][0] = '#';
            charMatrix[i][height - 1] = '#';
        }

        // putting all the obstacles
        for (int j = 5; j < 15 && j < height; j++) {
            if (10 < width) {
                charMatrix[10][j] = '#';
            }
        }

        // Putting all the other elements
        for (Coordinates key : this.map.getDict().keySet()) {
            MapElement element = this.map.getDict().get(key);
            int y = key.getY();
            int x = key.getX();

            if (y >= 0 && y < height && x >= 0 && x < width) {
                charMatrix[x][y] = element.getMapSymbol();
            } else {
                System.out.println("⚠️ Coordinates Out of Bounds!: " + key);
            }
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
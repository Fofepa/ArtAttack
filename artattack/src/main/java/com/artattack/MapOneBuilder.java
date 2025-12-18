package com.artattack;

import java.util.List;

public class MapOneBuilder implements MapBuilder {
    private Player playerOne;
    private Player playerTwo;
    private List<InteractableElement> interactableElements;
     private List<Enemy> enemies;
    
    /* @Override
    public void setDict(Map<Coordinates, ? extends MapElement> dictionaire) {
        // Implementation for setting the dictionary
    } */

    @Override
    public void setPlayerOne(Player player) {
        this.playerOne = player;
    }

    @Override
    public void setPlayerTwo(Player player) {
        this.playerTwo = player;
    }

    @Override
    public void setInteractableElements(List<InteractableElement> element) {
        this.interactableElements = element;
    }

    @Override
    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public Maps getResult() {
        return new Maps(this.playerOne, this.playerTwo, this.interactableElements, this.enemies);
    }
}
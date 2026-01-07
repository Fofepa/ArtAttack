package com.artattack;

import java.util.List;

public interface MapBuilder {
    /* public void setDict(Map<Coordinates, ? extends MapElement> dictionaire); */
    public void setPlayerOne(Player player);
    public void setPlayerTwo(Player player);
    public void setInteractableElements(List<InteractableElement> elements);
    public void setEnemies(List<Enemy> enemies);
    public void setDimension(int rows, int columns);
    public void startMap();     // only for the view part
    public void setDict();
    public void setTurnQueue(); 
    public Maps getResult();
}

package com.artattack.level;

import java.util.List;

import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.InteractableElement;
import com.artattack.mapelements.Player;

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

package com.artattack;

import java.util.List;

public interface MapBuilder {
    /* public void setDict(Map<Coordinates, ? extends MapElement> dictionaire); */
    public void setPlayerOne(Player player);
    public void setPlayerTwo(Player player);
    public void setInteractableElements(List<InteractableElement> element);
    public void setEnemies(List<Enemy> enemies);
}

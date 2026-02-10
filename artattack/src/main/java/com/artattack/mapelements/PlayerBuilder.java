package com.artattack.mapelements;

import java.util.List;

import com.artattack.items.Item;
import com.artattack.items.Key;
import com.artattack.level.Coordinates;
import com.artattack.mapelements.skilltree.SkillTree;
import com.artattack.moves.Weapon;

public interface PlayerBuilder {
    public void setID(int ID);
    public void setMapSymbol(char mapSymbol);
    public void setName(String name);
    public void setCoordinates(Coordinates coordinates);
    public void setSpritePath(String spritePath);
    public void setCurrHP(int currHP);
    public void setMaxHP(int maxHP);
    public void setWeapons(List<Weapon> weapons);
    public void setSpeed(int speed);
    public void setActionPoints(int actionPoints);
    public void setMaxActionPoints(int maxActionPoints);
    public void setMoveArea(List<Coordinates> moveArea);
    public void setType(PlayerType playerType);
    public void setCurrXP(int currXP);
    public void setMaxXP(int maxXP);
    public void setLevel(int level);
    public void setMaxWeapons(int maxWeapons);
    public void setInventory(List<Item> inventory);
    public void setKeys(List<Key> keys);
    public void setActionArea(List<Coordinates> actionArea);
    public void setSkillTree(SkillTree skillTree);
    public Player getResult();
}

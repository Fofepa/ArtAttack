package com.artattack.mapelements;

import java.util.List;

import com.artattack.items.Item;
import com.artattack.items.Key;
import com.artattack.level.Coordinates;
import com.artattack.moves.Weapon;

public interface EnemyBuilder {
    public abstract void setID(int ID);
    public abstract void setMapSymbol(char mapSymbol);
    public abstract void setName(String name);
    public abstract void setCoordinates(Coordinates coordinates);
    public abstract void setSpritePath(String spritePath);
    public abstract void setCurrHP(int currHP);
    public abstract void setMaxHP(int maxHP);
    public abstract void setWeapons(List<Weapon> weapons);
    public abstract void setSpeed(int speed);
    public abstract void setActionPoints(int actionPoints);
    public abstract void setMaxActionPoints(int maxActionPoints);
    public abstract void setMoveArea(List<Coordinates> moveArea);
    public abstract void setVisionArea(List<Coordinates> coordinates);
    public abstract void setDrops(List<Item> drops);
    public abstract void setKeys(List<Key> keys);
    public abstract void setDroppedXP(int droppedXP);
    public abstract void setEnemyType(EnemyType enemyType);
}

package com.artattack.mapelements;

import java.util.List;

import com.artattack.items.Item;
import com.artattack.items.Key;
import com.artattack.level.Coordinates;
import com.artattack.moves.Weapon;

public class ConcreteEnemyBuilder implements EnemyBuilder{
    private int ID;
    private char mapSymbol;
    private String name;
    private int currHP;
    private int maxHP;
    private List<Weapon> weapons;
    private int speed;
    private int actionPoints;
    private int maxActionPoints;
    private List<Coordinates> moveArea;
    private Coordinates coordinates;
    private String spritePath = null; 
    private List<Coordinates> visionArea;
    private List<Item> drops;
    private List<Key> keys;
    private int droppedXP;
    private EnemyType enemyType;

    @Override
    public void setID(int ID){
        this.ID = ID;
    }

    @Override
    public  void setMapSymbol(char mapSymbol){
        this.mapSymbol = mapSymbol;
    }
    @Override
    public  void setName(String name){
        this.name = name;
    }
    @Override
    public  void setCoordinates(Coordinates coordinates){
        this.coordinates = coordinates;
    }
    @Override
    public  void setSpritePath(String spritePath){
        this.spritePath = spritePath;
    }
    @Override
    public  void setCurrHP(int currHP){
        this.currHP = currHP;
    }
    @Override
    public  void setMaxHP(int maxHP){
        this.maxHP = maxHP;
    }
    @Override
    public  void setWeapons(List<Weapon> weapons){
        this.weapons = weapons;
    }
    @Override
    public  void setSpeed(int speed){
        this.speed = speed;
    }
    @Override
    public  void setActionPoints(int actionPoints){
        this.actionPoints = actionPoints;
    }
    @Override
    public  void setMaxActionPoints(int maxActionPoints){
        this.maxActionPoints = maxActionPoints;
    }
    @Override
    public  void setMoveArea(List<Coordinates> moveArea){
        this.moveArea = moveArea;
    }
    @Override
    public  void setVisionArea(List<Coordinates> visionArea){
        this.visionArea = visionArea;
    }
    @Override
    public  void setDrops(List<Item> drops){
        this.drops = drops;
    }
    @Override
    public  void setKeys(List<Key> keys){
        this.keys = keys;
    }
    @Override
    public  void setDroppedXP(int droppedXP){
        this.droppedXP = droppedXP;
    }
    @Override
    public  void setEnemyType(EnemyType enemyType){
        this.enemyType = enemyType;
    }

    public Enemy getResult(){
        return new Enemy(ID, mapSymbol, name, coordinates, enemyType, 
            currHP, maxHP, speed, weapons, actionPoints, maxActionPoints,
            moveArea, visionArea, drops, keys, droppedXP, spritePath);
    }
    
}

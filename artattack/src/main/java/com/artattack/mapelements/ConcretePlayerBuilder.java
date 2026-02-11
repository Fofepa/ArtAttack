package com.artattack.mapelements;

import java.util.List;

import com.artattack.items.Item;
import com.artattack.items.Key;
import com.artattack.level.Coordinates;
import com.artattack.mapelements.skilltree.SkillTree;
import com.artattack.moves.Weapon;

public class ConcretePlayerBuilder implements PlayerBuilder {
    private int ID;
    private char mapSymbol;
    private String name;
    private Coordinates coordinates;
    private String spritePath;
    private int currHP;
    private int maxHP;
    private List<Weapon> weapons;
    private int speed;
    private int actionPoints;
    private int maxActionPoints;
    private List<Coordinates> moveArea;
    private PlayerType type;
    private int currXP;
    private int maxXP;
    private int level;
    private boolean leveledUp;
    private int maxWeapons;
    private List<Item> inventory;
    private List<Key> keys;
    private List<Coordinates> actionArea;
    private SkillTree skillTree;

    @Override
    public void setID(int ID){
        this.ID = ID;
    }

    @Override
    public void setMapSymbol(char mapSymbol){
        this.mapSymbol = mapSymbol;
    }

    @Override
    public void setName(String name){
        this.name = name;
    }

    @Override
    public void setCoordinates(Coordinates coordinates){
        this.coordinates = coordinates;
    }

    @Override
    public void setSpritePath(String spritePath){
        this.spritePath = spritePath;
    }

    @Override
    public void setCurrHP(int currHP){
        this.currHP = currHP;
    }

    @Override
    public void setMaxHP(int maxHP){
        this.maxHP = maxHP;
    }

    @Override
    public void setWeapons(List<Weapon> weapons){
        this.weapons = weapons;
    }

    @Override
    public void setSpeed(int speed){
        this.speed = speed;
    }

    @Override
    public void setActionPoints(int actionPoints){
        this.actionPoints = actionPoints;
    }

    @Override
    public void setMaxActionPoints(int maxActionPoints){
        this.maxActionPoints = maxActionPoints;
    }

    @Override
    public void setMoveArea(List<Coordinates> moveArea){
        this.moveArea = moveArea;
    }

    @Override
    public void setType(PlayerType playerType){
        this.type = playerType;
    }

    @Override
    public void setCurrXP(int currXP){
        this.currHP = currXP;
    }

    @Override
    public void setMaxXP(int maxXP){
        this.maxXP = maxXP;
    }

    @Override
    public void setLevel(int level){
        this.level = level;
    }

    @Override
    public void setMaxWeapons(int maxWeapons){
        this.maxWeapons = maxWeapons;
    }

    @Override
    public void setInventory(List<Item> inventory){
        this.inventory = inventory;
    }

    @Override
    public void setKeys(List<Key> keys){
        this.keys = keys;
    }

    @Override
    public void setActionArea(List<Coordinates> actionArea){
        this.actionArea = actionArea;
    }

    @Override
    public void setSkillTree(SkillTree skillTree){
        this.skillTree = skillTree;
    }

    @Override
    public Player getResult(){
        return new Player(ID, mapSymbol, name, coordinates, weapons, actionPoints, maxActionPoints, moveArea, currHP, maxHP, currXP, maxXP, level,speed, maxWeapons,
        inventory, keys, type, skillTree, spritePath);
    }
    
    
}

package com.artattack;

import java.util.*;

public abstract class Player extends MapElement {
    
    //Attributes
    private int currHP;
    private int maxHP;
    private int currXP;
    private int maxXP;
    private int level;
    private int maxWeapons;
    private List<Weapon> weapons;
    private int speed;
    private List<? extends Item> inventory;
    private int actionPoints;
    private List<Coordinates> actionArea;
    private List<Coordinates> moveArea;

    public Player(int ID, char mapSymbol, String name, Coordinates coordinates,int currHP,int maxHP,int currXP, int maxXP, 
        int level, int maxWeapons, List<Weapon> weapons, int speed, int actionPoints, List<Coordinates> actionArea,
         List<Coordinates> moveArea, List<? extends Item> inventory){
            super(ID,mapSymbol,name,coordinates);
            this.maxHP = maxHP;
            this.currHP = currHP;
            this.maxXP = maxXP;
            this.currXP = currXP;
            this.level = level;
            this.maxWeapons = maxWeapons;
            this.weapons = weapons;
            this.speed = speed;
            this.actionPoints = actionPoints;
            this.actionArea = actionArea;
            this.moveArea = moveArea;
            this.inventory = inventory;
        }

    //primitive methods
    public void move(){

    }

    public void attack(){

    }

    public void openInventory(){

    }
    // guys do you remember? you can use the wildcard only in list and such
    public abstract  <T extends Item> T getItem(int k);

    public abstract  void interact(/*InteractableElement dude*/);

    public abstract  void skipTurn();

    public abstract  void setCoordinates(Coordinates coordinatesSet);

    //getters
    public int getCurrHP() {
        return this.currHP;
    }

    public int getMaxHP() {
        return this.maxHP;
    }

    public int getCurrXP() {
        return this.currXP;
    }

    public int getMaxXP() {
        return this.maxXP;
    }

    public int getLevel() {
        return this.level;
    }

    public int getMaxWeapons() {
        return this.maxWeapons;
    }

    public List<Weapon> getWeapons(){
        return this.weapons;
    }

    public int getSpeed() {
        return this.speed;
    }

    public List<? extends Item> getInventory() {
        return this.inventory;
    }

    public int getActionPoints() {
        return this.actionPoints;
    }

    public List<Coordinates> getActionArea() {
        return this.actionArea;
    }

    public List<Coordinates> getMoveArea() {
        return this.moveArea;
    }


}

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

    public Player(int ID, char mapSymbol, String name, Coordinates coordinates){
            super(ID,mapSymbol,name,coordinates);
        }

    public Player(int ID, char mapSymbol, String name, Coordinates coordinates, 
        int currHP, int maxHP, int currXP, int maxXP, int level, int speed){
        this(ID,mapSymbol,name,coordinates);
        this.currHP = currHP;
        this.maxHP = maxHP;
        this.currXP = currXP;
        this.maxXP = maxXP;
        this.level = level;
        this.speed = speed;
    }

    public Player(int ID, char mapSymbol, String name, Coordinates coordinates, 
        int currHP, int maxHP, int currXP, int maxXP, int level, int speed,
        int maxWeapons, List<Weapon> weapons , int actionPoints, List<? extends Item> inventory,
          List<Coordinates> actionArea, List<Coordinates> moveArea){
            this(ID,mapSymbol,name,coordinates,currHP, maxHP, currXP, maxXP,level, speed);
            this.maxWeapons = maxWeapons;
            this.weapons = weapons;
            this.actionPoints = actionPoints;
            this.inventory = inventory;
            this.actionArea = actionArea;
            this.moveArea  = moveArea;

        }

    //primitive methods
    //public void move(){;

   // public void attack();

    //public void openInventory();
    // guys do you remember? you can use the wildcard only in list and such
    //public abstract  <T extends Item> T getItem(int k);

    //public abstract  void interact(/*InteractableElement dude*/);

    //public abstract  void skipTurn();

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

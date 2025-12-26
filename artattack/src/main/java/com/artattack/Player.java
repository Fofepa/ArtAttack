package com.artattack;

import java.util.List;

public abstract class Player extends ActiveElement {
    
    //Attributes
    private int currXP;
    private int maxXP;
    private int level;
    private int maxWeapons;
    private List<Item> inventory;
    private List<Key> keys;
    private List<Coordinates> actionArea;

    public Player(int ID, char mapSymbol, String name, Coordinates coordinates){
        super(ID,mapSymbol,name,coordinates);
    } 

    public Player(int ID, char mapSymbol, String name, Coordinates coordinates,
        List<Weapon> weapons, int actionPoints, List<Coordinates> moveArea,
        int currHP, int maxHP, int currXP, int maxXP, int level,int speed){
        super(ID,mapSymbol,name,coordinates,currHP,maxHP,speed,weapons,actionPoints, moveArea);
        this.currXP = currXP;
        this.maxXP = maxXP;
        this.level = level;
    }

     public Player(int ID, char mapSymbol, String name, Coordinates coordinates,
        List<Weapon> weapons, int actionPoints, List<Coordinates> moveArea,
        int currHP, int maxHP, int currXP, int maxXP, int level,int speed, int maxWeapons, List<Item> inventory, List<Key> keys, List<Coordinates> actionArea){
            this(ID,mapSymbol,name,coordinates,weapons, actionPoints, moveArea, currHP, maxHP, currXP, maxXP, level,speed);
            this.maxWeapons = maxWeapons;
            this.inventory = inventory;
            this.keys = keys;
            this.actionArea = actionArea;
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

    public List<Item> getInventory() {
        return this.inventory;
    }

    public List<Key> getKeys(){
        return this.keys;
    }

    public List<Coordinates> getActionArea() {
        return this.actionArea;
    }

    public void addItem(Item item){
        this.inventory.add(item);
    }

    public void addKey(Key key){
        this.keys.add(key);
    }

    public void setMaxXP(int maxXP){
        this.maxXP = maxXP;
    }

    public void setLevel(){
        this.level += 1;
    }

    public void setMaxWeapons(){
        this.maxWeapons += 1;
    }

    public void updateCurrXP(int amount){
        this.currXP += amount;
    }
}

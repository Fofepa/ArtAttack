package com.artattack;

import java.util.List;

public abstract class Characters extends MapElement {
    
    //Attributes
    private String name;
    private int currHP;
    private int maxHP;
    private int currXP;
    private int maxXP;
    private int level;
    private int maxWeapons;
    private int speed;
    private List<? extends Item> inventory;
    private int actionPoints;
    private List<Coordinates> actionArea;
    private List<Coordinates> moveArea;

    public Characters(String name,int currHP,int maxHP,int currXP, int maxXP, 
        int level, int maxWeapons, int speed, int actionPoints, List<Coordinates> actionArea,
         List<Coordinates> moveArea, List<? extends Item> inventory){
            this.name = name;
            this.maxHP = maxHP;
            this.currHP = currHP;
            this.maxXP = maxXP;
            this.currXP = currXP;
            this.level = level;
            this.maxWeapons = maxWeapons;
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
        return currHP;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getCurrXP() {
        return currXP;
    }

    public int getMaxXP() {
        return maxXP;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxWeapons() {
        return maxWeapons;
    }

    public int getSpeed() {
        return speed;
    }

    public List<? extends Item> getInventory() {
        return inventory;
    }

    public int getActionPoints() {
        return actionPoints;
    }

    public List<Coordinates> getActionArea() {
        return actionArea;
    }

    public List<Coordinates> getMoveArea() {
        return moveArea;
    }


}

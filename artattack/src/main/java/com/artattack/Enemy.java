package com.artattack;

import java.util.List;

public class Enemy extends ActiveElement {
    //Attributes
    private List<Coordinates> visionArea;
    private List<Item> drops;
    private int droppedXP;
    private int playerOneDemage;
    private int playerTwoDemage;
    //private boolean isActive = false;     unused for now

    public Enemy(int ID, char mapSymbol, String name, Coordinates coordinates){
        super(ID,mapSymbol,name,coordinates);
    }

    public Enemy(int ID, char mapSymbol, String name, Coordinates coordinates, 
        int currHP, int maxHP, int speed, List<Weapon> weapons, int actionPoints, List<Coordinates> moveArea){
        super(ID,mapSymbol,name,coordinates,currHP,maxHP,speed,weapons,actionPoints, moveArea);
    }

    public Enemy(int ID, char mapSymbol, String name, Coordinates coordinates, 
        int currHP, int maxHP, int speed, List<Weapon> weapons, int actionPoints, List<Coordinates> moveArea,
        List<Coordinates> visionArea, List<Item> drops, int droppedXP){
            this(ID,mapSymbol,name,coordinates,currHP,maxHP,speed,weapons,actionPoints, moveArea);
            this.visionArea = visionArea;
            this.drops = drops;
            this.droppedXP = droppedXP;
            this.playerOneDemage = 0;
            this.playerTwoDemage = 0;
    }

    public List<Coordinates> getVisionArea() {
        return this.visionArea;
    }

    public List<Item> getDrops() {
        return this.drops;
    }

    public int getDroppedXP() {
        return this.droppedXP;
    }

    public int getPlayerOneDemage(){
        return this.playerOneDemage;
    }

    public int getPlayerTwoDemage(){
        return this.playerTwoDemage;
    }

    public void updatePlayerOneDemage(int demage){
        this.playerOneDemage += demage;
    }

    public void updatePlayerTwoDemage(int demage){
        this.playerTwoDemage += demage;
    }

/*     public boolean getIsActive(){
        return this.isActive;
    }

    public void activate(){
        this.isActive = true;           unused for now ---> for the aggro system
    }

    public void deactivate(){
        this.isActive = false;
    } */

    private int calculateDroppedXP(int demage){
        float percentDemage = ((float) demage) / this.getMaxHP();
        return Math.round(percentDemage * this.droppedXP);
    }

    public void dropXP(Player playerOne, Player playerTwo){
        playerOne.updateCurrXP(this.calculateDroppedXP(this.playerOneDemage));
        playerTwo.updateCurrXP(this.calculateDroppedXP(this.playerTwoDemage));
    }
}

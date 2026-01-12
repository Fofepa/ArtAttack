package com.artattack.mapelements;

import java.util.List;

import com.artattack.items.Item;
import com.artattack.items.Key;
import com.artattack.level.Coordinates;
import com.artattack.level.Maps;
import com.artattack.moves.Weapon;

public class Enemy extends ActiveElement {
    //Attributes
    private List<Coordinates> visionArea;
    private List<Item> drops;
    private List<Key> keys;
    private int droppedXP;
    private int playerOneDemage;
    private int playerTwoDemage;
    private EnemyType enemyType;
    //private boolean isActive = false;     unused for now

    public Enemy(int ID, char mapSymbol, String name, Coordinates coordinates, EnemyType enemyType){
        super(ID,mapSymbol,name,coordinates);
        this.enemyType = enemyType;
    }

    public Enemy(int ID, char mapSymbol, String name, Coordinates coordinates, EnemyType enemyType,
        int currHP, int maxHP, int speed, List<Weapon> weapons, int actionPoints, int maxActionPoints,List<Coordinates> moveArea){
        super(ID,mapSymbol,name,coordinates,currHP,maxHP,speed,weapons,actionPoints,maxActionPoints,moveArea);
        this.enemyType = enemyType;
    }

    public Enemy(int ID, char mapSymbol, String name, Coordinates coordinates, EnemyType enemyType,
        int currHP, int maxHP, int speed, List<Weapon> weapons, int actionPoints,int maxActionPoints, List<Coordinates> moveArea,
        List<Coordinates> visionArea, List<Item> drops, List<Key> keys, int droppedXP){
            this(ID,mapSymbol,name,coordinates, enemyType, currHP,maxHP,speed,weapons,actionPoints,maxActionPoints,moveArea);
            this.visionArea = visionArea;
            this.drops = drops;
            this.keys = keys;
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

    public List<Key> getKeys(){
        return this.keys;
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

    public EnemyType getEnemyType(){
        return this.enemyType;
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

    public void remove(Maps map){
        map.getMapMatrix()[this.getCoordinates().getX()][this.getCoordinates().getY()] = '.';
        map.getDict().remove(this.getCoordinates());
    }

}

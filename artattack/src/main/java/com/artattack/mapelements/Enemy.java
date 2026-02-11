package com.artattack.mapelements;

import java.util.List;

import com.artattack.items.Item;
import com.artattack.items.Key;
import com.artattack.level.Coordinates;
import com.artattack.level.Maps;
import com.artattack.moves.Weapon;

public class Enemy extends ActiveElement {

    private List<Coordinates> visionArea;
    private List<Item> drops;
    private List<Key> keys;
    private int droppedXP;
    private int PlayerOneDamage;
    private int PlayerTwoDamage;
    private EnemyType enemyType;
    private boolean isActive = false;    
    private boolean isStunned = false;

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
            this.PlayerOneDamage = 0;
            this.PlayerTwoDamage = 0;
    }

    public Enemy(int ID, char mapSymbol, String name, Coordinates coordinates, EnemyType enemyType, String spritePath){
        super(ID,mapSymbol,name,coordinates, spritePath);
        this.enemyType = enemyType;
    }

    public Enemy(int ID, char mapSymbol, String name, Coordinates coordinates, EnemyType enemyType,
        int currHP, int maxHP, int speed, List<Weapon> weapons, int actionPoints, int maxActionPoints,List<Coordinates> moveArea, String spritePath){
        super(ID,mapSymbol,name,coordinates,currHP,maxHP,
            speed,weapons,actionPoints,maxActionPoints,moveArea, spritePath);
        this.enemyType = enemyType;
    }

    public Enemy(int ID, char mapSymbol, String name, Coordinates coordinates, EnemyType enemyType,
        int currHP, int maxHP, int speed, List<Weapon> weapons, int actionPoints,int maxActionPoints, List<Coordinates> moveArea,
        List<Coordinates> visionArea, List<Item> drops, List<Key> keys, int droppedXP, String spritePath){
            this(ID,mapSymbol,name,coordinates, enemyType, currHP,maxHP,
                speed,weapons,actionPoints,maxActionPoints,moveArea, spritePath);
            this.visionArea = visionArea;
            this.drops = drops;
            this.keys = keys;
            this.droppedXP = droppedXP;
            this.PlayerOneDamage = 0;
            this.PlayerTwoDamage = 0;
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

    public int getPlayerOneDamage(){
        return this.PlayerOneDamage;
    }

    public int getPlayerTwoDamage(){
        return this.PlayerTwoDamage;
    }

    public EnemyType getEnemyType(){
        return this.enemyType;
    }

    public boolean isStunned(){
        return this.isStunned;
    }

    public void updatePlayerOneDamage(int damage){
        if (damage > this.getCurrHP()) {
            this.PlayerOneDamage += this.getCurrHP();
        }
        else {
            this.PlayerOneDamage += damage;
        }
        System.out.println(damage);
    }

    public void updatePlayerTwoDamage(int damage){
        if(damage > this.getCurrHP()){
            this.PlayerTwoDamage += this.getCurrHP();
        }
        else{
        this.PlayerTwoDamage += damage;   
        }
    }

     public boolean getIsActive(){
        return this.isActive;
    }

    public void activate(){
        this.isActive = true;           //unused for now ---> for the aggro system
    }

    public void deactivate(){
        this.isActive = false;
    }

    public void setIsStunned(boolean cond){
        this.isStunned = cond;
    }

    public void setEnemyType(EnemyType enemyType){
        this.enemyType = enemyType;
    }

    private int calculateDroppedXP(int damage){
        float percentdamage = ((float) damage) / this.getMaxHP();
        return Math.round(percentdamage * this.droppedXP);
    }

    public void dropXP(Player PlayerOne, Player PlayerTwo){
        PlayerOne.updateCurrXP(this.calculateDroppedXP(this.PlayerOneDamage));
        PlayerTwo.updateCurrXP(this.calculateDroppedXP(this.PlayerTwoDamage));
    }

    public void remove(Maps map){
        map.getMapMatrix()[this.getCoordinates().getX()][this.getCoordinates().getY()] = '.';
        map.getDict().remove(this.getCoordinates());
    }

    @Override
    public void onDeath(Maps map, ActiveElement killer) {
        dropXP(map.getPlayerOne(), map.getPlayerTwo());
        drop((Player)killer);
        remove(map);
        map.getConcreteTurnHandler().getConcreteTurnQueue().remove(this);
        map.getEnemies().remove(map.getEnemies().indexOf(this));
    }

    public void drop(Player player){
        if(this.drops != null && !this.drops.isEmpty())
            player.addItems(this.drops);
        if(this.keys != null && !this.keys.isEmpty())
            player.addKeys(this.keys);
    }

}

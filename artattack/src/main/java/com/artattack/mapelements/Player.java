package com.artattack.mapelements;

import java.util.List;

import com.artattack.items.Item;
import com.artattack.items.Key;
import com.artattack.level.Coordinates;
import com.artattack.level.Maps;
import com.artattack.moves.Weapon;

public class Player extends ActiveElement {
    
    //Attributes
    private PlayerType type;
    private int currXP;
    private int maxXP;
    private int level;
    private boolean leveledUp;
    private int maxWeapons;
    private List<Item> inventory;
    private List<Key> keys;
    private List<Coordinates> actionArea;

    public Player(int ID, char mapSymbol, String name, Coordinates coordinates){
        super(ID,mapSymbol,name,coordinates);
    } 

    public Player(int ID, char mapSymbol, String name, Coordinates coordinates,
        List<Weapon> weapons, int actionPoints,int maxActionPoints, List<Coordinates> moveArea,
        int currHP, int maxHP, int currXP, int maxXP, int level,int speed, PlayerType type){
        super(ID,mapSymbol,name,coordinates,currHP,maxHP,speed,weapons,actionPoints,maxActionPoints,moveArea);
        this.type = type;
        this.currXP = currXP;
        this.maxXP = maxXP;
        this.level = level;
        this.leveledUp = false;
    }

     public Player(int ID, char mapSymbol, String name, Coordinates coordinates,
        List<Weapon> weapons, int actionPoints,int maxActionPoints, List<Coordinates> moveArea,
        int currHP, int maxHP, int currXP, int maxXP, int level,int speed, int maxWeapons, List<Item> inventory, List<Key> keys, List<Coordinates> actionArea, PlayerType type){
            this(ID,mapSymbol,name,coordinates,weapons, actionPoints, maxActionPoints, moveArea, currHP, maxHP, currXP, maxXP, level,speed, type);
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

    public boolean getLeveledUp(){
        return this.leveledUp;
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

    public PlayerType getType(){
        return this.type;
    }

    public void addItems(List<Item> items){
        for(Item item : items)
            this.inventory.add(item);
    }

    public void addKeys(List<Key> k){
        for(Key key : k)
            this.keys.add(key);
    }

    public void setMaxXP(int maxXP){
        this.maxXP = maxXP;
    }

    public void setLevel(){
        this.level += 1;
        this.leveledUp = true;
    }

    public void setLeveledUp(boolean leveledUp) {
        this.leveledUp = leveledUp;
    }

    public void setMaxWeapons(){
        this.maxWeapons += 1;
    }

    public void updateCurrXP(int amount){
        if(this.currXP + amount < this.maxXP)
            this.currXP += amount;
        else{
            amount = amount - (this.maxXP - this.currXP);
            this.setLevel();
            setMaxXP(this.maxXP + 5);
            this.currXP = amount;
            
        }
    }

    @Override
    public void onDeath(Maps map, ActiveElement killer) {
        map.getConcreteTurnHandler().getConcreteTurnQueue().remove(this);

    }

    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null) return false;
        if(this.getClass() != obj.getClass()) return false;
        Player other = (Player) obj;
        if(this.getID() != other.getID()) return false;
        if(this.getMapSymbol() != other.getMapSymbol()) return false;
        if(!this.getName().equals(other.getName())) return false;
        if(!this.getCoordinates().equals(other.getCoordinates())) return false;
        //Aggiungi altri check
        return true;
    }
}
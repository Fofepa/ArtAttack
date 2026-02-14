package com.artattack.mapelements;

import java.util.List;

import com.artattack.items.Item;
import com.artattack.items.Key;
import com.artattack.level.Coordinates;
import com.artattack.level.Maps;
import com.artattack.mapelements.skilltree.SkillTree;
import com.artattack.moves.Weapon;

public class Player extends ActiveElement {
    
    //Attributes
    private PlayerType type;
    private int currXP;
    private int maxXP;
    private int level;
    private int leveledUp;
    private int maxWeapons;
    private List<Item> inventory;
    private List<Key> keys;
    private SkillTree skillTree;
    private char originalSymbol;
    private int skillPoints;

    public Player(int ID, char mapSymbol, String name, Coordinates coordinates){
        super(ID,mapSymbol,name,coordinates);
        this.skillPoints = 0;
    } 

    public Player(int ID, char mapSymbol, String name, Coordinates coordinates,
        List<Weapon> weapons, int actionPoints,int maxActionPoints, List<Coordinates> moveArea,
        int currHP, int maxHP, int currXP, int maxXP, int level,int speed, PlayerType type){
        
        super(ID,mapSymbol,name,coordinates,currHP,maxHP,speed,weapons,actionPoints,
            maxActionPoints,moveArea);
        this.type = type;
        this.currXP = currXP;
        this.maxXP = maxXP;
        this.level = level;
        this.leveledUp = 0;
    }

    public Player(int ID, char mapSymbol, String name, Coordinates coordinates,
        List<Weapon> weapons, int actionPoints,int maxActionPoints, List<Coordinates> moveArea,
        int currHP, int maxHP, int currXP, int maxXP, int level,int speed, PlayerType type, 
        String spritePath){
        
        super(ID,mapSymbol,name,coordinates,currHP,maxHP,speed,weapons,actionPoints,
            maxActionPoints,moveArea, spritePath);
        this.type = type;
        this.currXP = currXP;
        this.maxXP = maxXP;
        this.level = level;
        this.leveledUp = 0;
        this.originalSymbol = mapSymbol;
    }

    public Player(int ID, char mapSymbol, String name, Coordinates coordinates,
        List<Weapon> weapons, int actionPoints,int maxActionPoints, List<Coordinates> moveArea,
        int currHP, int maxHP, int currXP, int maxXP, int level,int speed, int maxWeapons, 
        List<Item> inventory, List<Key> keys, PlayerType type){
            
            this(ID,mapSymbol,name,coordinates,weapons, actionPoints, maxActionPoints, 
                moveArea, currHP, maxHP, currXP, maxXP, level,speed, type);
            this.maxWeapons = maxWeapons;
            this.inventory = inventory;
            this.keys = keys;
            this.skillTree = null;
    }

    public Player(int ID, char mapSymbol, String name, Coordinates coordinates,
        List<Weapon> weapons, int actionPoints,int maxActionPoints, List<Coordinates> moveArea,
        int currHP, int maxHP, int currXP, int maxXP, int level,int speed, int maxWeapons, 
        List<Item> inventory, List<Key> keys, PlayerType type, 
        SkillTree skillTree, String spritePath){
            
            this(ID,mapSymbol,name,coordinates,weapons, actionPoints, maxActionPoints, 
                moveArea, currHP, maxHP, currXP, maxXP, level,speed, type, spritePath);
            this.maxWeapons = maxWeapons;
            this.inventory = inventory;
            this.keys = keys;
            this.skillTree = skillTree;
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

    public int getLeveledUp(){
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
        this.leveledUp += 1;
        this.skillPoints += 1; // Grant 1 skill point per level
    }

    public void setLeveledUp() {
        this.leveledUp -= 1;
    }

    public void resetLeveledUp(){
        this.leveledUp = 0;
    }

    public void setMaxWeapons(){
        this.maxWeapons += 1;
    }

    public void updateCurrXP(int amount){
        if(this.currXP + amount >= this.maxXP){
            int amountFix = amount + this.currXP;
            while (amountFix >= this.maxXP){
                this.setLevel();
                amountFix -= this.maxXP;
                setMaxXP(this.maxXP + (int)((2.5)*this.level));
            }
            this.currXP = amountFix;
        }
        else{
            this.currXP += amount;
        }
    }

    @Override
    public void onDeath(Maps map, ActiveElement killer) {
        this.setMapSymbol('\u2620');
        map.getConcreteTurnHandler().getConcreteTurnQueue().remove(this);
    }

    @Override
    public void updateHP(int amount) {
        super.updateHP(amount); 
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

    public SkillTree getSkillTree() {
        return skillTree;
    }
    
    // Skill points management
    public int getSkillPoints() {
        return this.skillPoints;
    }
    
    public boolean spendSkillPoint() {
        if (this.skillPoints > 0) {
            this.skillPoints--;
            return true;
        }
        return false;
    }

    public void revive(Maps map){
        map.getConcreteTurnHandler().getConcreteTurnQueue().add(this);
        this.setMapSymbol(this.originalSymbol);
    }
}
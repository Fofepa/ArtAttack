package com.artattack;

import java.util.List;

public class ActiveElement extends MapElement implements Comparable<ActiveElement> {

    private int currHP;
    private int maxHP;
    private List<Weapon> weapons;
    private int speed;
    private int actionPoints;
    private int maxActionPoints;
    private List<Coordinates> moveArea;

    public ActiveElement(int ID, char mapSymbol, String name, Coordinates coordinates){
            super(ID,mapSymbol,name,coordinates);
        }

    public ActiveElement(int ID, char mapSymbol, String name, Coordinates coordinates, 
        int currHP, int maxHP, int speed, List<Weapon> weapons, int actionPoints, int maxActionPoints ,List<Coordinates> moveArea){
        this(ID,mapSymbol,name,coordinates);
        this.currHP = currHP;
        this.maxHP = maxHP;
        this.speed = speed;
        this.weapons = weapons;
        this.actionPoints = actionPoints;
        this.maxActionPoints = maxActionPoints;
        this.moveArea = moveArea;
    }
    
    public int getCurrHP() {
        return this.currHP;
    }

    public int getMaxHP() {
        return this.maxHP;
    }

    public List<Weapon> getWeapons(){
        return this.weapons;
    }

    public int getSpeed() {
        return this.speed;
    }

    public int getActionPoints() {
        return this.actionPoints;
    }

    public int getMaxActionPoints(){
        return this.maxActionPoints;
    }

    public List<Coordinates> getMoveArea() {
        return this.moveArea;
    }

    public void setSpeed(int speed){
        this.speed = speed;
    }
    
    public void setActionPoints(int actionPoints){
        this.actionPoints = actionPoints;
    }
    
    public void updateHP(int amount){
        if(this.currHP + amount > this.maxHP)
            this.currHP = this.maxHP;
        else
            this.currHP += amount;
    }

    @Override
    public int compareTo(ActiveElement other) {
        return Integer.compare(other.getSpeed(), this.getSpeed());
    }
    
    public boolean isAlive(){
        return this.currHP > 0; 
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ActiveElement other = (ActiveElement) obj;
        return this.getID() == other.getID();
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(getID());
    }
}

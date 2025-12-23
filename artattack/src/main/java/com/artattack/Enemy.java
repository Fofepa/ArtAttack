package com.artattack;

import java.util.List;

public class Enemy extends ActiveElement {
    //Attributes
    private List<Coordinates> visionArea;
    private List<Item> drops;
    private int droppedXP;

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

}

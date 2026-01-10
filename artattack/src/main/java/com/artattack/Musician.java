package com.artattack;

import java.util.List;

public class Musician extends Player{

  public Musician(int ID, char mapSymbol, String name, Coordinates coordinates){
            super(ID,mapSymbol,name,coordinates);
        }

    public Musician(int ID, char mapSymbol, String name, Coordinates coordinates,
        List<Weapon> weapons, int actionPoints,int maxActionPoints, List<Coordinates> moveArea,
        int currHP, int maxHP, int currXP, int maxXP, int level,int speed){
        super(ID,mapSymbol,name,coordinates,weapons, actionPoints,maxActionPoints, moveArea, currHP, maxHP, currXP, maxXP, level,speed);
        }

    public Musician(int ID, char mapSymbol, String name, Coordinates coordinates,
        List<Weapon> weapons, int actionPoints,int maxActionPoints ,List<Coordinates> moveArea,
        int currHP, int maxHP, int currXP, int maxXP, int level,int speed, int maxWeapons, List<Item> inventory, List<Key> keys, List<Coordinates> actionArea){
            super(ID,mapSymbol,name,coordinates,weapons, actionPoints,maxActionPoints, moveArea, currHP, maxHP, currXP, maxXP, level,speed,maxWeapons,inventory, keys, actionArea);
        }
    
}

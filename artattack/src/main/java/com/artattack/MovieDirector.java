package com.artattack;

import java.util.List;

public class MovieDirector extends Player {

   public MovieDirector(int ID, char mapSymbol, String name, Coordinates coordinates){
            super(ID,mapSymbol,name,coordinates);
        }

    public MovieDirector(int ID, char mapSymbol, String name, Coordinates coordinates,
        List<Weapon> weapons, int actionPoints, List<Coordinates> moveArea,
        int currHP, int maxHP, int currXP, int maxXP, int level,int speed){
        super(ID,mapSymbol,name,coordinates,weapons, actionPoints, moveArea, currHP, maxHP, currXP, maxXP, level,speed);
        }

    public MovieDirector(int ID, char mapSymbol, String name, Coordinates coordinates,
        List<Weapon> weapons, int actionPoints, List<Coordinates> moveArea,
        int currHP, int maxHP, int currXP, int maxXP, int level,int speed, int maxWeapons, List<Item> inventory, List<Key> keys, List<Coordinates> actionArea){
            super(ID,mapSymbol,name,coordinates,weapons, actionPoints, moveArea, currHP, maxHP, currXP, maxXP, level,speed,maxWeapons,inventory, keys, actionArea);
        }

    /*@Override
    public <T extends Item> T getItem(int k){
        return null;
    }*/


}

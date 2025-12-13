package com.artattack;

import java.util.List;

public class MovieDirector extends Player {

   public MovieDirector(int ID, char mapSymbol, String name, Coordinates coordinates){
            super(ID,mapSymbol,name,coordinates);
        }

    public MovieDirector(int ID, char mapSymbol, String name, Coordinates coordinates, 
        int currHP, int maxHP, int currXP, int maxXP, int level, int speed){
        super(ID,mapSymbol,name,coordinates,currHP, maxHP, currXP, maxXP,level, speed);
    }

    public MovieDirector(int ID, char mapSymbol, String name, Coordinates coordinates, 
        int currHP, int maxHP, int currXP, int maxXP, int level, int speed,
        int maxWeapons, List<Weapon> weapons , int actionPoints, List<Item> inventory,
          List<Coordinates> actionArea, List<Coordinates> moveArea){
            super(ID,mapSymbol,name,coordinates,currHP, maxHP, currXP, 
                maxXP,level, speed,maxWeapons,weapons,actionPoints, inventory, actionArea, moveArea);
            }

    /*@Override
    public <T extends Item> T getItem(int k){
        return null;
    }*/


}

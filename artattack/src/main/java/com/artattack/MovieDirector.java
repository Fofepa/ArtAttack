package com.artattack;

import java.util.*;

public class MovieDirector extends Player {

    public MovieDirector(int ID, char mapSymbol, String name, Coordinates coordinates,int currHP,int maxHP,int currXP, int maxXP, 
        int level, int maxWeapons, List<Weapon> weapons ,int speed, int actionPoints, List<Coordinates> actionArea,
         List<Coordinates> moveArea, List<? extends Item> inventory){
            super(ID,mapSymbol,name,coordinates,currHP,maxHP,currXP,maxHP,level,maxWeapons, weapons, speed,actionPoints,actionArea,
                moveArea,inventory);
    }

    @Override
    public <T extends Item> T getItem(int k){
        return null;
    }
}

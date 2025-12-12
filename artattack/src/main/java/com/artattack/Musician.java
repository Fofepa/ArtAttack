package com.artattack;

import java.util.*;

public class Musician extends Player{

    public Musician(int ID, char mapSymbol, String name, Coordinates coordinates,int currHP,int maxHP,int currXP, int maxXP, 
        int level, int maxWeapons, List<Weapon> weapons ,int speed, int actionPoints, List<Coordinates> actionArea,
         List<Coordinates> moveArea, List<? extends Item> inventory){
            super(ID,mapSymbol,name,coordinates,currHP,maxHP,currXP,maxHP,level,maxWeapons, weapons, speed,actionPoints,actionArea,
                moveArea,inventory);
    }
    
}

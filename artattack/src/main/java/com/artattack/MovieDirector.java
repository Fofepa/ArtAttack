package com.artattack;

import java.util.List;

public class MovieDirector extends Characters {

    public MovieDirector(String name,int currHP,int maxHP,int currXP, int maxXP, 
        int level, int maxWeapons, int speed, int actionPoints, List<Coordinates> actionArea,
         List<Coordinates> moveArea, List<? extends Item> inventory){
            super(name,currHP,maxHP,currXP,maxHP,level,maxWeapons,speed,actionPoints,actionArea,
                moveArea,inventory);
    }

    @Override
    public <T extends Item> T getItem(int k){
        return null;
    }
}

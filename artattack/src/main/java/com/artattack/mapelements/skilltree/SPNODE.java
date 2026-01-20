package com.artattack.mapelements.skilltree;

import com.artattack.mapelements.Player;

// type of nodes that increases the Speed
public class  SPNODE extends Node{
    int newSP;
    
    public SPNODE(Player player, int newSP){
        super(player);
        this.newSP = newSP;
    }

    @Override
    public void setSkill(){
        this.getPlayer().setSpeed(this.getPlayer().getSpeed()+ newSP);
        this.setSpent();
    }
}
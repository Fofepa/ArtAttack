package com.artattack.mapelements.skilltree;

import com.artattack.mapelements.Player;

// type of nodes that increases the maxWeapons
public class  MAXWPNODE extends Node{
    
    public MAXWPNODE(Player player){
        super(player);
    }

    @Override
    public void setSkill(){
        this.getPlayer().setMaxWeapons();
        this.setSpent();
    }
}
package com.artattack.mapelements.skilltree;

import com.artattack.mapelements.Player;

// type of nodes that increases the maxAP
public class  APNODE extends Node{
    int newAP;
    
    public APNODE(Player player, int newAP){
        super(player);
        this.newAP = newAP;
    }

    @Override
    public void setSkill(){
        this.getPlayer().setMaxActionPoints(this.getPlayer().getMaxActionPoints()+ newAP);
        this.setSpent();
    }
}

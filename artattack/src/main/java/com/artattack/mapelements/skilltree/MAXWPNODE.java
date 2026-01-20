package com.artattack.mapelements.skilltree;

import com.artattack.mapelements.Player;

// type of nodes that increases the maxWeapons
public class  MAXWPNODE extends Node{
    
    public MAXWPNODE(Player player){
        super(player);
    }

    @Override
    public void setSkill(){
        for(Node parent : this.getParents()){
            if(parent.isSpent() && !this.isSpent()){
                this.getPlayer().setMaxWeapons();
                this.setSpent();
                return;
            }
        }
        System.out.println("No parent has been used before");
    }
}
package com.artattack.mapelements.skilltree;

import com.artattack.mapelements.Player;

// type of nodes that increases the maxWeapons
public class  MAXWPNODE extends Node{
    
    public MAXWPNODE(){
        super(NodeType.MAXWP);
    }

    @Override
    public void setSkill(Player player){
        for(Node parent : this.getParents()){
            if(parent.isSpent() && !this.isSpent()){
                player.setMaxWeapons();
                this.setSpent();
                return;
            }
        }
        System.out.println("No parent has been used before");
    }
}
package com.artattack.mapelements.skilltree;

import com.artattack.mapelements.Player;

// type of nodes that increases the Speed
public class  SPNODE extends Node{
    int newSP;
    
    public SPNODE(Player player, int newSP){
        super(player, NodeType.SP);
        this.newSP = newSP;
    }

    @Override
    public void setSkill(){
        for(Node parent : this.getParents()){
            if(parent.isSpent() && !this.isSpent()){
                this.getPlayer().setSpeed(this.getPlayer().getSpeed()+ newSP);
                this.setSpent();        
                return;
            }
        }
        System.out.println("No parent has been used before");
    }
}
package com.artattack.mapelements.skilltree;

import com.artattack.mapelements.Player;

// type of nodes that increases the Speed
public class  SPNODE extends Node{
    int newSP;
    
    public SPNODE(int newSP){
        super(NodeType.SP);
        this.newSP = newSP;
    }

    @Override
    public void setSkill(Player player){
        for(Node parent : this.getParents()){
            if(parent.isSpent() && !this.isSpent()){
                player.setSpeed(player.getSpeed()+ newSP);
                this.setSpent();        
                return;
            }
        }
        System.out.println("No parent has been used before");
    }

    public int getNewSP() {
        return newSP;
    }

    
}


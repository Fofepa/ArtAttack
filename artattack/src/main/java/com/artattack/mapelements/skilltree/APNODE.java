package com.artattack.mapelements.skilltree;

import com.artattack.mapelements.Player;

// type of nodes that increases the maxAP
public class  APNODE extends Node{
    int newAP;
    
    public APNODE(int newAP){
        super(NodeType.AP);
        this.newAP = newAP;
    }

    @Override
    public void setSkill(Player player){
        for(Node parent : this.getParents()){
            if(parent.isSpent() && !this.isSpent()){
                player.setMaxActionPoints(player.getMaxActionPoints()+ newAP);
                this.setSpent();
                return;
            }
        }
        System.out.println("No parent has been used before");
    }

    public int getNewAP() {
        return newAP;
    }

    
}

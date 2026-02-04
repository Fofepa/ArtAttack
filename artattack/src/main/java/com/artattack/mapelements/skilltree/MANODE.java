package com.artattack.mapelements.skilltree;

import java.util.List;

import com.artattack.level.Coordinates;
import com.artattack.mapelements.Player;

// type of nodes that creates a new movementArea
public class  MANODE extends Node{
    private List<Coordinates> shape;    // piece of area to add
    
    public MANODE(List<Coordinates> shape){
        super(NodeType.MA);
        this.shape = shape;
    }

    @Override
    public void setSkill(Player player){
        for(Node parent : this.getParents()){
            if(parent.isSpent() && !this.isSpent()){
                player.getMoveArea().addAll(shape);
                this.setSpent();
                return;
            }
        }
        System.out.println("No parent has been used before");
    }

    public List<Coordinates> getShape() {
        return shape;
    }

    
}
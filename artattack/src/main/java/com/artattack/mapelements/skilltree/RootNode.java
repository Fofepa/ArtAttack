package com.artattack.mapelements.skilltree;

import com.artattack.mapelements.Player;

public class RootNode extends Node{
    public RootNode(){
        super(NodeType.ROOT);
    }

    @Override
    public void setSkill(Player player){
        System.out.println("Root created");
    }
}

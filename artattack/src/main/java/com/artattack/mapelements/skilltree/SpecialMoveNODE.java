package com.artattack.mapelements.skilltree;

import com.artattack.mapelements.Player;
import com.artattack.mapelements.PlayerType;
import com.artattack.moves.Move;
import com.artattack.moves.Weapon;

// type of nodes that creates a new SpecialMove
public class SpecialMoveNODE extends Node{
    private Move specialMove;

    public SpecialMoveNODE(Move specialMove){
        super(NodeType.SPECIALMOVE);
        this.specialMove = specialMove;
    }

    @Override
    public void setSkill(Player player){
        for(Node parent : this.getParents()){
            if(parent.isSpent() && !this.isSpent()){
                Weapon specialWeapon = new Weapon("Special", "You got it with tears", 1, PlayerType.MUSICIAN);
                if(!player.getWeapons().contains(specialWeapon)){
                    specialWeapon.addMove(specialMove);
                    player.setMaxWeapons();
                    player.getWeapons().add(specialWeapon);
                    this.setSpent();
                }
                else{
                    player.getWeapons().get(player.getWeapons().indexOf(specialWeapon)).addMove(specialMove);
                }
                return;
            }
        }
        System.out.println("No parent has been used before");
    }

    public Move getSpecialMove() {
        return specialMove;
    }

    
}
package com.artattack.mapelements;

import java.util.*;

import com.artattack.level.AreaBuilder;
import com.artattack.level.Coordinates;
import com.artattack.moves.Move;
import com.artattack.moves.Weapon;


// N-ary tree
public class SkillTree{
    private Player player;
    private Node root;
    
}

abstract class Node{
    private boolean spent;  // tells if it was used or not
    private int label;  // defines his number as a child
    private List<Node> children;
    private Player player;

    public Node(Player player){
        this.spent = false;
        this.children = new ArrayList<>();
        this.player = player;
    }

    public void setChildren(ArrayList<Node> children){
        this.children = children;
    }

    public Player getPlayer(){
        return this.player;
    }

    public int getLabel(){
        return this.label;
    }

    public abstract void setSkill();

}

// type of nodes that increases the maxHP
class  HPNODE extends Node{
    int newHP;
    
    public HPNODE(Player player, int newHP){
        super(player);
        this.newHP = newHP;
    }

    @Override
    public void setSkill(){
        this.getPlayer().setMaxHP(this.getPlayer().getMaxHP()+ newHP);
    }
}

// type of nodes that increases the maxAP
class  APNODE extends Node{
    int newAP;
    
    public APNODE(Player player, int newHP){
        super(player);
        this.newAP = newAP;
    }

    @Override
    public void setSkill(){
        this.getPlayer().setMaxActionPoints(this.getPlayer().getMaxActionPoints()+ newAP);
    }
}

// type of nodes that increases the Speed
class  SPNODE extends Node{
    int newSP;
    
    public SPNODE(Player player, int newSP){
        super(player);
        this.newSP = newSP;
    }

    @Override
    public void setSkill(){
        this.getPlayer().setSpeed(this.getPlayer().getSpeed()+ newSP);
    }
}

// type of nodes that increases the maxWeapons
class  MAXWPNODE extends Node{
    
    public MAXWPNODE(Player player){
        super(player);
    }

    @Override
    public void setSkill(){
        this.getPlayer().setMaxWeapons();
    }
}

// type of nodes that creates a new movementArea
class  MANODE extends Node{
    private List<Coordinates> shape;    // piece of area to add
    
    public MANODE(Player player, List<Coordinates> shape){
        super(player);
        this.shape = shape;
    }

    @Override
    public void setSkill(){
        this.getPlayer().getMoveArea().addAll(shape);
    }
}

// type of nodes that creates a new SpecialMove
class SpecialMoveNODE extends Node{
    private Move specialMove;

    public SpecialMoveNODE(Player player, Move specialMove){
        super(player);
        this.specialMove = specialMove;
    }

    @Override
    public void setSkill(){
        this.getPlayer().setMaxWeapons();
        Weapon specialWeapon = new Weapon("Special", "You got it with tears", 0);
        specialWeapon.addMove(specialMove);
        this.getPlayer().getWeapons().add(specialWeapon);
    }
}






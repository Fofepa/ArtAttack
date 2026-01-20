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

    public SkillTree(Player player, Node root){
        this.player = player;
        this.root = root;
        this.root.setSpent();
        this.root.setLabel();
    }

    // TODO: add the find and search method    
}

abstract class Node{
    private boolean spent;  // tells if it was used or not
    private int label;  // defines his number as a child
    private static int counter = 0;
    private List<Node> parents;
    private List<Node> children;
    private Player player;

    public Node(Player player){
        this.spent = false;
        this.children = new ArrayList<>();
        this.player = player;
    }

    public void addChildren(ArrayList<Node> children){
        for(Node child : children){
            if(!child.hasParent()){
                this.label = counter++;
            }
            child.setParent(this);
        }
        this.children = children;
    }

    public Player getPlayer(){
        return this.player;
    }

    public int getLabel(){
        return this.label;
    }

    public List<Node> getChildren(){
        return this.children;
    }

    public Node getChild(int index){
        return this.children.get(index);
    }

    public List<Node> getParents(){
        return this.parents;
    }

    public void setParent(Node parent){
        this.parents.add(parent);
    }

    public boolean isSpent(){
        return this.spent;
    }

    public void setSpent(){
        this.spent = true;
    }
    
    public boolean hasParent(){
        return this.parents.isEmpty() ? false : true;
    }

    public void setLabel(){
        this.label = counter++;
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
        this.setSpent();
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
        this.setSpent();
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
        this.setSpent();
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
        this.setSpent();
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
        this.setSpent();
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
        this.setSpent();
    }
}






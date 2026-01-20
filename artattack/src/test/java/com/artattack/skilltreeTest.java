package com.artattack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import com.artattack.level.AreaBuilder;
import com.artattack.level.Coordinates;
import com.artattack.mapelements.*;
import com.artattack.mapelements.skilltree.*;
import com.artattack.moves.Move;
import com.artattack.moves.Weapon;

public class skilltreeTest {
    private SkillTree skillTree;
    private Player player;

    @Before
    public void setUp(){
        AreaBuilder areaBuilder = new AreaBuilder();
        areaBuilder.addShape("4");
        List<Coordinates> area4 = areaBuilder.getResult();
        List<Coordinates> area = new ArrayList<>();
        area.add(new Coordinates(1,1));
        List<Weapon> weapons = new ArrayList<>();
        weapons.add(new Weapon("Hoe", "", 0));
        this.player = new Musician(1, '@', "Zappa", new Coordinates(0, 1), weapons, 5,5, area, 20, 20, 0, 20, 1, 5, 2, null, null, null);
        Node root = new RootNode(player);
        HPNODE node_1 = new HPNODE(player,1);
        APNODE node_2 = new APNODE(player, 5);
        MANODE node_3 = new MANODE(player,area4);
        APNODE node_4 = new APNODE(player, 4);
        SPNODE node_5 = new SPNODE(player,2);
        SPNODE node_6 = new SPNODE(player, 3);
        SPNODE node_7 = new SPNODE(player, 8);
        Move move_1 = new Move(); move_1.setName("SWORD"); move_1.setPower(10000); move_1.setActionPoints(18); 
        Move move_2 = new Move(); move_2.setName("MACHINE GUN"); move_2.setPower(10100); move_2.setActionPoints(11); 
        SpecialMoveNODE node_8 = new SpecialMoveNODE(player, move_1);
        SpecialMoveNODE node_9 = new SpecialMoveNODE(player, move_2);

        
        this.skillTree = new SkillTree(player, root);
        root.addChildren(List.of(node_1,node_2));
        node_1.addChildren(List.of(node_3,node_4));
        node_3.addChildren(List.of(node_5));
        node_4.addChildren(List.of(node_5));
        node_2.addChildren(List.of(node_6));
        node_6.addChildren(List.of(node_7));
        node_7.addChildren(List.of(node_8));
        node_8.addChildren(List.of(node_9));
        this.skillTree.buildTree(root);
    }

    @Test
    public void setskillTest(){
        skillTree.find(1).setSkill();   // basic use
        assertEquals(21, player.getMaxHP());

        skillTree.find(1).setSkill();   // shoudn't use the same Node twice
        assertEquals(21, player.getMaxHP());

        skillTree.find(6).setSkill();   // shouldn't use a node that has a parent not spent
        assertEquals(5, player.getSpeed());

        skillTree.find(3).setSkill();   // checks if the area gets update correctly
        assertEquals(5, player.getMoveArea().size());

        skillTree.find(5).setSkill();   // at least one parent has to be spent
        assertEquals(7, player.getSpeed());

        skillTree.find(7).setSpent();   // just to let us use the nodes 8 and 9

        skillTree.find(8).setSkill();   // checks if the move get created and added correctly
        assertEquals(1, player.getWeapons().get(1).getMoves().size());
        
        skillTree.find(9).setSkill();   // checks if the other move doesn't crea another weapon
        assertEquals(2, player.getWeapons().size());
    }

    @After
    public void tearDown(){
        skillTree = null;
        assertNull(skillTree);
        player = null;
        assertNull(player);
    }
}

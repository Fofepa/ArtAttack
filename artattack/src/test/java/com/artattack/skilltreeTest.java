package com.artattack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import com.artattack.level.Coordinates;
import com.artattack.mapelements.*;
import com.artattack.mapelements.skilltree.*;
import com.artattack.moves.Weapon;

public class skilltreeTest {
    private SkillTree skillTree;
    private Player player;

    @Before
    public void setUp(){
        this.player = new Musician(1, '@', "Zappa", new Coordinates(0, 1), List.of(new Weapon("Hoe", "", 0)), 5,5, null, 20, 20, 0, 20, 1, 5, 2, null, null, null);
        Node root = new RootNode(player);
        HPNODE node_1 = new HPNODE(player,1);
        APNODE node_2 = new APNODE(player, 5);
        SPNODE node_3 = new SPNODE(player,3);
        APNODE node_4 = new APNODE(player, 4);
        SPNODE node_5 = new SPNODE(player,2);
        SPNODE node_6 = new SPNODE(player, 3);
        SPNODE node_7 = new SPNODE(player, 8);

        
        this.skillTree = new SkillTree(player, root);
        root.addChildren(List.of(node_1,node_2));
        node_1.addChildren(List.of(node_3,node_4));
        node_3.addChildren(List.of(node_5));
        node_4.addChildren(List.of(node_5));
        node_2.addChildren(List.of(node_6));
        node_6.addChildren(List.of(node_7));
        this.skillTree.buildTree(root);
    }

    @Test
    public void setskillTest(){
        skillTree.find(1).setSkill();
        assertEquals(21, player.getMaxHP());
    }

    @After
    public void tearDown(){
        skillTree = null;
        assertNull(skillTree);
        player = null;
        assertNull(player);
    }
    
}

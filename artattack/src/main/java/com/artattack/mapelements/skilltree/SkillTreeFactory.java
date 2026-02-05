package com.artattack.mapelements.skilltree;

import java.util.List;

import com.artattack.mapelements.PlayerType;

public class SkillTreeFactory {
    
    public static SkillTree createSkillTree(PlayerType type) {
        return switch (type) {
            case MUSICIAN -> createZappaSkillTree();
            case MOVIE_DIRECTOR -> createLynchSkillTree();
        };
    }
    
    //Frank Zappa's tree node hierarchy method
    private static SkillTree createZappaSkillTree() {
        Node root = new RootNode();
        
        Node node_2 = new HPNODE(3);
        Node node_3 = new APNODE(2);
        
        Node node_4 = new APNODE(2);
        Node node_5 = new SPNODE(1);
        
        Node node_6 = new HPNODE(5);
        
        Node node_7 = new MAXWPNODE();
        
        Node node_8 = new MAXMVNODE();
        Node node_9 = new APNODE(3);
        
        Node node_10 = new MAXMVNODE();
        Node node_11 = new MAXMVNODE();
        Node node_13 = new SPNODE(2);
        
        Node node_12 = new SpecialMoveNODE(null);
        Node node_14 = new MANODE(null);
        
        Node node_15 = new SPNODE(1);
        
        Node node_16 = new APNODE(2);
        
        Node node_17 = new MANODE(null);
        Node node_18 = new HPNODE(4);
        
        Node node_19 = new HPNODE(5);
        Node node_20 = new MAXMVNODE();
        
        Node node_21 = new MANODE(null);
        
        Node node_22 = new MAXMVNODE();
        Node node_23 = new APNODE(3);
        
        Node node_24 = new MANODE(null);
        Node node_25 = new SPNODE(2);
        
        Node node_26 = new SpecialMoveNODE(null);

        SkillTree tree = new SkillTree(root);
        root.addChildren(List.of(node_2,node_3));
        //Left branch
        node_2.addChildren(List.of(node_4, node_5));
        node_4.addChildren(List.of(node_6));
        node_5.addChildren(List.of(node_6));
        node_6.addChildren(List.of(node_7));
        node_7.addChildren(List.of(node_8, node_9));
        node_8.addChildren(List.of(node_11, node_10));
        node_9.addChildren(List.of(node_13));
        node_10.addChildren(List.of(node_12));
        node_11.addChildren(List.of(node_12));
        node_13.addChildren(List.of(node_14));
        //Right branch
        node_3.addChildren(List.of(node_15));
        node_15.addChildren(List.of(node_16));
        node_16.addChildren(List.of(node_17, node_18));
        node_17.addChildren(List.of(node_19));
        node_18.addChildren(List.of(node_20));
        node_19.addChildren(List.of(node_21));
        node_20.addChildren(List.of(node_21));
        node_21.addChildren(List.of(node_22, node_23));
        node_22.addChildren(List.of(node_24));
        node_23.addChildren(List.of(node_25));
        node_24.addChildren(List.of(node_26));
        node_25.addChildren(List.of(node_26));

        tree.buildTree(root);
        return tree;
    }
    
    //David Lynch's tree node hierarchy method
    private static SkillTree createLynchSkillTree() {
        Node root = new RootNode();

        Node node_2 = new HPNODE(4);
        Node node_3 = new APNODE(2);
        
        Node node_4 = new APNODE(2);
        
        Node node_5 = new MAXMVNODE();
        Node node_6 = new MAXMVNODE();
        
        Node node_7 = new SPNODE(1);
        Node node_8 = new MANODE(null);
        
        Node node_9 = new MAXWPNODE();
        
        Node node_10 = new HPNODE(5);
        Node node_11 = new APNODE(3);
        
        Node node_12 = new MAXMVNODE();
        Node node_14 = new APNODE(3);
        
        Node node_13 = new SpecialMoveNODE(null);
        Node node_15 = new MANODE(null);
        
        Node node_16 = new SPNODE(1);
        
        Node node_17 = new MANODE(null);
        
        Node node_18 = new HPNODE(4);
        
        Node node_19 = new MAXMVNODE();
        Node node_20 = new MAXMVNODE();
        
        Node node_21 = new APNODE(3);
        
        Node node_22 = new SPNODE(2);
        
        Node node_23 = new HPNODE(6);
        Node node_24 = new SpecialMoveNODE(null);
        Node node_25 = new APNODE(4);
        
        SkillTree tree = new SkillTree(root);
        root.addChildren(List.of(node_2,node_3));
        //Left branch
        node_2.addChildren(List.of(node_4));
        node_4.addChildren(List.of(node_5, node_6));
        node_5.addChildren(List.of(node_7));
        node_6.addChildren(List.of(node_8));
        node_7.addChildren(List.of(node_9));
        node_8.addChildren(List.of(node_9));
        node_9.addChildren(List.of(node_10, node_11));
        node_10.addChildren(List.of(node_12));
        node_11.addChildren(List.of(node_14));
        node_12.addChildren(List.of(node_13));
        node_14.addChildren(List.of(node_15));
        //Right branch
        node_3.addChildren(List.of(node_16));
        node_16.addChildren(List.of(node_17));
        node_17.addChildren(List.of(node_18));
        node_18.addChildren(List.of(node_19, node_20));
        node_19.addChildren(List.of(node_21));
        node_20.addChildren(List.of(node_21));
        node_21.addChildren(List.of(node_22));
        node_22.addChildren(List.of(node_23, node_24, node_25));

        tree.buildTree(root);
        return tree;
    }
}
package com.artattack.mapelements.skilltree;

import com.artattack.mapelements.Player;

public class SkillTreeFactory {
    
    public static SkillTree createSkillTree(Player player) {
        return switch (player.getType()) {
            case MUSICIAN -> createZappaSkillTree(player);
            case MOVIE_DIRECTOR -> createLynchSkillTree(player);
        };
    }
    
    private static SkillTree createZappaSkillTree(Player player) {
        Node root = new RootNode(player);
        Node node_2 = new HPNODE(player, 0);
        Node node_3 = new APNODE(player, 0);
        Node node_4 = new APNODE(player, 0);
        Node node_5 = new SPNODE(player, 0);
        Node node_6 = new HPNODE(player, 0);
        Node node_7 = new MAXWPNODE(player);
        /* Node node_8 = new  */
        Node node_9 = new APNODE(player, 0);
        /* Node node_10 =  */
        /* Node node_11 =  */
        Node node_12 = new SpecialMoveNODE(player, null);
        Node node_13 = new SPNODE(player, 0);
        Node node_14 = new MANODE(player, null);
        Node node_15 = new SPNODE(player, 0);
        Node node_16 = new APNODE(player, 0);
        Node node_17 = new MANODE(player, null);
        Node node_18 = new HPNODE(player, 0);
        Node node_19 = new HPNODE(player, 0);
        /* Node node_20 = new ; */
        Node node_21 = new MANODE(player, null);
        /* Node node_22 = new ; */
        Node node_23 = new APNODE(player, 0);
        Node node_24 = new MANODE(player, null);
        Node node_25 = new SPNODE(player, 0);
        Node node_26 = new SpecialMoveNODE(player, null);


        
        SkillTree tree = new SkillTree(player, root);
        tree.buildTree(root);
        return tree;
    }
    
    private static SkillTree createLynchSkillTree(Player player) {
        Node root = new RootNode(player);

        Node node_2 = new HPNODE(player, 0);
        Node node_3 = new APNODE(player, 0);
        Node node_4 = new APNODE(player, 0);
        /* Node node_5 = new ; */
        /* Node node_6 = new ; */
        Node node_7 = new SPNODE(player, 0);
        Node node_8 = new MANODE(player, null);
        Node node_9 = new MAXWPNODE(player);
        Node node_10 = new HPNODE(player, 0);
        Node node_11 = new APNODE(player, 0);
        /* Node node_12 = new ; */
        Node node_13 = new SpecialMoveNODE(player, null);
        Node node_14 = new APNODE(player, 0);
        Node node_15 = new MANODE(player, null);
        Node node_16 = new SPNODE(player, 0);
        Node node_17 = new MANODE(player, null);
        Node node_18 = new HPNODE(player, 0);
        /* Node node_19 = new ; */
        /* Node node_20 = new ; */        
        Node node_21 = new APNODE(player, 0);
        Node node_22 = new SPNODE(player, 0);
        Node node_23 = new HPNODE(player, 0);
        Node node_24 = new SpecialMoveNODE(player, null);
        Node node_25 = new APNODE(player, 0);
        
        SkillTree tree = new SkillTree(player, root);
        tree.buildTree(root);
        return tree;
    }
}

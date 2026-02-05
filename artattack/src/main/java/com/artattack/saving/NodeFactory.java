package com.artattack.saving;

import com.artattack.mapelements.skilltree.APNODE;
import com.artattack.mapelements.skilltree.HPNODE;
import com.artattack.mapelements.skilltree.MANODE;
import com.artattack.mapelements.skilltree.MAXMVNODE;
import com.artattack.mapelements.skilltree.MAXWPNODE;
import com.artattack.mapelements.skilltree.Node;
import com.artattack.mapelements.skilltree.RootNode;
import com.artattack.mapelements.skilltree.SpecialMoveNODE;

public class NodeFactory {
    public static Node create(NodeData data) {

        if (data instanceof HPNODEData hp) {
            HPNODE node = new HPNODE(hp.getNewHP());
            return node;

        } else if (data instanceof APNODEData ap) {
            return new APNODE(ap.getNewAP());

        } else if (data instanceof MANODEData ma) {
            return new MANODE(ma.getShape());

        } else if (data instanceof SpecialMoveNodeData sm) {
            return new SpecialMoveNODE(sm.getSpecialMove());
        
        } else if (data instanceof MAXMVNODEData) {
            return new MAXMVNODE();
        
        } else if (data instanceof MAXWPNODEData) {
            return new MAXWPNODE();
        }
        
        return new RootNode();
    }
}


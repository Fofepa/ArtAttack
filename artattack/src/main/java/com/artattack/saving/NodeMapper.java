package com.artattack.saving;

import com.artattack.mapelements.skilltree.Node;
import com.artattack.mapelements.skilltree.APNODE;
import com.artattack.mapelements.skilltree.HPNODE;
import com.artattack.mapelements.skilltree.MANODE;
import com.artattack.mapelements.skilltree.SpecialMoveNODE;

public final class NodeMapper {

    private NodeMapper() {}

    public static NodeData toData(Node node) {

        NodeData data;

        if (node instanceof HPNODE hp) {
            HPNODEData d = new HPNODEData();
            d.setNewHP(hp.getNewHP());
            data = d;

        } else if (node instanceof APNODE ap) {
            APNODEData d = new APNODEData();
            d.setNewAP(ap.getNewAP());
            data = d;

        } else if (node instanceof MANODE ma) {
            MANODEData d = new MANODEData();
            d.setShape(ma.getShape());
            data = d;

        } else if (node instanceof SpecialMoveNODE sm) {
            SpecialMoveNodeData d = new SpecialMoveNodeData();
            d.setSpecialMove(sm.getSpecialMove());
            data = d;

        } else {
            data = new NodeData() {};
        }

        // campi comuni
        data.type = node.getType();
        data.spent = node.isSpent();
        data.label = node.getLabel();

        data.parents = node.getParents().stream()
            .map(Node::getLabel).toList();

        data.children = node.getChildren().stream()
            .map(Node::getLabel).toList();

        return data;
    }
}

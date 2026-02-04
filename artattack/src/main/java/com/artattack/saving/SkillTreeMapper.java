package com.artattack.saving;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.artattack.mapelements.skilltree.SkillTree;
import com.artattack.mapelements.skilltree.Node;

public class SkillTreeMapper {

    public static SkillTreeData toData(SkillTree tree) {
        SkillTreeData data = new SkillTreeData();
        data.rootLabel = tree.getRoot().getLabel();

        data.nodes = tree.getSupportList()
            .stream()
            .map(NodeMapper::toData)
            .toList();

        return data;
    }

    public static SkillTree fromData(SkillTreeData data) {
        Map<Integer, Node> map = new HashMap<>();

        // A – crea nodi
        for (NodeData nd : data.nodes) {
            Node node = NodeFactory.create(nd);
            node.setSpent(nd.spent);
            node.setLabel(nd.label);

            map.put(nd.label, node);
        }


        // B – collega

        for (NodeData nd : data.nodes) {
            Node node = map.get(nd.label);

            for (int parentLabel : nd.parents) {
                node.addParent(map.get(parentLabel));
            }

            for (int childLabel : nd.children) {
                node.addChild(map.get(childLabel));
            }
        }

        return new SkillTree(
                map.get(data.rootLabel),
                new ArrayList<>(map.values())
            );


    
    }
}

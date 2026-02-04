package com.artattack.saving;

import java.util.List;

import com.artattack.mapelements.skilltree.NodeType;

public class NodeData {
    NodeType type;
    boolean spent;
    int label;

    List<Integer> parents;
    List<Integer> children;

}


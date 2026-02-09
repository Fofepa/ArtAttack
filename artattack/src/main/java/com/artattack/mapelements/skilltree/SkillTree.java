package com.artattack.mapelements.skilltree;

import java.util.ArrayList;
import java.util.List;


// N-ary tree
public class SkillTree{
    private Node root;
    private List<Node> supportList;

    public SkillTree(Node root){
        this.root = root;
        this.root.setSpent();
        this.root.setLabel();
        this.supportList = new ArrayList<>();
        this.supportList.add(root);
    }

    public SkillTree(Node root, List<Node> supportList) {
    this.root = root;
    this.supportList = supportList;
}


    public void buildTree(Node root){
        if(root.getChildren().isEmpty())
            return;
        for (Node child : root.getChildren()){
            if(!supportList.contains(child)){
                this.supportList.add(child);
            }
            buildTree(child);
        }
    }

    public boolean isComplete(){
        for(Node node : this.supportList){
            if(node.isSpent()){
                return false;
            }
        }
        return true;
    }

    public Node find(int label){
        for(Node node : this.supportList){
            if(label == node.getLabel())
                return node;
        }
        System.out.println("No node found with that label");
        return null;
    }

    public List<Node> getSupportList(){
        return this.supportList;
    }
    
    public Node getRoot(){
        return this.root;
    }
    
}
package com.artattack.view;

import java.util.List;
import java.util.function.Consumer;

import com.artattack.level.Maps;
import com.artattack.mapelements.Player;
import com.artattack.mapelements.skilltree.Node;
import com.artattack.mapelements.skilltree.SkillTree;

public interface UIManager {
    public void showDialog(List<String> messages);
    public void showDialogWithChoice(String question, List<String> options, Consumer<Integer> callback);
    public void loadSprite(String spritePath);
    public void switchMap(Maps map);
    public void repaintInventoryPanel();
    public void showSkillTreePanel(Player player, SkillTree skillTree, Consumer<Node> callback);
}
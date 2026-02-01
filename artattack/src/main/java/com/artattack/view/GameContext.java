package com.artattack.view;

import com.artattack.interactions.SaveManager;
import com.artattack.level.MapManager;
import com.artattack.mapelements.skilltree.SkillTree;

public class GameContext {
    private final UIManager uiManager;
    private MapManager mapManager;
    private SaveManager saveManager;
    private SkillTree player1SkillTree;
    private SkillTree player2SkillTree;

    public GameContext(UIManager uiManager, MapManager mapManager){
        this.uiManager = uiManager;
        this.mapManager = mapManager;
        this.saveManager = new SaveManager();
    }

    public GameContext(UIManager uiManager, MapManager mapManager, SaveManager saveManager){
        this.uiManager = uiManager;
        this.mapManager = mapManager;
        this.saveManager = saveManager;
    }

    public UIManager getUiManager() {
        return uiManager;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public SaveManager getSaveManager() {
        return saveManager;
    }

    

    
    public void setPlayer1SkillTree(SkillTree skillTree) {
        this.player1SkillTree = skillTree;
    }

    public void setPlayer2SkillTree(SkillTree skillTree) {
        this.player2SkillTree = skillTree;
    }

    public SkillTree getPlayer1SkillTree() {
        return player1SkillTree;
    }

    public SkillTree getPlayer2SkillTree() {
        return player2SkillTree;
    }
}
package com.artattack.enemystrategy;

import java.util.List;

import com.artattack.level.Maps;
import com.artattack.mapelements.Enemy;
import com.artattack.view.MainFrame;

public class HealStrategy extends DecisionStrategy{

    public HealStrategy(MainFrame mainFrame){
        super(mainFrame);
    }

    @Override
    public void execute(Enemy enemy, Maps map){
        int heal = enemy.getWeapons().get(0).getMoves().get(enemy.getWeapons().get(0).getMoves().indexOf(this.getHealMove())).useMove(enemy, map);
        this.getMainFrame().showDialog(List.of(enemy.getName() + " is healing his allies! total amount: " + heal ), enemy.getSpritePath());
    }

}

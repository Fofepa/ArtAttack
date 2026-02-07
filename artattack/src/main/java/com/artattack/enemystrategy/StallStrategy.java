package com.artattack.enemystrategy;

import java.util.List;

import com.artattack.level.Maps;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.EnemyType;
import com.artattack.view.MainFrame;

public class StallStrategy extends DecisionStrategy {

    public StallStrategy(MainFrame mainFrame){
        super(mainFrame);
    }


    @Override
    public void execute(Enemy enemy, Maps map){
        switch (enemy.getEnemyType()){

             case EnemyType.DUMMY:
                this.getMainFrame().showDialog(List.of("Regretful being: I want to apologize to you gentlemen for reffering to you as homosexuals"));
                enemy.setActionPoints(enemy.getActionPoints()-3);
                break;

            case EnemyType.EMPLOYEE: 
                this.getMainFrame().showDialog(List.of("Scientist: AAAAHHH!!!"));
                enemy.setActionPoints(enemy.getActionPoints()-1);
                break;

            case EnemyType.GUARD:
                this.getMainFrame().showDialog(List.of("Guard: I'm reloading! Watch out!"));
                enemy.setActionPoints(enemy.getActionPoints()-2);
                break;

            case EnemyType.ROBOT:
                this.getMainFrame().showDialog(List.of("Guard Robot: Processing the better solution..."));
                enemy.setActionPoints(enemy.getActionPoints()-3);
                break;
        }
    }
}

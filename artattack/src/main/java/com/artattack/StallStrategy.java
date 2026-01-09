package com.artattack;

import java.util.List;

import com.artattack.view.MainFrame;

public class StallStrategy extends DecisionStrategy {

    public StallStrategy(MainFrame mainFrame){
        super(mainFrame);
    }


    @Override
    public void execute(Enemy enemy, Maps map){
        switch (enemy.getEnemyType()){
            case EnemyType.EMPLOYEE: 
                this.getMainFrame().showDialog(List.of("AAAAHHH!!!"));
                enemy.setActionPoints(enemy.getActionPoints()-1);
                break;

            case EnemyType.GUARD:
                this.getMainFrame().showDialog(List.of("I'm reloading! Watch out!"));
                enemy.setActionPoints(enemy.getActionPoints()-2);
                break;

            case EnemyType.ROBOT:
                this.getMainFrame().showDialog(List.of("Processing the better solution..."));
                enemy.setActionPoints(enemy.getActionPoints()-3);
                break;
        }
    }
}

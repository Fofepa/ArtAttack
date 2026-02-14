package com.artattack.enemystrategy;

import java.util.ArrayList;
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
                this.getMainFrame().showDialog(List.of("Regretful being: I want to apologize to you gentlemen for refering to you as homosexuals"), enemy.getSpritePath());
                enemy.setActionPoints(enemy.getActionPoints()-3);
                break;

            case EnemyType.EMPLOYEE: 
                this.getMainFrame().showDialog(List.of("Employee: AAAAHHH!!!"), enemy.getSpritePath());
                enemy.setActionPoints(enemy.getActionPoints()-1);
                break;

            case EnemyType.GUARD:
                this.getMainFrame().showDialog(List.of("Guard: I'm reloading! Watch out!"), enemy.getSpritePath());
                enemy.setActionPoints(enemy.getActionPoints()-2);
                break;

            case EnemyType.ROBOT:
                this.getMainFrame().showDialog(List.of("Guard Robot: Processing the better solution..."), enemy.getSpritePath());
                enemy.setActionPoints(enemy.getActionPoints()-3);
                break;

            case EnemyType.TOOLBOT:
                this.getMainFrame().showDialog(List.of("T.O.O.L.Bot: Time to clean!"), enemy.getSpritePath());
                enemy.setActionPoints(enemy.getActionPoints()-3);
                break;

            case EnemyType.BOB:
                this.getMainFrame().showDialog(List.of("B.O.B.: Nooo! Let me repair this thing, one moment!"), enemy.getSpritePath());
                enemy.setActionPoints(enemy.getActionPoints()-2);
                break;
            case EnemyType.SAM:
                enemy.setActionPoints(enemy.getActionPoints()-5);
                List<String> support = new ArrayList<>(List.of("Sam Altman: I'm in a Deep Learning state!"));
                if(enemy.getCurrHP() < enemy.getMaxActionPoints()){
                    support.add("Sam Altman recovered 10 hp");
                    enemy.updateHP(10);
                }
                this.getMainFrame().showDialog(support, enemy.getSpritePath());
                break;
            case EnemyType.MOSQUITO:
                this.getMainFrame().showDialog(List.of("MosquitoBot: BZZZZZZZZZZ!!"), enemy.getSpritePath());
                enemy.setActionPoints(enemy.getActionPoints()-2);
                break;
            case EnemyType.ATTENDANT:
                this.getMainFrame().showDialog(List.of("Attendant: I have no Keys I swear!!!"), enemy.getSpritePath());
                enemy.setActionPoints(enemy.getActionPoints()-4);
                break;
        }
    }
}

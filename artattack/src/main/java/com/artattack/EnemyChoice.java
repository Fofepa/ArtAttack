package com.artattack;
import java.util.*;

import javax.lang.model.util.ElementScanner14;

import com.artattack.view.MainFrame;

// getTargets
/*
    * Distance
    * EnemyType
        * Move or Stall
    *    
*/
public class EnemyChoice{   // Our Context class 
    private Enemy enemy;
    private Maps map;
    private DecisionStrategy strategy;
     private List<Move> usable; 
    private MainFrame mainFrame;

    public EnemyChoice(MainFrame mainFrame){
        this.mainFrame = mainFrame;
        this.usable = new ArrayList<Move>();
    }

    public void setEnemy(Enemy enemy){
        this.enemy = enemy;
    }

    public void setMap(Maps map){
        this.map = map;
    }

    public void choose(){
        
        // checks if there are any player to be hit and what move can hit them
        for(Move move : enemy.getWeapons().get(0).getMoves()){
            if(move.getAttackTargets(this.enemy, this.map) != null){
                for(ActiveElement element : move.getAttackTargets(this.enemy, this.map)){
                    if(element instanceof Player && enemy.getActionPoints() >= move.getActionPoints()) usable.add(move); 
                }
            }
        }

        boolean hasTarget = !usable.isEmpty(); 

        double r = Math.random();
        switch(this.enemy.getEnemyType()){
            case EMPLOYEE:
                if(hasTarget){
                    if(r < 0.6){
                        setStrategy(new DumbAttackStrategy(this.mainFrame));
                    }
                    else{
                        setStrategy(new StallStrategy(this.mainFrame));
                    }
                }
                else{
                    if(r > 0.6){
                        setStrategy(new StallStrategy(this.mainFrame));
                    }
                    else if(r > 0.3){
                        setStrategy(new FleeStrategy(this.mainFrame));
                    }
                    else{
                        setStrategy(new ApproachStrategy(this.mainFrame));
                    }
                }
                break;

            case GUARD:
                if(hasTarget){ setStrategy(new ApproachStrategy(this.mainFrame));
                    if(r < 0.7){
                        setStrategy(new AttackStrategy(this.mainFrame));
                    }
                    else{
                        setStrategy(new StallStrategy(this.mainFrame));
                    }
                }
                else{
                    if(r < 0.6){
                        setStrategy(new ApproachStrategy(this.mainFrame));
                    }
                    else{
                        setStrategy(new StallStrategy(this.mainFrame));
                    }
                }
                break;

            case ROBOT:
                if(hasTarget){
                    if(r < 0.95){
                        setStrategy(new SmartAttackStrategy(this.mainFrame));
                    }
                    else{
                        setStrategy(new StallStrategy(this.mainFrame));
                    }
                }
                else{
                    if(r < 0.8){
                        setStrategy(new ApproachStrategy(this.mainFrame));
                    }
                    else{
                        setStrategy(new StallStrategy(this.mainFrame));
                    }
                }
                break;
            default:
                map.getConcreteTurnHandler().next();
        }
    }

    private void setStrategy(DecisionStrategy strategy){
        this.strategy = strategy;
        if(!usable.isEmpty()) this.strategy.setMoves(usable);
    }
}
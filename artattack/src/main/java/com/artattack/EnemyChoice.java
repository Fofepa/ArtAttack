package com.artattack;
import java.util.*;

import javax.lang.model.util.ElementScanner14;

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

    private void choose(){
        
        // checks if there are any player to be hit and what move can hit them
        for(Move move : enemy.getWeapons().get(0).getMoves()){
            if(move.getTargets() != null){
                for(ActiveElement element : move.getTargets()){
                    if(element instanceof Player) usable.add(move);
                }
            }
        }

        boolean hasTarget = (usable != null); 

        double r = Math.random();
        switch(this.enemy.getEnemyType()){
            case EMPLOYEE:
                if(hasTarget){
                    if(r < 0.6){
                        setStrategy(new DumbAttackStrategy());
                    }
                    else{
                        setStrategy(new StallStrategy());
                    }
                }
                else{
                    if(r > 0.6){
                        setStrategy(new StallStrategy());
                    }
                    else if(r > 0.3){
                        setStrategy(new FleeStrategy());
                    }
                    else{
                        setStrategy(new ApproachStrategy());
                    }
                }
            break;

            case GUARD:
                if(hasTarget){
                    if(r < 0.7){
                        setStrategy(new AttackStrategy());
                    }
                    else{
                        setStrategy(new StallStrategy());
                    }
                }
                else{
                    if(r < 0.6){
                        setStrategy(new ApproachStrategy());
                    }
                    else{
                        setStrategy(new StallStrategy());
                    }
                }
            break;

            case ROBOT:
                if(hasTarget){
                    if(r < 0.95){
                        setStrategy(new SmartAttackStrategy());
                    }
                    else{
                        setStrategy(new StallStrategy());
                    }
                }
                else{
                    if(r < 0.8){
                        setStrategy(new ApproachStrategy());
                    }
                    else{
                        setStrategy(new StallStrategy());
                    }
                }
            break;
        }
    }

    public void setStrategy(DecisionStrategy strategy){
        this.strategy = strategy;
    }
}
package com.artattack.enemystrategy;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.artattack.level.Maps;
import com.artattack.mapelements.ActiveElement;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.EnemyType;
import com.artattack.mapelements.Player;
import com.artattack.moves.Move;
import com.artattack.view.MainFrame;

/*
    * Distance
    * EnemyType
        * Move or Stall
    *    
*/
public class EnemyChoice{  
    private Enemy enemy;
    private Maps map;
    private DecisionStrategy strategy;
    private MainFrame mainFrame;
    private boolean hasFinished = false;

    public EnemyChoice(MainFrame mainFrame){
        this.mainFrame = mainFrame;
    }

    public void setEnemy(Enemy enemy){
        this.enemy = enemy;
    }

    public void setMap(Maps map){
        this.map = map;
    }

    public void choose(){
        Map<Move,Integer> usable = new HashMap<Move,Integer>();
        Move healMove = null;

        // checks if there are any player to be hit and what move can hit them
        for(Move move : enemy.getWeapons().get(0).getMoves()){
            System.out.println("Checking if " + move.getName() + " from " + enemy.getWeapons().get(0).getName() + " is a valid move");
            if(move.getAttackTargets(this.enemy, this.map) != null){
                System.out.println("Found a move to use!\n\n");
                for(ActiveElement element : move.getAttackTargets(this.enemy, this.map)){
                    if(element instanceof Player && enemy.getActionPoints() >= move.getActionPoints()){
                        if(move.getAreaAttack() && usable.containsKey(move)){
                            usable.put(move, usable.get(move)+1);
                        }
                        else{
                            usable.put(move, 1);
                        }
                    }
                }
            }
        }

        if(enemy.getEnemyType() == EnemyType.BOB || enemy.getEnemyType() == EnemyType.MOSQUITO){
            for(Move move : enemy.getWeapons().get(0).getMoves()){
                if(move.getHealTargets(this.enemy, this.map) != null){
                    for(ActiveElement element : move.getHealTargets(this.enemy, this.map)){
                        if(element instanceof Enemy && enemy.getActionPoints() >= move.getActionPoints()){
                            healMove = move;
                        }
                    }
                }
            }
        }

        // checks if the enemy has still some AP
        boolean hasTarget = !usable.isEmpty(); 
        boolean hasHealTarget = healMove != null;
        if(enemy.getActionPoints() <= 0){
            setHasFinished();   
            return;
        }

        double r = Math.random();
        switch(this.enemy.getEnemyType()){
            case DUMMY:
                if(hasTarget){
                    if(r < 0.4){
                        setStrategy(new DumbAttackStrategy(this.mainFrame), usable);
                        this.strategy.execute(enemy, map);
                    }
                    else{
                        setStrategy(new StallStrategy(this.mainFrame), usable);
                        this.strategy.execute(enemy, map);
                    }
                }
                else{
                    if(r > 0.85){
                        setStrategy(new StallStrategy(this.mainFrame));
                        this.strategy.execute(enemy, map);
                    }
                    else{
                        setStrategy(new ApproachStrategy(this.mainFrame));
                        this.strategy.execute(enemy, map);
                    }
                }
                break;
            case EMPLOYEE:
                if(hasTarget){
                    if(r < 0.6){
                        setStrategy(new DumbAttackStrategy(this.mainFrame), usable);
                        this.strategy.execute(enemy, map);
                    }
                    else if (r < 0.8){
                        setStrategy(new StallStrategy(this.mainFrame), usable);
                        this.strategy.execute(enemy, map);
                    }
                    else{
                        setStrategy(new FleeStrategy(this.mainFrame));
                        this.strategy.execute(enemy, map);
                    }
                }
                else{
                    if(r > 0.6){
                        setStrategy(new StallStrategy(this.mainFrame));
                        this.strategy.execute(enemy, map);
                    }
                    else if(r > 0.3){
                        setStrategy(new FleeStrategy(this.mainFrame));
                        this.strategy.execute(enemy, map);
                    }
                    else{
                        setStrategy(new ApproachStrategy(this.mainFrame));
                        this.strategy.execute(enemy, map);
                    }
                }
                break;

            case GUARD:
                if(hasTarget){
                    System.out.println("Target acquired");
                    if(r < 0.7){
                        setStrategy(new AttackStrategy(this.mainFrame), usable);
                        this.strategy.execute(enemy, map);
                    }
                    else{
                        setStrategy(new StallStrategy(this.mainFrame));
                        this.strategy.execute(enemy, map);
                    }
                }
                else{
                    if(r < 0.6){
                        setStrategy(new ApproachStrategy(this.mainFrame));
                        this.strategy.execute(enemy, map);
                    }
                    else{
                        setStrategy(new StallStrategy(this.mainFrame));
                        this.strategy.execute(enemy, map);
                    }
                }
                break;

            case ROBOT:
                if(hasTarget){
                    if(r < 0.95){
                        setStrategy(new SmartAttackStrategy(this.mainFrame), usable);
                        this.strategy.execute(enemy, map);
                    }
                    else{
                        setStrategy(new StallStrategy(this.mainFrame));
                        this.strategy.execute(enemy, map);
                    }
                }
                else{
                    if(r < 0.8){
                        setStrategy(new ApproachStrategy(this.mainFrame));
                        this.strategy.execute(enemy, map);
                    }
                    else{
                        setStrategy(new StallStrategy(this.mainFrame));
                        this.strategy.execute(enemy, map);
                    }
                }
                break;

            case TOOLBOT:
                if(hasTarget){
                    if(r < 0.70){
                        setStrategy(new AttackStrategy(this.mainFrame), usable);
                        this.strategy.execute(enemy, map);
                    }
                    else{
                        setStrategy(new StallStrategy(this.mainFrame));
                        this.strategy.execute(enemy, map);
                    }
                }
                else{
                    if(r < 0.85){
                        setStrategy(new ApproachStrategy(this.mainFrame));
                        this.strategy.execute(enemy, map);
                    }
                    else{
                        setStrategy(new StallStrategy(this.mainFrame));
                        this.strategy.execute(enemy, map);
                    }
                }
                break;

            case BOB:
                if(hasTarget && hasHealTarget){
                    if(r<0.50){
                        setStrategy(new SmartAttackStrategy(this.mainFrame), usable);
                        this.strategy.execute(enemy, map);
                    }
                    else{
                    setStrategy(new HealStrategy(this.mainFrame), healMove);
                    this.strategy.execute(enemy, map);
                    }
                }
                else if(hasTarget){
                    if(r< 0.9){
                        setStrategy(new SmartAttackStrategy(this.mainFrame), usable);
                        this.strategy.execute(enemy, map);
                    }
                    else{
                        setStrategy(new StallStrategy(this.mainFrame));
                        this.strategy.execute(enemy, map);
                    }
                }
                else if(hasHealTarget){
                    if(r<0.9){
                        setStrategy(new HealStrategy(this.mainFrame), healMove);
                        this.strategy.execute(enemy, map);
                    }
                    else{
                        setStrategy(new StallStrategy(this.mainFrame));
                        this.strategy.execute(enemy, map);
                    }
                }
                else{
                    if(r<0.8){
                        setStrategy(new ApproachStrategy(this.mainFrame));
                        this.strategy.execute(enemy, map);
                    }
                    else{
                        setStrategy(new StallStrategy(this.mainFrame));
                        this.strategy.execute(enemy, map);
                    }
                }
                break;
            case SAM:
                if(hasTarget){
                    if(r <0.95){
                        setStrategy(new SmartAttackStrategy(this.mainFrame), usable);
                        this.strategy.execute(enemy, map);
                    }
                    else{
                        setStrategy(new StallStrategy(this.mainFrame));
                        this.strategy.execute(enemy, map);
                    }
                }
                else if(!hasTarget && this.enemy.getActionPoints()> 5){
                    setStrategy(new StallStrategy(this.mainFrame));
                        this.strategy.execute(enemy, map);
                }
                else{
                    setStrategy(new ApproachStrategy(this.mainFrame));
                    this.strategy.execute(enemy, map);
                }
                break;

            case MOSQUITO:
                if(hasHealTarget){
                    if(r<0.9){
                        setStrategy(new HealStrategy(this.mainFrame), healMove);
                        this.strategy.execute(enemy, map);
                    }
                    else{
                        setStrategy(new StallStrategy(this.mainFrame));
                        this.strategy.execute(enemy, map);
                    }
                }
                else{
                    if(r<0.95){
                        setStrategy(new RetreatStrategy(this.mainFrame));
                        this.strategy.execute(enemy, map);
                    }
                    else{
                        setStrategy(new StallStrategy(this.mainFrame));
                        this.strategy.execute(enemy, map);
                    }
                }
                break;
            
            case ATTENDANT: // once per turn he just talks
                setStrategy(new StallStrategy(this.mainFrame));
                this.strategy.execute(enemy, map);
                break;

            default:
                map.getConcreteTurnHandler().next();
            }

        }
        
        private void setStrategy(DecisionStrategy strategy, Map<Move, Integer> usable){
            this.strategy = strategy;
        if(!usable.isEmpty()){
                Map<Move, Integer> sortedUsable = usable.entrySet().stream().sorted((a,b) -> {
                    return (a.getKey().getPower() * a.getValue()) - (b.getKey().getPower() * b.getValue()); 
                }).
                collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (a,b) -> a,
                    LinkedHashMap::new
                ));
            this.strategy.setMoves(sortedUsable);
        }
    }

    private void setStrategy(DecisionStrategy strategy){
        this.strategy = strategy;
    }

    public void setStrategy(DecisionStrategy strategy, Move healMove){
        this.strategy = strategy;
        if(healMove != null){
            this.strategy.setHealMove(healMove);
        }
        
    }

    public void setHasFinished(){
        this.hasFinished = true;
    }

    public boolean getHasFinished(){
        return this.hasFinished;
    }

    public Enemy getEnemy(){
        return this.enemy;
    }
}
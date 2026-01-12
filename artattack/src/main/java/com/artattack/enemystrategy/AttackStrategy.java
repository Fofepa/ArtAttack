package com.artattack.enemystrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.artattack.level.Maps;
import com.artattack.mapelements.Enemy;
import com.artattack.moves.Move;
import com.artattack.view.MainFrame;

public class AttackStrategy extends DecisionStrategy {

    public AttackStrategy(MainFrame mainFrame){
        super(mainFrame);
    }


    @Override
    public void execute(Enemy enemy, Maps map){
        Move move = chooseUniform(this.getMoves());

         int damage = enemy.getWeapons().get(0).getMoves().get(enemy.getWeapons().get(0).getMoves().indexOf(move)).useMove(enemy, map);
         this.getMainFrame().showDialog(List.of(enemy.getName() + " has done " + damage + " dmg to the player"));

    }

    private Move chooseUniform(Map<Move, Integer> moves){   // just selects a move accordingly to the uniform distribution
        if (moves.isEmpty()) return null;
        List<Move> moveList = new ArrayList<>(moves.keySet());
        return moveList.get((int)(Math.random() * (moveList.size() - 1)));
    }
}
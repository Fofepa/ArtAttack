package com.artattack.enemystrategy;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.artattack.level.Maps;
import com.artattack.mapelements.Enemy;
import com.artattack.moves.Move;
import com.artattack.view.MainFrame;

public abstract class DecisionStrategy {
    private MainFrame mainframe;
    private Map<Move,Integer> moves;
    private Move healMove;

    public DecisionStrategy(MainFrame mainframe){
        this.mainframe = mainframe;
    }

    public abstract  void execute(Enemy enemy,Maps map);

    public MainFrame getMainFrame(){
        return this.mainframe;
    }

    public void setMoves(Map<Move,Integer> moves){
        this.moves = moves; 
    }

    public void setHealMove(Move move){
        this.healMove = move;
    }

    public Move getHealMove(){
        return this.healMove;
    }

    public Map<Move, Integer> getMoves(){
        return  this.moves;
    }
}
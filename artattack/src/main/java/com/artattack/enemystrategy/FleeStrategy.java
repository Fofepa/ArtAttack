package com.artattack.enemystrategy;

import com.artattack.level.Coordinates;
import com.artattack.level.Maps;
import com.artattack.mapelements.Enemy;
import com.artattack.view.MainFrame;

public class FleeStrategy extends DecisionStrategy {

    public FleeStrategy(MainFrame mainFrame){
        super(mainFrame);
    }


    @Override
    public void execute(Enemy enemy, Maps map){
        double max = 0;
        Coordinates maxCoord = null;
        // finds the coordinate that gets the enemy furthest to the player (the closest) it is meant even to go to the closest player, we should create a new strategy named RetreatStrategy that let the enemy go to the closest ally
        if(Coordinates.getDistance(enemy.getCoordinates(), map.getPlayerOne().getCoordinates()) <= Coordinates.getDistance(enemy.getCoordinates(), map.getPlayerTwo().getCoordinates())){
            maxCoord = map.getPlayerOne().getCoordinates();
            for(Coordinates coord : enemy.getMoveArea()){
                if (max < Coordinates.getDistance(Coordinates.sum(enemy.getCoordinates(), coord), map.getPlayerOne().getCoordinates()) && !Coordinates.sum(coord, enemy.getCoordinates()).equals(map.getPlayerOne().getCoordinates())
                    && map.getCell(Coordinates.sum(enemy.getCoordinates(), coord)) == '.'
                    && Coordinates.sum(enemy.getCoordinates(), coord).getX() < map.getColumns() && Coordinates.sum(enemy.getCoordinates(), coord).getY() < map.getRows()
                    && Coordinates.sum(enemy.getCoordinates(), coord).getX() >= 0 && Coordinates.sum(enemy.getCoordinates(), coord).getY() >= 0){
                    max = Coordinates.getDistance(Coordinates.sum(coord, enemy.getCoordinates()), map.getPlayerOne().getCoordinates());
                    maxCoord = coord;
                }
            }
        }
        else{
            maxCoord = map.getPlayerTwo().getCoordinates();
            for(Coordinates coord : enemy.getMoveArea()){
                if (max < Coordinates.getDistance(Coordinates.sum(enemy.getCoordinates(), coord), map.getPlayerTwo().getCoordinates()) && !Coordinates.sum(coord, enemy.getCoordinates()).equals(map.getPlayerTwo().getCoordinates())
                    && map.getCell(Coordinates.sum(enemy.getCoordinates(), coord)) == '.' 
                    && Coordinates.sum(enemy.getCoordinates(), coord).getX() < map.getColumns() && Coordinates.sum(enemy.getCoordinates(), coord).getY() < map.getRows()
                    && Coordinates.sum(enemy.getCoordinates(), coord).getX() >= 0 && Coordinates.sum(enemy.getCoordinates(), coord).getY() >= 0){
                    max = Coordinates.getDistance(Coordinates.sum(coord, enemy.getCoordinates()), map.getPlayerTwo().getCoordinates());
                    maxCoord = coord;
                }
            }
        }

        // sets the new position and decreases the AP
        map.setCell(enemy.getCoordinates(), '.');
        map.updateDict(enemy.getCoordinates(), Coordinates.sum(enemy.getCoordinates(), maxCoord));
        enemy.setCoordinates(Coordinates.sum(enemy.getCoordinates(), maxCoord));
        enemy.setActionPoints(enemy.getActionPoints()-1);
        map.setCell(enemy.getCoordinates(),enemy.getMapSymbol());
    }
}
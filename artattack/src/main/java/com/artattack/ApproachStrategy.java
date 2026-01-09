package com.artattack;

import com.artattack.view.MainFrame;

public class ApproachStrategy extends DecisionStrategy {

    public ApproachStrategy(MainFrame mainFrame){
        super(mainFrame);
    }

    @Override
    public void execute(Enemy enemy, Maps map){
        double min = 500;
        Coordinates minCoord = new Coordinates(500, 500);
        // finds the coordinate that gets the enemy closest to the player (the closest)
        if(Coordinates.getDistance(enemy.getCoordinates(), map.getPlayerOne().getCoordinates()) <= Coordinates.getDistance(enemy.getCoordinates(), map.getPlayerTwo().getCoordinates())){
            for(Coordinates coord : enemy.getMoveArea()){
                if (min > Coordinates.getDistance(Coordinates.sum(enemy.getCoordinates(), coord), map.getPlayerOne().getCoordinates()) && !Coordinates.sum(coord, enemy.getCoordinates()).equals(map.getPlayerOne().getCoordinates())
                    && map.getCell(Coordinates.sum(enemy.getCoordinates(), coord)) == '.'){
                    min = Coordinates.getDistance(Coordinates.sum(coord, enemy.getCoordinates()), map.getPlayerOne().getCoordinates());
                    minCoord = coord;
                }
            }
        }
        else{
            for(Coordinates coord : enemy.getMoveArea()){
                if (min > Coordinates.getDistance(Coordinates.sum(enemy.getCoordinates(), coord), map.getPlayerTwo().getCoordinates()) && !Coordinates.sum(coord, enemy.getCoordinates()).equals(map.getPlayerTwo().getCoordinates())
                    && map.getCell(Coordinates.sum(enemy.getCoordinates(), coord)) == '.'){
                    min = Coordinates.getDistance(Coordinates.sum(coord, enemy.getCoordinates()), map.getPlayerTwo().getCoordinates());
                    minCoord = coord;
                }
            }
        }

        // sets the new position and decreases the AP
        enemy.setCoordinates(Coordinates.sum(enemy.getCoordinates(), minCoord));
        enemy.setActionPoints(enemy.getActionPoints()-3);
        map.setCell(enemy.getCoordinates(),enemy.getMapSymbol());
    }
}
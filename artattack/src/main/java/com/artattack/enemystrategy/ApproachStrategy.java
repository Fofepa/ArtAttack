package com.artattack.enemystrategy;

import java.util.List;

import com.artattack.level.Coordinates;
import com.artattack.level.Maps;
import com.artattack.mapelements.Enemy;
import com.artattack.view.MainFrame;

public class ApproachStrategy extends DecisionStrategy {

    public ApproachStrategy(MainFrame mainFrame){
        super(mainFrame);
    }

    @Override
    public void execute(Enemy enemy, Maps map){
        double min = Double.MAX_VALUE;
        Coordinates minCoord = null;
        
        if(Coordinates.getDistance(enemy.getCoordinates(), map.getPlayerOne().getCoordinates()) <= Coordinates.getDistance(enemy.getCoordinates(), map.getPlayerTwo().getCoordinates()) 
                                    &&  map.getPlayerOne().isAlive()){
            for(Coordinates coord : enemy.getMoveArea()){
                if (min > Coordinates.getDistance(Coordinates.sum(enemy.getCoordinates(), coord), map.getPlayerOne().getCoordinates()) && !Coordinates.sum(coord, enemy.getCoordinates()).equals(map.getPlayerOne().getCoordinates())
                    && map.getCell(Coordinates.sum(enemy.getCoordinates(), coord)) == '.'
                    && Coordinates.sum(enemy.getCoordinates(), coord).getX() < map.getWidth() && Coordinates.sum(enemy.getCoordinates(), coord).getY() < map.getHeight()
                    && Coordinates.sum(enemy.getCoordinates(), coord).getX() >= 0 && Coordinates.sum(enemy.getCoordinates(), coord).getY() >= 0){
                    min = Coordinates.getDistance(Coordinates.sum(coord, enemy.getCoordinates()), map.getPlayerOne().getCoordinates());
                    minCoord = coord;
                }
            }
        }
        else if(map.getPlayerTwo().isAlive()){
            for(Coordinates coord : enemy.getMoveArea()){
                if (min > Coordinates.getDistance(Coordinates.sum(enemy.getCoordinates(), coord), map.getPlayerTwo().getCoordinates()) && !Coordinates.sum(coord, enemy.getCoordinates()).equals(map.getPlayerTwo().getCoordinates())
                    && map.getCell(Coordinates.sum(enemy.getCoordinates(), coord)) == '.'
                    && Coordinates.sum(enemy.getCoordinates(), coord).getX() < map.getWidth() && Coordinates.sum(enemy.getCoordinates(), coord).getY() < map.getHeight()
                    && Coordinates.sum(enemy.getCoordinates(), coord).getX() >= 0 && Coordinates.sum(enemy.getCoordinates(), coord).getY() >= 0){
                    min = Coordinates.getDistance(Coordinates.sum(coord, enemy.getCoordinates()), map.getPlayerTwo().getCoordinates());
                    minCoord = coord;
                }
            }
        }

        if (minCoord != null){
            this.getMainFrame().showDialog(List.of(enemy.getName() + " is approaching!"));
            map.setCell(enemy.getCoordinates(), '.');
            map.updateDict(enemy.getCoordinates(), Coordinates.sum(enemy.getCoordinates(), minCoord));
            enemy.setCoordinates(Coordinates.sum(enemy.getCoordinates(), minCoord));
            enemy.setActionPoints(enemy.getActionPoints()-1);
            map.setCell(enemy.getCoordinates(),enemy.getMapSymbol());
        }
        else{
            System.out.println("Cannot find  a valid move");
        }
    }
}
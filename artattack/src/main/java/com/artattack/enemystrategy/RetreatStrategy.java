package com.artattack.enemystrategy;

import java.util.List;

import com.artattack.level.Coordinates;
import com.artattack.level.Maps;
import com.artattack.mapelements.Enemy;
import com.artattack.view.MainFrame;

public class RetreatStrategy extends DecisionStrategy {
    public RetreatStrategy(MainFrame mainFrame){
        super(mainFrame);
    }

    @Override
    public void execute(Enemy enemy, Maps map){
        double min = Double.MAX_VALUE;
        Coordinates minCoord = null;
        Enemy e = null;
        for(Enemy en : map.getEnemies()){ 
            if((e == null || Coordinates.getDistance(enemy.getCoordinates(), en.getCoordinates()) < Coordinates.getDistance(enemy.getCoordinates(), e.getCoordinates())) 
                          && !en.equals(enemy)){
                e = en;
            }
        }

        // finds the coordinate that gets enemy as close as possible to e
        for(Coordinates coord : enemy.getMoveArea()){
            if (min > Coordinates.getDistance(Coordinates.sum(enemy.getCoordinates(), coord), e.getCoordinates()) && !Coordinates.sum(coord, enemy.getCoordinates()).equals(e.getCoordinates())
                    && map.getCell(Coordinates.sum(enemy.getCoordinates(), coord)) == '\u25EA'
                    && Coordinates.sum(enemy.getCoordinates(), coord).getX() < map.getWidth() && Coordinates.sum(enemy.getCoordinates(), coord).getY() < map.getHeight()
                    && Coordinates.sum(enemy.getCoordinates(), coord).getX() >= 0 && Coordinates.sum(enemy.getCoordinates(), coord).getY() >= 0){
                    min = Coordinates.getDistance(Coordinates.sum(coord, enemy.getCoordinates()), e.getCoordinates());
                    minCoord = coord;
            }
        }

        // sets the new position and decreases the AP
        if (minCoord != null){
            this.getMainFrame().showDialog(List.of(enemy.getName() + " is retreating!"));
            map.setCell(enemy.getCoordinates(), '\u25EA');
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

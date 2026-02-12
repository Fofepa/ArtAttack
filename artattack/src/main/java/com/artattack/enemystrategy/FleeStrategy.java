package com.artattack.enemystrategy;

import java.util.List;

import com.artattack.level.Coordinates;
import com.artattack.level.Maps;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.Player;
import com.artattack.view.MainFrame;

public class FleeStrategy extends DecisionStrategy {

    public FleeStrategy(MainFrame mainFrame){
        super(mainFrame);
    }


    @Override
    public void execute(Enemy enemy, Maps map){
        double max = 0;
        Coordinates maxCoord = null;
        Player targetPlayer = null;
        
        // Determine which player to approach
        Player p1 = map.getPlayerOne();
        Player p2 = map.getPlayerTwo();
        
        // Check if players exist and are alive
        boolean p1Valid = p1 != null && p1.isAlive();
        boolean p2Valid = p2 != null && p2.isAlive();
        
        if(p1Valid && p2Valid){
            // Both alive - choose closer one
            if(Coordinates.getDistance(enemy.getCoordinates(), p1.getCoordinates()) 
                <= Coordinates.getDistance(enemy.getCoordinates(), p2.getCoordinates())){
                targetPlayer = p1;
            } else {
                targetPlayer = p2;
            }
        }
        else if(p1Valid){
            targetPlayer = p1;
        }
        else if(p2Valid){
            targetPlayer = p2;
        }
        else{
            // No valid target
            System.out.println("No alive players to approach");
            return;
        }
    
        // Find best move toward target player
        for(Coordinates coord : enemy.getMoveArea()){
            Coordinates newPos = Coordinates.sum(enemy.getCoordinates(), coord);
            
            if (max < Coordinates.getDistance(newPos, targetPlayer.getCoordinates()) 
                && !newPos.equals(targetPlayer.getCoordinates())
                && map.getCell(newPos) == '.'
                && newPos.getX() < map.getWidth() && newPos.getY() < map.getHeight()
                && newPos.getX() >= 0 && newPos.getY() >= 0){
                
                max = Coordinates.getDistance(newPos, targetPlayer.getCoordinates());
                maxCoord = coord;
            }
        }

        if (maxCoord != null){
            this.getMainFrame().showDialog(List.of(enemy.getName() + " is approaching!"));
            map.setCell(enemy.getCoordinates(), '.');
            map.updateDict(enemy.getCoordinates(), Coordinates.sum(enemy.getCoordinates(), maxCoord));
            enemy.setCoordinates(Coordinates.sum(enemy.getCoordinates(), maxCoord));
            enemy.setActionPoints(enemy.getActionPoints()-1);
            map.setCell(enemy.getCoordinates(), enemy.getMapSymbol());
        }
        else{
            System.out.println("Cannot find a valid move");
        }
    }
}
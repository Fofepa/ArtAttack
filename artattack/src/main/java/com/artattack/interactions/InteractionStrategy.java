package com.artattack.interactions;

import com.artattack.inputcontroller.MovementStrategy;
import com.artattack.level.Coordinates;
import com.artattack.mapelements.InteractableElement;
import com.artattack.mapelements.MapElement;

public class InteractionStrategy {
    private MovementStrategy movementStrategy;
    
    public InteractionStrategy(MovementStrategy movementStrategy){
        this.movementStrategy = movementStrategy;
    } 


    public void acceptInteraction(){
        Coordinates cursor = this.movementStrategy.getCursor();

        if(Math.abs(cursor.getX() - this.movementStrategy.getPlayer().getCoordinates().getX()) <= 1 &&
            Math.abs(cursor.getY() - this.movementStrategy.getPlayer().getCoordinates().getY()) <= 1){
                MapElement element = this.movementStrategy.getMap().getDict().get(cursor);
                    if(element instanceof InteractableElement npc)
                        npc.interact(this.movementStrategy.getMainFrame().getGameContext(), this.movementStrategy.getPlayer(), npc.getSpritePath());
            movementStrategy.setIsSelected(false);        
        }
    }
}
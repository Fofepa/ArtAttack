package com.artattack;

public class InteractionStrategy {
    private MovementStrategy movementStrategy;
    private Maps map;
    private Player player;
    
    public InteractionStrategy(MovementStrategy movementStrategy){
        this.movementStrategy = movementStrategy;
        this.map = this.movementStrategy.getMap();
        this.player = this.movementStrategy.getPlayer();
    } 


    public void acceptInteraction(){
        Coordinates cursor = this.movementStrategy.getCursor();
        player = movementStrategy.getPlayer();


        if(Math.abs(cursor.getX() - this.player.getCoordinates().getX()) <= 1 &&
            Math.abs(cursor.getY() - this.player.getCoordinates().getY()) <= 1){
                MapElement element = this.map.getDict().get(cursor);
                    if(element instanceof InteractableElement npc)
                        npc.interact(this.player);
            movementStrategy.setIsSelected(false);        
        }
    }
}

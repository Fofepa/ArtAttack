package com.artattack;

public class InteractionStrategy {
    private Maps map;
    private Player player;
    private Coordinates cursor;
    private MovementStrategy movementStrategy;
    
    public InteractionStrategy(MovementStrategy movementStrategy){
        this.map = this.movementStrategy.getMap();
        this.player = this.movementStrategy.getPlayer();
        this.cursor = this.movementStrategy.getCursor();
    } 


    public void acceptInteraction(){
        if(Math.abs(this.cursor.getX() - player.getCoordinates().getX()) <= 1 &&
            Math.abs(this.cursor.getY() - player.getCoordinates().getY()) <= 1){
            if(this.map.getMapMatrix()[cursor.getY()][cursor.getX()] != '#' &&
                this.map.getMapMatrix()[cursor.getY()][cursor.getX()] != 'E' &&
                this.map.getMapMatrix()[cursor.getY()][cursor.getX()] != '$' &&
                this.map.getMapMatrix()[cursor.getY()][cursor.getX()] != '@' &&
                this.map.getMapMatrix()[cursor.getY()][cursor.getX()] != '.'){
                MapElement element = this.map.getDict().get(cursor);
                    if(element instanceof InteractableElement npc)
                        npc.interact(this.player);
            }
            movementStrategy.setIsSelected(false);        
        }
    }
}

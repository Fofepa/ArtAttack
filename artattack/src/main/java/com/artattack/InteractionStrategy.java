package com.artattack;

public class InteractionStrategy {
    private Maps map;
    private Player player;
    private Coordinates cursor;
    private MovementStrategy movementStrategy;
    
    public InteractionStrategy(Maps map,Player player, MovementStrategy movementStrategy){
        this.map = map;
        this.player = player;
        this.cursor = this.movementStrategy.getCursor();
    } 


    public void acceptInteraction(){
        if(this.map.getMapMatrix()[cursor.getY()][cursor.getX()] != '#' &&
            this.map.getMapMatrix()[cursor.getY()][cursor.getX()] != 'E' &&
            this.map.getMapMatrix()[cursor.getY()][cursor.getX()] != '$' &&
            this.map.getMapMatrix()[cursor.getY()][cursor.getX()] != '@' &&
            this.map.getMapMatrix()[cursor.getY()][cursor.getX()] != '.'){
            MapElement element = this.map.getDict().get(cursor);
            if(element instanceof InteractableElement){
                InteractableElement npc = (InteractableElement) element;
                npc.interact(this.player);
            }       
        }
        movementStrategy.setIsSelected(false);        
    }
}

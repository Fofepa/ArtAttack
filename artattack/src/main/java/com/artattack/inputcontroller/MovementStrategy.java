package com.artattack.inputcontroller;

import com.artattack.level.Coordinates;
import com.artattack.level.Maps;
import com.artattack.mapelements.Player;


// TODO: Make the cursor appear to the side of the player and when it goes inside it go directly on the next character.
public class MovementStrategy implements PlayerStrategy{
    private Maps map;
    private Player player;
    private Coordinates cursor;
    private boolean isSelected = false;
    
     public MovementStrategy(Maps map,Player player){
        this.map = map;
        setPlayer(player);
    }

    public MovementStrategy (Maps map){
        this.map =map;
    }

    @Override
    public void execute(int dx, int dy){
        if (dx != 0 || dy != 0) {
            if (!isSelected)
                isSelected = true;
            moveCursor(dx, dy); 
        }
    }



    private void moveCursor(int dx, int dy){
        Coordinates new_c = Coordinates.sum(cursor, new Coordinates(dx,dy));

        if (new_c.getX() >= 0 && new_c.getX() < this.map.getColumns() &&
            new_c.getY() >= 0 && new_c.getY() < this.map.getRows() &&
            Coordinates.sum(player.getMoveArea(), player.getCoordinates()).contains(new_c)){
            cursor = new_c;
        }
    }

    public void acceptMovement() {
        if (this.map.getMapMatrix()[cursor.getY()][cursor.getX()] == '.'){  // it's easier to check if it is a walkable character (Maybe int the future we can change the character)
            this.map.getMapMatrix()[player.getCoordinates().getY()][player.getCoordinates().getX()] = '.';
            player.setCoordinates(cursor);
            player.setActionPoints(player.getActionPoints() - 1);
            this.map.getMapMatrix()[player.getCoordinates().getY()][player.getCoordinates().getX()] = '@';
        }
        if(map.checkAggro(cursor) != null && !map.getConcreteTurnHandler().getConcreteTurnQueue().getTurnQueue().contains(map.checkAggro(cursor))){
            map.getConcreteTurnHandler().getConcreteTurnQueue().add(map.checkAggro(cursor));
        }
        isSelected = false;
    }

    public Coordinates getCursor(){
        return this.cursor;
    }

    public boolean getSelectedState() {
        return this.isSelected;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Maps getMap() {
        return this.map;
    }

    public void setIsSelected(boolean isSelected){
        this.isSelected = isSelected;
    }

    public void setMap(Maps map) {
        this.map = map;
    }

    public final void setPlayer(Player player) {
        this.player = player;
        this.cursor = Coordinates.sum(player.getCoordinates(), new Coordinates(0,1)); 
    }

    @Override
    public int getType(){
        return 0;
    }


}
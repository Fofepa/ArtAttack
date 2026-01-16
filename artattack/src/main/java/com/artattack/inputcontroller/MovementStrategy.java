package com.artattack.inputcontroller;

import com.artattack.level.Coordinates;
import com.artattack.level.Maps;
import com.artattack.mapelements.Player;
import com.artattack.mapelements.Trigger;
import com.artattack.view.MainFrame;

public class MovementStrategy implements PlayerStrategy{
    private Maps map;
    private Player player;
    private Coordinates cursor;
    private boolean isSelected = false;
    private MainFrame mainFrame; 
    
    public MovementStrategy(Maps map, Player player){
        this.map = map;
        setPlayer(player);
    }

    public MovementStrategy(Maps map){
        this.map = map;
    }
    
    /**
     * Sets the MainFrame reference for UI updates
     */
    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        // Initialize cursor display
        updateCursorDisplay();
    }

    @Override
    public void execute(int dx, int dy){
        if (dx != 0 || dy != 0) {
            if (!isSelected)
                isSelected = true;
            moveCursor(dx, dy); 
            updateCursorDisplay(); // Update cursor display after movement
        }
    }

    private void moveCursor(int dx, int dy){
        Coordinates new_c = Coordinates.sum(cursor, new Coordinates(dx, dy));

        // checks if the cursor gets on top of the player and shifts it
        if(Coordinates.getDistance(new_c, player.getCoordinates()) == 0){
            if (dx != 0){
                new_c = Coordinates.sum(new_c, new Coordinates(dx, 0));
            } else {
                new_c = Coordinates.sum(new_c, new Coordinates(0, dy));   
            }
        }

        if (new_c.getX() >= 0 && new_c.getX() < this.map.getWidth() &&
            new_c.getY() >= 0 && new_c.getY() < this.map.getHeight() &&
            Coordinates.sum(player.getMoveArea(), player.getCoordinates()).contains(new_c)){
            
            cursor = new_c;
        }
    }

    public void acceptMovement() {
        if (this.map.getMapMatrix()[cursor.getX()][cursor.getY()] == '.' || this.map.getMapMatrix()[cursor.getX()][cursor.getY()] == 't'){
            this.map.getMapMatrix()[player.getCoordinates().getX()][player.getCoordinates().getY()] = '.';
            player.setCoordinates(cursor);
            player.setActionPoints(player.getActionPoints() - 1);
            this.map.getMapMatrix()[player.getCoordinates().getX()][player.getCoordinates().getY()] = '@';
            if (map.getDict().get(player.getCoordinates()) instanceof Trigger t) {
                t.OnTrigger(this.player);
            }
        }
        if(map.checkAggro(cursor) != null && !map.getConcreteTurnHandler().getConcreteTurnQueue().getTurnQueue().contains(map.checkAggro(cursor))){
            map.getConcreteTurnHandler().getConcreteTurnQueue().add(map.checkAggro(cursor));
        }
        if(player.getActionPoints() == 0){
            this.mainFrame.getMap().getConcreteTurnHandler().next();
        }
        isSelected = false;
        clearCursorDisplay(); // Clear cursor after movement

    }

    /**
     * Updates the cursor display in the MapPanel
     */
    private void updateCursorDisplay() {
        if (mainFrame != null && cursor != null) {
            mainFrame.updateMovementCursor(cursor);
        }
    }
    
    /**
     * Clears the cursor display in the MapPanel
     */
    private void clearCursorDisplay() {
        if (mainFrame != null) {
            mainFrame.clearMovementCursor();
        }
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
        if (isSelected) {
            updateCursorDisplay();
        } else {
            clearCursorDisplay();
        }
    }

    public void setMap(Maps map) {
        this.map = map;
    }

    public final void setPlayer(Player player) {
        this.player = player;
        this.cursor = Coordinates.sum(player.getCoordinates(), new Coordinates(0, 1)); 
    }

    @Override
    public int getType(){
        return 0;
    }
}
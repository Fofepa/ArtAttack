package com.artattack.inputcontroller;

import java.util.List;

import com.artattack.level.Coordinates;
import com.artattack.level.Maps;
import com.artattack.mapelements.Enemy;
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
    
    
    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        if(isSelected) updateCursorDisplay();
    }

    @Override
    public void execute(int dx, int dy){
        if (dx != 0 || dy != 0) {
            if (!isSelected) {
                setIsSelected(true);
            }
            moveCursor(dx, dy); 
            updateCursorDisplay(); 
        }
    }

    private void moveCursor(int dx, int dy){
        Coordinates new_c = Coordinates.sum(cursor, new Coordinates(dx, dy));

        if(Coordinates.getDistance(new_c, player.getCoordinates()) == 0){
            if (dx != 0){
                new_c = Coordinates.sum(new_c, new Coordinates(dx, 0));
            } else {
                new_c = Coordinates.sum(new_c, new Coordinates(0, dy));   
            }
        }
        
        if(!Coordinates.sum(player.getMoveArea(), player.getCoordinates()).contains(new_c)){
            if(dx == 1){// case S
                if(Coordinates.sum(player.getMoveArea(), player.getCoordinates()).contains(Coordinates.sum(cursor, new Coordinates(2,0)))){
                    new_c = Coordinates.sum(new_c, new Coordinates(1,0));
                }
                else if(Coordinates.sum(player.getMoveArea(), player.getCoordinates()).contains(Coordinates.sum(cursor, new Coordinates(1,-1)))){
                    new_c = Coordinates.sum(new_c, new Coordinates(0,-1));
                }
                else if(Coordinates.sum(player.getMoveArea(), player.getCoordinates()).contains(Coordinates.sum(cursor, new Coordinates(1,-1)))){
                    new_c = Coordinates.sum(new_c, new Coordinates(0,1));
                }
            }
            if(dx == -1){// case W
                if(Coordinates.sum(player.getMoveArea(), player.getCoordinates()).contains(Coordinates.sum(cursor, new Coordinates(-2,0)))){
                    new_c = Coordinates.sum(new_c, new Coordinates(-1,0));
                }
                else if(Coordinates.sum(player.getMoveArea(), player.getCoordinates()).contains(Coordinates.sum(cursor, new Coordinates(-1,1)))){
                    new_c = Coordinates.sum(new_c, new Coordinates(0,1));
                }
                else if(Coordinates.sum(player.getMoveArea(), player.getCoordinates()).contains(Coordinates.sum(cursor, new Coordinates(-1,-1)))){
                    new_c = Coordinates.sum(new_c, new Coordinates(0,-1));
                }
            }
            if(dy == 1){// case D
                if(Coordinates.sum(player.getMoveArea(), player.getCoordinates()).contains(Coordinates.sum(cursor, new Coordinates(0,2)))){
                    new_c = Coordinates.sum(new_c, new Coordinates(0,1));
                }
                else if(Coordinates.sum(player.getMoveArea(), player.getCoordinates()).contains(Coordinates.sum(cursor, new Coordinates(-1,1)))){
                    new_c = Coordinates.sum(new_c, new Coordinates(-1,0));
                }
                else if(Coordinates.sum(player.getMoveArea(), player.getCoordinates()).contains(Coordinates.sum(cursor, new Coordinates(1,1)))){
                    new_c = Coordinates.sum(new_c, new Coordinates(1,0));
                }
            }
            if(dy == -1){// case A
                if(Coordinates.sum(player.getMoveArea(), player.getCoordinates()).contains(Coordinates.sum(cursor, new Coordinates(0,-2)))){
                    new_c = Coordinates.sum(new_c, new Coordinates(0,-1));
                }
                else if(Coordinates.sum(player.getMoveArea(), player.getCoordinates()).contains(Coordinates.sum(cursor, new Coordinates(1,-1)))){
                    new_c = Coordinates.sum(new_c, new Coordinates(1,0));
                }
                else if(Coordinates.sum(player.getMoveArea(), player.getCoordinates()).contains(Coordinates.sum(cursor, new Coordinates(-1,-1)))){
                    new_c = Coordinates.sum(new_c, new Coordinates(-1,0));
                }
            }
        }
        if (new_c.getX() >= 0 && new_c.getX() < this.map.getWidth() &&
        new_c.getY() >= 0 && new_c.getY() < this.map.getHeight() &&
        Coordinates.sum(player.getMoveArea(), player.getCoordinates()).contains(new_c)){
            
            cursor = new_c;
        }
    }

    public void acceptMovement() {
        if (this.map.getMapMatrix()[cursor.getX()][cursor.getY()] == '.'){
            this.map.getMapMatrix()[player.getCoordinates().getX()][player.getCoordinates().getY()] = '.';
            if (map.getDict().get(this.cursor) instanceof Trigger t) {
                t.OnTrigger(this.mainFrame.getGameContext(), this.player, t.getSpritePath());
            }
            this.map.updateDict(player.getCoordinates(), this.cursor);
            player.setCoordinates(this.cursor);
            player.setActionPoints(player.getActionPoints() - 1);
        }
        if(map.checkAggro(this.cursor) != null && !map.checkAggro(this.cursor).isEmpty()){
            for (Enemy e : map.checkAggro(this.cursor)) {
                if (!map.getConcreteTurnHandler().getConcreteTurnQueue().getTurnQueue().contains(e)) {
                    System.out.println("Adding " + e.getName() +  " to the queue");
                    
                    Player currentPlayer = this.player;
                    
                    int currSpeed = player.getSpeed();
                    this.player.setSpeed(200);
                    map.getConcreteTurnHandler().getConcreteTurnQueue().add(e);
                    this.player.setSpeed(currSpeed);
                    
                    map.getConcreteTurnHandler().updateIndexForElement(currentPlayer);
                    
                    this.mainFrame.repaintTurnOrderPanel();
                    e.activate();
                }
            }
        }
        map.checkPlayerEscape();
        if(player.getActionPoints() == 0){
            this.mainFrame.getMap().getConcreteTurnHandler().next();
        }
        setIsSelected(false);

    }

    
    private void updateCursorDisplay() {
        if (mainFrame != null && cursor != null) {
            mainFrame.updateMovementCursor(cursor);
        }
    }
    
    
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

    public MainFrame getMainFrame(){
        return this.mainFrame;
    }

    public void setIsSelected(boolean isSelected){
        this.isSelected = isSelected;
        
        if (isSelected) {
            updateCursorDisplay();
            
            if (mainFrame != null && mainFrame.getMapPanel() != null && player != null) {
                List<Coordinates> absoluteArea = Coordinates.sum(player.getMoveArea(), player.getCoordinates());
                mainFrame.getMapPanel().showMoveArea(absoluteArea, null);
            }
            
        } else {
            clearCursorDisplay();
            
            if (mainFrame != null && mainFrame.getMapPanel() != null) {
                mainFrame.getMapPanel().showMoveArea(null, null);
            }
        }
    }

    public void setMap(Maps map) {
        this.map = map;
    }

    public final void setPlayer(Player player) {
        this.player = player;
        this.cursor = Coordinates.sum(player.getCoordinates(), new Coordinates(1, 1)); 
    }

    @Override
    public int getType(){
        return 0;
    }
}
package com.artattack;

public class MovementStrategy {
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

    public void execute(int dx, int dy){
        if (dx != 0 || dy != 0) {
            if (!isSelected)
                isSelected = true;
            moveCursor(dx, dy); 
        }
    }



    private void moveCursor(int dx, int dy){
        int x = cursor.getX();
        int y = cursor.getY();
        Coordinates new_c = new Coordinates(dx + x,dy + y);

        if (Math.abs(new_c.getX() - player.getCoordinates().getX()) <= 1 &&
            Math.abs(new_c.getY() - player.getCoordinates().getY()) <= 1 &&
            new_c.getX() >= 0 && new_c.getX() < this.map.getColumns() &&
            new_c.getY() >= 0 && new_c.getY() < this.map.getRows() /*&&
            player.getActionArea().contains(new_c)*/){
            cursor = new_c;
        }
    }

    public void acceptMovement() {
        if (this.map.getMapMatrix()[cursor.getY()][cursor.getX()] != '#' &&
            this.map.getMapMatrix()[cursor.getY()][cursor.getX()] != 'E' &&
            this.map.getMapMatrix()[cursor.getY()][cursor.getX()] != '$'){
            this.map.getMapMatrix()[player.getCoordinates().getY()][player.getCoordinates().getX()] = '.';
            player.setCoordinates(cursor);
            this.map.getMapMatrix()[player.getCoordinates().getY()][player.getCoordinates().getX()] = '@';
        }
        if(map.checkAggro(cursor) != null){
            map.getConcreteTurnQueue().add(map.checkAggro(cursor));
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
        this.cursor = new Coordinates(player.getCoordinates().getX(), player.getCoordinates().getY());
    }


}
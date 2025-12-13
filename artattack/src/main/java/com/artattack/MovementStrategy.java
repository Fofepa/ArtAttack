package com.artattack;

public class MovementStrategy {
    private Maps map;
    private Player player;
    private Coordinates cursore;
    private boolean modalitaSelezione = false;
    
     public MovementStrategy(Maps map,Player player){
        this.map = map;
        this.player = player;
        this.cursore = player.getCoordinates();
    } 

    public void execute(int dx, int dy, boolean conferma){
        if (dx != 0 || dy != 0) {
            if (!modalitaSelezione)
                modalitaSelezione = true;
            movePlayer(dx, dy); 
        }
    }



    private void movePlayer(int dx, int dy){
        int x = cursore.getX();
        int y = cursore.getY();
        Coordinates new_c = new Coordinates(dx + x,dy + y);

        if (Math.abs(new_c.getX() - player.getCoordinates().getX()) <= 1 &&
            Math.abs(new_c.getY() - player.getCoordinates().getY()) <= 1 &&
            new_c.getX() >= 0 && new_c.getX() < this.map.getcolumns() &&
            new_c.getY() >= 0 && new_c.getY() < this.map.getRows() &&
            player.getActionArea().contains(new_c)){
            cursore = new_c;
        }
    }

    public void confermaMovimento() {
        if (this.map.getMapMatrix()[cursore.getY()][cursore.getX()] != '#') {
            this.map.getMapMatrix()[player.getCoordinates().getY()][player.getCoordinates().getX()] = '.';
            player.setCoordinates(cursore);
            this.map.getMapMatrix()[player.getCoordinates().getY()][player.getCoordinates().getX()] = '@';
        }
        modalitaSelezione = false;
    }

    public Coordinates getCursore(){
        return this.cursore;
    }

    public boolean isModalitaSelezione() {
        return modalitaSelezione;
    }
}
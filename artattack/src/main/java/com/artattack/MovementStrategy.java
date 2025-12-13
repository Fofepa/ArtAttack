package com.artattack;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

public class MovementStrategy {
    private Maps map;
    private Player player;
    private Coordinates currPosition;
    
     public MovementStrategy(Maps map,Player player){
        this.map = map;
        this.player = player;
        this.currPosition = player.getCoordinates();
    } 

    public void execute(KeyStroke key){

        if (key.getKeyType() == KeyType.Character){
            switch (key.getCharacter()) {
                case 'w':
                    movePlayer(-1 , 0);
                    break;
                case 'a':
                    movePlayer(0 , -1);
                    break;
                case 's':
                    movePlayer(1, 0);
                    break;
                case 'd':
                    movePlayer(0 , 1);
                    break;
            }
        }
        else if(key.getKeyType() == KeyType.Enter){
            this.player.setCoordinates(currPosition);
            
        }
        else 
            System.out.println("Ammazzati");
    }

    private void movePlayer(int dx, int dy){
        int x = currPosition.getX();
        int y = currPosition.getY();
        Coordinates c = new Coordinates(dx + x,dy + y);

        if (player.getActionArea().contains(c)){
            currPosition = c;
        }
    }
}
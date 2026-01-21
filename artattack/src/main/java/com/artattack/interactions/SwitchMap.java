package com.artattack.interactions;

import java.util.LinkedList;
import java.util.List;

import com.artattack.level.Coordinates;
import com.artattack.level.MapBuilder;
import com.artattack.mapelements.ActiveElement;
import com.artattack.mapelements.Player;
import com.artattack.turns.ConcreteTurnHandler;
import com.artattack.turns.ConcreteTurnQueue;
import com.artattack.view.MainFrame;

public class SwitchMap extends Interaction {

    private List<String> dialog;
    private Player playerOne;
    private Player playerTwo;
    private MapBuilder builder;
    private List<Coordinates> nextCoordinateses;

    public SwitchMap(MainFrame mainFrame, List<String> dialog, Player playerOne, Player playerTwo,  MapBuilder builder, List<Coordinates> nextCoordinateses){
        super(mainFrame);
        this.dialog = dialog;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.builder = builder;
        this.nextCoordinateses = nextCoordinateses;
    }

    @Override
    public void doInteraction(Player player){
        if (getMainFrame() != null) {
            getMainFrame().showDialog(dialog);
        }

        setNextMap(player);

        getMainFrame().switchMap(this.builder.getResult());
    }

    public MapBuilder getBuilder(){
        return this.builder;
    }

    public List<String> getDialog(){
        return this.dialog;
    }

    private void setNextMap(Player player){
        List<ActiveElement> list = new LinkedList<ActiveElement>();
        list.add(player);
        player.setCoordinates(this.nextCoordinateses.get(0));
        if(player.equals(this.playerOne)){
            this.builder.setPlayerOne(player);
            this.playerTwo.setCoordinates(this.nextCoordinateses.get(1));
            this.builder.setPlayerTwo(this.playerTwo);
            list.add(this.playerTwo);
            this.builder.setTurnQueue(player, this.playerTwo);
        } else {
            this.builder.setPlayerTwo(player);
            this.playerOne.setCoordinates(this.nextCoordinateses.get(1));
            this.builder.setPlayerOne(this.playerOne);
            list.add(this.playerOne);
            this.builder.setTurnQueue(player, this.playerOne);
        }
        this.builder.setDict();
        this.builder.startMap(); 
    }
}

package com.artattack.interactions;

import java.util.LinkedList;
import java.util.List;

import com.artattack.level.Coordinates;
import com.artattack.level.Maps;
import com.artattack.mapelements.ActiveElement;
import com.artattack.mapelements.Player;
import com.artattack.turns.ConcreteTurnHandler;
import com.artattack.turns.ConcreteTurnQueue;
import com.artattack.view.MainFrame;

public class SwitchMap extends Interaction {

    private List<String> dialog;
    private Maps currentMap;
    private Maps nextMap;
    private List<Coordinates> nextCoordinateses;

    public SwitchMap(MainFrame mainFrame, List<String> dialog,Maps currentMap,  Maps nextMap, List<Coordinates> nextCoordinateses){
        super(mainFrame);
        this.dialog = dialog;
        this.currentMap = currentMap;
        this.nextMap = nextMap;
        this.nextCoordinateses = nextCoordinateses;
    }

    @Override
    public void doInteraction(Player player){
        if (getMainFrame() != null) {
            getMainFrame().showDialog(dialog);
        }

        setNextMap(player);

        this.currentMap.remove(this.currentMap.getPlayerOne());
        this.currentMap.remove(this.currentMap.getPlayerTwo());

        getMainFrame().switchMap(this.nextMap);
    }

    public Maps getNextMap(){
        return this.nextMap;
    }

    public List<String> getDialog(){
        return this.dialog;
    }

    private void setNextMap(Player player){
        List<ActiveElement>list = new LinkedList<ActiveElement>();
        list.add(player);
        player.setCoordinates(this.nextCoordinateses.get(0));
        if(player.equals(this.currentMap.getPlayerOne())){
            this.nextMap.setPlayerOne(player);
            this.currentMap.getPlayerTwo().setCoordinates(this.nextCoordinateses.get(1));
            this.nextMap.setPlayerTwo(this.currentMap.getPlayerTwo());
            list.add(this.currentMap.getPlayerTwo());
        } else {
            this.nextMap.setPlayerTwo(player);
            this.currentMap.getPlayerOne().setCoordinates(this.nextCoordinateses.get(1));
            this.nextMap.setPlayerOne(this.currentMap.getPlayerTwo());
            list.add(this.currentMap.getPlayerOne());
        }
        ConcreteTurnQueue turnQueue = new ConcreteTurnQueue(new LinkedList<ActiveElement>(list));
        this.nextMap.setTurnHandler((ConcreteTurnHandler) turnQueue.createTurnHandler()); 
    }
}

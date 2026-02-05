package com.artattack.inputcontroller;

import java.util.List;

import com.artattack.level.Maps;
import com.artattack.mapelements.Player;
import com.artattack.view.MainFrame;

public class InventoryStrategy implements PlayerStrategy {
    private Maps map;
    private Player player;
    private boolean isSelected = false;
    private int inventoryIndex = 0;
    //private int elementIndex = 0;
    private MainFrame mainFrame;

    public InventoryStrategy(Maps map, Player player){
        this.map = map;
        this.player = player;
    }

    @Override
    public void execute(int dx, int dy) {
        if(this.player.getInventory().size() != 0){
            moveInventoryIndex(dx);
        }
        else{   // checks if the inventory has something inside
            System.out.println("The inventory is empty");
            mainFrame.showDialog(List.of("the inventory is empty"));
        }
        /* if (dx >=0 && dx <= player.getInventory().size()-1 && dy == 0){ // selection of the item, dy must be 0                   // this is needed if a player can use the item on other entities
            moveInventoryIndex(dx);
        }
        else if (dx >=0 && dx <= player.getInventory().size()-1 && dy >= 1 && dy <= 2){ // selection of the element we want the item to interact
            movePlayerIndex(dy);
        } */
    }

    private void moveInventoryIndex(int index){
        this.inventoryIndex =  (index + this.inventoryIndex) % (player.getInventory().size());
    }

    /* private void movePlayerIndex(int index){
        this.inventoryIndex = (index-1) % 2;
    } */

    public int acceptItem(Player other){
        if (this.player.getInventory().size() != 0){
            int value = player.getInventory().get(inventoryIndex).use(player);
    
            if(value != 0){     // TODO: we have to check what kind of Item it was in order to print the correct message inside the InteractionPanel
                mainFrame.showDialog(List.of(player.getName() + " used " + player.getInventory().get(inventoryIndex).getName()));
                System.out.println("used the item " + player.getInventory().get(inventoryIndex).getName());
                player.getInventory().remove(inventoryIndex); // it HAS to be a mutable List.
                isSelected = false;
                inventoryIndex = 0;
            }
            else{
                mainFrame.showDialog(List.of(player.getInventory().get(inventoryIndex).getName() + " could not be used..."));
                isSelected = false;
                inventoryIndex = 0;
            } 
            return value;
        }
        System.out.println("The inventory is empty");
        mainFrame.showDialog(List.of("the inventory is empty"));
        return 0;
    }

    public void giveItem() {
        if (!this.player.getInventory().isEmpty()) {
            Player other = this.player.equals(map.getPlayerOne()) ? map.getPlayerTwo() : map.getPlayerOne();
            mainFrame.showDialog(List.of(player.getName() + " gave " + player.getInventory().get(this.inventoryIndex).getName() + " to " + other.getName() + "."));
            other.getInventory().add(player.getInventory().get(this.inventoryIndex));
            player.getInventory().remove(this.inventoryIndex);
            this.isSelected = false;
            this.inventoryIndex = 0;
        }
        else {
            System.out.println(this.player.getName() + "'s inventory is empty: can't give item");
            mainFrame.showDialog(List.of("Your inventory is empty!"));
        }
    }

    public void useItemOnOther() {
        if (!this.player.getInventory().isEmpty()) {
            Player other = this.player.equals(map.getPlayerOne()) ? map.getPlayerTwo() : map.getPlayerOne();
            mainFrame.showDialog(List.of(player.getName() + " used " + player.getInventory().get(this.inventoryIndex).getName() + " on " + other.getName() + "."));
            player.getInventory().get(this.inventoryIndex).use(other);
            player.getInventory().remove(this.inventoryIndex);
            this.isSelected = false;
            this.inventoryIndex = 0;
        }
        else {
            System.out.println(this.player.getName() + "'s inventory is empty: can't give item");
            mainFrame.showDialog(List.of("Your inventory is empty!"));
        }
    }

    @Override
    public int getType() {
        return 2;
    }

    public Maps getMap(){
        return this.map;
    }

    public Player getPlayer(){
        return this.player;
    }

    public boolean isSelected(){
        return this.isSelected;
    }

    public int getInventoryIndex(){
        return this.inventoryIndex;
    }

    public void setMainFrame(MainFrame mainFrame){
        this.mainFrame = mainFrame;
    }

    public void setCurrentPlayer(Player player){
        this.player = player;
    }
}

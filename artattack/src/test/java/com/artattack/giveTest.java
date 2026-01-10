package com.artattack;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import com.artattack.view.InteractionPanel;

public class giveTest {
    
    private Interaction interaction;
    private List<Item> inventory;
    private Item item;
    private Player player;

    @Before
    public void setUp(){
        inventory = new ArrayList<Item>();
        item = new Cure("","",0);
        player = new MovieDirector(0, ' ', "", new Coordinates(0, 0),
            null, 5,5, null, 0, 0, 0, 0, 0, 0, 0, inventory,null,null);
        interaction = new GiveFactory(new InteractionPanel(),List.of(" "), item).createInteraction();
    }

    @Test
    public void dointeractionTest(){
        interaction.doInteraction(player);

        assertTrue("Give dointeractionTest faild. Item not in Player's inventory.",
                    player.getInventory().contains(item));
        assertEquals("Give diinteractionTest faild. Number of items in Player's inventory not as expected.",
                    1,
                    player.getInventory().size());
    }

    @After
    public void tearDown(){
        this.interaction = null;
        this.item = null;
        this.player = null;
        this.inventory = null;

        assertNull(item);
        assertNull(interaction);
        assertNull(player);
        assertNull(inventory);
    } 
}

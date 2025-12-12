package com.artattack;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class giveTest {
    
    private Interaction interaction;
    private List<Item> inventory;
    private Item item;
    private Player player;

    @Before
    public void setUp(){
        inventory = new ArrayList<Item>();
        item = new Cure();
        player = new MovieDirector(0, ' ', null, null, 0, 0, 0, 0, 0, 0, null, 0, 0, null, null, inventory);
        interaction = new GiveFactory()
                        .createInteraction(null, null, item);
    }

    @Test
    public void testDoAction(){
        interaction.doAction(player);

        assertTrue("Give testDoAsction faild. Item not in Player's inventory.",
                    player.getInventory().contains(item));
        assertEquals("Give testDoAction faild. Number of items in Player's inventory not as expected.",
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

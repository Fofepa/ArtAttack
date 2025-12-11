package com.artattack;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class giveTest {
    
    private Give interaction;
    private Item item;
    private Player player;

    @Before
    public void setUp(){
        item = new Cure();
        player = new MovieDirector();
        interaction = new GiveFactory()
                        .createInteraction()
                        .setItem(item);
    }

    @Test
    public void testDoAction(){
        interaction.doAction(player)

        assertTrue("Give testDo faild. Item not in Player's inventory.",
                    player.getInventory.contains(item));
        assertEquals("Give testDoAction faild. Number of items in Player's inventory not as expected.",
                    1,
                    player.getInventory().size());
    }

    @After
    public void tearDown(){
        this.interaction = null;
        this.item = null;
        this.player = null;

        assertNull(item);
        assertNull(interaction);
        asserNull(player);
    } 
}

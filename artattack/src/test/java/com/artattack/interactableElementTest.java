package com.artattack;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class interactableElementTest {
    private InteractableElement npc;
    private Player player;
    private Item item;
    private Interaction interaction;

    @Before
    public void setUp(){
        this.player = new MovieDirector();
        this.item = new Cure();
        this.interaction = new Give("Test interaction", this.item);
        this.npc = new InteractableElement(0,'*',"npcTest",new Coordinates(0,0), new ArrayList<Interaction>());
        this.npc.setInteraction(this.interaction);
    }

    @Test
    public void testInteract(){
        this.npc.interact(this.player);
        assertTrue("interactableElementTest faild. Item not in player inventory.", 
            this.player.getInventory().contains(this.item);
        )
    }

    @After
    public void tearDown(){
        this.npc = null;
        this.player = null;
        this.item = null;
        this.interaction = null;

        assertNull(item);
        assertNull(npc);
        asserNull(player);
        assertNull(interaction);
    }

}

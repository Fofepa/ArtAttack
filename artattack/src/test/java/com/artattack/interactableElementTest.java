package com.artattack;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class interactableElementTest {
    private InteractableElement npc;
    private Player player;
    private Item item;
    private Interaction interaction;

    @Before
    public void setUp(){
        this.player = new MovieDirector(0, '*',null,null,0,0,0,0,0,0,null,0,0,0,null,new ArrayList<Item>());
        this.item = new Cure();
        this.interaction = new Give(null, "", this.item);
        this.npc = new InteractableElement(0,'*',null,null, new ArrayList<Interaction>().add(this.interaction));
    }

    @Test
    public void testInteract(){
        this.npc.interact(this.player);
        assertTrue("interactableElementTest faild. Item not in player inventory.", 
            this.player.getInventory().contains(this.item)
        );
    }

    @After
    public void tearDown(){
        this.npc = null;
        this.player = null;
        this.item = null;
        this.interaction = null;

        assertNull(item);
        assertNull(npc);
        assertNull(player);
        assertNull(interaction);
    }

}

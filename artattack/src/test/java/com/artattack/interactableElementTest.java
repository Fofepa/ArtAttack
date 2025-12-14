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
    private List<Interaction> interactions;

    @Before
    public void setUp(){
        this.player = new MovieDirector(0, '*',null,null,0,0,0,0,0,0,null,0,0,0,null,new ArrayList<Item>());
        this.item = new Cure();
        this.interactions = new ArrayList<Interaction>();
        this.interactions.add(new Give(null, new ArrayList<String>(), this.item));
        this.interactions.add(new Talk(null, new ArrayList<String>()));
        this.npc = new InteractableElement(0,'*',null,null, interactions);
    }

    @Test
    public void testInteract(){
        this.npc.interact(this.player);
        assertTrue("interactableElementTest faild. Item not in player inventory.", 
            this.player.getInventory().contains(this.item)
        );
        assertEquals("interactableElementTest fails. Number of interaction doesn't match.", 1, npc.getCurrInteraction());
        this.npc.interact(this.player);
        assertEquals("interactableElementTest fails. Number of interaction doesn't match.", 2, npc.getCurrInteraction());
        this.npc.interact(this.player);
        assertEquals("interactableElementTest fails. Number of interaction doesn't match.", 2, npc.getCurrInteraction());
    }

    @After
    public void tearDown(){
        this.npc = null;
        this.player = null;
        this.item = null;
        this.interactions = null;

        assertNull(item);
        assertNull(npc);
        assertNull(player);
        assertNull(interactions);
    }

}

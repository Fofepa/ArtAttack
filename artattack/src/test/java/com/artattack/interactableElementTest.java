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

public class interactableelementTest {
    private InteractableElement npc;
    private Player player;
    private Item item;
    private List<Interaction> interactions;
    private InteractionPanel panel;

    @Before
    public void setUp(){
        this.player = new MovieDirector(0,'+'," ", new Coordinates(0, 0), 0, 0, 0, 0, 0, 0, 0, null, 0, new ArrayList<Item>(), null, null);
        this.item = new Cure(" ", " ", 0);
        this.interactions = new ArrayList<Interaction>();
        this.panel = new InteractionPanel();
        this.interactions.add(new Give(this.panel, List.of(" "), this.item));
        this.interactions.add(new Talk(this.panel, List.of(" ")));
        this.npc = new InteractableElement(0,'*',null,null, interactions);
    }

    @Test
    public void interactTest(){
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
        this.panel = null;

        assertNull(this.item);
        assertNull(this.npc);
        assertNull(this.player);
        assertNull(this.interactions);
        assertNull(this.panel);
    }

}

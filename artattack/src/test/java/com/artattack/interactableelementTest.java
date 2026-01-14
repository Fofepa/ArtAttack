package com.artattack;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import com.artattack.interactions.Give;
import com.artattack.interactions.GiveFactory;
import com.artattack.interactions.Interaction;
import com.artattack.interactions.Talk;
import com.artattack.interactions.TalkFactory;
import com.artattack.items.Cure;
import com.artattack.items.Item;
import com.artattack.level.Coordinates;
import com.artattack.level.Maps;
import com.artattack.level.TestMapBuilder;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.EnemyType;
import com.artattack.mapelements.InteractableElement;
import com.artattack.mapelements.MovieDirector;
import com.artattack.mapelements.Musician;
import com.artattack.mapelements.Player;
import com.artattack.view.InteractionPanel;
import com.artattack.view.MainFrame;
import com.artattack.view.SpritePanel;

public class interactableelementTest {
    private InteractableElement npc;
    private Player player;
    private Item item;
    private List<Interaction> interactions;
    private InteractionPanel panel;
    private MainFrame mainFrame;
    private TestMapBuilder tmb;

    @Before
    public void setUp(){
        TestMapBuilder tmb = new TestMapBuilder();
        assertNotNull(this.tmb);
        tmb.setDimension(26, 135);
        tmb.setPlayerOne(new Musician(1, '@', "Zappa", new Coordinates(0, 1)));
        tmb.setInteractableElements(List.of(
            new InteractableElement(0, '$', "Chitarra", new Coordinates(10, 10),List.of(new Talk(new InteractionPanel(), List.of("Ciao!"))), "",null,null),
            new InteractableElement(1, '$', "Batteria", new Coordinates(15, 15),List.of(new Talk(new InteractionPanel(), List.of("Haloa!"))), "",null,null
         )));
        tmb.setTurnQueue();
        tmb.setDict();
        tmb.startMap();
        Maps map = tmb.getResult();
        this.mainFrame = new MainFrame(map);
        assertNotNull(mainFrame);
        player = new MovieDirector(0, '+', "", new Coordinates(0, 0),
            null, 5,5, null, 0, 0, 0, 0, 0, 0, 0, new ArrayList<Item>(), null, null);
        this.item = new Cure(" ", " ", 0);
        this.interactions = new ArrayList<Interaction>();
        this.panel = new InteractionPanel();
        this.interactions.add(new GiveFactory(List.of(" "), this.item).createInteraction());
        this.interactions.add(new TalkFactory(List.of(" ")).createInteraction());
        this.npc = new InteractableElement(0,'*',null,null, interactions, "", new SpritePanel(), mainFrame);
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

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
import com.artattack.interactions.Interaction;
import com.artattack.interactions.Talk;
import com.artattack.items.Item;
import com.artattack.items.ItemType;
import com.artattack.level.Coordinates;
import com.artattack.level.Maps;
import com.artattack.level.TestMapBuilder;
import com.artattack.mapelements.InteractableElement;
import com.artattack.mapelements.Player;
import com.artattack.mapelements.PlayerType;
import com.artattack.view.GameContext;

public class interactableelementTest {
    private InteractableElement npc;
    private Player player;
    private Item item;
    private List<Interaction> interactions;
    private GameContext gameContext;
    private Maps map;

    @Before
    public void setUp(){
        TestMapBuilder tmb = new TestMapBuilder();
        tmb.setDimension(26, 135);
        tmb.setPlayerOne(new Player(1, '@', "Zappa", new Coordinates(0, 1)));
        tmb.setInteractableElements(List.of(
            new InteractableElement(0, '$', "Chitarra", new Coordinates(10, 10),List.of(new Talk(List.of("Ciao!"))), ""),
            new InteractableElement(1, '$', "Batteria", new Coordinates(15, 15),List.of(new Talk(List.of("Haloa!"))), ""
        )));
        tmb.setTurnQueue();
        tmb.setDict();
        tmb.startMap();
        this.map = tmb.getResult();
        assertNotNull(tmb);
        this.gameContext = new GameContext(null, null);
        assertNotNull(this.gameContext);
        player = new Player(0, '+', "", new Coordinates(0, 0),
            null, 5,5, null, 0, 0, 0, 0, 0, 0, 0, new ArrayList<Item>(), null, PlayerType.MOVIE_DIRECTOR);
        this.item = new Item(ItemType.CURE, " ", " ", 0);
        this.interactions = new ArrayList<Interaction>();
        this.interactions.add(new Give(List.of(" "), List.of(this.item)));
        this.interactions.add(new Talk(List.of(" ")));
        this.npc = new InteractableElement(0,'*',null,null, interactions, "");
    }

    @Test
    public void interactTest(){
        this.npc.interact(this.gameContext, this.player);
        assertTrue("interactableElementTest faild. Item not in player inventory.", 
            this.player.getInventory().contains(this.item)
        );
        assertEquals("interactableElementTest fails. Number of interaction doesn't match.", 1, npc.getCurrInteraction());
        this.npc.interact(this.gameContext, this.player);
        assertEquals("interactableElementTest fails. Number of interaction doesn't match.", 2, npc.getCurrInteraction());
        this.npc.interact(this.gameContext,this.player);
        assertEquals("interactableElementTest fails. Number of interaction doesn't match.", 2, npc.getCurrInteraction());
    }

    @After
    public void tearDown(){
        this.npc = null;
        this.player = null;
        this.item = null;
        this.interactions = null;
        this.map = null;
        this.gameContext = null;

        assertNull(this.item);
        assertNull(this.npc);
        assertNull(this.player);
        assertNull(this.interactions);
        assertNull(this.map);
        assertNull(this.gameContext);
    }

}

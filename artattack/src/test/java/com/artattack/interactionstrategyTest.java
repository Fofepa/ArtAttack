package com.artattack;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

import com.artattack.view.InteractionPanel;
import com.artattack.view.SpritePanel;

public class interactionstrategyTest {
    private TestMapBuilder tmb;
    private Player player;
    private Maps map;
    private MovementStrategy movementStrategy;
    private InteractionStrategy interactionStrategy;


    @Before
    public void setUp(){
        //Initializing tmb
        this.tmb = new TestMapBuilder();
        assertNotNull(this.tmb);
        //Creating player
        player = new MovieDirector(0, '@', "", new Coordinates(1, 1),
            null, 0, null, 0, 0, 0, 0, 0, 0, 0, new ArrayList<>(), null, null);
        //Creating map
        tmb.setDimension(26, 150);
        tmb.setPlayerOne(this.player);
        tmb.setPlayerTwo(new MovieDirector(0, '@', "Lynch", new Coordinates(5, 5)));
        tmb.setInteractableElements(List.of(
            new InteractableElement(0, 'I', "Chitarra", new Coordinates(2, 2),List.of(new Give(new InteractionPanel(), List.of(""), 
            new Cure(" ", " ", 0)), new Talk(new InteractionPanel(), List.of(""))), "", new SpritePanel(),new InteractionPanel()),
            new InteractableElement(1, 'I', "Batteria", new Coordinates(15, 15), List.of(new Talk(new InteractionPanel(), List.of(""))), "",new SpritePanel(),new InteractionPanel())));
        tmb.setTurnQueue();
        tmb.setDict();
        tmb.startMap();
        this.map = tmb.getResult();
        //Creating movementStrategy
        this.movementStrategy = new MovementStrategy(this.map, this.player);
        //Creating interactionStrategy
        this.interactionStrategy = new InteractionStrategy(this.movementStrategy);
    }

    @Test
    public void acceptinteractionTest(){
        this.movementStrategy.execute(1,1);
        this.interactionStrategy.acceptInteraction();

        assertEquals("acceptinteractionTest faild. Interaction not executed.", 1, this.player.getInventory().size());
    }

    @After
    public void tearDown(){
        this.player = null;
        this.map = null;
        this.movementStrategy = null;
        this.interactionStrategy = null;

        assertNull(this.player);
        assertNull(this.map);
        assertNull(this.movementStrategy);
        assertNull(this.interactionStrategy);
    }
}

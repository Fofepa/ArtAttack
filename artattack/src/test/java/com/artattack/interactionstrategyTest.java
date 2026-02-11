package com.artattack;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

import com.artattack.interactions.Give;
import com.artattack.interactions.InteractionStrategy;
import com.artattack.interactions.Talk;
import com.artattack.items.Item;
import com.artattack.items.ItemType;
import com.artattack.level.AreaBuilder;
import com.artattack.level.Coordinates;
import com.artattack.level.Maps;
import com.artattack.level.TestMapBuilder;
import com.artattack.mapelements.InteractableElement;
import com.artattack.mapelements.Player;
import com.artattack.mapelements.PlayerType;
import com.artattack.view.GameContext;
import com.artattack.view.MainFrame;

public class interactionstrategyTest {
    private TestMapBuilder tmb;
    private Player player;
    private Maps map;
    private InteractionStrategy interactionStrategy;
    private MainFrame mainFrame;


    @Before
    public void setUp(){
        AreaBuilder areaBuilder = new AreaBuilder();
        areaBuilder.addShape("8");
        List<Coordinates> area = areaBuilder.getResult();
        //Initializing tmb
        this.tmb = new TestMapBuilder();
        assertNotNull(this.tmb);
        //Creating player
        player = new Player(0, '@', "", new Coordinates(1, 1),
            null, 5,5, area, 0, 0, 0, 0, 0, 0, 0, new ArrayList<>(), null, PlayerType.MOVIE_DIRECTOR);
        //Creating map
        tmb.setDimension(26, 150);
        tmb.setPlayerOne(this.player);
        tmb.setPlayerTwo(new Player(0, '@', "Lynch", new Coordinates(5, 5)));
        tmb.setInteractableElements(List.of(
            new InteractableElement(0, 'I', "Chitarra", new Coordinates(2, 2),List.of(new Give(List.of(""), 
            List.of(new Item(ItemType.CURE," ", " ", 0))), new Talk(List.of(""))),""),
            new InteractableElement(1, 'I', "Batteria", new Coordinates(15, 15), List.of(new Talk(List.of(""))), "")));
        tmb.setTurnQueue();
        tmb.setDict();
        tmb.startMap();
        this.map = tmb.getResult();
        this.mainFrame = new MainFrame(map);
        this.mainFrame.setGameContext(new GameContext(mainFrame, null));
        assertNotNull(mainFrame);
        //Creating movementStrategy
        this.mainFrame.setCurrentPlayer(this.player);
        //Creating interactionStrategy
        this.interactionStrategy = new InteractionStrategy(this.mainFrame.getMovementStrategy());
    }

    @Test
    public void acceptinteractionTest(){
        this.mainFrame.getMovementStrategy().execute(1,0);
        this.mainFrame.getMovementStrategy().execute(0, 1);    // Execute cannot have dx and dy changed at the same time and more than 1.
        this.interactionStrategy.acceptInteraction();

        assertEquals("acceptinteractionTest faild. Interaction not executed.", 1, this.player.getInventory().size());
    }

    @After
    public void tearDown(){
        this.player = null;
        this.map = null;
        this.interactionStrategy = null;
        this.mainFrame = null;

        assertNull(this.player);
        assertNull(this.map);
        assertNull(this.interactionStrategy);
        assertNull(mainFrame);
    }
}

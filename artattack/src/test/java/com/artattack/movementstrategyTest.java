package com.artattack;


import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.artattack.inputcontroller.MovementStrategy;
import com.artattack.interactions.Talk;
import com.artattack.level.AreaBuilder;
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



public class movementstrategyTest {
    private TestMapBuilder tmb;
    private Maps map;
    private MovementStrategy move;
    private Coordinates coord;
    private Player player;

    @Before
    public void setUp() throws Exception{
        AreaBuilder areaBuilder = new AreaBuilder();
        areaBuilder.addShape("8");
        List<Coordinates> area = areaBuilder.getResult();
        this.tmb = new TestMapBuilder();
        assertNotNull(this.tmb);
        tmb.setDimension(26, 135);
        tmb.setPlayerOne(new Musician(1, '@', "Zappa", new Coordinates(4,4), null, 20, 20, area, 10, 10, 10, 12, 0, 0));
        tmb.setPlayerTwo(new MovieDirector(0, '@', "Lynch", new Coordinates(5, 5), null, 20, 20, area, 10, 10, 10, 12, 0, 0));
        /* tmb.setInteractableElements(List.of(
            new InteractableElement(0, '$', "Chitarra", new Coordinates(10, 10),List.of(new Talk(null, List.of("Ciao!"))), "",null,null),
            new InteractableElement(1, '$', "Batteria", new Coordinates(15, 15),List.of(new Talk(new InteractionPanel(), List.of("Haloa!"))), "",null,null
         ))); */
        tmb.setEnemies(List.of(
            new Enemy(0, 'E', "Goblin", new Coordinates(4, 7), EnemyType.EMPLOYEE, 10, 10, 1, null, 10,10, null, area, null,null, 0),
            new Enemy(1, 'E', "Orco", new Coordinates(25, 25), EnemyType.EMPLOYEE, 20, 20, 2, null, 15,15, null, List.of(new Coordinates(10, 10)), null,null ,0)));
        tmb.setTurnQueue();
        tmb.setDict();
        tmb.startMap();
        this.map = tmb.getResult();
        assertNotNull(map);
        move = new MovementStrategy(map, map.getPlayerOne());
        assertNotNull(move);
        //player = new MovieDirector();

    }

    @After
    public void tearDown() throws Exception{
        move = null;
        map = null;
        assertNull(map);
        assertNull(move);
    }

    @Test
    public void executeTest(){
        Coordinates t = move.getCursor();
        move.execute(0,-1);
        assertTrue(Coordinates.sum(t, new Coordinates(0, -2)).equals(move.getCursor()));    // it skips the player coordinates since the cursor is always at 0, 1 from the player.

        


    }

    @Test
    public void acceptmovementTest(){
        move.acceptMovement();
        assertTrue(map.getPlayerOne().getCoordinates().equals(new Coordinates(4,5)));

        move.execute(0,1);
        move.acceptMovement();
        assertEquals(new Enemy(0, 'E', "Goblin", new Coordinates(2, 3), EnemyType.EMPLOYEE), map.getConcreteTurnHandler().getConcreteTurnQueue().getTurnQueue().get(0));
    }
}
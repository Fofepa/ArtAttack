package com.artattack;


import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;


public class movementstrategyTest {
    private Maps map;
    private MovementStrategy move;
    private Coordinates coord;
    private Player player;

    @Before
    public void setUp() throws Exception{
        map = new Maps(new Musician(1, '@', "Zappa", new Coordinates(0, 1)),
         new MovieDirector(0, '@', "Lynch", new Coordinates(5, 5)), List.of(
            new InteractableElement(0, '$', "Chitarra", new Coordinates(10, 10),null),
            new InteractableElement(1, '$', "Batteria", new Coordinates(15, 15),null)
         )/*, List.of(
            new Enemy(0, 'E', "Goblin", new Coordinates(20, 20)),
            new Enemy(1, 'E', "Orco", new Coordinates(25, 25))
         )*/);
        assertNotNull(map);
        move = new MovementStrategy(map, new Musician(0,'M',"Frank Zappa",new Coordinates(1,1)));
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
        Coordinates t = new Coordinates(move.getCursor().getX(),move.getCursor().getY());
        move.execute(0,1);
        assertEquals(new Coordinates(t.getX(),t.getY()+1),move.getCursor());
    }

    @Test
    public void acceptmovementTest(){
        move.acceptMovement();
        assertEquals(move.getCursor(),move.getPlayer().getCoordinates());
    }
}
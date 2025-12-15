package com.artattack;


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
        map = new Maps();
        assertNotNull(map);
        move = new MovementStrategy(new Maps(), new Musician(0,'M',"Frank Zappa",new Coordinates(1,1)));
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
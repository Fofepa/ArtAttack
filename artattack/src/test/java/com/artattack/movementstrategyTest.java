package com.artattack;


import org.junit.After;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

import com.googlecode.lanterna.input.KeyStroke;

public class movementstrategyTest {
    private Maps map;
    private MovementStrategy move;
    private Coordinates coord;
    private Player player;

    @Before
    public void setUp() throws Exception{
        map = new Maps();
        assertNotNull(map);
        move = new MovementStrategy();
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
    public void executeTest(KeyStroke k){
        coord = new Coordinates(1,1);
    }
}
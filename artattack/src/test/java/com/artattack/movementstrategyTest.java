package com.artattack;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.*;
import com.googlecode.lanterna.input.KeyStroke;

public class movementstrategyTest {
    private Maps map;
    private MovementStrategy move;
    private Coordinates coord;

    @Before
    public void setUp() throws Exception{
        map = new Maps();
        assertNotNull(map);
        move = new MovementStrategy();
        assertNotNull(move);

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
        assertTrue("Not equals", true, map.updateMovement(coord));
    }
}
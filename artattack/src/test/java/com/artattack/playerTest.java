package com.artattack;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;


public class playerTest {
    private Player player;

    @Before
    public void setUp() throws Exception{
        this.player = new MovieDirector(0, 'M', "David Lynch", new Coordinates(0,0), null, 5,5, null, 15, 20, 10, 20, 0, 2, 1, new ArrayList<Item>(), null, 
            new ArrayList<Coordinates>());
    }

    @After
    public void tearDown(){
        this.player = null;
        assertNull(this.player);
    }

    @Test
    public void updatecurrxpTest(){
        this.player.updateCurrXP(1);
        assertEquals("updatecurrxpTest faild. currXP not as expected.", 11, this.player.getCurrXP());
        assertFalse("updatecurrxpTest faild. leveledUp not as expected.", this.player.getLeveledUp());
        this.player.updateCurrXP(10);
        assertEquals("updatecurrxpTest faild. currXP not as expected.", 1, this.player.getCurrXP());
        assertTrue("updatecurrxpTest faild. leveledUp not as expected.", this.player.getLeveledUp());
    }
}

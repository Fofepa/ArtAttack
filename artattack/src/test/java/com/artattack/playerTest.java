package com.artattack;

import java.util.ArrayList;

import org.junit.After;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

import com.artattack.items.Item;
import com.artattack.level.Coordinates;
import com.artattack.mapelements.Player;
import com.artattack.mapelements.PlayerType;


public class playerTest {
    private Player player;

    @Before
    public void setUp() throws Exception{
        this.player = new Player(0, 'M', "David Lynch", new Coordinates(0,0), null, 5,5, null, 15, 20, 10, 20, 0, 2, 1, new ArrayList<Item>(), null, PlayerType.MOVIE_DIRECTOR);
    }

    @After
    public void tearDown(){
        this.player = null;
        assertNull(this.player);
    }

    @Test
    public void updatecurrxpTest(){
        /* this.player.updateCurrXP(1);
        assertEquals("updatecurrxpTest faild. currXP not as expected.", 11, this.player.getCurrXP());
        assertFalse("updatecurrxpTest faild. leveledUp not as expected.", this.player.getLeveledUp());
        this.player.updateCurrXP(10);
        assertEquals("updatecurrxpTest faild. currXP not as expected.", 1, this.player.getCurrXP());
        assertTrue("updatecurrxpTest faild. leveledUp not as expected.", this.player.getLeveledUp()); */
    }
}

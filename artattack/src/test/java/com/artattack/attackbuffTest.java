package com.artattack;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

import com.artattack.items.AttackBuff;
import com.artattack.level.Coordinates;
import com.artattack.mapelements.MovieDirector;
import com.artattack.mapelements.Player;

public class attackbuffTest{

    private AttackBuff ab;
    private Player p;

    @Before
    public void setUp() throws Exception {
        ab = new AttackBuff("Test", "Test", 1);
        p = new MovieDirector(0, 'i', "TestPlayer", new Coordinates(0, 0));

        assertNotNull(ab);
        assertNotNull(p);
    }

    @After
    public void tearDown() throws Exception {
        ab = null;
        p = null;

        assertNull(ab);
        assertNull(p);
    }

    @Test
    public void testUse() {
        assertEquals("Function use has failed", 0, ab.use(p));
    }

}
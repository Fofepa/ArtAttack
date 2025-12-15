package com.artattack;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class speedbuffTest{

    private SpeedBuff sb;
    private Player p;

    @Before
    public void setUp() throws Exception {
        sb = new SpeedBuff("TestSpeedBuff", "TestDesc", 1);
        p = new MovieDirector(0, 'i', "TestPlayer", new Coordinates(0, 0));

        assertNotNull(sb);
        assertNotNull(p);
    }

    @After
    public void tearDown() throws Exception {
        sb = null;
        p = null;

        assertNull(sb);
        assertNull(p);
    }

    @Test
    public void testUse() {
        assertEquals("SpeedBuff.use(Player player) has failed", p.getSpeed() + sb.getAmount(), sb.use(p));
    }

}
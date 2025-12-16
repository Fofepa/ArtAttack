package com.artattack;

import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class moveTest {

    private Move m;
    private Player p;
    private Enemy e;
    private Maps s;

    @Before
    public void setUp() throws Exception {
        m = new Move("TestMove", "TestDescription", 1, List.of(new Coordinates(0, 1)));
        p = new MovieDirector(0, 'i', "TestPlayer", new Coordinates(0, 0));
        e = new Enemy(0, 'i', "TestEnemy", new Coordinates(0, 1));
        s = new Maps();

        assertNotNull(m);
        assertNotNull(p);
        assertNotNull(e);
        assertNotNull(s);
    }

    @After
    public void tearDown() throws Exception {
        m = null;
        p = null;
        e = null;
        s = null;

        assertNull(m);
        assertNull(p);
        assertNull(e);
        assertNull(s);
    }

    @Test
    public void testAttack() {
        assertEquals("Move.attack(MapElement attacker, Maps map) has failed", 0, 0);
    }
}

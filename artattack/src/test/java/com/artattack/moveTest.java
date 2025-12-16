package com.artattack;

import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class moveTest {

    private Move m1, m2;
    private Player p1, p2;
    private Enemy e;
    private Maps maps;

    @Before
    public void setUp() throws Exception {
        m1 = new Move("TestMove", "TestDescription", 1, List.of(new Coordinates(0, 1)));
        m2 = new Move("TestMove", "TestDescription", 1, List.of(new Coordinates(1, 0)));
        p1 = new MovieDirector(0, 'i', "TestPlayerOne", new Coordinates(0, 1), 10, 10, 0, 1, 1, 1);
        p2 = new Musician(0, 'i', "TestPlayerTwo", new Coordinates(4, 4), 10, 10, 0, 1, 1, 1);
        e = new Enemy(0, 'i', "TestEnemy", new Coordinates(1, 1), 10, 10);
        maps = new Maps(p1, p2, null, List.of(e));

        assertNotNull(m1);
        assertNotNull(p1);
        assertNotNull(p2);
        assertNotNull(e);
        assertNotNull(maps);
    }

    @After
    public void tearDown() throws Exception {
        m1 = null;
        p1 = null;
        p2 = null;
        e = null;
        maps = null;

        assertNull(m1);
        assertNull(p1);
        assertNull(p2);
        assertNull(e);
        assertNull(maps);
    }

    @Test
    public void testAttack() {
        m2.attack(p1, maps);
        assertEquals("Move.attack(MapElement attacker, Maps map) has failed", 9, e.getCurrHP()); //Player attacks Enemy
        m1.attack(e, maps);
        assertEquals("Move.attack(MapElement attacker, Maps map) has failed", 9, p2.getCurrHP()); //Enemy attacks Player*/
    }
}

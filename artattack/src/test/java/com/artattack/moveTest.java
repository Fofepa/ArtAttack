package com.artattack;

import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class moveTest {

    private Move m1, m2, m3, m4;
    private Player p1, p2;
    private Enemy e;
    private Maps maps;

    @Before
    public void setUp() throws Exception {
        m1 = new Move("TestMove", "TestDescription", 1, List.of(new Coordinates(0, 1)));
        m2 = new Move("TestMove", "TestDescription", 1, List.of(new Coordinates(1, 0)));
        p1 = new MovieDirector(0, 'i', "TestPlayerOne", new Coordinates(0, 0),
            null, 0, null, 10, 10, 0, 10, 0, 0, 0, null,null,null);
        p2 = new Musician(0, 'i', "TestPlayerTwo", new Coordinates(1, 1),
            null, 0, null, 10, 10, 0, 10, 0, 0, 0, null,null,null);
        e = new Enemy(0, 'i', "TestEnemy", new Coordinates(0, 1), 10, 10, 0, null, 0, null,null,null,10);
        maps = new Maps(p1, p2, null, List.of(e));

        m3 = new Move("TestMove", "TestDescription", 8, List.of(new Coordinates(0, 1)));
        m4 = new Move("TestMove", "TestDescription", 2, List.of(new Coordinates(-1, 0)));

        assertNotNull(m1);
        assertNotNull(p1);
        assertNotNull(p2);
        assertNotNull(e);
        assertNotNull(maps);
        assertNotNull(m3);
        assertNotNull(m4);
    }

    @After
    public void tearDown() throws Exception {
        m1 = null;
        p1 = null;
        p2 = null;
        e = null;
        maps = null;
        m3 = null;
        m4 = null;

        assertNull(m1);
        assertNull(p1);
        assertNull(p2);
        assertNull(e);
        assertNull(maps);
        assertNull(m3);
        assertNull(m4);
    }

    @Test
    public void testAttack() {
        //m1.attack(p1, maps);
        //assertEquals("Move.attack(MapElement attacker, Maps map) has failed", 9, e.getCurrHP()); //Player attacks Enemy
        m2.attack(e, maps);
        assertEquals("Move.attack(MapElement attacker, Maps map) has failed", 9, p2.getCurrHP()); //Enemy attacks Player*/
        m3.attack(p1,maps);
        m4.attack(p2,maps);
        assertEquals("Move.attack(MapElement attacker, Maps map) has failed", 0, e.getCurrHP());
        assertEquals("testAttack faild. p1 currXP not as expected.", 8, p1.getCurrXP());
        assertEquals("testAttack faild. p2 currXP not as expected.", 2, p2.getCurrXP());
    }
}

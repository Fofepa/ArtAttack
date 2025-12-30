package com.artattack;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class moveTest {
    private Move m1, m2, m3, m4, m5;
    private MoveBuilder1 mb1;
    private Player p1, p2;
    private Enemy e;
    private Maps maps;

    @Before
    public void setUp() throws Exception {
        //Initializing MoveBuilder1
        mb1 = new MoveBuilder1();
        //Creating m1
        mb1.setName("TestMove");
        mb1.setDescription("TestDescription");
        mb1.setPower(1);
        mb1.setActionPoints(1);
        mb1.setAttackArea(List.of(new Coordinates(0, 1)));
        m1 = mb1.getResult();
        //Creating m2
        mb1.setName("TestMove");
        mb1.setDescription("TestDescription");
        mb1.setPower(1);
        mb1.setActionPoints(1);
        mb1.setAttackArea(List.of(new Coordinates(1, 0)));
        m2 = mb1.getResult();
        //Creating m3
        mb1.setName("TestMove");
        mb1.setDescription("TestDescription");
        mb1.setPower(8);
        mb1.setActionPoints(1);
        mb1.setAttackArea(List.of(new Coordinates(0, 1)));
        m3 = mb1.getResult();
        //Creating m4
        mb1.setName("TestMove");
        mb1.setDescription("TestDescription");
        mb1.setPower(2);
        mb1.setActionPoints(1);
        mb1.setAttackArea(List.of(new Coordinates(-1, 0)));
        m4 = mb1.getResult();
        //Creating m5
        mb1.setName("TestMove");
        mb1.setDescription("TestDescription");
        mb1.setHealAmount(1);
        mb1.setActionPoints(1);
        mb1.setHealArea(List.of(new Coordinates(1, 1)));
        m5 = mb1.getResult();

        p1 = new MovieDirector(0, 'i', "TestPlayerOne", new Coordinates(0, 0),
            null, 0, null, 10, 10, 0, 10, 0, 0, 0, null,null,null);
        p2 = new Musician(0, 'i', "TestPlayerTwo", new Coordinates(1, 1),
            null, 0, null, 10, 10, 0, 10, 0, 0, 0, new ArrayList<Item>(), new ArrayList<Key>(),null);
        e = new Enemy(0, 'i', "TestEnemy", new Coordinates(0, 1), 10, 10, 0, null, 0, null,null,List.of(new Cure("cure", " ", 10)),List.of(new Key("","",0)),10);
        maps = new Maps(p1, p2, null, List.of(e));

        /*m3 = new Move("TestMove", "TestDescription", 8, List.of(new Coordinates(0, 1)));
        m4 = new Move("TestMove", "TestDescription", 2, List.of(new Coordinates(-1, 0)));*/

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
    public void testUseMove() {
        //m1.useMove(p1, maps);
        //assertEquals("Move.useMove(MapElement attacker, Maps map) has failed", 9, e.getCurrHP()); //Player attacks Enemy
        e.setActionPoints(m2.getActionPoints());
        m2.useMove(e, maps);
        assertEquals("Move.useMove(MapElement attacker, Maps map) has failed", 9, p2.getCurrHP()); //Enemy attacks Player*/
        p1.setActionPoints(m3.getActionPoints());
        m3.useMove(p1,maps);
        p2.setActionPoints(m4.getActionPoints());
        m4.useMove(p2,maps);

        //Testing droppedXP
        assertEquals("Move.useMove(MapElement attacker, Maps map) has failed", 0, e.getCurrHP());
        assertEquals("testAttack faild. p1 currXP not as expected.", 8, p1.getCurrXP());
        assertEquals("testAttack faild. p2 currXP not as expected.", 2, p2.getCurrXP());
        //Testing dropped Items and Keys
        assertEquals("testAttack faild. p2 inventory not as expected.", 1, p2.getInventory().size());
        assertEquals("testAttack faild. p2 keys not as expected.", 1, p2.getKeys().size());

        //Testing enemy.remove()
        assertFalse("removeTest faild. Enemy still in the dict.", maps.getDict().containsValue(e));
        assertTrue("removeTest faild. mapMatrix not updated.", maps.getMapMatrix()[e.getCoordinates().getX()][e.getCoordinates().getY()]=='.');

        //Testing insufficient ActionPoints
        p1.setActionPoints(0);
        assertEquals("Move.useMove(MapElement attacker, Maps map) has failed", 0, m1.useMove(p1, maps));

        //Testing healing
        p1.setActionPoints(m5.getActionPoints());
        p2.updateHP(-p2.getCurrHP() + 1);
        m5.useMove(p1, maps);
        assertEquals(2, p2.getCurrHP());
    }
}

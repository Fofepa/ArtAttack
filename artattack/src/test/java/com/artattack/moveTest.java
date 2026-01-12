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

import com.artattack.items.Cure;
import com.artattack.items.Key;
import com.artattack.level.Coordinates;
import com.artattack.level.Maps;
import com.artattack.level.TestMapBuilder;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.EnemyType;
import com.artattack.mapelements.MovieDirector;
import com.artattack.mapelements.Musician;
import com.artattack.mapelements.Player;
import com.artattack.moves.Move;
import com.artattack.moves.MoveBuilder1;

public class moveTest {
    private Move m1, m2, m3, m4, m5;
    private MoveBuilder1 mb1;
    private Player p1, p2;
    private Enemy e;
    private TestMapBuilder tmb;
    private Maps maps;

    @Before
    public void setUp() throws Exception {
        //Initializing MoveBuilder1
        this.mb1 = new MoveBuilder1();
        assertNotNull(this.mb1);
        //Initializing TestMapBuilder
        this.tmb = new TestMapBuilder();
        assertNotNull(this.tmb);
        //Creating m1
        mb1.setName("TestMove");
        mb1.setDescription("TestDescription");
        mb1.setPower(1);
        mb1.setActionPoints(1);
        mb1.setAttackArea(List.of(new Coordinates(0, 1)));
        this.m1 = mb1.getResult();
        //Creating m2
        mb1.setName("TestMove");
        mb1.setDescription("TestDescription");
        mb1.setPower(1);
        mb1.setActionPoints(1);
        mb1.setAttackArea(List.of(new Coordinates(1, 0)));
        this.m2 = mb1.getResult();
        //Creating m3
        mb1.setName("TestMove");
        mb1.setDescription("TestDescription");
        mb1.setPower(8);
        mb1.setActionPoints(1);
        mb1.setAttackArea(List.of(new Coordinates(0, 1)));
        this.m3 = mb1.getResult();
        //Creating m4
        mb1.setName("TestMove");
        mb1.setDescription("TestDescription");
        mb1.setPower(2);
        mb1.setActionPoints(1);
        mb1.setAttackArea(List.of(new Coordinates(-1, 0)));
        this.m4 = mb1.getResult();
        //Creating m5
        mb1.setName("TestMove");
        mb1.setDescription("TestDescription");
        mb1.setHealAmount(1);
        mb1.setActionPoints(1);
        mb1.setHealArea(List.of(new Coordinates(1, 1)));
        this.m5 = mb1.getResult();
        //Creating p1
        this.p1 = new MovieDirector(0, 'i', "TestPlayerOne", new Coordinates(0, 0), 
        null, 5,5, null, 10, 10, 0, 10, 0, 0, 0, new ArrayList<>(), new ArrayList<>(), null);
        //Creating p2
        this.p2 = new Musician(0, 'i', "TestPlayerTwo", new Coordinates(1, 1),
        null, 5,5, null, 10, 10, 0, 10, 0, 0, 0, new ArrayList<>(), new ArrayList<>() ,null);
        //Creating e
        this.e = new Enemy(0, 'i', "TestEnemy", new Coordinates(0, 1), EnemyType.EMPLOYEE, 100, 100, 1, 
            new ArrayList<>(), 5,5, new ArrayList<>(), null, List.of(new Cure(".", ".", 1)), 
            List.of(new Key(".", ".", 0)), 100);
        //Creating maps
        tmb.setDimension(26, 135);
        tmb.setPlayerOne(p1);
        tmb.setPlayerTwo(p2);
        tmb.setEnemies(List.of(e));
        tmb.setDict();
        tmb.startMap();
        this.maps = tmb.getResult();

        assertNotNull(this.m1);
        assertNotNull(this.m2);
        assertNotNull(this.m3);
        assertNotNull(this.m4);
        assertNotNull(this.p1);
        assertNotNull(this.p2);
        assertNotNull(this.e);
        assertNotNull(this.maps);
    }

    @After
    public void tearDown() throws Exception {
        this.m1 = null;
        this.m2 = null;
        this.m3 = null;
        this.m4 = null;
        this.p1 = null;
        this.p2 = null;
        this.e = null;
        this.maps = null;

        assertNull(this.m1);
        assertNull(this.m2);
        assertNull(this.m3);
        assertNull(this.m4);
        assertNull(this.p1);
        assertNull(this.p2);
        assertNull(this.e);
        assertNull(this.maps);
    }

    @Test
    public void testUseMove() {
        //m1.useMove(p1, maps);
        //assertEquals("Move.useMove(MapElement attacker, Maps map) has failed", 9, e.getCurrHP()); //Player attacks Enemy
        e.setActionPoints(m2.getActionPoints());
        m2.useMove(e, maps);
        assertEquals("Move.useMove(MapElement attacker, Maps map) has failed", 9, p2.getCurrHP()); //Enemy attacks Player*/
        //p1.setActionPoints(m3.getActionPoints());
        //m3.useMove(p1,maps);
        p2.setActionPoints(m4.getActionPoints());
        m4.useMove(p2,maps);
        assertEquals("Move.useMove(MapElement attacker, Maps map) has failed", 98, e.getCurrHP());

        //Testing droppedXP
        p2.setActionPoints(m4.getActionPoints());
        e.updateHP(-e.getCurrHP() + m4.getPower());
        m4.useMove(p2, maps);
        assertEquals(0, e.getCurrHP());
        e.updatePlayerOneDemage(-e.getPlayerOneDemage() + 8);
        e.updatePlayerTwoDemage(-e.getPlayerTwoDemage() + 2);
        p1.updateCurrXP(-p1.getCurrXP());
        p2.updateCurrXP(-p2.getCurrXP());
        e.dropXP(p1, p2);
        assertEquals(false, e.isAlive());
        assertEquals("testAttack failed. p1 currXP not as expected.", 8, p1.getCurrXP());
        assertEquals("testAttack failed. p2 currXP not as expected.", 2, p2.getCurrXP());
        //Testing dropped Items and Keys
        assertEquals("testAttack failed. p2 inventory not as expected.", 1, p2.getInventory().size());
        assertEquals("testAttack failed. p2 keys not as expected.", 1, p2.getKeys().size());

        //Testing enemy.remove()
        assertFalse("removeTest failed. Enemy still in the dict.", maps.getDict().containsValue(e));
        assertTrue("removeTest failed. mapMatrix not updated.", maps.getMapMatrix()[e.getCoordinates().getX()][e.getCoordinates().getY()]=='.');

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

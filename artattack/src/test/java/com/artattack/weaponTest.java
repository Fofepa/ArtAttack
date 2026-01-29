package com.artattack;

import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

import com.artattack.level.Coordinates;
import com.artattack.moves.Move;
import com.artattack.moves.MoveBuilder1;
import com.artattack.moves.Weapon;

public class weaponTest {
    private Weapon w;
    private MoveBuilder1 mb1;
    private Move m1;
    private Move m2;

    @Before
    public void setUp() throws Exception {
        w = new Weapon("TestWeapon", "TestDescription", 3, null);
        mb1 = new MoveBuilder1();
        mb1.setName("TestName");
        mb1.setDescription("TestDescription");
        mb1.setPower(1);
        mb1.setActionPoints(1);
        mb1.setAttackArea(List.of(new Coordinates(0, 0), new Coordinates(1, 1)));
        m1 = mb1.getResult();
        mb1.setName("TestName");
        mb1.setDescription("TestDescription");
        mb1.setPower(2);
        mb1.setActionPoints(1);
        mb1.setAttackArea(List.of(new Coordinates(0, 0), new Coordinates(1, 1)));
        m2 = mb1.getResult();

        assertNotNull(w);
        assertNotNull(mb1);
        assertNotNull(m1);
        assertNotNull(m2);
    }

    @After
    public void tearDown() throws Exception {
        w = null;
        mb1 = null;
        m1 = null;
        m2 = null;

        assertNull(w);
        assertNull(mb1);
        assertNull(m1);
        assertNull(m2);
    }

    @Test
    public void addMoveTest() {
        assertEquals("Weapon.addMove(Move move) has failed the test", true, w.addMove(m1)); //Adding a move when there are none
        assertEquals("Weapon.addMove(Move move) has failed the test", false, w.addMove(m1)); //Adding a move that is already in the list
        assertEquals("Weapon.addMove(Move move) has failed the test", true, w.addMove(m2)); //Adding a move that isn't already in the list
    }
}

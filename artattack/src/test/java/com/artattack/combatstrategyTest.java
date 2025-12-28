package com.artattack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.*;
import org.junit.*;
import org.junit.Assert.*;

import com.artattack.*;
import com.artattack.view.*;

public class combatstrategyTest {
    private CombatStrategy combatStrategy;

    @Before
    public void setUp(){
        Maps map = new Maps(new Musician(1, '@', "Zappa", new Coordinates(0, 1), List.of(new Weapon("Hoe", "", 0)), 5, null, 20, 20, 0, 20, 1, 5, 2, null, null, null),
         new MovieDirector(0, '@', "Lynch", new Coordinates(5, 5)), List.of(
            new InteractableElement(0, '$', "Chitarra", new Coordinates(10, 10),List.of(new Talk(new InteractionPanel(), List.of("Ciao!"))), "",null,null),
            new InteractableElement(1, '$', "Batteria", new Coordinates(15, 15),List.of(new Talk(new InteractionPanel(), List.of("Haloa!"))), "",null,null
         )),
         List.of(
            new Enemy(0, 'E', "Goblin", new Coordinates(1, 1)),
            new Enemy(1, 'E', "Orco", new Coordinates(1, 2))
         ));
        assertNotNull(map);
        map.getPlayerOne().getWeapons().get(0).addMove(new Move("Kick", "", 3, List.of(new Coordinates(1, 0))));
        map.getPlayerOne().getWeapons().get(0).addMove(new Move("Bump", "", 5, List.of(new Coordinates(1, 1))));
        map.getPlayerOne().getWeapons().get(0).getMoves().get(0).setActionPoints(3);
        map.getPlayerOne().getWeapons().get(0).getMoves().get(1).setActionPoints(4);
        combatStrategy = new CombatStrategy(map);
        combatStrategy.setCurrentPlayer(map.getPlayerOne());
        assertNotNull(combatStrategy);
    }

    @Test
    public void executeTest(){
        combatStrategy.execute(1,2);
        assertNotEquals(1, combatStrategy.getWeaponIndex());
        assertNotEquals(2, combatStrategy.getMoveIndex());

        combatStrategy.execute(0,2);
        assertEquals(0, combatStrategy.getWeaponIndex());
        assertEquals(1, combatStrategy.getMoveIndex());
    }

    @Test
    public void acceptmoveTest(){
        combatStrategy.execute(0,2);
        int c = combatStrategy.acceptMove();

        assertFalse(combatStrategy.isSelected());
        assertEquals(5,c);

        combatStrategy.execute(0,1);
        c = combatStrategy.acceptMove();

        assertFalse(combatStrategy.isSelected());
        assertEquals(0,c);
    }

    @After
    public void tearDown(){
        combatStrategy = null;
        assertNull(combatStrategy);
    }
}

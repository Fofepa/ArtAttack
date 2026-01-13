package com.artattack;

import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

import com.artattack.inputcontroller.CombatStrategy;
import com.artattack.interactions.Talk;
import com.artattack.level.Coordinates;
import com.artattack.level.MapBuilder;
import com.artattack.level.Maps;
import com.artattack.level.TestMapBuilder;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.EnemyType;
import com.artattack.mapelements.InteractableElement;
import com.artattack.mapelements.MovieDirector;
import com.artattack.mapelements.Musician;
import com.artattack.moves.Move;
import com.artattack.moves.Weapon;
import com.artattack.view.InteractionPanel;

public class combatstrategyTest {
    private CombatStrategy combatStrategy;

    @Before
    public void setUp(){
        MapBuilder mapBuilder = new TestMapBuilder(); 
        mapBuilder.setPlayerOne(new Musician(1, '@', "Zappa", new Coordinates(0, 1), List.of(new Weapon("Hoe", "", 0)), 5,5, null, 20, 20, 0, 20, 1, 5, 2, null, null, null));
        mapBuilder.setPlayerTwo(new MovieDirector(0, '@', "Lynch", new Coordinates(5, 5)));
        mapBuilder.setInteractableElements(List.of(
            new InteractableElement(0, '$', "Chitarra", new Coordinates(10, 10),List.of(new Talk(new InteractionPanel(), List.of("Ciao!"))), "",null,null),
            new InteractableElement(1, '$', "Batteria", new Coordinates(15, 15),List.of(new Talk(new InteractionPanel(), List.of("Haloa!"))), "",null,null
         )));
        mapBuilder.setEnemies(List.of(
            new Enemy(0, 'E', "Goblin", new Coordinates(1, 1),EnemyType.EMPLOYEE),
            new Enemy(1, 'E', "Orco", new Coordinates(1, 2),EnemyType.EMPLOYEE)
         ));
        mapBuilder.setDimension(36, 150);
        mapBuilder.setDict();
        mapBuilder.startMap();
        assertNotNull(mapBuilder);
        Maps map = mapBuilder.getResult();
        assertNotNull(map);
        Move m1 = new Move(); m1.setName("Kick"); m1.setPower(3); m1.setAttackArea(List.of(new Coordinates(1, 0))); m1.setActionPoints(3);
        Move m2 = new Move(); m2.setName("Bump"); m2.setPower(5); m2.setAttackArea(List.of(new Coordinates(1, 1))); m2.setActionPoints(4);
        map.getPlayerOne().getWeapons().get(0).addMove(m1);
        map.getPlayerOne().getWeapons().get(0).addMove(m2);
        combatStrategy = new CombatStrategy(map, map.getPlayerOne());
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

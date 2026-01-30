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
import com.artattack.level.Coordinates;
import com.artattack.level.MapBuilder;
import com.artattack.level.Maps;
import com.artattack.level.TestMapBuilder;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.EnemyType;
import com.artattack.mapelements.Player;
import com.artattack.mapelements.PlayerType;
import com.artattack.moves.Move;
import com.artattack.moves.Weapon;
import com.artattack.view.MainFrame;

public class combatstrategyTest {
    private CombatStrategy combatStrategy;
    private MainFrame mainFrame;

    @Before
    public void setUp(){
        MapBuilder mapBuilder = new TestMapBuilder(); 
        mapBuilder.setPlayerOne(new Player(1, '@', "Zappa", new Coordinates(0, 1), List.of(new Weapon("Hoe", "", 3, PlayerType.MUSICIAN)), 10,10, null, 20, 20, 0, 20, 1, 5, 2, null, null, null, PlayerType.MUSICIAN));
        mapBuilder.setPlayerTwo(new Player(0, '@', "Lynch", new Coordinates(5, 5)));
        /* mapBuilder.setInteractableElements(List.of(
            new InteractableElement(0, '$', "Chitarra", new Coordinates(10, 10),null, "",null,null),
            new InteractableElement(1, '$', "Batteria", new Coordinates(15, 15),null, "",null,null
         ))); */
        mapBuilder.setEnemies(List.of(
            new Enemy(0, 'E', "Frank", new Coordinates(1,2),EnemyType.GUARD, 20, 20, 3,
                                 null,5,5,null, null, null, null, 0),
            new Enemy(1, 'E', "Orco", new Coordinates(10, 20),EnemyType.EMPLOYEE)
         ));
        mapBuilder.setDimension(36, 150);
        mapBuilder.setDict();
        mapBuilder.startMap();
        assertNotNull(mapBuilder);
        Maps map = mapBuilder.getResult();
        assertNotNull(map);
        this.mainFrame = new MainFrame(map);
        assertNotNull(mainFrame);
        Move m1 = new Move(); m1.setName("Kick"); m1.setPower(3); m1.setAttackArea(List.of(new Coordinates(1, 0))); m1.setActionPoints(3);
        Move m2 = new Move(); m2.setName("Bump"); m2.setPower(5); m2.setAttackArea(List.of(new Coordinates(1, 1))); m2.setActionPoints(4);
        map.getPlayerOne().getWeapons().get(0).addMove(m1);
        map.getPlayerOne().getWeapons().get(0).addMove(m2);
        combatStrategy = new CombatStrategy(map, map.getPlayerOne());
        combatStrategy.setCurrentPlayer(map.getPlayerOne());
        combatStrategy.setMainFrame(this.mainFrame);
        assertNotNull(combatStrategy);
    }

    @Test
    public void executeTest(){
        combatStrategy.execute(1,0);
        assertNotEquals(1, combatStrategy.getWeaponIndex());

        combatStrategy.execute(1,1);
        assertEquals(0, combatStrategy.getWeaponIndex());
        assertEquals(1, combatStrategy.getMoveIndex());
    }

    @Test
    public void acceptmoveTest(){
        combatStrategy.execute(1,1);
        int c = combatStrategy.acceptMove();

        assertEquals(0, combatStrategy.getWeaponIndex());
        assertEquals(0, combatStrategy.getMoveIndex());


        assertFalse(combatStrategy.isSelected());
        assertEquals(5,c); // case hit

        /* combatStrategy.execute(1,1); */
        c = combatStrategy.acceptMove();

        assertEquals(0,c);  // case wiffs
    }

    @After
    public void tearDown(){
        combatStrategy = null;
        assertNull(combatStrategy);
        mainFrame = null;
        assertNull(mainFrame);
    }
}

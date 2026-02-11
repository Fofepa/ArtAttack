package com.artattack;

import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import com.artattack.interactions.Talk;
import com.artattack.level.Coordinates;
import com.artattack.level.Maps;
import com.artattack.level.TestMapBuilder;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.EnemyType;
import com.artattack.mapelements.InteractableElement;
import com.artattack.mapelements.Player;
import com.artattack.mapelements.TriggerGroup;
import com.artattack.view.MainFrame;

public class mapTest {
    private TestMapBuilder tmb;
    private Maps map;
    private MainFrame mainFrame;
    private Player player;
    private Enemy enemy;
    
    
    @Before
    public void setUp() throws Exception{
        //Initializing tmb
        this.tmb = new TestMapBuilder();
        assertNotNull(this.tmb);

        //Initialazing player and enemy for removeTest
        this.player = new Player(1, '@', "Zappa", new Coordinates(0, 1));
        assertNotNull(this.player);
        this.enemy = new Enemy(0, 'E', "Goblin", new Coordinates(20, 20), EnemyType.EMPLOYEE);
        assertNotNull(this.enemy);
        //Creating map
        tmb.setDimension(26, 135);
        tmb.setPlayerOne(this.player);
        tmb.setPlayerTwo(new Player(0, '@', "Lynch", new Coordinates(5, 5)));
        tmb.setInteractableElements(List.of(
            new InteractableElement(0, '$', "Chitarra", new Coordinates(10, 10),List.of(new Talk(List.of(""))), ""),
            new InteractableElement(1, '$', "Batteria", new Coordinates(15, 15),List.of(new Talk(List.of(""))), "")));
        tmb.setEnemies(List.of(
            this.enemy,
            new Enemy(1, 'E', "Orco", new Coordinates(25, 25), EnemyType.EMPLOYEE)));
        tmb.setDict();
        //Setting turnQueue for removeTest
        tmb.setTurnQueue();
        tmb.startMap();
        this.map = tmb.getResult();
        this.map.getConcreteTurnHandler().getConcreteTurnQueue().add(this.enemy);
        this.mainFrame = new MainFrame(map);
        assertNotNull(this.map);
    }

    @After
    public void tearDown() throws Exception{
        this.map = null;
        assertNull(map);
        this.mainFrame = null;
        assertNull(mainFrame);
        this.player = null;
        assertNull(player);
        this.enemy = null;
        assertNull(enemy);
    }

    @Test
    public void setcellTest(){
        map.setCell(new Coordinates(1,1),'#');
        assertEquals('#', map.getCell(new Coordinates(1,1)));

    }

    @Test
    public void addTriggerGroupTest() {
        TriggerGroup tg = new TriggerGroup(new Talk( List.of("TestDialog")));
        map.addTriggerGroup(tg, new Coordinates(1, 1), 3, 3, "");
        map.setDict();
        for (int q = 1; q < 4; q++) {
            for (int k = 1; k < 4; k++) {
                assertNotNull(map.getDict().get(new Coordinates(q, k)));
            }
        }
    }
    
    @Test
    public void removeTest() {
        this.map.remove(enemy);
        //assertFalse(this.map.getEnemies().contains(enemy));
        assertTrue(this.map.getMapMatrix()[enemy.getCoordinates().getX()][enemy.getCoordinates().getY()] == '.');
        assertFalse(this.map.getDict().containsValue(enemy));
        this.map.remove(player);
        assertNull(this.map.getPlayerOne());
        assertTrue(this.map.getMapMatrix()[player.getCoordinates().getX()][player.getCoordinates().getY()] == '.');
        assertFalse(this.map.getDict().containsValue(player));
    }
}
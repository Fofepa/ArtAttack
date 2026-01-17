package com.artattack;

import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

import com.artattack.interactions.Talk;
import com.artattack.level.Coordinates;
import com.artattack.level.Maps;
import com.artattack.level.TestMapBuilder;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.EnemyType;
import com.artattack.mapelements.InteractableElement;
import com.artattack.mapelements.MovieDirector;
import com.artattack.mapelements.Musician;
import com.artattack.mapelements.TriggerGroup;
import com.artattack.view.MainFrame;
import com.artattack.view.SpritePanel;

public class mapTest {
    private TestMapBuilder tmb;
    private Maps map;
    private MainFrame mainFrame;
    
    
    @Before
    public void setUp() throws Exception{
        //Initializing tmb
        this.tmb = new TestMapBuilder();
        assertNotNull(this.tmb);
        //Creating map
        tmb.setDimension(26, 135);
        tmb.setPlayerOne(new Musician(1, '@', "Zappa", new Coordinates(0, 1)));
        tmb.setPlayerTwo(new MovieDirector(0, '@', "Lynch", new Coordinates(5, 5)));
        tmb.setInteractableElements(List.of(
            new InteractableElement(0, '$', "Chitarra", new Coordinates(10, 10),List.of(new Talk(null, List.of(""))), "",new SpritePanel(),null),
            new InteractableElement(1, '$', "Batteria", new Coordinates(15, 15),List.of(new Talk(null, List.of(""))), "",new SpritePanel(),null)));
        tmb.setEnemies(List.of(
            new Enemy(0, 'E', "Goblin", new Coordinates(20, 20), EnemyType.EMPLOYEE),
            new Enemy(1, 'E', "Orco", new Coordinates(25, 25), EnemyType.EMPLOYEE)));
        tmb.setDict();
        tmb.startMap();
        this.map = tmb.getResult();
        this.mainFrame = new MainFrame(map);
        mainFrame.linkInteractablePanels();
        assertNotNull(this.map);
    }

    @After
    public void tearDown() throws Exception{
        this.map = null;
        assertNull(map);
        this.mainFrame = null;
        assertNull(mainFrame);
    }

    @Test
    public void setcellTest(){
        map.setCell(new Coordinates(1,1),'#');
        assertEquals('#', map.getCell(new Coordinates(1,1)));

    }

    @Test
    public void addTriggerGroupTest() {
        TriggerGroup tg = new TriggerGroup(new Talk(new MainFrame(this.map), List.of("TestDialog")));
        map.addTriggerGroup(tg, new Coordinates(1, 1), 3, 3);
        map.setDict();
        for (int q = 1; q < 4; q++) {
            for (int k = 1; k < 4; k++) {
                assertNotNull(map.getDict().get(new Coordinates(q, k)));
            }
        }
    }
}
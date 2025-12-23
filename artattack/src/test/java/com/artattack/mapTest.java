package com.artattack;

import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

import com.artattack.view.InteractionPanel;
import com.artattack.view.SpritePanel;

public class mapTest {
    private Maps map;
    
    
    @Before
    public void setUp() throws Exception{
        map = new Maps(new Musician(1, '@', "Zappa", new Coordinates(0, 1)),
         new MovieDirector(0, '@', "Lynch", new Coordinates(5, 5)), List.of(
            new InteractableElement(0, '$', "Chitarra", new Coordinates(10, 10),List.of(new Talk(new InteractionPanel(), List.of(""))), "",new SpritePanel(),new InteractionPanel()),
            new InteractableElement(1, '$', "Batteria", new Coordinates(15, 15),List.of(new Talk(new InteractionPanel(), List.of(""))), "",new SpritePanel(),new InteractionPanel())
         ), List.of(
            new Enemy(0, 'E', "Goblin", new Coordinates(20, 20)),
            new Enemy(1, 'E', "Orco", new Coordinates(25, 25))
         ));
        assertNotNull(map);
    }

    @After
    public void tearDown() throws Exception{
        map = null;
        assertNull(map);
    }

    @Test
    public void setcellTest(){
        map.setCell(1,1,'#');
        assertEquals('#', map.getCell(1,1));

    }  
}
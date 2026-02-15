package com.artattack;

import java.io.IOException;
import java.util.HashMap;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.artattack.level.MapBuilderTypeOne;
import com.artattack.level.MapDirector;
import com.artattack.level.MapManager;
import com.artattack.level.Maps;
import com.artattack.saving.SaveManager;

public class savemanagerTest {
    MapDirector director;
    MapBuilderTypeOne builder;
    MapManager mapManager;
    SaveManager saveManager;

    @Before
    public void setUp() throws Exception{
        this.builder =  new MapBuilderTypeOne();
        this.director = new MapDirector(builder);
        this.builder.setTurnQueue();

        assertNotNull(this.builder);
        assertNotNull(this.director);

        director.make("Tutorial");
        Maps map = builder.getResult();

        director.make("1");
        Maps map2 = builder.getResult();

        this.mapManager = new MapManager(new HashMap<Integer, Maps>(), map.getID());
        this.mapManager.getLevels().put(map.getID(), map);
        this.mapManager.getLevels().put(map2.getID(), map2);

        assertNotNull(this.mapManager);

        this.saveManager = new SaveManager();
    }

    @After
    public void tearDown(){
        this.director = null;
        this.builder = null;
        this.mapManager = null;
        this.saveManager = null;

        assertNull(this.director);
        assertNull(this.builder);
        assertNull(this.mapManager);
        assertNull(this.saveManager);
    }

    @Test
    public void saveTest(){
        try{
            this.saveManager.save(this.mapManager);
            MapManager mapManager2 = this.saveManager.load();

            assertEquals("savemanagerTest faild. mapManager not as expected.", 0, mapManager2.getCurrMap());
           

        } catch(IOException e){

        }
    }
    
}

package com.artattack;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

import com.artattack.inputcontroller.InventoryStrategy;
import com.artattack.items.Item;
import com.artattack.items.ItemType;
import com.artattack.level.Coordinates;
import com.artattack.level.MapBuilderTypeOne;
import com.artattack.level.MapDirector;
import com.artattack.level.Maps;
import com.artattack.mapelements.Player;
import com.artattack.mapelements.PlayerType;
import com.artattack.view.MainFrame;

public class inventorystrategyTest {
    private MapBuilderTypeOne mb;
    private MapDirector md;
    private Maps map;
    private InventoryStrategy is;
    private Player p1, p2;
    private Item i;

    @Before
    public void setUp() throws Exception {
        this.mb = new MapBuilderTypeOne();
        assertNotNull(this.mb);

        this.md = new MapDirector(this.mb);
        assertNotNull(this.md);
        this.md.make("Tutorial");
        
        
        this.i = new Item(ItemType.CURE, "TestCure", "TestDescription", 5);
        assertNotNull(this.i);
        
        this.p1 = new Player(0, '0', "TestPlayer", new Coordinates(1, 1), new ArrayList<>(), 10, 10, new ArrayList<>(), 5, 10, 0, 5, 1, 1, 5, new ArrayList<>(List.of(this.i, this.i, this.i)), new ArrayList<>(), new ArrayList<>(), PlayerType.MUSICIAN);
        assertNotNull(this.p1);
        
        this.p2 = new Player(0, '0', "TestPlayer", new Coordinates(1, 1), new ArrayList<>(), 10, 10, new ArrayList<>(), 5, 10, 0, 5, 1, 1, 5, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), PlayerType.MOVIE_DIRECTOR);
        assertNotNull(this.p2);
        
        this.mb.setPlayerOne(this.p1);
        this.mb.setPlayerTwo(this.p2);
        this.mb.setID(0);
        this.mb.setDict();
        this.mb.setTurnQueue();
        this.mb.startMap();
        this.map = mb.getResult();
        assertNotNull(this.map);

        this.is = new InventoryStrategy(this.map, this.p1);
        assertNotNull(this.is);
        this.is.setMainFrame(new MainFrame(this.map));
    }

    @Test
    public void acceptItemTest() {
        is.acceptItem(this.p1);
        assertEquals(10, this.p1.getCurrHP());
    }

    @Test
    public void giveItemTest() {
        is.giveItem();
        assertEquals(1, this.p2.getInventory().size());
    }

    @Test
    public void useItemOnOtherTest() {
        is.useItemOnOther();
        assertEquals(10, this.p2.getCurrHP());
    }

    @After
    public void tearDown() throws Exception {
        this.mb = null;
        assertNull(this.mb);

        this.md = null;
        assertNull(this.md);

        this.map = null;
        assertNull(this.map);

        this.p1 = null;
        assertNull(this.p1);

        this.p2 = null;
        assertNull(this.p2);

        this.is = null;
        assertNull(this.is);

        this.i = null;
        assertNull(this.i);
    }
}

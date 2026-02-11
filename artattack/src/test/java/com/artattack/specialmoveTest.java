package com.artattack;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

import com.artattack.items.Item;
import com.artattack.items.ItemType;
import com.artattack.items.Key;
import com.artattack.level.AreaBuilder;
import com.artattack.level.Coordinates;
import com.artattack.level.Maps;
import com.artattack.level.TestMapBuilder;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.EnemyType;
import com.artattack.mapelements.Player;
import com.artattack.mapelements.PlayerType;
import com.artattack.moves.Move;
import com.artattack.moves.MoveBuilder1;

public class specialmoveTest {
    private Move lu, st, er, wah;
    private MoveBuilder1 mb1;
    private Player p1, p2;
    private Enemy e1,e2,e3;
    private TestMapBuilder mb;
    private Maps maps;

    @Before
    public void setUp() throws Exception {  //TODO: test the Little Umbrellas move
        //Initializing MoveBuilder1
        this.mb1 = new MoveBuilder1();
        assertNotNull(this.mb1);
        //Initializing MapBuilder
        this.mb = new TestMapBuilder();
        //Creating m1
        mb1.setName("Little Umbrellas");
        mb1.setDescription("With his skills zappa makes a nebula of acid rain appear in all the map that does damage, but helps himself and the other player by giving him a little umbrella.");
        mb1.setPower(8);
        mb1.setActionPoints(20);
        mb1.setAreaAttack(true);
        mb1.setAttackArea(new ArrayList<>());
        this.lu = mb1.getResult();
        //Creating m2
        mb1.setName("ST. Alfonzo's Pancake Breakfast");
        mb1.setDescription("Music nowdays does miracles, look at this beautiful Pancake Zappa has made! It heals a lot and can be used on the others!!");
        mb1.setHealAmount(25);
        mb1.setActionPoints(14);
        AreaBuilder ab = new AreaBuilder();
        ab.addShape("circle", 8, true);
        mb1.setHealArea(ab.getResult());
        this.st = mb1.getResult();
        //Creating m3
        mb1.setName("ERASERHEAD");
        mb1.setDescription("Maybe you don't remember the film, but surely the enemy head got deleted, it loses the aggro, loses a turn and takes damage!");
        mb1.setPower(8);
        mb1.setActionPoints(25);
        ab.addShape("square",1,true);
        mb1.setAttackArea(ab.getResult());
        this.er = mb1.getResult();
        //Creating m4
        mb1.setName("Wild at Heart");
        mb1.setDescription("Makes the enemy say a bad slur to the player, a group of thugs takes care of it... After being beaten up if the enemy isn't dead it becomes a Wild at heart!");
        mb1.setPower(30);
        mb1.setActionPoints(40);
        ab.addShape("circle",1,true);
        mb1.setAttackArea(ab.getResult());
        this.wah = mb1.getResult();
        //Creating p1
        this.p1 = new Player(0, 'i', "TestPlayerOne", new Coordinates(2, 2), 
        null, 100,100, null, 20, 20, 10, 10, 5, 5, 4, new ArrayList<>(), new ArrayList<>(), PlayerType.MOVIE_DIRECTOR);
        //Creating p2
        this.p2 = new Player(0, 'i', "TestPlayerTwo", new Coordinates(1, 1),
        null, 100,100, null, 1, 40, 10, 10, 5, 5, 4, new ArrayList<>(), new ArrayList<>(), PlayerType.MOVIE_DIRECTOR);
        //Creating e
        this.e1 = new Enemy(0, 'i', "TestEnemy", new Coordinates(3, 3), EnemyType.EMPLOYEE, 100, 100, 1, 
            new ArrayList<>(), 5,5, new ArrayList<>(), null, List.of(new Item(ItemType.CURE,
                ".", ".", 1)), 
            List.of(new Key(".", ".", 0)), 100);
        this.e2 = new Enemy(0, 'i', "TestEnemy", new Coordinates(3, 5), EnemyType.EMPLOYEE, 100, 100, 1, 
            new ArrayList<>(), 5,5, new ArrayList<>(), null, List.of(new Item(ItemType.CURE,
                ".", ".", 1)), 
            List.of(new Key(".", ".", 0)), 100);
        this.e3 = new Enemy(0, 'i', "TestEnemy", new Coordinates(10, 10), EnemyType.EMPLOYEE, 100, 100, 1, 
            new ArrayList<>(), 5,5, new ArrayList<>(), null, List.of(new Item(ItemType.CURE,
                ".", ".", 1)), 
            List.of(new Key(".", ".", 0)), 100);
        //Creating maps
        mb.setDimension(26, 135);
        mb.setPlayerOne(this.p1);
        mb.setPlayerTwo(this.p2);
        mb.setEnemies(new ArrayList<>(List.of(this.e1,this.e2,this.e3)));
        mb.setDict();
        mb.setTurnQueue();
        mb.startMap();
        this.maps = mb.getResult();
    }
    
    @Test
    public void specialusemoveTest(){
        //Testing the Little Umbrellas move
        lu.useMove(p1,maps);
        assertEquals(1, p2.getCurrHP()); // the other isn't hitted by the Little Umbrellas move
        assertEquals(276, e1.getCurrHP() + e2.getCurrHP() + e3.getCurrHP()); //Check If I hit all the enemies

        //Testing the ST. Alfonzo's Pancake Breakfast move
        st.useMove(p1,maps);
        assertEquals(26, p2.getCurrHP());

        //Testing the Eraserhead move
        er.useMove(p1,maps);
        assertEquals(84, e1.getCurrHP());

        //Testing the Wild at Heart move
        wah.useMove(p1, maps);
        assertEquals(54, e1.getCurrHP());
        assertEquals(EnemyType.DUMMY, e1.getEnemyType());
    }

    @After
    public void tearDown(){
        lu = null;
        assertNull(lu);
        st = null;
        assertNull(st);
        er = null;
        assertNull(er);
        wah = null;
        assertNull(wah);
        mb1 = null;
        assertNull(mb1);
        p1 = null;
        assertNull(p1);
        p2 = null;
        assertNull(p2);
        e1 = null;
        assertNull(e1);
        e2 = null;
        assertNull(e2);
        e3 = null;
        assertNull(e3);
        maps = null;
        assertNull(maps);
        mb = null;
        assertNull(e1);
    }
}

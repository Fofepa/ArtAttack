package com.artattack;

import java.security.Guard;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class dumbattackstrategyTest {
    private DumbAttackStrategy dumbAttack;

    @Before
    public void setUp(){
        Enemy enemy = new Enemy(0, 'E', "Frank", new Coordinates(1,1),EnemyType.GUARD, 20, 20, 3,
                                 new ArrayList<Weapon>(),5,null,null,null,null,0);
        

    }
}

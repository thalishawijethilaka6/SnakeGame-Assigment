/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Text;

import lk.assignment.main.Food;
import lk.assignment.main.GameMain;
import lk.assignment.main.Snake;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jayasanka shop
 */
public class GameMainJUnitTest {

    public GameMainJUnitTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        GameMain gameMain = new GameMain();
        gameMain.gameInit();
        assertEquals(1, new Snake());
        assertEquals(1, new Food());
    }

    @AfterClass
    public static void tearDownClass() {
        GameMain gameMain = new GameMain();
        gameMain.gameUpdate();
        
    }

    @Before
    public void setUp() {
        GameMain gameMain = new GameMain();
        gameMain.gameStart();
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}

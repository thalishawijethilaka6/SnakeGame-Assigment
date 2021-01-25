/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Text;

import lk.assignment.main.Food;
import lk.assignment.main.GameMain;
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
public class FoodJUnitTest {
    
    public FoodJUnitTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        Food food = new Food();
        food.getX();
        food.getY();
         
    }
    
    @AfterClass
    public static void tearDownClass() {
        Food food = new Food();
        food.regenerate();
    }
    
    @Before
    public void setUp() {
         //Food food = new Food();    
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.assignment.main;

import java.awt.*;
import java.util.*;

/**
 *
 * @author Thalisha-
 */
public class Food {

    private int x, y;
    //color for display
    private Color color = Color.BLUE;
    private Random rand = new Random();

    //default constructor
    public Food() {

        x = -1;
        y = -1;
    }

    public void regenerate() {
        x = rand.nextInt(GameMain.COLUMNS - 4) + 2;
        y = rand.nextInt(GameMain.ROWS - 4) + 2;

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fill3DRect(x * GameMain.CELL_SIZE,
                y * GameMain.CELL_SIZE,
                GameMain.CELL_SIZE,
                GameMain.CELL_SIZE,
                true);
    }

}

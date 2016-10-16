/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author ford.terrell
 */
public class Credits extends JPanel {
    
    private static long endTime;
    private static MyMouseListener listener;
    
    public Credits(int ticks) {
        endTime = System.currentTimeMillis() + ticks;
        listener = new MyMouseListener();
        addMouseListener(listener);
        requestFocus();
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
    
    public static boolean isRunning() {
        if(listener.isPressed()) {
            return false;
        }
        if(System.currentTimeMillis() > endTime) {
            return false;
        }
        return true;
    }
}

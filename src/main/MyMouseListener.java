/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.event.MouseEvent;

/**
 *
 * @author Terrell
 */
public class MyMouseListener implements java.awt.event.MouseListener {
    
    private boolean pressed, hovered;
    
    protected MyMouseListener() {
        pressed = false;
        hovered = false;
    }

    public void mouseEntered(MouseEvent evt) {
        hovered = true;
    }

    public void mouseExited(MouseEvent evt) {
        hovered = false;
    }

    public void mousePressed(MouseEvent evt) {
        pressed = true;
    }

    public void mouseReleased(MouseEvent evt) {
        pressed = false;
    }

    public void mouseClicked(MouseEvent e) {
    }
    
    protected boolean isPressed() {
        return pressed;
    }
    
    protected boolean isHovered() {
        return hovered;
    }
}

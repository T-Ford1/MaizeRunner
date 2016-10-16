package input;

import java.awt.event.*;

import render.Screen;

public class InputHandler implements MouseMotionListener, MouseListener, MouseWheelListener, KeyListener, FocusListener {

    private final boolean[] key = new boolean[125];
    private int x, y, b;
    private boolean pressed, clicked;
    
    private static final int xMid = Screen.WIDTH / 2, yMid = Screen.HEIGHT / 2;

    public boolean FWD, BACK, RIGHT, LEFT, SHIFT, CTRL, SPACE;
    public boolean LOOK_UP, LOOK_DOWN, LOOK_RIGHT, LOOK_LEFT;
    public boolean ESCAPE;

    public boolean isPressed(int keyCode) {
        if (keyCode < 0 || keyCode >= key.length) {
            return false;
        }
        return key[keyCode];
    }

    public boolean isMousePressed() {
        return pressed || clicked;
    }

    public int getMouseX() {
        return x;
    }

    public int getMouseY() {
        return y;
    }
    
    public int getMouseButton() {
        return b;
    }

    public int getXDistFromMid() {
        return x - xMid;
    }

    public int getYDistFromMid() {
        return y - yMid;
    }

    public int getMaxYDist() {
        return yMid;
    }

    public int getMaxXDist() {
        return xMid;
    }

    private void updateKeys() {
        if (key[KeyEvent.VK_W] || key[KeyEvent.VK_UP]) {
            if (key[KeyEvent.VK_S] || key[KeyEvent.VK_DOWN]) {
                FWD = false;
                BACK = false;
            } else {
                FWD = true;
                BACK = false;
            }
        } else {
            if (key[KeyEvent.VK_S] || key[KeyEvent.VK_DOWN]) {
                FWD = false;
                BACK = true;
            } else {
                FWD = false;
                BACK = false;
            }
        }
        if (key[KeyEvent.VK_A] || key[KeyEvent.VK_LEFT]) {
            if (key[KeyEvent.VK_D] || key[KeyEvent.VK_RIGHT]) {
                RIGHT = false;
                LEFT = false;
            } else {
                RIGHT = false;
                LEFT = true;
            }
        } else {
            if (key[KeyEvent.VK_D] || key[KeyEvent.VK_RIGHT]) {
                RIGHT = true;
                LEFT = false;
            } else {
                RIGHT = false;
                LEFT = false;
            }
        }
        CTRL = key[KeyEvent.VK_CONTROL];
        SPACE = key[KeyEvent.VK_SPACE];
        SHIFT = key[KeyEvent.VK_SHIFT];
        ESCAPE = key[KeyEvent.VK_ESCAPE];
    }

    private void updateMouse() {
        if (x < xMid) {
            LOOK_LEFT = true;
            LOOK_RIGHT = false;
        } else {
            LOOK_LEFT = false;
            LOOK_RIGHT = true;
        }
        if (y < yMid) {
            LOOK_UP = true;
            LOOK_DOWN = false;
        } else {
            LOOK_UP = false;
            LOOK_DOWN = true;
        }
        clicked = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode > 0 && keyCode < key.length) {
            key[keyCode] = true;
        }
        updateKeys();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode > 0 && keyCode < key.length) {
            key[keyCode] = false;
        }
        updateKeys();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        updateMouse();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        updateMouse();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        clicked = true;
        b = e.getButton();
        x = e.getX();
        y = e.getY();
        updateMouse();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        pressed = true;
        b = e.getButton();
        x = e.getX();
        y = e.getY();
        updateMouse();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        pressed = false;
        x = e.getX();
        y = e.getY();
        updateMouse();
    }

    @Override
    public void focusGained(FocusEvent e) {
    }

    @Override
    public void focusLost(FocusEvent e) {
    }
}

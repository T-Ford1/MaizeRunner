package main;

import graphics.MessageMaker;
import graphics.Texture;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JButton;

/**
 *
 * @author ford.terrell
 */
public class MyButton extends JButton {

    private final Texture name;
    private final MyMouseListener listener;
    private final Color color;

    public MyButton(String n, Color c) {
        color = c;
        name = MessageMaker.getMessage(n, c, Color.black, 54);
        setRolloverEnabled(true);
        listener = new MyMouseListener();
        addMouseListener(listener);
    }

    public void paint(Graphics g) {
        if (!MyPanel.isRenderingText()) {
            g.setColor(color);
            g.fillRect(0, 0, 400, 75);
            int left = (getWidth() - name.getWidth()) / 2;
            int top = (getHeight() - name.getHeight()) / 2;
            g.drawImage(name.getImage(), left, top, null);
        }
    }
}

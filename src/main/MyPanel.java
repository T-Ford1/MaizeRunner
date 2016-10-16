/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import graphics.MessageMaker;
import graphics.Texture;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;

/**
 *
 * @author ford.terrell
 */
public class MyPanel extends JPanel {

    private static final ArrayList<Texture> textScroll = new ArrayList<>();
    private static double topTextbarPos;

    public MyPanel() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        };
        timer.scheduleAtFixedRate(task, 0, 10);
    }

    public static void rollCredits(String text, int large, int small) {
        if (MyPanel.isRenderingText()) {
            return;
        }
        String[] lines = text.split("\n");
        textScroll.add(MessageMaker.getMessage(lines[0], Color.black, Color.green, large));
        for (int i = 1; i < lines.length; i++) {
            textScroll.add(MessageMaker.getMessage(lines[i], Color.black, Color.green, small));
        }
        topTextbarPos = GameStart.root.getHeight();
    }

    public void paint(Graphics g) {
        if (!MyPanel.isRenderingText()) {
            super.paint(g);
            return;
        }
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        while (!textScroll.isEmpty() && topTextbarPos + textScroll.get(0).getHeight() * 2 < 0) {
            topTextbarPos += textScroll.get(0).getHeight();
            textScroll.remove(0);
        }
        int pos = (int) topTextbarPos;
        for (int i = 0; i < textScroll.size(); i++) {
            BufferedImage b = textScroll.get(i).getImage();
            pos += textScroll.get(i).getHeight();
            if (b == null) {
                continue;
            }
            int xOff = (getWidth() - b.getWidth()) / 2;
            g.drawImage(b, xOff, pos, null);
        }
        topTextbarPos -= 0.35;
    }

    public static boolean isRenderingText() {
        return !textScroll.isEmpty();
    }
}

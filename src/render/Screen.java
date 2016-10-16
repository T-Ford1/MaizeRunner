package render;

import game.Level;
import game.entity.mob.player.Controller;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import input.InputHandler;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import thread.TimerMaster;

public class Screen extends Canvas {

    private static final long serialVersionUID = 1L;

    public static int WIDTH, HEIGHT;
    public static boolean FULLSCREEN, UNDECORATED;

    private BufferStrategy buffer;
    public static InputHandler INPUT;

    public Screen() {
        try {
            Scanner keyb = new Scanner(new File("res/screenSize.txt"));
            keyb.nextLine();
            keyb.nextLine();
            WIDTH = keyb.nextInt();
            keyb.nextLine();
            keyb.nextLine();
            HEIGHT = keyb.nextInt();
            keyb.nextLine();
            keyb.nextLine();
            FULLSCREEN = keyb.nextBoolean();
            keyb.nextLine();
            keyb.nextLine();
            UNDECORATED = keyb.nextBoolean();
            if (FULLSCREEN) {
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                WIDTH = d.width;
                HEIGHT = d.height;
            }
        } catch (FileNotFoundException ex) {
            WIDTH = 800;
            HEIGHT = 600;
            FULLSCREEN = false;
        }
    }

    public void init() {
        createBufferStrategy(3);
        buffer = getBufferStrategy();
        INPUT = new InputHandler();
        addKeyListener(INPUT);
        addFocusListener(INPUT);
        addMouseListener(INPUT);
        addMouseWheelListener(INPUT);
        addMouseMotionListener(INPUT);
        requestFocus();
    }

    public void render(BufferedImage frame, int fps) {
        Graphics g = buffer.getDrawGraphics();
        g.drawImage(frame, 0, 0, WIDTH, HEIGHT, null);
        Controller c = TimerMaster.getPlayerController();
        g.setFont(new Font("Verdana", 0, 30));
        g.setColor(Color.yellow);
        g.drawString("FPS: " + fps + " X: " + (int) c.x + ", Z: " + (int) c.y, 5, 30);
        if (Level.TIME > 0) {
            int minutes = Level.TIME / 60;
            double d = (Level.TIME % 60) / 100.;
            g.drawString(String.format(minutes + ":%.2f", d).replace("0.", ""), Screen.WIDTH - 100, 30);
        }
        g.drawString("Wasps Eliminated: " + Level.KILLS, Screen.WIDTH / 2 - 20, 30);
        buffer.show();
    }
}

package main;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import render.Screen;
import thread.TimerMaster;

public class Main {

    private static final long serialVersionUID = 1L;

    protected static JFrame frame;
    private static TimerMaster master;
    private static boolean restart;

    public static Screen DISPLAY;
    public static final String NAME = "MAIZE RUNNER";

    public static synchronized void start() {
        frame = new JFrame(NAME);
        DISPLAY = new Screen();
        frame.add(DISPLAY);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Screen.WIDTH, Screen.HEIGHT);
        frame.setUndecorated(Screen.UNDECORATED);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(new ImageIcon("res/kgmo.png").getImage());
        frame.setVisible(true);
        if (TimerMaster.running) {
            return;
        }
        restart = false;
        TimerMaster.running = true;
        master = new TimerMaster();
        master.start();
    }

    public static synchronized void stop() {
        if (!TimerMaster.running) {
            return;
        }
        System.exit(0);
        TimerMaster.running = false;
        try {
            master.join();
        } catch (Exception ex) {
            master = null;
        }
    }

    public static void restart() {
        frame.setVisible(false);
        frame = null;
        restart = true;
    }

    public static void main(String[] args) {
        while (true) {
            GameStart.start();
            restart = false;
            while (!restart) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                }
            }
        }

    }

    public static Screen getDisplay() {
        return DISPLAY;
    }
}

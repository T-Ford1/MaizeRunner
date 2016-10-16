package thread;

import game.Level;
import game.entity.mob.player.Controller;
import game.entity.mob.player.Player;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JOptionPane;
import main.Main;

public class TimerMaster extends WorkerThread {

    protected static double FPS;
    protected static double UPS;

    public static boolean running = false;
    public static final Level LEVEL = new Level();

    // conversion from nanoseconds to a second
    private static double u;
    private static double f;
    private static long updates, renders, startTime;
    private static int fps, runningFPS;
    private static RenderMaster render;

    public TimerMaster() {
        super("TimerMaster");
        try {
            Scanner s = new Scanner(new File("res/updateSettings.txt"));
            s.nextLine();
            int ups = s.nextInt();
            s.nextLine();
            s.nextLine();
            int fps = s.nextInt();
            setTimes(ups, fps);
        } catch (FileNotFoundException ex) {
            setTimes(60, 60);
        }
        runningFPS = 0;
        render = new RenderMaster();
        maxSleep = 13;
        defaultSleep = 3;
    }

    protected final void setTimes(double ups, double fps) {
        if (ups > 100) {
            ups = 100;
        }
        if (fps > 144) {
            fps = 144;
        }
        if (ups < 0) {
            ups = 60;
        }
        if (fps < 0) {
            fps = 60;
        }
        UPS = ups;
        FPS = fps;
        u = 1_000_000_000.0 / UPS;
        f = 1_000_000_000.0 / FPS;
        updates = 0;
        renders = 0;
        startTime = System.nanoTime();
    }

    protected void init() {
        startTime = System.nanoTime();
        Main.DISPLAY.init();
    }

    protected void updateThread() {
        updated = false;
        // do game updates (60 per second)
        // if scheduled for one or multiple
        // updates, do game tick
        while ((System.nanoTime() - startTime) / u > updates) {
            updated = true;
            updates++;
            // one more tick recorded
            update();
            // update the game
            if (updates % (int) (UPS) == 0) {
                if (fps + 5 < FPS) {
                    increaseSpeed();
                }
                runningFPS = fps;
                fps = 0;
            }
        }
        if ((System.nanoTime() - startTime) / f > renders) {
            updated = true;
            render();
            renders++;
            if (render.hasRendered()) {
                fps++;
                Main.DISPLAY.render(render.getFrame(), runningFPS);
            }
        }
        // if has updated game, render image
    }
    
    protected void endTask() {
        //the main game code has been stopped.
        //the final operation before thread shutdown
        JOptionPane.showMessageDialog(null, "Nice Test Drive!"
                + "\nYou Removed " + Level.KILLS + " wasp"
                + (Level.KILLS != 1 ? "s" : "")
                + "\nfrom the farm. As official rater"
                + "\nof wasp removal productivity, I"
                + "\ndeclare your score to be...."
                + "\n\t" + getRating(), 
                Main.NAME, JOptionPane.PLAIN_MESSAGE);
        Main.restart();
    }
    
    private static String getRating() {
        if(Level.KILLS > 30) {
            return "AMAZING!!";
        } else if (Level.KILLS > 20) {
            return "EXCELLENT!";
        } else if(Level.KILLS > 10) {
            return "GOOD.";
        } else if(Level.KILLS > 5) {
            return "OK.";
        }
        return "NOT VERY GOOD :(";
    }

    private void update() {
        LEVEL.update();
    }

    private void render() {
        render.render();
    }

    public static Player getClientPlayer() {
        return LEVEL.getPlayer();
    }

    public static Controller getPlayerController() {
        return getClientPlayer().getController();
    }
}

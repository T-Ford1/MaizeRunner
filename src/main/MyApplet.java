
package main;

import java.applet.Applet;

/**
 *
 * @author ford.terrell
 */
public class MyApplet extends Applet {
    
    public void start() {
        GameStart.start();
        add(GameStart.root);
    }
    
    public void stop() {
        removeAll();
        GameStart.root = null;
        GameStart.panel = null;
        Main.DISPLAY = null;
        Main.frame = null;
    }
}

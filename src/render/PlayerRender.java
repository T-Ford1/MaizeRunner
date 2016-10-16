/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package render;

import game.Level;
import game.entity.mob.player.Player;
import graphics.Texture;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import thread.TimerMaster;

/**
 *
 * @author ford.terrell
 */
public class PlayerRender {
    
    public static void render(BufferedImage b, int xOff, int yOff) {
        int[] pixels = ((DataBufferInt) b.getRaster().getDataBuffer()).getData();
        Player p = TimerMaster.getClientPlayer();
        Texture t = p.getTexture();
        for (int y = 0; y < t.getHeight(); y++) {
            int yPos = (int) ((y + p.getYDouble()) * Level.ZOOM) - yOff;
            for (int x = 0; x < t.getWidth(); x++) {
                int xPos = (int) ((x + p.getXDouble()) * Level.ZOOM) - xOff;
                renderPixel(b, pixels, xPos, yPos, t.getPixel(x, y));
            }
        }
        t = p.getTurretTexture();
        for (int y = 0; y < t.getHeight(); y++) {
            int yPos = (int) ((5 + y + p.getYDouble()) * Level.ZOOM) - yOff;
            for (int x = 0; x < t.getWidth(); x++) {
                int xPos = (int) ((5 + x + p.getXDouble()) * Level.ZOOM) - xOff;
                renderPixel(b, pixels, xPos, yPos, t.getPixel(x, y));
            }
        }
    }
    
    private static void renderPixel(BufferedImage b, int[] pixels, int xOff, int yOff, int rgb) {
        if(rgb == 0xFF_FF_00_FF) return;
        for (int y = 0; y < Level.ZOOM; y++) {
            int yPos = y + yOff;
            if(yPos < 0 || yPos >= b.getHeight()) continue;
            for (int x = 0; x < Level.ZOOM; x++) {
                int xPos = x + xOff;
                if(xPos < 0 || xPos >= b.getWidth()) continue;
                pixels[yPos * b.getWidth() + xPos] = rgb;
            }
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package render;

import game.Level;
import graphics.Texture;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import thread.TimerMaster;

/**
 *
 * @author ford.terrell
 */
public class BGRender {
    
    public static void render(BufferedImage b, int x, int y) {
        int[] pix = ((DataBufferInt) (b.getRaster().getDataBuffer())).getData();
        int xMax = x + Screen.WIDTH;
        int yMax = y + Screen.HEIGHT;
        int size = Level.SIZE * Level.ZOOM;
        
        int xLeft = x / size;
        int xRight = xMax / size;
        int yTop = y / size;
        int yBottom = yMax / size;
        
        if(x < 0) xLeft--;
        if(y < 0) yTop--;
        
        int xOff = xLeft * size - x;
        int yOff = yTop * size - y;
        
        for (int yTile = yTop; yTile <= yBottom; yTile++) {
            int yPos = (yTile - yTop) * size + yOff;
            for (int xTile = xLeft; xTile <= xRight; xTile++) {
                int xPos = (xTile - xLeft) * size + xOff;
                renderTile(b, pix, TimerMaster.LEVEL.getTile(xTile, yTile), xPos, yPos);
                Texture zone = TimerMaster.LEVEL.getZone(xTile, yTile);
                if(zone != null) {
                    renderTile(b, pix, zone, xPos, yPos);
                }
            }
        }
    }
    
    private static void renderTile(BufferedImage b, int[] pixels, Texture t, int xOff, int yOff) {
        for (int y = 0; y < t.getHeight(); y++) {
            int yPos = y * Level.ZOOM+ yOff;
            
            for (int x = 0; x < t.getWidth(); x++) {
                int xPos = x * Level.ZOOM + xOff;
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

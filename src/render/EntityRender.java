/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package render;

import game.Level;
import game.entity.Entity;
import game.entity.mob.AIMob;
import graphics.Texture;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 *
 * @author ford.terrell
 */
public class EntityRender {
    
    public static void render(BufferedImage b, int xOff, int yOff) {
        int[] pixels = ((DataBufferInt) b.getRaster().getDataBuffer()).getData();
        for (AIMob m : Level.mobs) {
            renderEntity(b, pixels, m, xOff, yOff, !m.isFacingLeft());
        }
        for (Entity e : Level.entities) {
            renderEntity(b, pixels, e, xOff, yOff, false);
        }
    }
    
    private static void renderEntity(BufferedImage b, int[] pixels, Entity e, int xOff, int yOff, boolean flip) {
        Texture t = e.getTexture();
        int xMod = flip ? -1 : 1;
        int lenMod = flip ? t.getWidth() - 1 : 0;
        int xPos = (int) (e.getXDouble() * Level.ZOOM - xOff);
        int yPos = (int) (e.getYDouble() * Level.ZOOM - yOff);
        if(xPos + t.getWidth() * Level.ZOOM < 0 || xPos >= b.getWidth()) return;
        if(yPos + t.getHeight() * Level.ZOOM < 0 || yPos >= b.getHeight()) return;
        
        for (int y = 0; y < t.getHeight(); y++) {
            for (int x = 0; x < t.getWidth(); x++) {
                renderPixel(b, pixels, xPos + x * Level.ZOOM, yPos + y * Level.ZOOM, t.getPixel(x * xMod + lenMod, y));
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

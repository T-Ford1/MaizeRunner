/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import java.awt.Color;

/**
 *
 * @author Terrell
 */
public class MessageMaker {
    
    public static Texture getMessage(String m, int size) {
        return getMessage(m, new Color(0xFF_FF_00_FF), new Color(0xFF_00_00_00), size);
    }
    
    public static Texture getMessage(String m, Color invisColor, Color regColor, int size) {
        Texture s = new Texture(0xFF_FF_00_FF, size * m.length(), size);
        for (int i = 0, xOff = 0; i < m.length(); i++, xOff += size) {
            int index = TextureSheet.ALPHA.indexOf(m.charAt(i) + "");
            if(index < 0) {
                //unparsable char
                continue;
            }
            Texture let = Texture.scaleTexture(TextureSheet.ALPHABET.getTextures()[index], size, size);
            for (int y = 0; y < let.getHeight(); y++) {
                for (int x = 0; x < let.getWidth(); x++) {
                    s.setPixel(x + xOff, y, let.getPixel(x, y));
                }
            } 
        }
        s.setColor(regColor.getRGB());
        s.setInvisibleColor(invisColor.getRGB());
        return s;
    }
}

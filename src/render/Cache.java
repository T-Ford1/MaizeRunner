/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package render;

import java.awt.image.BufferedImage;

/**
 *
 * @author terre
 */
public class Cache {

    public static BufferedImage newFrame() {
        return new BufferedImage(Screen.WIDTH, Screen.HEIGHT, BufferedImage.TYPE_INT_RGB);
    }
}

package graphics;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 *
 * @author ford.terrell
 */
public class Texture implements Renderable {

    public static final Texture DEFAULT = new Texture(0xFF_00_00_FF, 32, 32);

    protected final int WIDTH, HEIGHT;
    protected final int[] pixels;

    /**
     * creates a Texture stretched or shrinked to the width and height required
     * from the Renderable r
     *
     * @param xOff the x position that the Texture starts in img
     * @param yOff the y position the Texture starts in img
     * @param w width
     * @param h height
     * @param img the image file used
     */
    protected Texture(int xOff, int yOff, int w, int h, BufferedImage img) {
        WIDTH = w;
        HEIGHT = h;
        pixels = new int[w * h];
        img.getRGB(xOff, yOff, w, h, pixels, 0, w);
    }

    /**
     * a Texture colored totally c and w wide and h high
     *
     * @param rgb the red, green, blue color in byte form
     * @param w width
     * @param h height
     */
    public Texture(int rgb, int w, int h) {
        WIDTH = w;
        HEIGHT = h;
        pixels = new int[w * h];
        setColor(rgb);
    }

    /**
     * a copy of s
     *
     * @param s Renderable subclass
     */
    public Texture(Renderable s) {
        WIDTH = s.getWidth();
        HEIGHT = s.getHeight();
        pixels = new int[s.getPixels().length];
        System.arraycopy(s.getPixels(), 0, pixels, 0, pixels.length);
    }

    /**
     * a Texture which takes up the entire image file
     *
     * @param p a path to a file
     */
    public Texture(String p) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(p));
        } catch (IOException e) {
            img = DEFAULT.getImage();
        } finally {
            pixels = new int[(HEIGHT = img.getHeight()) * (WIDTH = img.getWidth())];
            img.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
        }
    }

    public Texture(int[] p, int w) {
        WIDTH = w;
        HEIGHT = p.length / w;
        pixels = new int[WIDTH * HEIGHT];
        System.arraycopy(p, 0, pixels, 0, pixels.length);
    }

    public BufferedImage getImage() {
        if(WIDTH == 0 || HEIGHT == 0) return null;
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
        return img;
    }

    public int[] getPixels() {
        return pixels;
    }

    public int getPixel(int x, int y) {
        return pixels[y * WIDTH + x];
    }
    
    public void setPixel(int x, int y, int rgb) {
        pixels[y * WIDTH + x] = rgb;
    }

    /**
     * sets the area not invisible to the color
     * @param rgb
     */
    public final void setColor(int rgb) {
        for (int i = 0; i < pixels.length; i++) {
            if(pixels[i] != 0xFF_FF_00_FF) {
                pixels[i] = rgb;
            }
        }
    }
    
    /**
     * sets the invisible pixels to the color
     * @param rgb
     */
    public final void setInvisibleColor(int rgb) {
        for (int i = 0; i < pixels.length; i++) {
            if(pixels[i] == 0xFF_FF_00_FF) {
                pixels[i] = rgb;
            }
        }
    }

    public int getHeight() {
        return HEIGHT;
    }

    public int getWidth() {
        return WIDTH;
    }

    public void update() {
    }

    public Texture copyOf() {
        return new Texture(this);
    }
    
    public static Texture scaleTexture(Renderable r, double scale) {
        return scaleTexture(r, (int) (r.getWidth() * scale), (int) (r.getHeight() * scale));
    }
    
    /**
     * creates a Texture stretch or shrink to the width and height required
     * from the Renderable r
     *
     * @param w width
     * @param h height
     * @param r the original Texture
     */
    public static Texture scaleTexture(Renderable r, int w, int h) {
        Texture toRet = new Texture(0xFF_00_00_00, w, h);
        int index = 0;
        for (int y = 0; y < h; y++) {
            int yPos = (int) (((double) y / h) * r.getHeight());
            for (int x = 0; x < w; x++) {
                int xPos = (int) (((double) x / w) * r.getWidth());
                toRet.pixels[index++] = r.getPixel(xPos, yPos);
            }
        }
        return toRet;
    }
}

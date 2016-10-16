package graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author ford.terrell
 */
public class TextureSheet {

    public final int COLUMNS, ROWS;
    private final Texture[] Textures;
    
    public TextureSheet(String p, int w, int h) {
        BufferedImage image;
        try {
            image = ImageIO.read(new File(p));
            int[] pixels = new int[image.getWidth() * image.getHeight()];
            image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
        } catch (IOException ex) {
            image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
        }
        COLUMNS = image.getWidth() / w;
        ROWS = image.getHeight() / h;
        Textures = new Texture[COLUMNS * ROWS];
        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                Textures[y * COLUMNS + x] = new Texture(x * w, y * h, w, h, image);
            }
        }
    }

    public TextureSheet(String p, int s) {
        this(p, s, s);
    }

    public Texture getTexture(int x, int y) {
        return Textures[y * COLUMNS + x];
    }
    
    public Texture[] getTextures() {
        return Textures;
    }
    
    public void setTexture(Texture s, int x, int y) {
        Textures[y * COLUMNS + x] = s;
    }
    
    public void setTextures(Texture[] s) {
        for (int i = 0; i < Textures.length & i < s.length; i++) {
            Textures[i] = s[i];
        }
    }
    
    public static final TextureSheet ALPHABET = new TextureSheet("res/alpha.png", 8);
    public static final String ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890!?()-=+[]%' .:,\"";
    
    public static final TextureSheet WASP_SHEET = new TextureSheet("res/wasp.png", 20);
    public static final TextureSheet PARTICLE_SHEET = new TextureSheet("res/particle.png", 1);
    public static final TextureSheet BULLET_SHEET = new TextureSheet("res/bullet.png", 4);
    public static final TextureSheet FIRE_SHEET = new TextureSheet("res/fire.png", 8);
    public static final TextureSheet PLAYER_SHEET = new TextureSheet("res/player.png", 30);
    public static final TextureSheet TURRET_SHEET = new TextureSheet("res/turret.png", 20);
    
    public static final TextureSheet TEXTURE_SHEET = new TextureSheet("res/tiles.png", 16);
    public static final TextureSheet ZONE_SHEET = new TextureSheet("res/hedge.png", 16);
}

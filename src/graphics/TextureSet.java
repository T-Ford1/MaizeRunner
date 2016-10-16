
package graphics;

import static graphics.TextureSheet.*;

/**
 *
 * @author Terrell
 */
public class TextureSet {

    private final int width, height;
    private final Texture[] Textures;
    
    public TextureSet(TextureSheet sheet) {
        this(0, 0, sheet.COLUMNS, sheet.ROWS, sheet);
    }

    public TextureSet(int x, int y, int width, int height, TextureSheet sheet) {
        this.width = width;
        this.height = height;
        Textures = new Texture[width * height];
        for (int y1 = 0; y1 < height; y1++) {
            int yOff = y1 + y;
            for (int x1 = x; x1 < x + width; x1++) {
                int xOff = x1 + x;
                Textures[y1 * width + x1] = sheet.getTexture(xOff, yOff);
            }
        }
    }
    
    private TextureSet(int width, int height, int length) {
        this.width = width;
        this.height = height;
        Textures = new Texture[length];
    }

    public Texture getTexture(int x, int y) {
        return Textures[y * width + x];
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }

    public static final TextureSet FIRE_SET = new TextureSet(FIRE_SHEET);
    public static final TextureSet BULLET_SET = new TextureSet(BULLET_SHEET);
    public static final TextureSet PARTICLE_SET = new TextureSet(PARTICLE_SHEET);
    public static final TextureSet PLAYER_SET = new TextureSet(PLAYER_SHEET);
    public static final TextureSet TURRET_SET = new TextureSet(TURRET_SHEET);
    public static final TextureSet WASP_MOVE = new TextureSet(0, 0, 4, 2, WASP_SHEET);
    public static final TextureSet WASP_ATTACK = new TextureSet(0, 2, 7, 1, WASP_SHEET);
}

package game;

import game.entity.Entity;
import game.entity.mob.AIMob;
import game.entity.mob.Mob;
import game.entity.mob.player.Player;
import game.entity.projectile.Projectile;
import graphics.Texture;
import graphics.TextureSheet;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import render.Screen;
import thread.RenderMaster;
import thread.TimerMaster;

/**
 *
 * @author Terrell
 */
public class Level {

    public static final int SIZE = 16;
    public static final int ZOOM = 4;
    public static int TIME = -1, KILLS = 0;

    public static final ArrayList<AIMob> mobs = new ArrayList<>();
    public static final ArrayList<Entity> entities = new ArrayList<>();
    public static Dimension mapSize/*Textures*/, mapPixels;/*pixels*/

    private static Player player;
    private int update;

    protected BufferedImage map, zn;
    protected int[] textures;
    protected int[] spriteSheet;
    protected int[] zones;

    public Level() {
        init();
    }

    public void resetAll() {
        mobs.clear();
        entities.clear();
        player = new Player();
        update = 0;
        KILLS = 0;
    }
    
    public void activateClock() {
        TIME = 180;
    }
    
    public void deactivateClock() {
        TIME = -1;
    }

    protected void init() {
        try {
            map = ImageIO.read(new File("res/level.png"));
            mapSize = new Dimension(map.getWidth(), map.getHeight());
            mapPixels = new Dimension(map.getWidth() * Level.SIZE, map.getHeight() * Level.SIZE);
            textures = map.getRGB(0, 0, map.getWidth(), map.getHeight(), null, 0, map.getWidth());
            spriteSheet = new int[textures.length];
            for (int i = 0; i < textures.length; i++) {
                switch (textures[i]) {
                    case 0xFF_17_FF_0F://RGB(23, 255, 15) LIGHT GREEN
                        spriteSheet[i] = adjacency(map, i);
                        break;
                    case 0xFF_00_7F_0E://RGB(0, 127, 14) DARK GREEN
                        /*
                         There are two cases of what could be beside dark green:
                         -brown or light green, so two sets of adjacency tiles
                         have been put into the spritesheet, one for each case.
                         */
                        spriteSheet[i] = 16 + darkGreenAdjacency(map, i);
                        break;
                    case 0xFF_7C_62_2C://new Color(124, 98, 44) BROWN
                        spriteSheet[i] = 48 + adjacency(map, i);
                        break;
                    default:
                        System.out.println("PROBLEM AT: " + i % map.getWidth() + ", " + (i / map.getWidth()));
                        System.out.println("DATA IS " + Integer.toHexString(textures[i]));
                        break;
                }
            }
        } catch (IOException e) {
        }
        try {
            zn = ImageIO.read(new File("res/zones.png"));
            zones = new int[zn.getHeight() * zn.getWidth()];
            for (int y = 0; y < zn.getHeight(); y++) {
                for (int x = 0; x < zn.getWidth(); x++) {
                    int i = y * zn.getWidth() + x;
                    switch (zn.getRGB(x, y)) {
                        case 0xFF_00_00_00://RGB(0, 0, 0) BLACK
                            zones[i] = zoneAdjacency(zn, x, y);
                            break;
                        default:
                            zones[i] = -1;
                            break;
                    }
                }
            }
        } catch (IOException e) {
        }
    }

    /**
     * No dark greens are directly touching the edge of the map, so no
     * complicated checks for IndexOutOfBoundsExceptions are needed
     *
     * @param map
     * @param i
     * @return
     */
    private int darkGreenAdjacency(BufferedImage map, int i) {
        int x = i % map.getWidth();
        int y = i / map.getWidth();
        int val = adjacency(map, i);
        if (val == 0) {
            return val;
        }
        int[] adjacent = new int[]{
            map.getRGB(x, y - 1),
            map.getRGB(x - 1, y),
            map.getRGB(x, y + 1),
            map.getRGB(x + 1, y)
        };
        for (int adj = 0; adj < adjacent.length; adj++) {
            if (adjacent[adj] == 0xFF_17_FF_0F) {//its next to a light green
                return val;//this index is correct for light green tiles
            } else if (adjacent[adj] == 0xFF_7C_62_2C) {//its next to brown
                return val + 16;//the brown tiles shifted 16 over in indexes
            }
        }
        return val;
    }

    /**
     * adjacency(x, y) looks checks if the terrain is different adjacent to each
     * face of the tile specified. The hedge which forms the maze barriers have
     * to be opposite this with small adjustments for the edges of the map.
     *
     * @param map
     * @param x
     * @param y
     * @return
     */
    private int zoneAdjacency(BufferedImage map, int x, int y) {
        int notAdjacent = adjacency(map, x, y);
        if (x == 0) {
            notAdjacent |= 2;//2 is left. This is the left side of the map.
        }
        if (x == map.getWidth() - 1) {
            notAdjacent |= 8;//8 is right.
        }
        if (y == 0) {
            notAdjacent |= 1;//1 is up.
        }
        if (y == map.getHeight() - 1) {
            notAdjacent |= 4;//4 is down.
        }
        return 15 - notAdjacent;
    }

    private int adjacency(BufferedImage map, int i) {
        int x = i % map.getWidth();
        int y = i / map.getWidth();
        return adjacency(map, x, y);
    }

    /**
     * adjacency outputs the index of a tile which has different types of tiles
     * near it. ex: dark green with brown above it and dark green below, left,
     * and right would be 1 (up) + 0 (left) + 0 (down) + 0 (right) 1 = up, 2 =
     * left, 4 = down, 8 = right
     *
     * @param map
     * @param i
     * @return
     */
    private int adjacency(BufferedImage map, int x, int y) {
        int value = 0;
        int color = map.getRGB(x, y);
        if (y - 1 >= 0) {
            int up = map.getRGB(x, y - 1);
            if (up != color) {
                value += 1;
            }
        }
        if (x - 1 >= 0) {
            int left = map.getRGB(x - 1, y);
            if (left != color) {
                value += 2;
            }
        }
        if (y + 1 < map.getHeight()) {
            int down = map.getRGB(x, y + 1);
            if (down != color) {
                value += 4;
            }
        }
        if (x + 1 < map.getWidth()) {
            int right = map.getRGB(x + 1, y);
            if (right != color) {
                value += 8;
            }
        }
        return value;
    }

    public final void add(Entity e) {
        if (e instanceof AIMob) {
            mobs.add((AIMob) e);
        } else {
            entities.add(e);
        }
    }

    public void update() {
        if(Screen.INPUT.ESCAPE) {
            System.exit(0);
        }
        for (int i = 0; i < mobs.size(); i++) {
            if (mobs.get(i).isRemoved()) {
                if(mobs.get(i) instanceof AIMob) {
                    KILLS++;
                }
                mobs.remove(i);
                i--;
            } else {
                mobs.get(i).update();
            }
        }
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).isRemoved()) {
                entities.remove(i);
                i--;
            } else {
                entities.get(i).update();
            }
        }
        player.update();
        update++;
        if(update % 60 == 0) {
            int xPos = (int) (Math.random() * zn.getWidth());
            int yPos = (int) (Math.random() * zn.getHeight());
            if(!RenderMaster.isOnTileScreen(xPos, yPos)) {
                add(new AIMob(xPos * SIZE, yPos * SIZE));
            }
            TIME--;
        }
        if(TIME == 0) {
            TimerMaster.running = false;
        }
    }

    public Texture getTile(int x, int y) {
        int index = x + y * map.getWidth();
        if (index >= 0 && index < spriteSheet.length && x >= 0 && x < map.getWidth() && y >= 0) {
            return TextureSheet.TEXTURE_SHEET.getTexture(spriteSheet[index], 0);
        }
        return TextureSheet.TEXTURE_SHEET.getTexture(0, 3);
    }

    public Texture getZone(int x, int y) {
        int index = x + y * map.getWidth();
        if (index >= 0 && index < zones.length && x >= 0 && x < map.getWidth() && y >= 0) {
            int texIndex = zones[index];
            if (texIndex >= 0 && texIndex < 16) {
                return TextureSheet.ZONE_SHEET.getTexture(zones[index], 0);
            }
        }
        return null;
    }

    public Texture getTilePixels(int x, int y) {
        return getTile(x / Level.SIZE, y / Level.SIZE);
    }
    
    public double getTileSpeed(Entity e, int x, int y) {
        if(getZone(x, y) == null) return 1.0;
        return 1.0 / e.getZoneMod();
    }

    public Dimension getMapSize() {
        return mapSize;
    }

    public Dimension getMapPixels() {
        return mapPixels;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean collision(Entity e) {
        if (e instanceof Player) {
            return false;
        }
        for (Mob m : mobs) {
            if (m == e) {
                continue;
            }
            if (e.isCollided(m)) {
                return doDamage(e, m);
            }
        }
        return false;
    }

    private boolean doDamage(Entity e, Mob m) {
        if (e instanceof Projectile) {
            Projectile p = (Projectile) e;
            return doDamage(p, m);
        }
        return false;
    }

    private boolean doDamage(Projectile p, Mob m) {
        //makes sure only a projectile fired from an enemy damages the
        //player or the player's projectile damages the enemy
        m.doDamage(p.getDamage());
        return true;
    }
    
    public static int getWidth() {
        return Level.mapSize.width;
    }
    
    public static int getHeight() {
        return Level.mapSize.height;
    }
}

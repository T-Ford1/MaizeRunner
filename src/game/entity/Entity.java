
package game.entity;

import game.Level;
import graphics.Texture;
import graphics.TextureSet;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;
import thread.TimerMaster;

/**
 *
 * @author ford.terrell
 */
public abstract class Entity {
    
    protected static final Random random = new Random();
    protected TextureSet animation;
    protected boolean moving, removed, entityCollide;
    protected int update, move, dir;
    protected double zoneMod;
    protected final int spawnX, spawnY;
    protected Rectangle bounds;
    
    private double x, y, xRemain, yRemain;

    public Entity(int xSpwn, int ySpwn) {
        entityCollide = false;
        x = spawnX = xSpwn;
        y = spawnY = ySpwn;
        update = random.nextInt(5);
    }

    public void update() {
        update++;
    }

    protected Rectangle getBounds() {
        Texture t = getTexture();
        return new Rectangle(getX(), getY(), t.getWidth(), t.getHeight());
    }

    protected Rectangle getTileBounds() {
        Rectangle pos = getBounds();
        int size = Level.SIZE;
        int left = pos.x / size;
        int right = (pos.x + pos.width) / size;
        int top = pos.y / size;
        int bottom = (pos.y + pos.height) / size;
        int width = (right - left) + 1;
        int height = (bottom - top) + 1;
        return new Rectangle(left, top, width, height);
    }
    
    public abstract Texture getTexture();

    protected abstract void onCollision();

    protected void move(double x, double y, boolean slow) {
        moving = x != 0 | y != 0;
        setDir(x, y);
        if (slow) {
            x /= 4.0;
            y /= 4.0;
        }
        int oldX = getX();
        int oldY = getY();
        move(x, y);
        if ((getX() != oldX | getY() != oldY) && isZoneCollided()) {
            onCollision();
            //if the x,y position has changed, check if the
            //entity is collided. If so, move back a step.
            move(-x, -y);
            //if the player/entity is totally trapped, allow
            //them to move a tiny bit.
            move(x * zoneMod, y * zoneMod);
            if (x != 0) {
                setDir(x, 0);
                xStep(x);
            }
            if (y != 0) {
                setDir(0, y);
                yStep(y);
            }
        }
    }

    private void xStep(double x) {
        int step = x < 0 ? -1 : 1;
        int xDist = (int) (xRemain + x);
        xRemain += x - xDist;
        for (int i = 0; i != xDist;) {
            move(step, 0);
            if (isZoneCollided()) {
                move(-step, 0);
                break;
            }
            i += dir == 1 ? 1 : -1;
        }
    }

    private void yStep(double y) {
        int step = y < 0 ? -1 : 1;
        int yDist = (int) (yRemain + y);
        yRemain += y - yDist;
        for (int i = 0; i != yDist;) {
            move(0, step);
            if (isZoneCollided()) {
                move(0, -step);
                break;
            }
            i += dir == 3 ? 1 : -1;
        }
    }

    private void move(double xChange, double yChange) {
        x += xChange;
        y += yChange;
    }

    private void setDir(double x, double y) {
        if (dir == 1 | dir == 0) {
            if (x != 0) {
                dir = x > 0 ? 1 : 0;
            } else {
                dir = y > 0 ? 3 : 2;
            }
        } else {
            if (y != 0) {
                dir = y > 0 ? 3 : 2;
            } else {
                dir = x > 0 ? 1 : 0;
            }
        }
    }
    
    public boolean isZoneCollided(Texture tex) {
        int xOff = getX() % Level.SIZE;
        int yOff = getY() % Level.SIZE;
        //0 to Level.SIZE, how far from the last tile edge the entity is
        Texture[][] zones = getZones();
        for (int i = 0; i < zones.length; i++) {
            for (int j = 0; j < zones[i].length; j++) {
                if(isPixelCollided(zones[i][j], tex, j * Level.SIZE - xOff, i * Level.SIZE - yOff)) {
                    return true;
                }
            }
        }
        if (entityCollide) {
            return TimerMaster.LEVEL.collision(this);
        }
        return false;
    }

    public boolean isZoneCollided() {
        return isZoneCollided(getTexture());
    }
    
    private boolean isPixelCollided(Texture one, Texture two, int xOff, int yOff) {
        if(one == null || two == null) return false;
        for (int y = 0; y < one.getHeight(); y++) {
            int yPos = y + yOff;
            if(yPos < 0 || yPos >= two.getHeight()) continue;
            for (int x = 0; x < one.getWidth(); x++) {
                int xPos = x + xOff;
                if(xPos < 0 || xPos >= two.getWidth()) continue;
                if(one.getPixel(x, y) == 0xFF_FF_00_FF) continue;
                if(two.getPixel(xPos, yPos) == 0xFF_FF_00_FF) continue;
                return true;
            }
        }
        return false;
    }
    
    private Texture[][] getZones() {
        Rectangle r = getTileBounds();
        Texture[][] zones = new Texture[r.height][r.width];
        for (int y = 0; y < r.height; y++) {
            for (int x = 0; x < r.width; x++) {
                zones[y][x] = TimerMaster.LEVEL.getZone(x + r.x, y + r.y);
            }
        }
        return zones;
    }

    public boolean isCollided(Entity other) {
        Rectangle bds = getBounds();
        Rectangle oBds = other.getBounds();
        boolean collide = bds.intersects(oBds);
        if(!collide) return false;
        int xOff = other.getX() - getX();
        int yOff = other.getY() - getY();
        int areaOne = bds.height * bds.width;
        int areaTwo = oBds.height * oBds.width;
        if(areaOne > areaTwo) return isPixelCollided(other.getTexture(), getTexture(), xOff, yOff);
        else return isPixelCollided(getTexture(), other.getTexture(), -xOff, -yOff);
    }

    public boolean isMoving() {
        return moving;
    }

    public void remove() {
        removed = true;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setPosition(double xPos, double yPos) {
        x = xPos;
        y = yPos;
    }

    public Point getPos() {
        return new Point(getX(), getY());
    }

    public Point getTilePos() {
        return new Point(getTileX(), getTileY());
    }

    public int getX() {
        return (int) x;
    }

    public double getXDouble() {
        return x;
    }

    public double getYDouble() {
        return y;
    }
    
    public double getZoneMod() {
        return zoneMod;
    }

    public int getY() {
        return (int) y;
    }

    public int getTileX() {
        return getX() / Level.SIZE;
    }

    public int getTileY() {
        return getY() / Level.SIZE;
    }

    public int getSpawnX() {
        return spawnX;
    }

    public int getSpawnY() {
        return spawnY;
    }

    public Point getTileSpawnPoint() {
        return new Point(spawnX/ Level.SIZE, spawnY / Level.SIZE);
    }

    public void setRemoved(boolean rm) {
        removed = rm;
    }

    public double getDistance(Entity other) {
        return other.getPos().distance(getPos());
    }

    public double getDistanceSqr(Entity other) {
        return other.getPos().distanceSq(getPos());
    }

    protected double getDistanceSqr(double x, double z) {
        return x * x + z * z;
    }

    protected double getDistance(double x, double z) {
        return Math.sqrt(getDistanceSqr(x, z));
    }

    public String toString() {
        return "abstract entity " + hashCode();
    }
}

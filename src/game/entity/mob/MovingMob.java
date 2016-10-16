package game.entity.mob;

import game.Level;
import game.ai.AStarPath;
import graphics.Texture;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author ford.terrell
 */
public abstract class MovingMob extends AttackMob {
    
    public boolean facingLeft;
    
    private double lastX, lastY;
    
    private int moveTexture;
    private ArrayList<Point> target;
    public Mob enemy;

    public MovingMob(int x, int y) {
        super(x, y);
        facingLeft = true;
        move = 1;
    }
    
    public void update() {
        super.update();
        lastX = getXDouble();
        lastY = getYDouble();
        if(update % 600 == 0) {
            enemy = nextTarget();
            target = AStarPath.getPath(this, nextDestination());
        }
        if (enemy == null) {
            enemy = nextTarget();
        }
        if (target == null || target.isEmpty()) {
            target = AStarPath.getPath(this, nextDestination());
        }
        if (target != null && !target.isEmpty()) {
            moveToTarget(target.get(target.size() - 1));
        }
        if(moving && update % 10 == 0) {
            moveTexture = (moveTexture + 1) % animation.getWidth();
        } else if(!moving) {
            moveTexture = 0;
        }
        if(lastX != getXDouble()) {
            facingLeft = getXDouble() < lastX;
        }
    }
    
    public Texture getTexture() {
        if(!isAttacking && moving) return animation.getTexture(moveTexture, 1);
        return super.getTexture();
    }
    
    private void moveToTarget(Point p) {
        Point tPix = new Point(p.x * Level.SIZE + Level.SIZE / 2, p.y * Level.SIZE + Level.SIZE / 2);
        int x = 0, y = 0;
        if (getX() < tPix.x) {
            x = move;
        } else if (getX() > tPix.x) {
            x = -move;
        }
        if (getY() < tPix.y) {
            y = move;
        } else if (getY() > tPix.y) {
            y = -move;
        }
        super.move(x, y, false);
        if (getTilePos().equals(p)) {
            target.remove(p);
        }
    }
    
    public boolean isFacingLeft() {
        return facingLeft;
    }
    
    protected abstract Mob nextTarget();
    
    protected abstract Point nextDestination();
}

package game.entity.mob;

import game.entity.Entity;
import game.entity.projectile.Bullet;
import game.entity.projectile.Projectile;
import graphics.Texture;
import graphics.TextureSet;
import java.awt.Point;
import thread.TimerMaster;

/**
 *
 * @author Terrell
 */
public abstract class Mob extends Entity {

    protected int anim, fire, healthMax, health;

    public Mob(int x, int z) {
        super(x, z);
        healthMax = 500;
        health = healthMax;
        animation = TextureSet.PLAYER_SET;
        move = 2;
    }

    protected void shoot(Mob target) {
        shoot(target.getPos());
    }

    public int getMaxHealth() {
        return healthMax;
    }

    public void setHealth(int hp) {
        if (hp > healthMax) {
            health = healthMax;
        } else {
            health = hp;
        }
    }

    public Texture getTexture() {
        return animation.getTexture(dir, anim);
    }

    protected void shoot(Point target) {
        if (fire > 0) {
            return;
        }
        Point start = getPos();
        int xTravel = target.x - start.x;
        int yTravel = target.y - start.y;
        double theta = Math.atan2(yTravel, xTravel);
        Projectile proj = new Bullet(this, getX(), getY(), theta);
        fire = proj.getFireRate();
        TimerMaster.LEVEL.add(proj);
    }

    public void doDamage(int hp) {
        health -= hp;
    }
    
    public int getHealth() {
        return health;
    }

    public void update() {
        if (fire > 0) {
            fire--;
        }
        if (health <= 0) {
            removed = true;
        }
        if (health < healthMax) {
            health++;
        }
        if (update % 10 == 0 && isMoving()) {
//            anim = (anim + 1) % animation.getHeight();
        } else if (!isMoving()) {
            dir = 3;
            anim = 0;
        }
        super.update();
    }

    protected void onCollision() {
    }

    //protected abstract void onCollisionTile(Tile collide);
    //protected abstract void onCollisionEntity(Entity collide);
    public String toString() {
        return "mob " + hashCode();
    }
}

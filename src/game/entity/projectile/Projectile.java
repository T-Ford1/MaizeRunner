/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.entity.projectile;

import game.entity.Entity;
import game.entity.mob.Mob;
import graphics.Texture;
import graphics.TextureSet;

/**
 *
 * @author ford.terrell
 */
public abstract class Projectile extends Entity {

    protected int rateOfFire = 20, damage = 200;
    public static final double speed = 2.5, range = 1000, rangeSqr = range * range;
    protected final Mob source;
    protected int index = 0;

    private final double xStep, yStep, theta;

    public Projectile(Mob s, int spwnX, int spwnY, double t) {
        super(spwnX, spwnY);
        entityCollide = true;
        animation = TextureSet.FIRE_SET;
        theta = t;
        source = s;
        xStep = Math.cos(theta) * speed;
        yStep = Math.sin(theta) * speed;
        move((Math.cos(theta) * 0), (Math.sin(theta) * 0), false);
    }
    
    public Texture getTexture() {
        return animation.getTexture(index, 0);
    }

    protected void onCollision() {
        removed = true;
        for (int i = 0; i < 20; i++) {
            addParticles();
        }
    }
    
    protected abstract void addParticles();

    public void update() {
        if (rangeSqr < getDistanceSqr(getX() - getSpawnX(), getY() - getSpawnY())) {
            removed = true;
        }
        if (!removed) {
            move(xStep, yStep, false);
            super.update();
        }
    }

    public int getFireRate() {
        return rateOfFire;
    }
    
    public Mob getSource() {
        return source;
    }
    
    public int getDamage() {
        return damage;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.entity.particle;

import graphics.TextureSet;
import game.entity.Entity;
import graphics.Texture;

/**
 *
 * @author Terrell
 */
public abstract class Particle extends Entity {
    
    protected Entity source;
    protected int life, range, index;
    private final double speed;
    private double zStep, xStep, yStep;
    
    public Particle(Entity src, int xSpwn, int ySpwn, int l, double s) {
        super(xSpwn, ySpwn);
        life = l;
        speed = s;
        source = src;
        animation = TextureSet.PARTICLE_SET;
        xStep = random.nextGaussian() * speed;
        yStep = random.nextGaussian() * speed;
        index = 0;
    }
    
    public void update() {
        if (update >= life) {
            removed = true;
        }
        zStep += .05;
        move(xStep, yStep + zStep, false);
        super.update();
    }
    
    public Texture getTexture() {
        return animation.getTexture(index, 0);
    }
    
    protected void onCollision() {
        removed = true;
    }
}
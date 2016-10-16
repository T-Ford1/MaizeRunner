/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.entity.projectile;

import game.entity.mob.Mob;
import graphics.Texture;
import graphics.TextureSet;

/**
 *
 * @author Terrell
 */
public class Bullet extends Projectile {

    public Bullet(Mob s, int spwnX, int spwnY, double t) {
        super(s, spwnX, spwnY, t);
        index = 1;
        damage = 50;
        rateOfFire = 2;
        animation = TextureSet.BULLET_SET;
    }
    
    public Texture getTexture() {
        return animation.getTexture(update % animation.getWidth(), 0);
    }

    protected void addParticles() {
    }
}

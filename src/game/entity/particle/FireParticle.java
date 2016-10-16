/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.entity.particle;


import game.entity.Entity;
import graphics.Texture;

/**
 *
 * @author ford.terrell
 */
public class FireParticle extends Particle {

    public FireParticle(Entity src, int xSpwn, int ySpwn, int l, double s) {
        super(src, xSpwn, ySpwn, l, s);
    }
    
    public Texture getTexture() {
        return animation.getTexture(update % animation.getWidth(), 0);
    }
}

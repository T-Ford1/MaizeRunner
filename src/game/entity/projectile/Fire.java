package game.entity.projectile;

import game.entity.mob.Mob;
import game.entity.particle.FireParticle;
import graphics.Texture;
import thread.TimerMaster;

/**
 *
 * @author ford.terrell
 */
public class Fire extends Projectile {

    public Fire(Mob s, int spwnX, int spwnY, double t) {
        super(s, spwnX, spwnY, t);
        damage = 30;
        rateOfFire = 3;
    }
    
    public Texture getTexture() {
        return animation.getTexture(update % animation.getWidth(), 0);
    }

    protected void addParticles() {
        TimerMaster.LEVEL.add(new FireParticle(this, getX(), getY(), 10 + random.nextInt(25), .5));
    }
}

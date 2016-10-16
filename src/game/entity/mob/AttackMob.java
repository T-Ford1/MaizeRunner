package game.entity.mob;

import graphics.Texture;
import graphics.TextureSet;
import thread.TimerMaster;

/**
 *
 * @author ford.terrell
 */
public class AttackMob extends Mob {
    
    public static int AGGRO_RANGE = 4000;
    public static int ATTACK_RANGE = 256;

    //0 is flying stationary, facing left
    //
    private static TextureSet attack = TextureSet.WASP_ATTACK;
    private static int ATTACK_DAMAGE = 50;
    private static int ATTACK_COOLDOWN = 120;
    protected boolean isAttacking;
    private int attackFrame, renderFrame, lastAttack;

    public AttackMob(int x, int y) {
        super(x, y);
        animation = TextureSet.WASP_MOVE;
        zoneMod = .18;
    }

    public Texture getTexture() {
        if (isAttacking) {
            return attack.getTexture(attackFrame, 0);
        }
        return animation.getTexture(renderFrame, 0);
    }

    public int getX() {
        return super.getX();
    }

    public void update() {
        super.update();
        if (isAttacking && update % 10 == 0) {
            attackFrame++;
        } else if(!isAttacking) {
            attackFrame = 0;
        }
        if (attackFrame >= attack.getWidth()) {
            isAttacking = false;
            renderFrame = 0;
        } else if(update % 10 == 0) {
            renderFrame++;
        }
        if(renderFrame >= animation.getWidth()) {
            renderFrame = 0;
        }
    }
    
    protected void attack() {
        if(isAttacking) return;
        if(update - lastAttack < ATTACK_COOLDOWN) return;
        isAttacking = true;
        lastAttack = update;
        TimerMaster.getClientPlayer().doDamage(ATTACK_DAMAGE);
    }
}

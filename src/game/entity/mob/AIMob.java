/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.entity.mob;

import game.entity.mob.player.Player;
import graphics.TextureSet;
import java.awt.Point;
import thread.TimerMaster;

/**
 *
 * @author Terrell
 */
public class AIMob extends MovingMob {
    
    private static final int NEW_PATH_COOLDOWN = 240;
    
    private int lastNewPos;

    public AIMob(int x, int y) {
        super(x, y);
        animation = TextureSet.WASP_MOVE;
        //aibase has to be set to run... its null, rn
    }

    public void update() {
        if (enemy != null && getDistanceSqr(enemy) < ATTACK_RANGE) {
            attack();
        }
        super.update();
    }

    public String toString() {
        return "aimob" + super.toString();
    }

    protected Mob nextTarget() {
        Player p = TimerMaster.getClientPlayer();
        if (p.getDistanceSqr(this) < AIMob.AGGRO_RANGE) {
            return p;
        }
        return null;
    }

    public Point nextDestination() {
        if (enemy != null) {
            return enemy.getTilePos();
        }
        if(update - lastNewPos < NEW_PATH_COOLDOWN) {
            return getTilePos();
        }
        lastNewPos = update;
        Point p = getTileSpawnPoint();
        Point next = null;
        while (next == null) {
            int x = p.x + random.nextInt(10) - 5;
            int y = p.y + random.nextInt(10) - 5;
            if (TimerMaster.LEVEL.getZone(x, y) == null) {
                next = new Point(x, y);
            }
        }
        return next;
    }
}

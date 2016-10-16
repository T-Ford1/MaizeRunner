package game.entity.mob.player;

import game.Level;
import game.entity.mob.Mob;
import game.entity.projectile.Fire;
import game.entity.projectile.Bullet;
import game.entity.projectile.Projectile;
import graphics.Texture;
import graphics.TextureSet;
import thread.TimerMaster;

/**
 *
 * @author Terrell
 */
public class Player extends Mob {

    private final Controller input;
    private TextureSet turret;
    private static final double TANGENT_WIDTH = 7.0;
    private static final double NORMAL_WIDTH = 7.0;

    public Player() {
        super(33 * Level.SIZE, 33 * Level.SIZE);
        input = new Controller(this, getX(), getY());
        healthMax = 2000;
        health = 1000;
        animation = TextureSet.PLAYER_SET;
        turret = TextureSet.TURRET_SET;
        zoneMod = .02;
    }
    
    protected void shoot() {
        if (fire > 0) {
            return;
        }
        int button = input.getButton();
        double theta = input.turretAngle;
        Projectile proj;
        double normal = theta + Math.PI / 2.;
        double y1 = Math.sin(normal) * NORMAL_WIDTH;
        double x1 = Math.cos(normal) * NORMAL_WIDTH;
        double y2 = Math.sin(theta) * TANGENT_WIDTH;
        double x2 = Math.cos(theta) * TANGENT_WIDTH;
        if(button == 1) {
            proj = new Fire(this, (int) (getX() + 12 - x1 + x2), (int) (getY() + 12 - y1 + y2), theta);
        } else if(button == 3) {
            proj = new Bullet(this, (int) (getX() + 14 + x1 + x2), (int) (getY() + 14 + y1 + y2), theta);
        } else {
            return;
        }
        TimerMaster.LEVEL.add(proj);
        fire = proj.getFireRate();
    }
    
    protected void updateIndices() {
        
    }

    public void remove() {
        super.remove();
    }
    
    protected void move(double x, double z) {
        super.move(x, z, false);
    }
    
    public Texture getTexture() {
        return animation.getTexture(getIndex(animation, -input.horAngle - Math.PI / 2.), 0);
    }
    
    public Texture getTurretTexture() {
        return turret.getTexture(getIndex(turret, -input.turretAngle - Math.PI / 2.), 0);
    }
    
    private static double regularAngle(double angle) {
        double temp = angle;
        while(temp < 0) temp += Math.PI * 2.;
        while(temp >= Math.PI * 2.) temp -= Math.PI * 2.;
        return temp;
    }

    public void update() {
        setPosition(input.x, input.y);
        double angle = input.horAngle;
        input.update();
        if (input.isMousePressed()) {
            shoot();
        }
        
        boolean collided = isZoneCollided(animation.getTexture(getIndex(animation, -input.horAngle - Math.PI / 2.), 0));
        if(collided) {
            input.horAngle = angle;
        }
        super.update();
    }
    
    private int getIndex(TextureSet s, double angle) {
        double regAngle = regularAngle(angle);//from 0 to 2PI, the player angle
        double percent = regAngle / (Math.PI * 2.);//from 0 to 1, the percent of the way around a circle
        double index = (percent * s.getWidth() * s.getHeight());//which one chosen based on amount of available angles
        return (int) index;
    }
    
    public double getFiringAngle() {
        return input.turretAngle;
    }
    
    public double getRenderAngle() {
        return input.horAngle;
    }
    
    public Controller getController() {
        return input;
    }

    public String toString() {
        return "player";
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.entity.mob.player;

import game.Level;
import render.Screen;

/**
 *
 * @author Terrell
 */
public class Controller {

    public double x, y;
    public double horAngle;
    public double turretAngle;

    private double xv, yv;
    private double move, turn;
    private final Player source;

    private static final double MOVE_STEP = .5;
    private static final double MOVE_DECEL = .90;

    private static final double TURN_STEP = Math.PI / 120;
    private static final double TURN_DECEL = .8;

    private static final double TURRET_STEP = Math.PI / 60;
    
    public Controller(Player src, int x, int y) {
        source = src;
        this.x = x;
        this.y = y;
        horAngle = -Math.PI / 2;
    }

    protected void update() {
        updateMove();
        updateAngle();
    }

    private void updateMove() {
        if (Screen.INPUT.BACK) {
            move = -MOVE_STEP;
        } else if (Screen.INPUT.FWD) {
            move = MOVE_STEP;
        } else {
            move *= MOVE_DECEL;
        }
        xv = move * Math.cos(horAngle);
        yv = move * Math.sin(horAngle);
        source.move(xv, yv);
        x = source.getXDouble();
        y = source.getYDouble();
    }

    private void updateAngle() {
        if (Screen.INPUT.RIGHT) {
            turn = TURN_STEP;
        } else if (Screen.INPUT.LEFT) {
            turn = -TURN_STEP;
        } else {
            turn *= TURN_DECEL;
        }
        horAngle += turn;
        
        int xMouse = Screen.INPUT.getXDistFromMid();
        int yMouse = Screen.INPUT.getYDistFromMid();
        double mouseTheta = Math.atan2(yMouse, xMouse);
        double bearing = normalBearing(mouseTheta, turretAngle);
        if(Math.abs(bearing) < TURRET_STEP) {
            turretAngle += bearing;
        } else if(bearing > 0) {
            turretAngle += TURRET_STEP;
        } else {
            turretAngle -= TURRET_STEP;
        }
    }
    
    private static double normalBearing(double angOne, double angTwo) {
        double dif = angOne - angTwo;
        while(dif < -Math.PI) {
            dif += Math.PI * 2;
        }
        while(dif > Math.PI) {
            dif -= Math.PI * 2;
        }
        return dif;
    }

    public boolean isMousePressed() {
        return Screen.INPUT.isMousePressed();
    }

    public int getButton() {
        return Screen.INPUT.getMouseButton();
    }

    public int getMouseX() {
        return Screen.INPUT.getMouseX();
    }

    public int getMouseY() {
        return Screen.INPUT.getMouseY();
    }
}

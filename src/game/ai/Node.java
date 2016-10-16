/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.ai;

import java.awt.Point;

/**
 *
 * @author Terrell
 */
public class Node implements Comparable<Node> {
    
    private final double hCost, fCost, gCost;
    private final Node parent;
    private final Point pos;
    
    /**
     * this a-star does allow diagonal mvmt
     * 
     * @param parent the previous node the path traveled to
     * @param nPos the position moving to
     * @param end the end goal
     * @param fCost the path cost so far
     * @param gCost the cost to move through this tile
     */
    public Node(Node parent, Point nPos, Point end, double fCost, double gCost) {
        this.parent = parent;
        pos = nPos;
        hCost = nPos.distance(end);
        this.fCost = fCost;
        this.gCost = gCost;
    }
    
    public double costEstimate() {
        return fCost + hCost;
    }
    
    public double pathCost() {
        return fCost + gCost;
    }
    
    public double getHCost() {
        return hCost;
    }
    
    public double getFCost() {
        return fCost;
    }
    
    public double getGCost() {
        return gCost;
    }
    
    public Node getParent() {
        return parent;
    }
    
    public Point getPos() {
        return pos;
    }

    /**
     * sorting into greatest order, to least
     * 
     * @param t
     * @return 
     */
    public int compareTo(Node t) {
        if(t.costEstimate() < costEstimate()) return -1;//move towards beginning
        if(t.costEstimate() > costEstimate()) return 1;//move to end
        return 0;
    }
    
}

package com.falcione.nic.spaceinvaders.model;

import java.applet.AudioClip;
import java.awt.Graphics2D;

import com.falcione.nic.spaceinvaders.util.Constants;
import com.falcione.nic.spaceinvaders.util.Utilities;

/**
 * Class to inherit all types of invaders
 * 
 * @author Nic Falcione
 * @version 2021
 */

@SuppressWarnings("deprecation")
public abstract class SIInvader extends SIShip {

    private int points;
    private String direc;
    private AudioClip shoot;

    /**
     * Constructor to create an invader
     * 
     * @param x
     *            x position
     * @param y
     *            y position
     * @param width
     *            width of invader
     * @param height
     *            height of invader
     */
    protected SIInvader(int x, int y, int width, int height) {
        super(x, y, width, height);
        shoot = Utilities.getSound(Constants.S_IBASE_SHOOT_WAV, getClass());
    }

    /**
     * Method to set the amount of points an invader is worth
     * 
     * @param points
     *            number of points an invader is worth
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Method to set the amount of points an invader is worth
     * 
     * @param points
     *            number of points an invader is worth
     */
    public int getPoints() {
        return points;
    }

    /**
     * Sets the direction of the invader
     * 
     * @param s
     *            the direction
     */
    public void setDirec(String s) {
        direc = s;
    }

    /**
     * Getter for the direction of the invader
     * 
     * @return the direction
     */
    public String getDirec() {
        return direc;
    }

    /**
     * Shoots a new bomb
     */
    public SIBomb shoot() {
        shoot.play();
        return new SIBomb(getX() + 12, getY() - getHeight() / 3, 2, 10);
    }

    /**
     * Draw method for alternating images
     * 
     * @param g2
     *            Graphics Object
     */
    public abstract void draw2(Graphics2D g2);

    /**
     * Invader is dying
     */
    public abstract void dying();

    /**
     * Invader is dead
     */
    public abstract void dead();

    /**
     * Checks to see if the alien is dead
     * 
     * @return true or false
     */
    public abstract boolean isDead();
}

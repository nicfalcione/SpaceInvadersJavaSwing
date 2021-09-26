package com.falcione.nic.spaceinvaders.model;
import java.applet.AudioClip;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.falcione.nic.spaceinvaders.util.Constants;
import com.falcione.nic.spaceinvaders.util.Utilities;

/**
 * Class for the mystery Invader
 * 
 * @author Nic Falcione
 * @version 2021
 */
@SuppressWarnings("deprecation")
public class SIMystery extends SIInvader {

    private ArrayList<Integer> pntVals;
    private String direc = "";
    
    private boolean dying, dead;
    
    private Image image, hit;
    private AudioClip sound;

    /**
     * Constructor for the Mystery Invader
     * 
     * @param x
     *            x pos of the invader
     * @param y
     *            y pos of the invader
     * @param width
     *            width of the invader
     * @param height
     *            height of the invader
     */
    public SIMystery(int x, int y, int width, int height) {
        super(x, y, width, height);

        // Get point value
        pntVals = new ArrayList<Integer>(Arrays.asList(50, 100, 150, 300));
        Collections.shuffle(pntVals);
        super.setPoints(pntVals.get(0));

        image = Utilities.getImage(Constants.S_IMYSTERY_GIF, getClass());
        hit = Utilities.getImage(Constants.S_IINVADER_BLAST_GIF, getClass());
        sound = Utilities.getSound(Constants.S_IMYSTERY_WAV, getClass());
    }

    /**
     * Graphics drawing method overridden
     * 
     * @param g2
     *            Graphics object
     */
    @Override
    public void draw(Graphics2D g2) {
        if (dying) {
            g2.drawImage(hit, getX(), getY(), null);
        } else {
            g2.drawImage(image, getX(), getY(), null);
        }
    }

    /**
     * Invader is dying
     */
    public void dying() {
        dying = true;
    }

    /**
     * Invader is dead
     */
    public void dead() {
        dying = false;
        dead = true;
    }

    /**
     * Checks to see if the alien is dead
     * 
     * @return true or false
     */
    public boolean isDead() {
        return dead;
    }

    /**
     * Moves the Space Invader
     */
    @Override
    public void move() {
        if (direc.equals("left")) {
            setX(getX() - 5);
        } else if (direc.equals("right")) {
            setX(getX() + 5);
        }
    }

    /**
     * Overloaded Method to get sound of mystery
     * 
     * @return the mystery sound
     */
    public AudioClip getSound() {
        return sound;
    }

    /**
     * Method to set the direction of the mystery ship
     * 
     * @param d
     *            direction to set
     */
    public void setDirection(String d) {
        direc = d;
    }

    /**
     * Graphics drawing method overridden
     * 
     * @param g2
     *            Graphics object
     */
    @Override
    public void draw2(Graphics2D g2) {
        // Unimplemented
    }

}

package com.falcione.nic.spaceinvaders.model;
import java.awt.Graphics2D;
import java.awt.Image;

import com.falcione.nic.spaceinvaders.util.Constants;
import com.falcione.nic.spaceinvaders.util.Utilities;

/**
 * Class for the Bottom Row Invader
 * 
 * @author Nic Falcione
 * @version 2021
 */
public class SIBottom extends SIInvader {

    private Image image1, image2, hit;
    private boolean dying, dead;

    /**
     * Constructor for the Bottom Row Invader
     * 
     * @param x
     *            x position of the invader
     * @param y
     *            y position of the invader
     * @param width
     *            width of the invader
     * @param height
     *            height of the invader
     */
    public SIBottom(int x, int y, int width, int height) {
        super(x, y, width, height);
        super.setPoints(10);
        
        image1 = Utilities.getImage(Constants.S_IBOTTOM0_GIF, getClass());
        image2 = Utilities.getImage(Constants.S_IBOTTOM1_GIF, getClass());
        hit = Utilities.getImage(Constants.S_IINVADER_BLAST_GIF, getClass());
        
        dying = dead = false;
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
        } else if (!dead) {
            g2.drawImage(image1, getX(), getY(), null);
        }
    }

    /**
     * Graphics drawing method overridden
     * 
     * @param g2
     *            Graphics object
     */
    public void draw2(Graphics2D g2) {
        if (dying) {
            g2.drawImage(hit, getX(), getY(), null);
        } else if (!dead) {
            g2.drawImage(image2, getX(), getY(), null);
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
        if (getBomb() != null) {
            getBomb().move();
            if (getBomb().getY() < 0) {
                deleteBomb();
            }
        }
        
        if (getDirec().equals("left")) {
            setX(getX() - 5);
        } else if (getDirec().equals("right")) {
            setX(getX() + 5);
        }
    }
}

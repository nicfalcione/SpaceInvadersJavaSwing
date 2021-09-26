package com.falcione.nic.spaceinvaders.model;
import java.applet.AudioClip;
import java.awt.Graphics2D;
import java.awt.Image;

import com.falcione.nic.spaceinvaders.data.Direction;
import com.falcione.nic.spaceinvaders.util.Constants;
import com.falcione.nic.spaceinvaders.util.Utilities;

/**
 * SIbase object, what the user controls.
 * 
 * @author Nic Falcione
 * @version 2021
 */
@SuppressWarnings("deprecation")
public class SIBase extends Entity {

    private Image base;
    private Image hit;
    private AudioClip shoot;
    private AudioClip die;
    private boolean left, right;
    private SIMissile missile;
    private boolean dead;

    /**
     * SIthing constructor for game entities
     * 
     * @param x
     *            x position
     * @param y
     *            y position
     * @param width
     *            width of object
     * @param height
     *            height of object
     */
    public SIBase() {
        super(225, 375, 5, 5);
        dead = false;
        
        hit = Utilities.getImage(Constants.S_IBASE_BLAST_GIF, getClass());
        base = Utilities.getImage(Constants.S_IBASE_GIF, getClass());
        shoot = Utilities.getSound(Constants.S_IBASESHOOT_WAV, getClass());
        die = Utilities.getSound(Constants.S_ISHIP_HIT_WAV, getClass());
    }

    /**
     * Method that moves the base around
     * 
     * @param keyPressed
     *            used to keep moving while key is pressed
     * @param direction
     *            direction of movement
     */
    public void setDirection(boolean keyPressed, Direction direction) {
        switch (direction) {
        case LEFT:
            left = keyPressed;
            break;
        case RIGHT:
            right = keyPressed;
            break;
        }
        
        if (missile != null) {
            missile.move();
        }
    }

    /**
     * Overloaded method for actual movement
     */
    public void move() {
        if (x < 0) {
            x = 0;
        } else if (x > 460) {
            x = 460;
        }
        
        if (left)
            x -= 5;
        if (right)
            x += 5;
        
        if (missile != null) {
            missile.move();
            if (missile.getY() < 0) {
                missile = null;
            }
        }
    }

    /**
     * Returns the missile object
     * 
     * @return the missile object
     */
    public SIMissile getMissile() {
        return missile;
    }

    /**
     * Deletes the missile upon hit detection
     */
    public void deleteMissile() {
        missile = null;
    }

    /**
     * Method to draw the base
     * 
     * @param g2
     *            Graphics object
     */
    public void draw(Graphics2D g2) {
        if (missile != null) {
            missile.draw(g2);
        }

        if (dead) {
            g2.drawImage(hit, x, y, null);
        }

        else {
            g2.drawImage(base, x, y, null);
        }
    }

    /**
     * Keeps track of the bases life
     */
    public void die() {
        dead = true;
    }

    /**
     * Plays death sound
     */
    public void death() {
        die.play();
    }

    /**
     * Plays the shooting sound for the base
     */
    public void shoot() {
        if (missile == null) {
            missile = new SIMissile(x + 12, y + base.getHeight(null) / 3, 2, 10);
            shoot.play();
        }
    }
}

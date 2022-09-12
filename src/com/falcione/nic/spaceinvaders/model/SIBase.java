package com.falcione.nic.spaceinvaders.model;
import java.applet.AudioClip;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.falcione.nic.spaceinvaders.data.Direction;
import com.falcione.nic.spaceinvaders.util.Constants;
import com.falcione.nic.spaceinvaders.util.Utilities;

/**
 * SIbase object, what the user controls.
 * 
 * @author Nic Falcione
 * @version 2022
 */
@SuppressWarnings("deprecation")
public class SIBase extends Entity {

    private Image base;
    private Image hit;
    private AudioClip hitSound; 
    private AudioClip die;
    private boolean left, right;
    private List<SIMissile> missiles;
    private boolean dead;
    private int health;

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
        super(225, Constants.BASE_Y, 5, 5);
        dead = false;
        missiles = new ArrayList<>();
        health = 3;
        
        hit = Utilities.getImage(Constants.S_IBASE_BLAST_GIF, getClass());
        hitSound = Utilities.getSound(Constants.SI_BASE_HIT, getClass());
        base = Utilities.getImage(Constants.S_IBASE_GIF, getClass());
        die = Utilities.getSound(Constants.SI_BASE_DEATH_WAV, getClass());
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
        
        for (Iterator<SIMissile> iter = missiles.iterator(); iter.hasNext();) {
            SIMissile missile = iter.next();
            if (missile != null) {
                missile.move();
                if (missile.getY() < 0) {
                    iter.remove();
                }
            }
        }
    }

    /**
     * Returns the missile list
     * 
     * @return the missile list
     */
    public List<SIMissile> getMissiles() {
        return missiles;
    }

    /**
     * Deletes the missile upon hit detection
     */
    public void deleteMissile(SIMissile missile) {
        missiles.remove(missile);
    }

    /**
     * Method to draw the base
     * 
     * @param g2
     *            Graphics object
     */
    public void draw(Graphics2D g2) {
        for (SIMissile missile : missiles) {
            if (missile != null) {
                missile.draw(g2);
            }
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

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.hitSound.play();
        this.health = health;
    }
    
    public void increaseHealth() {
        health++;
    }

    /**
     * Plays the shooting sound for the base and fires a new missile
     */
    public void shoot() {
        missiles.add(new SIMissile(x + 12, y + base.getHeight(null) / 3, 2, 10));
    }
}

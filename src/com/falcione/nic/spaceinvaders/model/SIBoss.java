package com.falcione.nic.spaceinvaders.model;

import java.applet.AudioClip;
import java.awt.Graphics2D;
import java.awt.Image;

import com.falcione.nic.spaceinvaders.util.Constants;
import com.falcione.nic.spaceinvaders.util.Utilities;

/**
 * Class for the Middle Row Invader
 * 
 * @author Nic Falcione
 * @version 2021
 *
 */
@SuppressWarnings("deprecation")
public class SIBoss extends SIInvader {

    private Image image1, hit;
    private boolean dying, dead;
    private int health;
    private int maxHealth;
    private AudioClip shoot;

    /**
     * Constructor for the Middle Row Invader
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
    public SIBoss(int x, int y, int width, int height) {
        super(x, y, width, height);
        health = Integer.MIN_VALUE;
        maxHealth = Integer.MIN_VALUE;
        
        image1 = Utilities.getImage(Constants.SI_BOSS_GIF, getClass());
        hit = Utilities.getImage(Constants.SI_BOSS_BLAST, getClass());
        shoot = Utilities.getSound(Constants.S_IBASESHOOT_WAV, getClass());
        
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
     * Boss is dying
     */
    public void dying() {
        dying = true;
    }

    /**
     * Boss is dead
     */
    public void dead() {
        dying = false;
        dead = true;
    }

    /**
     * Checks to see if the boss is dead
     * 
     * @return true or false
     */
    public boolean isDead() {
        return dead;
    }
    
    /**
     * Sets the Boss's health
     */
    public void setHealth(int health) {
        if (maxHealth == Integer.MIN_VALUE)
            maxHealth = health;
        this.health = health;
    }
    
    /**
     * Returns the number of hit points the boss currently has left
     * 
     * @return the integer health value
     */
    public int getHealth() {
        return health;
    }
    
    /**
     * Returns the max number of hit points the boss starts with
     * 
     * @return the integer max health value
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Moves the Boss
     */
    @Override
    public void move() {
        if (getDirec().equals("left")) {
            setX(getX() - 5);
        } else if (getDirec().equals("right")) {
            setX(getX() + 5);
        }
    }

    @Override
    public void draw2(Graphics2D g2) {
    }
    
    /**
     * Shoots a new bomb from the Boss
     */
    public SIBomb shoot(int i) {
        return new SIBomb(x + (i * getWidth() / 3), y + getHeight(), 2, 10);
//        shoot.play();
    }
}


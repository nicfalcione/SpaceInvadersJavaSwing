package com.falcione.nic.spaceinvaders.powerup;

import java.applet.AudioClip;
import java.awt.Graphics2D;
import java.awt.Image;

import com.falcione.nic.spaceinvaders.util.Constants;
import com.falcione.nic.spaceinvaders.util.Utilities;

/**
 * The Rapid Fire power up object
 * 
 * @author Nic Falcione
 * @version 2022
 */
@SuppressWarnings("deprecation")
public class RapidFire extends PowerUp {
    
    private Image rapidFire;
    private AudioClip sound;

    /**
     * Constructor for a {@link Life} Powerup
     * 
     * @param x
     *          x position for the powerup
     * @param y
     *          y position for the powerup
     */
    public RapidFire(int x, int y) {
        super(x, y, 30, 30);
        rapidFire = Utilities.getImage(Constants.RAPID_FILE_POWERUP_FILE, getClass());
    }

    /**
     * Overloaded Method to get sound of PowerUp
     * 
     * @return the Rapid Fire PowerUp sound
     */
    public AudioClip getSound() {
        return sound;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(rapidFire, x, y, null);
    }
}

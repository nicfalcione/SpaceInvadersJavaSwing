package com.falcione.nic.spaceinvaders.powerup;

import com.falcione.nic.spaceinvaders.model.Entity;

/**
 * Interface to outline the structure of a PowerUp Object
 * 
 * @author Nic Falcione
 * @version 2022
 */
public abstract class PowerUp extends Entity {
    
    /**
     * PowerUp Constructor
     * 
     * @param x
     *          x position
     * @param y
     *          y position
     * @param width
     *          width of the PowerUp
     * @param height
     *          height of the PowerUp
     */
    protected PowerUp(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void move() {
        setY(getY() + 8);
    }
}

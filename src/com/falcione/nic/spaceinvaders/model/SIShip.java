package com.falcione.nic.spaceinvaders.model;
/**
 * Abstract class for the Ship
 * 
 * @author Nic Falcione
 * @version 2021
 */
public abstract class SIShip extends Entity {

    /**
     * Constructor for the Ship
     * 
     * @param x
     *            x pos of the ship
     * @param y
     *            y pos of the ship
     * @param width
     *            width of the ship
     * @param height
     *            height of the ship
     */
    protected SIShip(int x, int y, int width, int height) {
        super(x, y, width, height);
    }
}

package com.falcione.nic.spaceinvaders.model;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Class to create an Invader Bomb object
 * 
 * @author Nic Falcione
 * @version 2021
 */
public class SIBomb extends Entity {

    /**
     * Constructor for a bomb
     * 
     * @param x
     *            x position of the bomb
     * @param y
     *            y position of the bomb
     * @param width
     *            width of the bomb
     * @param height
     *            height of the bomb
     */
    public SIBomb(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    /**
     * Moves the bomb
     */
    @Override
    public void move() {
        setY(getY() + 3);
    }

    /**
     * Graphics drawing method overridden
     * 
     * @param g2
     *            Graphics object
     */
    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.fill(new Rectangle2D.Double(getX(), getY(), 2f, 10f));
    }
}

package com.falcione.nic.spaceinvaders.model;
import java.awt.Graphics2D;

/**
 * Abstract class to represent Game Entities
 * 
 * @author Nic Falcione
 * @version 2021
 */
public abstract class Entity {

    protected int x;
    protected int y;

    private int width;
    private int height;

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
    protected Entity(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * getter method for x position
     * 
     * @return x position of object
     */
    public int getX() {
        return x;
    }

    /**
     * setter method for x position
     * 
     * @param x
     *            x position to be set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * getter method for y position
     * 
     * @return y position of object
     */
    public int getY() {
        return y;
    }

    /**
     * setter method for y position
     * 
     * @param y
     *            y position to be set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * getter method for width
     * 
     * @return width of object
     */
    public int getWidth() {
        return width;
    }

    /**
     * setter method for width
     * 
     * @param width
     *            width to be set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * getter method for height position
     * 
     * @return height of object
     */
    public int getHeight() {
        return height;
    }

    /**
     * setter method for height position
     * 
     * @param height
     *            height to be set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * draw method for any game entity
     * 
     * @param g2
     *            Grahics2D object
     */
    public abstract void draw(Graphics2D g2);

    /**
     * moves game entity
     */
    public abstract void move();
}

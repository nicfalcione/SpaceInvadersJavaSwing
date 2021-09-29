package com.falcione.nic.spaceinvaders.data;

/**
 * Enumeration for Level attributes
 * 
 * @author Nic Falcione
 * @version 2021
 */
public enum Level {

    ONE     (50, 0.7,   10, 1,  6, 5,  40), 
    TWO     (50, 0.675, 13, 2,  6, 6,  38), 
    THREE   (50, 0.65,  16, 3,  6, 7,  36), 
    FOUR    (50, 0.65,  20, 4,  6, 8,  34), 
    FIVE    (50, 0.625, 25, 5,  6, 9,  32),
    SIX     (50, 0.625, 27, 6,  5, 10, 30),
    SEVEN   (50, 0.6,   28, 7,  5, 11, 28),
    EIGHT   (50, 0.6,   30, 8,  5, 12, 26),
    NINE    (50, 0.575, 30, 9,  5, 13, 24),
    TEN     (50, 0.575, 32, 10, 4, 14, 22),
    ELEVEN  (50, 0.55,  34, 11, 4, 15, 21),
    TWELVE  (50, 0.55,  35, 12, 4, 16, 20),
    THIRTEEN(50, 0.525, 36, 13, 4, 17, 19),
    FOURTEEN(50, 0.525, 37, 14, 3, 18, 18);
    
    private final int invaderCount;         // Starting count of invaders for level
    private final double pulseSpeedFactor;  // Factor by which invaders move delay decreases when bounds are hit
    private final int bombRandomFactor;     // Chances of any alive invader shooting a bomb; x in 10000 every 10ms
    private final int scoreFactor;          // Factor by which score increases for level
    private final double speedFactor;       // Minimum invader move delay time against game loop
    private final int maxBombs;             // Maximum number of active bombs allowed
    private final int initPulseSpeedFactor; // Starting factor by which invaders move delay is; x * 10ms
    
    private static Level[] levels = values();
    
    private Level(int invaderCount, double pulseSpeedFactor, int bombRandomFactor, int scoreFactor, 
            double speedFactor, int maxBombs, int initPulseSpeedFactor) {
        this.invaderCount = invaderCount;
        this.pulseSpeedFactor = pulseSpeedFactor;
        this.bombRandomFactor = bombRandomFactor;
        this.scoreFactor = scoreFactor;
        this.speedFactor = speedFactor;
        this.maxBombs = maxBombs;
        this.initPulseSpeedFactor = initPulseSpeedFactor;
    }
    
    public Level next()
    {
        return levels[(this.ordinal() + 1) % levels.length];
    }
    
    public int getInvaderCount() {
        return invaderCount;
    }
    
    public double getPulseSpeedFactor() {
        return pulseSpeedFactor;
    }
    
    public double getBombRandomFactor() {
        return bombRandomFactor;
    }
    
    public int getScoreFactor() {
        return scoreFactor;
    }
    
    public double getSpeedFactor() {
        return speedFactor;
    }
    
    public int getMaxBombs() {
        return maxBombs;
    }
    
    public int getInitPulseSpeedFactor() {
        return initPulseSpeedFactor;
    }
}

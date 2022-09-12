package com.falcione.nic.spaceinvaders.services;

import java.awt.Graphics;
import java.awt.Graphics2D;

import com.falcione.nic.spaceinvaders.data.Level;
import com.falcione.nic.spaceinvaders.engine.GameTimer;
import com.falcione.nic.spaceinvaders.model.SIBase;
import com.falcione.nic.spaceinvaders.util.Constants;

/**
 * Contains Game State objects and properties to track state of the game.
 * 
 * @author Nic Falcione
 * @version 2022
 */
public class GameStateService {

    private static GameStateService instance = new GameStateService();
    private static InvaderService invaderService = InvaderService.getInstance();
    private static GameTimer timer = GameTimer.getInstance();
    
    private Level currentLevel;
    private boolean won;
    private boolean lost;
    
    private int alienCount;    
    private int score;
    
    private boolean achievedNewHighScore;
    private int pulseSpeedInvaders;
    private boolean needToSpeedUpInvaders;
    
    private SIBase base;
    private boolean isBaseRapidFire;
    private int powerUpTimeRemaining;
    
    /**
     * Initialize Game State Objects
     */
    protected GameStateService() {
        won = lost = achievedNewHighScore = false;
        needToSpeedUpInvaders = false;
        currentLevel = Level.ONE;
        alienCount = Constants.BASE_INVADER_COUNT;
        score = 0;
        
        base = new SIBase();
        isBaseRapidFire = false;
        powerUpTimeRemaining = 0;
        
        pulseSpeedInvaders = currentLevel.getInitPulseSpeedFactor();
    }
    
    public static GameStateService getInstance() {
        return instance;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Level currentLevel) {
        this.currentLevel = currentLevel;
    }

    public boolean hasWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public boolean hasLost() {
        return lost;
    }

    public void setLost(boolean lost) {
        this.lost = lost;
    }

    public boolean hasAchievedNewHighScore() {
        return achievedNewHighScore;
    }

    public void setAchievedNewHighScore(boolean achievedNewHighScore) {
        this.achievedNewHighScore = achievedNewHighScore;
    }

    public int getPulseSpeedInvaders() {
        return pulseSpeedInvaders;
    }

    public void setPulseSpeedInvaders(int pulseSpeedInvaders) {
        this.pulseSpeedInvaders = pulseSpeedInvaders;
    }
    
    public boolean isNeedToSpeedUpInvaders() {
        return needToSpeedUpInvaders;
    }

    public void setNeedToSpeedUpInvaders(boolean needToSpeedUpInvaders) {
        this.needToSpeedUpInvaders = needToSpeedUpInvaders;
    }
    
    /**
     * Checks if there are 0 aliens left in the current level
     */
    public void checkIfLevelHasBeenBeaten() {
        if (alienCount == 0) {
            won = true;
        }
    }

    public int getAlienCount() {
        return alienCount;
    }
    
    public SIBase getBase() {
        return base;
    }

    public void setAlienCount(int alienCount) {
        this.alienCount = alienCount;
    }

    public int getScore() {
        return score;
    }

    /**
     * Increases the current score by the specified amount
     * 
     * @param score
     *              the amount ot increase the current score by
     */
    public void increaseScoreBy(int score) {
        this.score += score;
    }
    
    /**
     * Initiates rapid fire if the rapid fire powerup is activated
     */
    public void initiateRapidFire() {
        isBaseRapidFire = true;
        powerUpTimeRemaining = 1000;
    }
    
    /**
     * Fires the base missiles with the Rapid Fire PowerUp in mind
     */
    public void fireBaseMissiles() {
        // Shoots a missile if the normal or Rapid fire delay has been met
        if (isBaseRapidFire && timer.getPulseCount() % Constants.BASE_RAPID_FIRE_DELAY == 0
                || timer.getPulseCount() % Constants.BASE_FIRE_DELAY == 0) {
            base.shoot();
        }
        
        // Turns off rapid fire when the time is up and decreases the time remaining if activated
        if (isBaseRapidFire && powerUpTimeRemaining <= 0) {
            isBaseRapidFire = false;
        } else if (isBaseRapidFire && powerUpTimeRemaining > 0) {
            powerUpTimeRemaining--;
        }
    }

    /**
     * Speeds up the invaders if need be
     */
    public void speedUpInvaders() {
        // If we need to speed up invaders and the invaders have not achieved the Level's max invader movement speed
        if (needToSpeedUpInvaders && pulseSpeedInvaders >= currentLevel.getSpeedFactor()) {
            pulseSpeedInvaders *= currentLevel.getPulseSpeedFactor();
            needToSpeedUpInvaders = false;
        }
    }

    /**
     * Increases the level after the aliens in the current level has been defeated
     * 
     * @param g
     *          1D Graphics
     * @param g2
     *          2D Graphics
     */
    public void increaseLevel(Graphics g, Graphics2D g2) {
        won = false;
        needToSpeedUpInvaders = true;
        currentLevel = currentLevel.next();
        
        // Trigger Boss round every defined interval of waves
        if (getCurrentLevel().getScoreFactor() % Constants.BOSS_WAVE_INTERVAL == 0) {
            invaderService.makeBoss();
            invaderService.drawBossHealthBar(g2);
            
            alienCount = 1;
        } else {
            alienCount = Constants.BASE_INVADER_COUNT;
            
            pulseSpeedInvaders = currentLevel.getInitPulseSpeedFactor();
            
            invaderService.makeInvaders();
        }
    }
    
    /**
     * Starts a new game and resets the state
     */
    public void startNewGame() {
        won = lost = achievedNewHighScore = false;
        needToSpeedUpInvaders = false;
        currentLevel = Level.ONE;
        alienCount = Constants.BASE_INVADER_COUNT;
        score = 0;
        
        base = new SIBase();
        
        pulseSpeedInvaders = currentLevel.getInitPulseSpeedFactor();
        
        invaderService.makeInvaders();
        timer.start();
    }
}

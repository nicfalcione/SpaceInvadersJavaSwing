package com.falcione.nic.spaceinvaders.services;

import java.awt.Graphics;
import java.awt.Graphics2D;

import com.falcione.nic.spaceinvaders.data.Level;
import com.falcione.nic.spaceinvaders.model.SIBase;
import com.falcione.nic.spaceinvaders.util.Constants;

/**
 * Contains Game State objects and properties to track state of the game.
 * 
 * @author Nic Falcione
 * @version 2021
 */
public class GameStateService {

    private static GameStateService instance = new GameStateService();
    private static InvaderService invaderService = InvaderService.getInstance();
    
    private Level currentLevel;
    private boolean won;
    private boolean lost;
    
    private int alienCount;    
    private int score;
    
    private boolean achievedNewHighScore;
    private int pulseSpeedInvaders;
    private boolean needToSpeedUpInvaders;
    private SIBase base;
    
    protected GameStateService() {
        won = lost = achievedNewHighScore = false;
        needToSpeedUpInvaders = false;
        currentLevel = Level.ONE;
        alienCount = Constants.BASE_INVADER_COUNT;
        score = 0;
        
        base = new SIBase();
        
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

    public void increaseScoreBy(int score) {
        this.score += score;
    }

    public void speedUpInvaders() {
        // If we need to speed up invaders and the invaders have not achieved the Level's max invader movement speed
        if (needToSpeedUpInvaders && pulseSpeedInvaders >= currentLevel.getSpeedFactor()) {
            pulseSpeedInvaders *= currentLevel.getPulseSpeedFactor();
            needToSpeedUpInvaders = false;
        }
    }

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
        
//        timer.start();
    }
}

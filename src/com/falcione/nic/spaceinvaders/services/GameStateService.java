package com.falcione.nic.spaceinvaders.services;

import com.falcione.nic.spaceinvaders.data.Level;
import com.falcione.nic.spaceinvaders.util.Constants;

/**
 * Contains Gamestate objects and properties to maintain
 * 
 * @author Nic Falcione
 * @version 2021
 */
public class GameStateService {

    private static GameStateService instance = new GameStateService();
    
    private Level currentLevel;

    private boolean won;

    private boolean lost;
    
    private int alienCount;
    
    private int score;
    
    private boolean achievedNewHighScore;

    private int pulseSpeedInvaders;

    private boolean needToSpeedUpInvaders;
    
    protected GameStateService() {
        won = lost = achievedNewHighScore = false;
        needToSpeedUpInvaders = false;
        currentLevel = Level.ONE;
        alienCount = Constants.BASE_INVADER_COUNT;
        score = 0;
        
        pulseSpeedInvaders = currentLevel.getInitPulseSpeedFactor();
    }
    
    public static GameStateService getInstance() {
        return instance;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }
    
    public void increaseLevel() {
        currentLevel = currentLevel.next();
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
    
    public void assignLevelInitialInvaderSpeed() {
        pulseSpeedInvaders = currentLevel.getInitPulseSpeedFactor();
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
        if (needToSpeedUpInvaders && pulseSpeedInvaders >= currentLevel.getSpeedFactor()) {
            pulseSpeedInvaders *= currentLevel.getPulseSpeedFactor();
            needToSpeedUpInvaders = false;
        }
    }
}

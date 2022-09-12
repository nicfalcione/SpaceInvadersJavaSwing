package com.falcione.nic.spaceinvaders.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.falcione.nic.spaceinvaders.powerup.Life;
import com.falcione.nic.spaceinvaders.powerup.PowerUp;
import com.falcione.nic.spaceinvaders.powerup.RapidFire;
import com.falcione.nic.spaceinvaders.util.Constants;

/**
 * Singleton Class to handle powerup actions for use in game loop
 * 
 * @author Nic
 * @version 2022
 */
public class PowerUpService {

    private static PowerUpService instance = new PowerUpService();
    private PowerUp powerUp;
    
    protected PowerUpService() {
        powerUp = null;
    }

    public PowerUp getPowerUp() {
        return powerUp;
    }

    public void removePowerUp() {
        powerUp = null;
    }

    public static PowerUpService getInstance() {
        return instance;
    }

    /**
     * Makes the PowerUp
     */
    public void makePowerUp() {
        List<String> potentialPowerUps = new ArrayList<>(
            Arrays.asList(Constants.LIFE_POWERUP, Constants.RAPID_FILE_POWERUP));

        Collections.shuffle(potentialPowerUps);
        int randomXPosition = new Random().nextInt(451) + 30;
        powerUp = getPowerUpImpl(potentialPowerUps.get(0), randomXPosition);
    }

    /**
     * Gets the relevant PowerUp Implementation 
     * 
     * @param type
     *          the type of the PowerUp
     * @param xPosition
     *          the x position of the PowerUp
     * @return the correct {@link PowerUp} implementation
     */
    private PowerUp getPowerUpImpl(String type, int xPosition) {
        PowerUp powerUp = null;
        switch (type) {
            case Constants.LIFE_POWERUP:
                powerUp = new Life(xPosition, 10);
                break;
            case Constants.RAPID_FILE_POWERUP:
                powerUp = new RapidFire(xPosition, 10);
                break;
        }
        return powerUp;
    }
}

package com.falcione.nic.spaceinvaders.engine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import com.falcione.nic.spaceinvaders.SI;
import com.falcione.nic.spaceinvaders.model.SIBomb;
import com.falcione.nic.spaceinvaders.services.GameStateService;
import com.falcione.nic.spaceinvaders.services.InvaderService;
import com.falcione.nic.spaceinvaders.util.Constants;

/**
 * Encapsulated implementation of Java Swing's {@link Timer} for the game.
 * 
 * @author Nic
 * @version 2021
 */
public class GameTimer {
    
    private static GameTimer instance = new GameTimer();
    
    private static InvaderService invaderService = InvaderService.getInstance();
    private static GameStateService gameStateService = GameStateService.getInstance();
    
    private Timer timer;
    private int pulseCount;
    
    protected GameTimer() {
        pulseCount = 0;
        
        // Sets timer update
        timer = new Timer(Constants.TIMER_SPEED, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pulseCount++;

                // Speeds up invaders if bounds are hit
                gameStateService.speedUpInvaders();

                gameStateService.getBase().move();

                // Find left and right most invaders that are alive
                int leftMostAliveInvader = invaderService.getInvaders().get(0).size() - 1;
                int rightMostAliveInvader = 0;
                
                for (int i = 0; i < invaderService.getInvaders().size(); i++) {
                    for (int j = 0; j < invaderService.getInvaders().get(0).size(); j++) {
                        if (!invaderService.getInvaders().get(i).get(j).isDead()) {
                            leftMostAliveInvader = leftMostAliveInvader > j ? j : leftMostAliveInvader;
                            rightMostAliveInvader = rightMostAliveInvader < j ? j : rightMostAliveInvader;
                        }
                    }
                }

                // Moves Invaders Down and changes direction to right based on farthest left alive invader
                if (invaderService.getInvaders().get(0).get(leftMostAliveInvader).getX() == 0
                        && invaderService.getInvaders().get(0).get(0).getDirec().equals("left")) {
                    gameStateService.setNeedToSpeedUpInvaders(true);
                    invaderService.getInvaders().forEach(row -> row.forEach(
                        invader -> {
                            invader.setDirec("right");
                            invader.setY(invader.getY() + 12);
                        }
                    ));
                }
                
                // Moves Invaders Down and changes direction to left based on farthest right alive invader
                if (invaderService.getInvaders().get(0).get(rightMostAliveInvader)
                        .getX() == 460
                        && invaderService.getInvaders().get(0).get(0).getDirec().equals("right")) {
                    gameStateService.setNeedToSpeedUpInvaders(true);
                    invaderService.getInvaders().forEach(row -> row.forEach(
                        invader -> {
                            invader.setDirec("left");
                            invader.setY(invader.getY() + 12);
                        }
                    ));
                }

                // Moves bombs
                for (SIBomb s : invaderService.getBombs()) {
                    s.move();
                }

                // Moves Invaders
                if (pulseCount % gameStateService.getPulseSpeedInvaders() == 0) {
                    invaderService.getInvaders().forEach(row -> row.forEach(invader -> invader.move()));
                }

                // Deletes Mystery when out of bounds
                if (invaderService.getMystery() != null
                        && (invaderService.getMystery().getX() < -10 || invaderService.getMystery().getX() > 499)) {
                    invaderService.removeMystery();
                }
                
                int ran = (int) (Math.random() * 1000);

                // Declares mystery if there isn't one and in a .3% chance every
                // 10ms
                if (ran > 996 && invaderService.getMystery() == null) {
                    invaderService.makeMystery();
                }

                // Moves Mystery
                if (invaderService.getMystery() != null && pulseCount % 2 == 0) {
                    invaderService.getMystery().move();
                }
                
                // Moves Boss
                if (invaderService.getBoss() != null && pulseCount % 6 == 0) {
                    invaderService.getBoss().move();
                    if (invaderService.getBoss().getX() > 295) {
                        invaderService.getBoss().setDirec("left");
                    } else if (invaderService.getBoss().getX() < -60) {
                        invaderService.getBoss().setDirec("right");
                    }
                }

                // Checks for bottom of invaders. Loops backwards 
                int max = 0;
                A: for (int i = invaderService.getInvaders().size() - 1; i >= 0; i--) {
                    for (int j = invaderService.getInvaders().get(0).size() - 1; j >= 0; j--) {
                        if (!invaderService.getInvaders().get(i).get(j).isDead()) {
                            max = i;
                            break A; // Breaks out of the nested loop completely
                        }
                    }
                }

                // Ends Game if invaders hit base
                if (invaderService.getInvaders().get(max).get(0).getY() > Constants.BASE_Y) {
                    gameStateService.setLost(true);
                }

                // Checks to see if all aliens are killed
                gameStateService.checkIfLevelHasBeenBeaten();

                SI.getPanel().repaint();
            }

        });
    }
    
    public static GameTimer getInstance() {
        return instance;
    }
    
    /**
     * Used to stop timer in other classes
     */
    public void stop() {
        timer.stop();
    }

    /**
     * Used to start timer in other classes
     */
    public void start() {
        timer.start();
    }
}

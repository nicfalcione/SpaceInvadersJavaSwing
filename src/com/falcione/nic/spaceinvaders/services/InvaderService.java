package com.falcione.nic.spaceinvaders.services;

import java.util.ArrayList;

import com.falcione.nic.spaceinvaders.model.SIBottom;
import com.falcione.nic.spaceinvaders.model.SIInvader;
import com.falcione.nic.spaceinvaders.model.SIMiddle;
import com.falcione.nic.spaceinvaders.model.SITop;

/**
 * Singleton Class to handle invader actions for use in game loop
 * 
 * @author Nic
 * @version 2021
 */
public class InvaderService {

    private static InvaderService instance = new InvaderService();

    protected InvaderService() {
    }

    public static InvaderService getInstance() {
        return instance;
    }

    /**
     * Used to create the 2d list of invaders
     * 
     * @return 2d ArrayList of invaders
     */
    public ArrayList<ArrayList<SIInvader>> makeInvaders() {

        ArrayList<ArrayList<SIInvader>> invaders = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            invaders.add(new ArrayList<SIInvader>());
        }

        int y = 85;
        int x = 70;
        for (int i = 0; i < 50; i++) {
            if (i < 10) {
                invaders.get(0).add(new SITop(x, y, 30, 15));
                x += 35;
                if (i == 9) {
                    y += 25;
                    x = 70;
                }
            } else if (i < 20) {
                invaders.get(1).add(new SIMiddle(x, y, 30, 15));
                x += 35;
                if (i == 19) {
                    y += 25;
                    x = 70;
                }
            } else if (i < 30) {
                invaders.get(2).add(new SIMiddle(x, y, 30, 15));
                x += 35;
                if (i == 29) {
                    y += 25;
                    x = 70;
                }
            } else if (i < 40) {
                invaders.get(3).add(new SIBottom(x, y, 30, 15));
                x += 35;
                if (i == 39) {
                    y += 25;
                    x = 70;
                }
            } else {
                invaders.get(4).add(new SIBottom(x, y, 30, 15));
                x += 35;
            }
        }

        // Initializes the invaders to start moving right
        invaders.forEach(row -> row.forEach(invader -> invader.setDirec(
                "right")));

        return invaders;
    }
}

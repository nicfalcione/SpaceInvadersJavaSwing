package com.falcione.nic.spaceinvaders.util;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

import com.falcione.nic.spaceinvaders.model.Entity;

/**
 * Class to contain useful utilities for the project.
 * 
 * @author Nic Falcione
 * @version 2021
 */
@SuppressWarnings({"deprecation", "rawtypes"})
public class Utilities {

    /**
     * Gets the image for the entity
     * 
     * @param filename Name of the file associated with the image
     * @param entityClass the entity's class
     * 
     * @see {@link Entity}
     * @return Image associated with the entity
     */
    public static Image getImage(String filename, Class entityClass) {
        URL url = entityClass.getResource(filename);
        return new ImageIcon(url).getImage();
    }

    /**
     * Gets the sound for the entity
     * 
     * @param filename Name of the file associated with the entity's sound
     * @param entityClass the entity's class
     * 
     * @see {@link Entity}
     * @return The Sound of the entity
     */
    public static AudioClip getSound(String filename, Class entityClass) {
        URL url = entityClass.getResource(filename);
        return Applet.newAudioClip(url);
    }
}

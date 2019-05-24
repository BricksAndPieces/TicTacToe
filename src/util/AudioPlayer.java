package util;

import javax.sound.sampled.*;
import java.io.File;

/**
 * Class that makes it simple to play audio by abstracting away object conversions
 * Acts as a wrapper for the javax default library
 * NOTE: This only supports .wav file types
 */
public class AudioPlayer {

    private final Clip clip;

    /**
     * Default constructor for the AudioPlayer object
     * Creates a new Clip object that can be played when needed
     *
     * @param fileName
     * The path to the file that wants to be played
     *
     * @throws Exception
     * Throws if unable to find or play the file
     */
    public AudioPlayer(String fileName) throws Exception {
        AudioInputStream stream = AudioSystem.getAudioInputStream(new File(fileName));
        this.clip = AudioSystem.getClip();
        this.clip.open(stream);
        stream.close();
    }

    /**
     * Sets the starting point to 0 and plays the clip
     */
    public void play() {
        if(!this.clip.isOpen())
            throw new IllegalStateException("AudioPlayer has already been closed");

        this.clip.setFramePosition(0);
        this.clip.start();
    }

    /**
     * Closes the clip which in effect closes the AudioPlayer object
     */
    public void close() {
        this.clip.close();
    }
}
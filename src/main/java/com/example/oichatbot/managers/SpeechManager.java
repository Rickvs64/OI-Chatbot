package com.example.oichatbot.managers;

/**
 * Responsible for text-to-speech conversion and audio output.
 * Singleton class.
 */
public class SpeechManager {
    private boolean shouldPlayAudio = true;

    private static SpeechManager instance = null;

    public SpeechManager() {
    }

    // Static method to maintain one persistent instance.
    public static SpeechManager getInstance()
    {
        if (instance == null)
            instance = new SpeechManager();

        return instance;
    }

    public boolean shouldPlayAudio() {
        return shouldPlayAudio;
    }

    public void setShouldPlayAudio(boolean shouldPlayAudio) {
        this.shouldPlayAudio = shouldPlayAudio;
    }
}

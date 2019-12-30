package com.example.oichatbot.managers;

import com.example.oichatbot.domains.Message;

import java.util.Random;

/**
 * Much like DialogFlowBridge, ExceptionManager is responsible for handling user input and determining the appropriate response.
 * Instead of relying on the DialogFlow API, this class simply returns an extreme (agitated) response regardless of user intent.
 * Singleton class.
 */
public class ExceptionManager {
    private Float maxExtremeChance = 0.5f;      // At the lowest possible Patience level, this is the max chance of an extreme response.
    private Float maxPatience = -0.3f;          // Patience has to be lower than this for an extreme response to even be considered.

    private static ExceptionManager instance = null;

    private ExceptionManager() {

    }

    // Static method to maintain one persistent instance.
    public static ExceptionManager getInstance() {
        if (instance == null)
            instance = new ExceptionManager();

        return instance;
    }

    /**
     * Determine if the application/bot should return an extreme response, based on current emotions.
     * @return True if an extreme response is recommended, false if not (and DialogFlow should be used instead).
     */
    public Boolean shouldRespondExtreme() {
        Float patience = PersonalityManager.getInstance().getEmotions().get("Patience");

        // Is patience currently too high for an extreme to be considered?
        if (patience > maxPatience)
            return false;

        // The lower patience drops, the lower it becomes as a normalized value (0-1).
        // Thus also making it MORE likely for a random normalized float to be higher.
        return (new Random().nextFloat() > normalize(patience, -1.0f, maxPatience));
    }

    /**
     * Return a (random) extreme response/exception.
     * @return The complete message object (including font and audio properties).
     */
    public Message getExtremeResponse() {
        
    }

    /**
     * Normalize a float to any given range.
     * @param value Value to normalize.
     * @param min Minimum range.
     * @param max Maximum range.
     * @return The normalized output of the input parameter (0-1).
     */
    private float normalize(float value, float min, float max) {
        return (value - min) / (max - min);
    }
}

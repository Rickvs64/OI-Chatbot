package com.example.oichatbot.domains;

import java.util.Map;

/**
 * Tracks all current character qualities and emotions.
 * Singleton class - may seem redundant for now since DialogFlowBridge is a singleton too.
 */
public class PersonalityManager {

    private Map<String, Double> emotions;
    private Map<String, Double> personality;

    private boolean allowDynamicEmotions = false;
    private boolean allowDynamicPersonality = false;

    private static PersonalityManager instance = null;

    private PersonalityManager() {
        initEmotions();
        initPersonality();
    }

    // Static method to maintain one persistent instance.
    public static PersonalityManager getInstance()
    {
        if (instance == null)
            instance = new PersonalityManager();

        return instance;
    }

    private void initEmotions() {
        emotions.clear();

        // Patience (1.0d) <---> Frustration (0.0d).
        emotions.put("Patience", 0.5d);
    }

    private void initPersonality() {
        personality.clear();

        // Desire -> high values lead to expressing attraction and occasional dirty talk.
        personality.put("Desire", 0.5d);
        // Curiosity -> high values lead to asking many questions and potentially coming across as "nosy".
        personality.put("Curiosity", 0.5d);
    }

}

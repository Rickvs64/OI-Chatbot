package com.example.oichatbot.domains;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tracks all current character qualities and emotions.
 * Singleton class - may seem redundant for now since DialogFlowBridge is a singleton too.
 */
public class PersonalityManager {

    private Map<String, Float> emotions;    // Emotions that range from -1.0f to 1.0f.
    private Map<String, Float> personality;     // Personality traits that range from 0.0f to 1.0f.
    private Map<String, String> colors;     // Colors assigned to specific (extreme) emotions.

    private boolean allowDynamicEmotions = false;
    private boolean allowDynamicPersonality = false;

    private static PersonalityManager instance = null;

    private PersonalityManager() {
        initEmotions();
        initPersonality();
        initColors();
        temp();
    }

    // Static method to maintain one persistent instance.
    public static PersonalityManager getInstance()
    {
        if (instance == null)
            instance = new PersonalityManager();

        return instance;
    }

    private void initEmotions() {
        emotions = new HashMap<>();

        // Patience (1.0f) <---> Frustration (-1.0f).
        emotions.put("Patience", 0.0f);
    }

    private void initPersonality() {
        personality = new HashMap<>();

        // Desire -> high values lead to expressing attraction and occasional dirty talk.
        personality.put("Desire", 0.5f);
        // Curiosity -> high values lead to asking many questions and potentially coming across as "nosy".
        personality.put("Curiosity", 0.5f);
    }

    public String getLeadingPersonality() {
        return getHighestKeyInMap(personality);
    }

    private String getHighestKeyInMap(Map<String, Float> map) {
        // Separate into two arrays.
        List<String> strings = new ArrayList<String>();
        List<Float> floats = new ArrayList<Float>();
        for (Map.Entry<String, Float> entry : map.entrySet()) {
            strings.add(entry.getKey());
            floats.add(entry.getValue());
        }

        Float highestValue = 0.0f;
        int highestIndex = 0;
        for (int i = 0; i < strings.size(); i++){
            if (floats.get(i) > Math.abs(highestValue)) {
                highestValue = floats.get(i);
                highestIndex = i;
            }
        }

        return strings.get(highestIndex);
    }

    private void initColors() {
        colors = new HashMap<>();

        // Every possible emotion needs a LOW and HIGH defined extreme.
        colors.put("Patience_LOW", "#f08080");
        colors.put("Patience_HIGH", "#b0e0e6");
    }

    private String determineSuggestedColor() {
        // First check for the currently strongest emotion.
        
    }

    private void temp() {
        // Just sets a high default character trait for testing.
        // Using map.put() is fine since duplicates aren't allowed.
        personality.put("Desire", 1.0f);
    }

}

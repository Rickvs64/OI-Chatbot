package com.example.oichatbot.managers;

import java.awt.*;
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
        // Turns values positive first with Math.abs (so an emotion at -0.8 is stronger than 0.7).
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

    public String determineSuggestedColor() {
        // First check for the currently strongest emotion.
        String emotion = getHighestKeyInMap(emotions);

        // Get LOW and HIGH color variant of the chosen emotion.
        Color colorLow = Color.decode(colors.get(emotion + "_LOW"));
        Color colorHigh = Color.decode(colors.get(emotion + "_HIGH"));

        // Interpolate between LOW and HIGH variants to get the appropriate color value.
        // First we need to convert emotions' range (-1 to 1) to a standard lerp alpha (0 to 1).
        Float alpha = normalizeToRange(emotions.get(emotion), -1.0f, 1.0f);
        // Parse as HEX string, does NOT support transparency.
        return "#" + Integer.toHexString(lerpColors(colorHigh, colorLow, alpha).getRGB()).substring(2);
    }

    private Color lerpColors(Color c1, Color c2, Float alpha) {
        Float inverse = 1.0f - alpha;
        int r = (int) (c1.getRed() * alpha + c2.getRed() * inverse);
        int g = (int) (c1.getGreen() * alpha + c2.getGreen() * inverse);
        int b = (int) (c1.getBlue() * alpha + c2.getBlue() * inverse);
        return new Color(r, g, b);
    }

    private Float normalizeToRange(float value, float min, float max) {
        return (value - min) / (max - min);
    }

    public Map<String, Float> getEmotions() {
        return emotions;
    }

    public Map<String, Float> getPersonality() {
        return personality;
    }

    private void temp() {
        // Just sets a high default character trait for testing.
        // Using map.put() is fine since duplicates aren't allowed.
        personality.put("Desire", 1.0f);    // Set 'Desire' as leading trait.

        emotions.put("Patience", -1.0f);    // Extremely frustrated.
    }

}

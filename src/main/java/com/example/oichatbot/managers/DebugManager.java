package com.example.oichatbot.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Holds debug information and methods (such as directly changing personality traits).
 * Singleton class.
 */
public class DebugManager {
    private Boolean inDebug = false;
    private List<String> entryPhrases;      // Possible phrases to be used by the user.
    private List<String> exitPhrases;       // Possible phrases to be used by the user.

    private List<String> introMessages;     // Possible messages to be used by the bot.
    private List<String> outroMessages;     // Possible messages to be used by the bot.

    private String debugColor = "#DFDFDF";  // Default background color for debug messages in the front-end.

    private static DebugManager instance = null;

    public DebugManager() {
        initEntryPhrases();
        initExitPhrases();
        initIntroMessages();
        initOutroMessages();
    }

    // Static method to maintain one persistent instance.
    public static DebugManager getInstance()
    {
        if (instance == null)
            instance = new DebugManager();

        return instance;
    }

    public Boolean wantsToEnterDebug(String input) {
        String parsed = input.trim();
        parsed = parsed.replaceAll("/^[A-Za-z]+$/", "");    // Letters and whitespaces.
        parsed = parsed.toLowerCase();
        return (entryPhrases.contains(parsed));
    }

    public Boolean wantsToExitDebug(String input) {
        String parsed = input.trim();
        parsed = parsed.replaceAll("/^[A-Za-z]+$/", "");    // Letters and whitespaces.
        parsed = parsed.toLowerCase();
        return (exitPhrases.contains(parsed));
    }

    public String enterDebug() {
        inDebug = true;
        return getEntryMessage();
    }

    public String exitDebug() {
        inDebug = false;
        return getOutroMessage();
    }

    /**
     * Fill entryPhrases array with possible recognized commands.
     */
    private void initEntryPhrases() {
        entryPhrases = new ArrayList<>();
        entryPhrases.add("analysis");
        entryPhrases.add("enter analysis");
        entryPhrases.add("analysis mode");
        entryPhrases.add("enter analysis mode");
        entryPhrases.add("debug");
        entryPhrases.add("enter debug");
        entryPhrases.add("debug mode");
        entryPhrases.add("enter debug mode");
    }

    /**
     * Fill exitPhrases array with possible recognized commands.
     */
    private void initExitPhrases() {
        exitPhrases = new ArrayList<>();
        exitPhrases.add("exit");
        exitPhrases.add("exit analysis");
        exitPhrases.add("exit analysis mode");
        exitPhrases.add("exit debug");
        exitPhrases.add("exit debug mode");
        exitPhrases.add("normal mode");
        exitPhrases.add("resume");
        exitPhrases.add("back to normal");
        exitPhrases.add("go back to normal");
    }

    private void initIntroMessages() {
        introMessages = new ArrayList<>();
        introMessages.add("ENTERING DEBUG MODE.");
        introMessages.add("ENTERING ANALYSIS MODE.");
        introMessages.add("BEEP BOOP. ANALYSIS MODE NOW ENABLED.");
        introMessages.add("ANALYSIS MODE ENABLED.");
    }

    private void initOutroMessages() {
        outroMessages = new ArrayList<>();
        outroMessages.add("Right! Where were we?");
        outroMessages.add("Huh? What just happened?");
        outroMessages.add("That was weird.");
        outroMessages.add("Get out of my head, you weirdo.");
        outroMessages.add("Huh? I feel different somehow.");
        outroMessages.add("Back to normal, then.");
        outroMessages.add("Alright then.");
    }

    private String getEntryMessage() {
        int randomIndex = new Random().nextInt(introMessages.size());
        return introMessages.get(randomIndex);
    }

    private String getOutroMessage() {
        int randomIndex = new Random().nextInt(outroMessages.size());
        return outroMessages.get(randomIndex);
    }


    public Boolean inDebug() {
        return inDebug;
    }

    public String getDebugColor() {
        return debugColor;
    }
}

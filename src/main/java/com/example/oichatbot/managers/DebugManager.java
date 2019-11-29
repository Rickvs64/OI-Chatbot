package com.example.oichatbot.managers;

/**
 * Holds debug information and methods (such as directly changing personality traits).
 * Singleton class.
 */
public class DebugManager {
    private Boolean inDebug = false;

    private static DebugManager instance = null;

    public DebugManager() {

    }

    // Static method to maintain one persistent instance.
    public static DebugManager getInstance()
    {
        if (instance == null)
            instance = new DebugManager();

        return instance;
    }

    public Boolean wantsToEnterDebug() {
        return false;
    }

    public Boolean wantsToExitDebug() {
        return false;
    }

    public Boolean inDebug() {
        return inDebug;
    }

    public void setInDebug(Boolean inDebug) {
        this.inDebug = inDebug;
    }
}

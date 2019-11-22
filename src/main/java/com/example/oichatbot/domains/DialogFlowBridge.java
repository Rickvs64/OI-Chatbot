package com.example.oichatbot.domains;

/**
 * This class is responsible for connecting to the (appropriate) DialogFlow project depending on current personality/emotions.
 * Singleton class.
 */
public class DialogFlowBridge {
    private PersonalityManager pm;

    private static DialogFlowBridge instance = null;

    private DialogFlowBridge() {
        // ...
    }

    // Static method to maintain one persistent instance.
    public static DialogFlowBridge getInstance()
    {
        if (instance == null)
            instance = new DialogFlowBridge();

        return instance;
    }
}

package com.example.oichatbot.managers;

/**
 * Much like DialogFlowBridge, ExceptionManager is responsible for handling user input and determining the appropriate response.
 * Instead of relying on the DialogFlow API, this class simply returns an extreme (agitated) response regardless of user intent.
 * Singleton class.
 */
public class ExceptionManager {
    private Float maxExtremeChance = 0.5f;      // At the lowest possible Patience level, this is the max chance of an extreme response.
    private Float maxPatience = -0.75f;         // Patience has to be lower than this for an extreme response to even be considered.
}

package com.example.oichatbot.managers;

/**
 * Parse raw messages from DialogFlow to dynamically add/alter/remove blocks of text.
 * Singleton class.
 */
public class MessageParser {
    private static MessageParser instance = null;

    public MessageParser() {
    }

    public static MessageParser getInstance() {
        if (instance == null)
            instance = new MessageParser();

        return instance;
    }

    /**
     * Starting point for parsing messages. Checks for special tags, data to be formatted (e.g. timestamps), etc.
     * @param message Input message to be parsed and formatted.
     * @return Formatted (readable) output message.
     */
    public String parseMessage(String message) {
        // ...
    }
}

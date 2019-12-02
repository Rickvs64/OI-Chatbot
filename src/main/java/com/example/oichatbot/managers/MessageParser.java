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
        String output = message;

        // First we format special tags/blocks to determine whether they need to be shown or hidden.
        // We repeat this step for every block we find (until none remain).
        while (output.contains("[")) {
            output = formatEmotionBlock(output);
        }

        // Now we check for other misc. text from DialogFlow that requires extra formatting, such as datetime.
        output = formatTime(output);

        // Finally return the fully formatted message.
        return output;
    }

    private String formatEmotionBlock(String message) {
        Integer startIndex = message.indexOf("[");
        Integer endIndex = message.indexOf("]");
        String emotionBlock = message.substring(startIndex, endIndex + 1);
        System.out.println("Detected emotion block: " + emotionBlock);



        return "temp";
    }

    private String formatTime(String message) {
        // todo: format time from DialogFlow (e.g. to remove timezone and seconds).
        return message;
    }
}

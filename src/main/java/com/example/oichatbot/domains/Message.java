package com.example.oichatbot.domains;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Simple prototype class for messages sent by either the user or the AI.
 */
public class Message {
    private String content;
    private String time;
    private Boolean fromBot;
    private String suggestedColor;

    public Message() {

    }

    public Message(String content, String time, Boolean fromBot) {
        this.content = content;
        this.time = time;
        this.fromBot = fromBot;
    }

    public Message(String content, String time, Boolean fromBot, String suggestedColor) {
        this.content = content;
        this.time = time;
        this.fromBot = fromBot;
        this.suggestedColor = suggestedColor;
    }

    public Message(String content, Boolean fromBot) {
        this.content = content;
        this.time = getCurrentTime();
        this.fromBot = fromBot;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getFromBot() {
        return fromBot;
    }

    public void setFromBot(Boolean fromBot) {
        this.fromBot = fromBot;
    }

    public String getSuggestedColor() {
        return suggestedColor;
    }

    public void setSuggestedColor(String suggestedColor) {
        this.suggestedColor = suggestedColor;
    }

    /**
     * Get the current time (hours + minutes) in string format.
     * @return
     */
    private String getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(cal.getTime());
    }
}
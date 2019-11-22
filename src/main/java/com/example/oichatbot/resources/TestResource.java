package com.example.oichatbot.resources;

import com.example.oichatbot.domains.DialogFlowBridge;
import com.example.oichatbot.domains.Message;
import com.example.oichatbot.domains.PersonalityManager;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Lists;
import com.google.api.client.util.Maps;
import com.google.api.gax.paging.Page;
import com.google.auth.Credentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.*;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/test")
public class TestResource {

    private Integer incrementTest = 0;

    /**
     * Authentication test method for Google Cloud functionality.
     * Will throw exceptions if the Google Cloud project isn't properly linked.
     * @return The (useless) eventual object display name.
     */
    @RequestMapping("/auth")
    public String auth() {
        // If you don't specify credentials when constructing the client, the
        // client library will look for credentials in the environment.

        Storage storage = StorageOptions.getDefaultInstance().getService();


        Page<Bucket> buckets = storage.list();
        for (Bucket bucket : buckets.iterateAll()) {
            // Display iterated buckets?
            // No actions necessary for now.
        }
        return buckets.toString();
    }

    /**
     * Simple increment request method to confirm persistent variable values throughout different requests.
     * @return The same value as last time, plus one.
     */
    @RequestMapping("/increment")
    public Integer increment() {
        incrementTest ++;
        return incrementTest;
    }

    /**
     * Determine the one best reply for any given singular chat message.
     * @param message Message to respond to.
     * @return Message object containing the text response.
     * @throws Exception
     */
    @PostMapping(path = "/chat/post", consumes = "application/json", produces = "application/json")
    public Message chatSimple(@RequestBody Message message) throws Exception {
        String answer = detectIntentSimple("openinno", message.getContent(), "123456", "en-US");
        Message response = new Message(answer, true);
        return response;
    }

    /**
     * Display a list of possible intents recognized by the DialogFlow API.
     * @return List of recognized intents and possible responses.
     * @throws Exception
     */
    @RequestMapping("/list")
    public String listIntents() throws Exception {
        return listIntents("openinno").toString();
    }


    /**
     * Simplified variant of 'detectIntentTexts' that returns only one string response.
     * @param projectId Project ID, default is "openinno".
     * @param sessionId Session ID, use the same ID in successive requests for a continuous conversation.
     * @param languageCode Language code, default is "en-US".
     * @return The full response object, containing the message to be displayed and extra data regarding intent extraction and context.
     * @throws Exception
     */
    private static String detectIntentSimple(String projectId, String input, String sessionId, String languageCode) throws Exception {
        return DialogFlowBridge.getInstance().detectIntentSimple(projectId, input, sessionId, languageCode);
    }

    /**
     * Retrieve a list of all possible recognized intents (commands) via the DialogFlow API.
     * @param projectId Project ID, default is "openinno".
     * @return List of possible intents the API can respond to.
     * @throws Exception
     */
    private static List<Intent> listIntents(String projectId) throws Exception {
        return DialogFlowBridge.getInstance().listIntents(projectId);
    }

}

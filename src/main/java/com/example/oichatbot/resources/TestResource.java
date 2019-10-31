package com.example.oichatbot.resources;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
     * Send a simple message to the DialogFlow API. Uses the same constant session ID to support entire conversations.
     * @param message Message to send to DialogFlow, e.g. "Can I please have a room?".
     * @return Returns DialogFlow's entire response object, converted to string (to avoid JsonMappingExceptions).
     * @throws Exception
     */
    @RequestMapping("/chat/{message}")
    public String chat(@PathVariable(value="message") String message) throws Exception {
        List<String> texts = new ArrayList<String>();
        texts.add(message);

        Map<String, QueryResult> resultMap = detectIntentTexts("openinno", texts, "123456", "en-US");
        // QueryResult has issues serializing so for now we'll convert to string.
        // The eventual chatbot application will simply extract the essential values we need from QueryResult and convert them into a custom response class.
        return resultMap.toString();
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
     * Simple test method for connecting to the DialogFlow API with a given project ID, intent message, sesson ID and language code.
     * @param projectId Project ID, default is "openinno".
     * @param texts Input texts (e.g. "Good morning!"), using only one initial message is recommended.
     * @param sessionId Session ID, use the same ID in successive requests for a continuous conversation.
     * @param languageCode Language code, default is "en-US".
     * @return The full response object, containing the message to be displayed and extra data regarding intent extraction and context.
     * @throws Exception
     */
    private static Map<String, QueryResult> detectIntentTexts(String projectId, List<String> texts, String sessionId, String languageCode) throws Exception {
        Map<String, QueryResult> queryResults = Maps.newHashMap();
        // Instantiates a client
        try (SessionsClient sessionsClient = SessionsClient.create()) {
            // Set the session name using the sessionId (UUID) and projectID (my-project-id)
            SessionName session = SessionName.of(projectId, sessionId);
            System.out.println("Session Path: " + session.toString());

            // Detect intents for each text input
            for (String text : texts) {
                // Set the text (hello) and language code (en-US) for the query
                TextInput.Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode(languageCode);

                // Build the query with the TextInput
                QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

                // Performs the detect intent request
                DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);

                // Display the query result
                QueryResult queryResult = response.getQueryResult();

                System.out.println("====================");
                System.out.format("Query Text: '%s'\n", queryResult.getQueryText());
                System.out.format("Detected Intent: %s (confidence: %f)\n",
                        queryResult.getIntent().getDisplayName(), queryResult.getIntentDetectionConfidence());
                System.out.format("Fulfillment Text: '%s'\n", queryResult.getFulfillmentText());

                queryResults.put(text, queryResult);
            }
        }
        return queryResults;
    }

    /**
     * Retrieve a list of all possible recognized intents (commands) via the DialogFlow API.
     * @param projectId Project ID, default is "openinno".
     * @return List of possible intents the API can respond to.
     * @throws Exception
     */
    private static List<Intent> listIntents(String projectId) throws Exception {
        List<Intent> intents = Lists.newArrayList();
        // Instantiates a client
        try (IntentsClient intentsClient = IntentsClient.create()) {
            // Set the project agent name using the projectID (my-project-id)
            ProjectAgentName parent = ProjectAgentName.of(projectId);

            // Performs the list intents request
            for (Intent intent : intentsClient.listIntents(parent).iterateAll()) {
                System.out.println("====================");
                System.out.format("Intent name: '%s'\n", intent.getName());
                System.out.format("Intent display name: '%s'\n", intent.getDisplayName());
                System.out.format("Action: '%s'\n", intent.getAction());
                System.out.format("Root followup intent: '%s'\n", intent.getRootFollowupIntentName());
                System.out.format("Parent followup intent: '%s'\n", intent.getParentFollowupIntentName());

                System.out.format("Input contexts:\n");
                for (String inputContextName : intent.getInputContextNamesList()) {
                    System.out.format("\tName: %s\n", inputContextName);
                }
                System.out.format("Output contexts:\n");
                for (Context outputContext : intent.getOutputContextsList()) {
                    System.out.format("\tName: %s\n", outputContext.getName());
                }

                intents.add(intent);
            }
        }
        return intents;
    }

}

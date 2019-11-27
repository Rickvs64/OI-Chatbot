package com.example.oichatbot.domains;

import com.google.api.client.util.Lists;
import com.google.cloud.dialogflow.v2.*;

import java.util.List;

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

    /**
     * New chat method with support for personality and dynamic response building.
     * Call this method as a starting point for any chat message.
     * @return
     */
    public Message chat(String input, String languageCode) throws Exception {
        // Todo: Use PersonalityManager to determine if and how much its emotions/personality are altered.

        // Determine relevant project id.
        String projectId = determineProjectId();
        String sessionId = "1234567";

        String rawAnswer = detectIntentSimple(projectId, input, sessionId, languageCode);
        Message parsedAnswer = new Message(parseAnswer(rawAnswer), true);

        // Todo: Determine message object's advanced parameters like suggested color.

        return parsedAnswer;
    }


    /**
     * Simplified variant of 'detectIntentTexts' that returns only one string response.
     * @param projectId Project ID, default is "openinno".
     * @param sessionId Session ID, use the same ID in successive requests for a continuous conversation.
     * @param languageCode Language code, default is "en-US".
     * @return The full response object, containing the message to be displayed and extra data regarding intent extraction and context.
     * @throws Exception
     */
    private String detectIntentSimple(String projectId, String input, String sessionId, String languageCode) throws Exception {
        // Set default response text (in case of an error).
        String answer = "(Error getting intent response.)";
        // Instantiates a client.
        try (SessionsClient sessionsClient = SessionsClient.create()) {
            // Set the session name using the sessionId (UUID) and projectID (my-project-id).
            SessionName session = SessionName.of(projectId, sessionId);
            System.out.println("Session Path: " + session.toString());

            // Set the text (hello) and language code (en-US) for the query.
            TextInput.Builder textInput = TextInput.newBuilder().setText(input).setLanguageCode(languageCode);

            // Build the query with the TextInput.
            QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();
            Context context = Context.newBuilder().setName(session.toString() + "/contexts/" + "testcontext").setLifespanCount(999).build();      // <-- THIS ONE?
            QueryParameters params = QueryParameters.newBuilder().addContexts(context).build();

            // Build a new DetectIntentRequest with determined input and parameters.
            DetectIntentRequest request = DetectIntentRequest.newBuilder()
                    .setQueryInput(queryInput)
                    .setQueryParams(params)
                    .setSession(session.toString())
                    .build();

            // Performs the detect intent request.
            // DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);
            DetectIntentResponse response = sessionsClient.detectIntent(request);

            // Display the query result.
            QueryResult queryResult = response.getQueryResult();

            System.out.println("====================");
            System.out.format("Query Text: '%s'\n", queryResult.getQueryText());
            System.out.format("Detected Intent: %s (confidence: %f)\n",
                    queryResult.getIntent().getDisplayName(), queryResult.getIntentDetectionConfidence());
            System.out.format("Fulfillment Text: '%s'\n", queryResult.getFulfillmentText());
            System.out.format("Output contexts: '%s'\n", queryResult.getOutputContextsList());
            answer = queryResult.getFulfillmentText();
        }
        return answer;
    }

    /**
     * Retrieve a list of all possible recognized intents (commands) via the DialogFlow API.
     * @param projectId Project ID, default is "openinno".
     * @return List of possible intents the API can respond to.
     * @throws Exception
     */
    public List<Intent> listIntents(String projectId) throws Exception {
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

    private String determineProjectId() {
        // todo: Possibly choose between different project ID's depending on current emotions/personality.
        return "openinno";
    }

    private String parseAnswer(String rawAnswer) {
        // todo: Parse DialogFlow's response (with its optional cursewords etc.) to a readable format.
        return rawAnswer;
    }
}

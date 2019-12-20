package com.example.oichatbot.managers;

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for text-to-speech conversion and audio output.
 * Singleton class.
 */
public class SpeechManager {
    private boolean shouldPlayAudio = true;
    private Map<String, SsmlVoiceGender> voiceTypes;
    private Map<String, Double> basePitches;
    private Map<String, Double> baseRates;

    private static SpeechManager instance = null;

    public SpeechManager() {
        initVoiceTypes();
        initBasePitches();
        initBaseRates();
    }

    // Static method to maintain one persistent instance.
    public static SpeechManager getInstance()
    {
        if (instance == null)
            instance = new SpeechManager();

        return instance;
    }

    /**
     * Toggle audio output (inverts boolean).
     * @return Boolean (true if should play, false if muted).
     */
    public boolean toggleAudio() {
        shouldPlayAudio = !shouldPlayAudio;
        return shouldPlayAudio;
    }

    public String say(String inputText, String fileName) {
        String audioContent = "";

        // Instantiates a client.
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            // Set the text input to be synthesized.
            SynthesisInput input = SynthesisInput.newBuilder()
                    .setText(inputText)
                    .build();

            // Build the voice request, select the language code ("en-US") and the ssml voice gender.
            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode("en-US")
                    .setSsmlGender(determineVoiceType(PersonalityManager.getInstance().getLeadingPersonality()))
                    .build();

            // Select the type of audio file you want returned.
            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3)
                    .setPitch(determineBasePitch(PersonalityManager.getInstance().getLeadingPersonality()))
                    .setSpeakingRate(determineBaseRate(PersonalityManager.getInstance().getLeadingPersonality()))
                    .build();

            // Perform the text-to-speech request on the text input with the selected voice parameters and
            // audio file type.
            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice,
                    audioConfig);

            // Get the audio contents from the response.
            ByteString audioContents = response.getAudioContent();

            // Write the response to the output file.
            try (OutputStream out = new FileOutputStream(fileName)) {
                out.write(audioContents.toByteArray());
                System.out.println("Audio content written to file \"" + fileName + "\"");

                // This used to be handled in the back-end as a temporary test method.
                // playAudio(fileName);
                // Now we encode the audio file to Base64 so it can be sent in the original POST response.
                audioContent = encodeFileToBase64(new File(fileName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return audioContent;
    }

    /**
     * Encode an audio file (.mp3)'s content to Base64.
     * @param file Audio file (preferably .mp3 format).
     * @return The Base64 content as String.
     */
    private String encodeFileToBase64(File file) {
        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            throw new IllegalStateException("could not read file " + file, e);
        }
    }

    /**
     * Determine the right text-to-speech voice type depending on the current leading personality.
     * @param leadingTrait
     * @return The recommended SsmlVoiceGender enum value.
     */
    private SsmlVoiceGender determineVoiceType(String leadingTrait) {
        return voiceTypes.get(leadingTrait);
    }

    /**
     * Initialize the voiceTypes Map with voice type recommendations.
     */
    private void initVoiceTypes() {
        voiceTypes = new HashMap<>();
        voiceTypes.put("Default", SsmlVoiceGender.NEUTRAL);
        voiceTypes.put("Desire", SsmlVoiceGender.FEMALE);
        voiceTypes.put("Curiosity", SsmlVoiceGender.MALE);
    }

    /**
     * Determine a recommended voice pitch based on the current leading personality trait.
     * @param leadingTrait Currently leading character trait.
     * @return The suggested (base) voice pitch.
     */
    private Double determineBasePitch(String leadingTrait) {
        return basePitches.get(leadingTrait);
    }

    /**
     * Initialize the basePitches Map with voice pitch recommendations.
     */
    private void initBasePitches() {
        basePitches = new HashMap<>();
        basePitches.put("Default", 0.0d);
        basePitches.put("Desire", 0.0d);
        basePitches.put("Curiosity", 4.0d);
    }

    /**
     * Determine a recommended speaking rate based on the current leading personality trait.
     * @param leadingTrait Currently leading character trait.
     * @return The suggested (base) speakign rate.
     */
    private Double determineBaseRate(String leadingTrait) {
        return baseRates.get(leadingTrait);
    }

    /**
     * Initialize the baseRates Map with speaking rate recommendations.
     */
    private void initBaseRates() {
        baseRates = new HashMap<>();
        baseRates.put("Default", 1.0d);
        baseRates.put("Desire", 0.7d);
        baseRates.put("Curiosity", 1.2d);
    }

    public boolean shouldPlayAudio() {
        return shouldPlayAudio;
    }

    public void setShouldPlayAudio(boolean shouldPlayAudio) {
        this.shouldPlayAudio = shouldPlayAudio;
    }
}

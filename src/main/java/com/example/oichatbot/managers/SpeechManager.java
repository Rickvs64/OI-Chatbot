package com.example.oichatbot.managers;

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;

/**
 * Responsible for text-to-speech conversion and audio output.
 * Singleton class.
 */
public class SpeechManager {
    private boolean shouldPlayAudio = true;

    private static SpeechManager instance = null;

    public SpeechManager() {
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
                    .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                    .build();

            // Select the type of audio file you want returned.
            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3)
                    .setPitch(20.0d)
                    .setSpeakingRate(0.5d)
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

    private static String encodeFileToBase64(File file) {
        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            throw new IllegalStateException("could not read file " + file, e);
        }
    }

    public boolean shouldPlayAudio() {
        return shouldPlayAudio;
    }

    public void setShouldPlayAudio(boolean shouldPlayAudio) {
        this.shouldPlayAudio = shouldPlayAudio;
    }
}

package com.example.oichatbot.managers;

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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

    public void say(String inputText) {
        // Instantiates a client
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            // Set the text input to be synthesized
            SynthesisInput input = SynthesisInput.newBuilder()
                    .setText(inputText)
                    .build();

            // Build the voice request, select the language code ("en-US") and the ssml voice gender
            // ("neutral")
            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode("en-US")
                    .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                    .build();

            // Select the type of audio file you want returned
            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3)
                    .build();

            // Perform the text-to-speech request on the text input with the selected voice parameters and
            // audio file type
            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice,
                    audioConfig);

            // Get the audio contents from the response
            ByteString audioContents = response.getAudioContent();

            // Write the response to the output file.
            try (OutputStream out = new FileOutputStream("output.mp3")) {
                out.write(audioContents.toByteArray());
                System.out.println("Audio content written to file \"output.mp3\"");
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean shouldPlayAudio() {
        return shouldPlayAudio;
    }

    public void setShouldPlayAudio(boolean shouldPlayAudio) {
        this.shouldPlayAudio = shouldPlayAudio;
    }
}

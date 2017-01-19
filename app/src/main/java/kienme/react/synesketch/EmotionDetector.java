package kienme.react.synesketch;

import java.io.IOException;

import synesketch.emotion.Emotion;
import synesketch.emotion.EmotionalState;
import synesketch.emotion.Empathyscope;

/**
 * Created by ravikiran on 19/1/17.
 *
 */

public class EmotionDetector {

    private static int detectEmotion(String text) {
        EmotionalState state = null;

        try {
            Empathyscope scope = Empathyscope.getInstance();
            state= scope.feel(text);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return state.getStrongestEmotion().getType();
    }

    public static String getEmotionKeyword(String text) {

        String emotionKeyword = "";
        int emotion = detectEmotion(text);

        if (emotion == Emotion.ANGER) emotionKeyword = "angry";
        else if (emotion == Emotion.DISGUST) emotionKeyword = "disgust";
        else if (emotion == Emotion.FEAR) emotionKeyword = "fear";
        else if (emotion == Emotion.HAPPINESS) emotionKeyword = "yay";
        else if (emotion == Emotion.NEUTRAL) emotionKeyword = "ok";
        else if (emotion == Emotion.SADNESS) emotionKeyword = "sad";
        else if (emotion == Emotion.SURPRISE) emotionKeyword = "wow";

        return emotionKeyword;
    }
}

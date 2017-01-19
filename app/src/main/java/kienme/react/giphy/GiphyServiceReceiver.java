package kienme.react.giphy;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kienme.react.GifGridItem;

/**
 * Created by ravikiran on 19/1/17.
 *
 */

public class GiphyServiceReceiver extends ResultReceiver {
    private static Listener listener;

    public static final String GIFS_LIST_KEY = "gifs_list";

    String TAG = "GiphyServiceReceiver";

    public GiphyServiceReceiver(Handler handler) {
        super(handler);
    }

    public static void setListener(Listener pListener) {
        listener = pListener;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

        ArrayList<String> gifsList = new ArrayList<>();
        ArrayList<String> resultsArray = resultData.getStringArrayList(GiphyIntentService.RESULTS_LIST_KEY);

        for (String result : resultsArray) {
            try {
                JSONObject response = new JSONObject(result);
                String url = response.getJSONObject("data").getString("image_url");
                Log.d(TAG, "Image obtained: "+url);
                gifsList.add(url);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (listener != null) {
            Log.d(TAG, "Listener not null");
            Bundle gifsBundle = new Bundle();
            gifsBundle.putStringArrayList(GIFS_LIST_KEY, gifsList);
            listener.onReceiveResult(resultCode, gifsBundle);
        }

        else
            Log.d(TAG, "Listener is null");
    }

    public static interface Listener {
        void onReceiveResult(int resultCode, Bundle resultData);
    }
}

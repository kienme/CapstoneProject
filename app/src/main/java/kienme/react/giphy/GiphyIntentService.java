package kienme.react.giphy;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class GiphyIntentService extends IntentService {
    private static final String ACTION_FETCH = "kienme.react.giphy.action.FETCH";
    private static final String KEYWORDS = "kienme.react.giphy.extra.KEYWORDS";
    private static final String NUM_RESULTS = "kienme.react.giphy.extra.NUM_RESULTS";

    private final String BASE_URL = "http://api.giphy.com/v1/gifs/random?";

    //Replace the below with appropriate calls to obtain api key
    //Since current key is public, it is hardcoded here
    private String API_KEY = "dc6zaTOxFJmzC";

    String TAG = "GiphyIntentService";

    public GiphyIntentService() {
        super("GiphyIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    public static void startActionFetch(Context context, String keywords, int numResults) {
        Intent intent = new Intent(context, GiphyIntentService.class);
        intent.setAction(ACTION_FETCH);
        intent.putExtra(KEYWORDS, keywords);
        intent.putExtra(NUM_RESULTS, numResults);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FETCH.equals(action)) {
                final String keywords = intent.getStringExtra(KEYWORDS);
                final int numResults = intent.getIntExtra(NUM_RESULTS, 10);
                handleActionFetch(keywords, numResults);
            }
        }
    }

    /**
     * Handle action Fetch in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFetch(String keywords, int numResults) {

        for(int i = 0; i < numResults; ++i) {
            StringBuilder data = new StringBuilder("");
            try {
                Log.d(TAG, "handleActionFetch");
                URL url = new URL(BASE_URL + "api_key=" + API_KEY + "&tag=" + keywords);
                Log.d(TAG, "URL: " + url.toString());
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    data.append(line);
                    Log.d(TAG, line);
                }

                if (inputStream != null) {
                    inputStream.close();
                    //parseResult(data.toString());
                    Log.d(TAG, "Input stream not null");
                } else Log.d(TAG, "Input stream IS null");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

package kienme.react.giphy;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by ravikiran on 19/1/17.
 *
 */

public class GiphyServiceReceiver extends ResultReceiver {
    private Listener listener;

    public GiphyServiceReceiver(Handler handler) {
        super(handler);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        //This, here is where you parse the stuff

        if (listener != null)
            listener.onReceiveResult(resultCode, resultData);
    }

    public static interface Listener {
        void onReceiveResult(int resultCode, Bundle resultData);
    }
}

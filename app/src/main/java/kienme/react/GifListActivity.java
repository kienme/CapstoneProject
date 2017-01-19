package kienme.react;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import java.io.IOException;
import java.util.ArrayList;

import kienme.react.giphy.GiphyIntentService;
import kienme.react.giphy.GiphyServiceReceiver;
import synesketch.emotion.Emotion;
import synesketch.emotion.EmotionalState;
import synesketch.emotion.Empathyscope;

/**
 * An activity representing a list of Gifs. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link GifDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class GifListActivity extends AppCompatActivity implements GiphyServiceReceiver.Listener{

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    static boolean mTwoPane;

    GifGridViewAdapter gifGridViewAdapter;
    GridView gridView;
    static Context context;

    String TAG = "GifListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        context = this;
        gridView = (GridView) findViewById(R.id.gif_list);
        //gridView.setColumnWidth(calcImageSize());

        if (findViewById(R.id.gif_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        GiphyServiceReceiver receiver = new GiphyServiceReceiver(new Handler());
        receiver.setListener(this);
        //GiphyIntentService.startActionFetch(this, receiver, "silicon+valley", 20);

    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if(resultCode == GiphyIntentService.RESULT_SUCCESS) {
            ArrayList<GifGridItem> gridData = new ArrayList<>();
            ArrayList<String> gifsList = resultData.getStringArrayList(GiphyServiceReceiver.GIFS_LIST_KEY);

            for (String gif : gifsList)
                gridData.add(new GifGridItem(gif));

            gifGridViewAdapter = new GifGridViewAdapter(this, R.layout.gif_list_content, gridData, getSupportFragmentManager());
            gifGridViewAdapter.setGridData(gridData);
            gridView.setAdapter(gifGridViewAdapter);
        }
    }


}

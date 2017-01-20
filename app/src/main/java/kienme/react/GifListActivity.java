package kienme.react;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import kienme.react.content.DBHelper;
import kienme.react.content.FavouritesProvider;
import kienme.react.giphy.GiphyIntentService;
import kienme.react.giphy.GiphyServiceReceiver;
import kienme.react.synesketch.EmotionDetector;

/**
 * An activity representing a list of Gifs. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link GifDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class GifListActivity extends AppCompatActivity
        implements GiphyServiceReceiver.Listener, LoaderManager.LoaderCallbacks<Cursor>{

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    public static boolean mTwoPane;

    boolean directSearch = true;

    GifGridViewAdapter gifGridViewAdapter;
    GridView gridView;
    static Context context;

    String searchTerm = "";

    private static final int LOADER_ID = 33;

    String TAG = "GifListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_list);

        context = this;
        gridView = (GridView) findViewById(R.id.gif_list);

        final GiphyServiceReceiver receiver = new GiphyServiceReceiver(new Handler());
        receiver.setListener(this);

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

        final EditText searchBar = (EditText) findViewById(R.id.search_bar);
        ImageButton searchButton = (ImageButton) findViewById(R.id.seach_btn);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = searchBar.getText().toString();
                if (text.length() != 0) {
                    searchTerm = "";

                    if(directSearch)
                        searchTerm = text.replace(" ", "+");
                    else
                        searchTerm = EmotionDetector.getEmotionKeyword(text);

                    GiphyIntentService.startActionFetch(context, receiver, searchTerm, 2);
                }
            }
        });

        //gridView.setColumnWidth(calcImageSize());

        if (findViewById(R.id.gif_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }


        //GiphyIntentService.startActionFetch(this, receiver, "silicon+valley", 20);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if(resultCode == GiphyIntentService.RESULT_SUCCESS) {
            ArrayList<GifGridItem> gridData = new ArrayList<>();
            ArrayList<String> gifsList = resultData.getStringArrayList(GiphyServiceReceiver.GIFS_LIST_KEY);

            for (String gif : gifsList)
                gridData.add(new GifGridItem(gif, searchTerm));

            gifGridViewAdapter = new GifGridViewAdapter(this, R.layout.gif_list_content, gridData, getSupportFragmentManager());
            gifGridViewAdapter.setGridData(gridData);
            gridView.setAdapter(gifGridViewAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.direct_check:
                item.setChecked(directSearch = !item.isChecked());
                return true;

            case R.id.favourites:
                onFavTabSelected();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onFavTabSelected() {
        //Toast.makeText(getBaseContext(), "Favs yo", Toast.LENGTH_SHORT).show();
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, FavouritesProvider.CONTENT_URI, new String[]{DBHelper.COL_URL}, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ArrayList<GifGridItem> favGifs = new ArrayList<>();
        data.moveToFirst();
        while (!data.isAfterLast()) {
            favGifs.add(new GifGridItem(data.getString(0), "Favourite"));
            data.moveToNext();
        }

        if(gifGridViewAdapter == null)
            gifGridViewAdapter = new GifGridViewAdapter(this, R.layout.gif_list_content, favGifs, getSupportFragmentManager());
            gifGridViewAdapter.setGridData(favGifs);
            gridView.setAdapter(gifGridViewAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

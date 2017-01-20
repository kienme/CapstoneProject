package kienme.react.widget;

import android.app.LoaderManager;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.concurrent.ExecutionException;

import kienme.react.R;
import kienme.react.content.DBHelper;
import kienme.react.content.FavouritesProvider;

/**
 * Created by ravikiran on 20/1/17.
 *
 */

public class FavWidgetService extends RemoteViewsService {

    String TAG = "FavWidgetService";

    public FavWidgetService() {
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {

            private Cursor cursor = null;

            @Override
            public void onCreate() {
                Log.d(TAG, "onCreate");
            }

            @Override
            public void onDataSetChanged() {
                Log.d(TAG, "onDataSetChanged");
                if (cursor != null)
                    cursor.close();

                long id = Binder.clearCallingIdentity();
                cursor = getContentResolver().query(FavouritesProvider.CONTENT_URI, new String[]{DBHelper.COL_URL}, null, null, null);

                Binder.restoreCallingIdentity(id);
            }

            @Override
            public void onDestroy() {
                Log.d(TAG, "onDestroy");
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }
            }

            @Override
            public int getCount() {
                Log.d(TAG, "getCount");
                if(cursor == null)
                    return 0;

                return cursor.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                Log.d(TAG, "getViewAt");
                if (position == AdapterView.INVALID_POSITION ||
                        cursor == null || !cursor.moveToPosition(position)) {
                    return null;
                }

                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget_imageview);
                Bitmap bitmap = null;
                try {
                    bitmap = Glide.with(getBaseContext()).load(cursor.getString(0)).asBitmap().into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                remoteViews.setImageViewBitmap(R.id.widget_imageview, bitmap);
                Log.d(TAG, "Cursor url: "+cursor.getString(0));

                return remoteViews;
            }

            @Override
            public RemoteViews getLoadingView() {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int i) {
                Log.d(TAG, "getItemId");
                if (cursor.moveToPosition(i))
                    return cursor.getInt(0);

                return i;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
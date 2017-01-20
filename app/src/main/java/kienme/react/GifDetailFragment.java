package kienme.react;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

/**
 * A fragment representing a single Gif detail screen.
 * This fragment is either contained in a {@link GifListActivity}
 * in two-pane mode (on tablets) or a {@link GifDetailActivity}
 * on handsets.
 */
public class GifDetailFragment extends Fragment {
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    final int WRITE_PERMISSION_CODE = 1;
    String image;
    long enqueue;
    DownloadManager downloadManager;

    String gifPath;
    String TAG = "GifDetailFragment";

    public GifDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //gifPath = getActivity().getFilesDir().getAbsolutePath();
        gifPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+Environment.DIRECTORY_DOWNLOADS;
        Log.d(TAG, "gifpath:" + gifPath);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "Broadcast Received");
                if (intent.getAction() == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
                    Log.d(TAG, "DL complete action");
                    long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(enqueue);
                    Cursor cursor = downloadManager.query(query);
                    if (cursor.moveToFirst()) {
                        int colIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(colIndex)) {
                            Log.d(TAG, "starting share intent");

                            File imageFile = new File(gifPath+"/sample.gif");
                            Uri imageUri = Uri.fromFile(imageFile);
                            Log.d(TAG, "image uri: "+imageUri);

                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("image/gif");
                            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                            startActivity(Intent.createChooser(shareIntent, "Share GIF"));
                        }
                    }
                }
            }
        };

        getActivity().registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle data;
        final Activity activity = getActivity();
        final Context context = getContext();
        ImageButton shareButton = null;

        if(GifListActivity.mTwoPane) {
            data = getArguments();
        }
        else {
            data = activity.getIntent().getExtras();
            shareButton = (ImageButton) activity.findViewById(R.id.share_button);
        }

        image = data.getString("image");
        ImageView imageView = (ImageView) activity.findViewById(R.id.gif_detail_image);
        Glide.with(context).load(image).into(imageView);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Download the gif

                Log.d(TAG, "onCLickShare");
                if (Build.VERSION.SDK_INT >= 23) {
                    if ((ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) ||
                            (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED)) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, WRITE_PERMISSION_CODE);
                    }
                    else {
                        downloadFile(image);
                    }
                }
                else {
                    downloadFile(image);
                }
            }
        });

    }

    void downloadFile(String url) {
        //Delete existing file first
        File imageFile = new File(gifPath+"/sample.gif");
        imageFile.delete();

        Log.d(TAG, "downloadFile()");
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("ReacT");
        request.setDescription("Downloading gif...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "sample.gif");

        downloadManager = (DownloadManager) getActivity().getSystemService(getContext().DOWNLOAD_SERVICE);
        enqueue = downloadManager.enqueue(request);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.gif_detail, container, false);
        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            //Verify multiple request grants
            case WRITE_PERMISSION_CODE:
                Log.d(TAG, "WRITE_PERM_CODE");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "calling downloadFile()");
                    downloadFile(image);
                }
                else
                    Toast.makeText(getContext(), "Permission needed", Toast.LENGTH_SHORT).show();
        }
    }
}

package kienme.react;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by ravikiran on 19/1/17.
 *
 */

public class GifGridViewAdapter extends ArrayAdapter<GifGridItem> {
    View row;
    private Context context;
    private int layoutResource;
    private ArrayList<GifGridItem> gridData = new ArrayList<>();
    boolean firstRun = true;
    FragmentManager fragmentManager;
    String TAG = "GifGridViewAdapter";

    public GifGridViewAdapter(Context context, int layoutResource, ArrayList<GifGridItem> gridData, FragmentManager manager) {
        super(context, layoutResource, gridData);
        this.context = context;
        this.gridData = gridData;
        this.layoutResource = layoutResource;
        fragmentManager = manager;
    }

    public void setGridData(ArrayList<GifGridItem> gridData) {
        this.gridData = gridData;
        notifyDataSetChanged();
        Log.d(TAG, "setGridData");
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        row = convertView;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResource, parent, false);
        }

        final GifGridItem item = gridData.get(position);
        Log.d(TAG, "Position: "+position);

        ImageView imageView = (ImageView)(row.findViewById(R.id.gif_image_view));
        Glide.with(context).load(item.getImage()).into(imageView);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(GifListActivity.mTwoPane) {
                    GifDetailFragment fragment = new GifDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("image", item.getImage());
                    fragment.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .replace(R.id.gif_detail_container, fragment)
                            .commit();
                }
                else {
                    Intent startDetailsActivity = new Intent(context, GifDetailActivity.class);
                    startDetailsActivity.putExtra("image", item.getImage());
                    context.startActivity(startDetailsActivity);
                }
            }
        });

        return row;
    }
}

package lifesgame.tapstudios.ca.lifesgame.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import lifesgame.tapstudios.ca.lifesgame.R;

/**
 * Created by viditsoni on 2018-03-10.
 */

public class CustomGridAdapter extends BaseAdapter {
    private Activity mContext;

    // Keep all Images in array
    public Integer[] mThumbIds;

    // Constructor
    public CustomGridAdapter(Activity mainActivity, Integer[] items) {
        this.mContext = mainActivity;
        this.mThumbIds = items;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(R.layout.custom_profile_grid_layout, null);
        ImageView customProfilePic = (ImageView) convertView.findViewById(R.id.grid_custom_profile_image);
        customProfilePic.setImageResource(mThumbIds[position]);

        return convertView;
    }

}

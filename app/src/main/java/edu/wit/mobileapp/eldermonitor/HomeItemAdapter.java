package edu.wit.mobileapp.eldermonitor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class HomeItemAdapter extends ArrayAdapter<ListItem> {
    private static final String TAG = "HomeItemAdapter";
    private LayoutInflater mInflater;

    public HomeItemAdapter(Context context, int rid, List<ListItem> list) {
        super(context, rid, list);

        mInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v(TAG, "Entering getView");
        //Retrieve data
        ListItem item = getItem(position);

        //User layout file to generate View
        View view = mInflater.inflate(R.layout.home_list_item, null);

        //Set image
        ImageView image;
        image = (ImageView)view.findViewById(R.id.profile);
        image.setImageBitmap(item.image);

        //Set user name
        TextView name;
        name = (TextView)view.findViewById(R.id.name);
        name.setText(item.fname + " " + item.lname);

        if(item.help) {
            Log.v(TAG, "Help = true");
            ImageView helpImg= view.findViewById(R.id.alert);
            helpImg.setVisibility(view.VISIBLE);
        }

        return view;
    }
}

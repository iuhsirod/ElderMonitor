package edu.wit.mobileapp.eldermonitor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RequestItemAdapter extends ArrayAdapter<ListItem> {
    private LayoutInflater mInflater;

    public RequestItemAdapter(Context context, int rid, List<ListItem> list) {
        super(context, rid, list);
        mInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);


    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //Retrieve data
        ListItem item = getItem(position);

        //User layout file to generate View
        View view = mInflater.inflate(R.layout.request_list_item, null);

        //Set image
        ImageView image;
        image = (ImageView)view.findViewById(R.id.image);
        image.setImageBitmap(item.image);

        //Set user name
        TextView name;
        name = (TextView)view.findViewById(R.id.name);
        name.setText(item.name);

        return view;
    }
}

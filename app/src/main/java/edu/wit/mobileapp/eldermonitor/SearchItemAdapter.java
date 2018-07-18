package edu.wit.mobileapp.eldermonitor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class SearchItemAdapter extends ArrayAdapter<ListItem> {
    private LayoutInflater mInflater;

    public SearchItemAdapter(Context context, int rid, List<ListItem> list) {
        super(context, rid, list);
        mInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);


    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //Retrieve data
        ListItem item = getItem(position);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("user");


        //User layout file to generate View
        View view = mInflater.inflate(R.layout.search_list_item, null);

        final String searchUID = item.uid;
        System.out.println(searchUID);
        final String currentUID = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        System.out.println(currentUID);
        ImageButton mApproveRequestBtn = (ImageButton) view.findViewById(R.id.search_add);
        mApproveRequestBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myRef.child(currentUID).child("contact").child("pending_out").child(searchUID).setValue("");
                myRef.child(searchUID).child("contact").child("pending_in").child(currentUID).setValue("");
            }
        });

        //Set image
        ImageView image;
        image = (ImageView)view.findViewById(R.id.search_profile);
        image.setImageBitmap(item.image);

        //Set user name
        TextView name;
        name = (TextView)view.findViewById(R.id.search_name);
        name.setText(item.name);

        return view;
    }
}
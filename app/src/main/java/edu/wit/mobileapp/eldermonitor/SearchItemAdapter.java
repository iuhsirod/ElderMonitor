package edu.wit.mobileapp.eldermonitor;

import android.content.Context;
import android.util.Log;
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
    private static final String TAG = "SearchItemAdapter";

    private LayoutInflater mInflater;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("user");

    private String currentUID = mAuth.getCurrentUser().getUid().toString();

    public SearchItemAdapter(Context context, int rid, List<ListItem> list) {
        super(context, rid, list);

        mInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v(TAG, "Entering getView");

        //Retrieve data
        ListItem item = getItem(position);

        //User layout file to generate View
        View view = mInflater.inflate(R.layout.search_list_item, null);

        final String searchUID = item.uid;

        ImageButton mSendRequestBtn = (ImageButton) view.findViewById(R.id.search_add);
        mSendRequestBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.v(TAG, "Sending request");

                myRef.child(currentUID).child("contact").child("pending_out").child(searchUID).setValue("");
                myRef.child(searchUID).child("contact").child("pending_in").child(currentUID).setValue("");

                notifyDataSetChanged();
            }
        });

        //Set image
        ImageView image;
        image = (ImageView)view.findViewById(R.id.search_profile);
        image.setImageBitmap(item.image);

        //Set user name
        TextView name;
        name = (TextView)view.findViewById(R.id.search_name);
        name.setText(item.fname);

        return view;
    }
}

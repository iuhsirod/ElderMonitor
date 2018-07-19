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

public class RequestItemAdapter extends ArrayAdapter<ListItem> {

    private static final String TAG = "RequestItemAdapter";

    private LayoutInflater mInflater;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("user");

    private String currentUID = mAuth.getCurrentUser().getUid().toString();

    public RequestItemAdapter(Context context, int rid, List<ListItem> list) {
        super(context, rid, list);

        mInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v(TAG, "Entering getView");

        //User layout file to generate View
        View view = mInflater.inflate(R.layout.request_list_item, null);
        ListItem item = getItem(position);
        final String searchUID = item.uid;

        //approving request
        ImageButton mApproveRequestBtn = (ImageButton) view.findViewById(R.id.accept_request);
        mApproveRequestBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //fix later
                myRef.child(currentUID).child("contact").child("pending_in").child(searchUID).removeValue();
                myRef.child(searchUID).child("contact").child("pending_out").child(currentUID).removeValue();

                myRef.child(currentUID).child("contact").child("approved").child(searchUID).setValue("");
                myRef.child(searchUID).child("contact").child("approved").child(currentUID).setValue("");
            }
        });

        //declining request
        ImageButton mDeclineRequestBtn = (ImageButton) view.findViewById(R.id.decline_request);
        mDeclineRequestBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.v(TAG, "Declined. Deleting request.");

                myRef.child(currentUID).child("contact").child("pending_in").child(searchUID).removeValue();
                myRef.child(searchUID).child("contact").child("pending_out").child(currentUID).removeValue();

                notifyDataSetChanged();
            }
        });

        //Set image
        ImageView image;
        image = (ImageView)view.findViewById(R.id.request_profile);
        image.setImageBitmap(item.image);

        //Set user name
        TextView name;
        name = (TextView)view.findViewById(R.id.request_name);
        name.setText(item.fname);

        return view;
    }
}

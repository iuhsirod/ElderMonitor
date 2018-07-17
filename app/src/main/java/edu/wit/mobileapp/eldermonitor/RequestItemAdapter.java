package edu.wit.mobileapp.eldermonitor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RequestItemAdapter extends ArrayAdapter<ListItem> {
    private LayoutInflater mInflater;

    public RequestItemAdapter(Context context, int rid, List<ListItem> list) {
        super(context, rid, list);
        mInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);


    }

    public View getView(int position, View convertView, ViewGroup parent) {

        //User layout file to generate View
        View view = mInflater.inflate(R.layout.request_list_item, null);
        ListItem item = getItem(position);
        final String searchUID = item.uid;

        final String currentUID = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        //approving request
        ImageButton mApproveRequestBtn = (ImageButton) view.findViewById(R.id.accept_request);
        mApproveRequestBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //fix later
                FirebaseDatabase.getInstance().getReference("user").child(currentUID).child("contact").child("pending_in").child(searchUID).removeValue();
                FirebaseDatabase.getInstance().getReference("user").child(searchUID).child("contact").child("pending_out").child(currentUID).removeValue();

                FirebaseDatabase.getInstance().getReference("user").child(currentUID).child("contact").child("approved").child(searchUID).setValue("");
                FirebaseDatabase.getInstance().getReference("user").child(searchUID).child("contact").child("approved").child(currentUID).setValue("");
            }
        });

        //declining request
        ImageButton mDeclineRequestBtn = (ImageButton) view.findViewById(R.id.decline_request);
        mDeclineRequestBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("user").child(currentUID).child("contact").child("pending_in").child(searchUID).removeValue();
                FirebaseDatabase.getInstance().getReference("user").child(searchUID).child("contact").child("pending_out").child(currentUID).removeValue();
            }
        });



        //Set image
        ImageView image;
        image = (ImageView)view.findViewById(R.id.image);
//        image.setImageBitmap(item.image);

        //Set user name
        TextView name;
        name = (TextView)view.findViewById(R.id.request_name);
        name.setText(item.name);

        return view;
    }
}

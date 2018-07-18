package edu.wit.mobileapp.eldermonitor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class CancelItemAdapter extends ArrayAdapter<ListItem> {
    private LayoutInflater mInflater;

    private String TAG = "CancelItemAdapter";
    public CancelItemAdapter(Context context, int rid, List<ListItem> list) {
        super(context, rid, list);
        mInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);


    }

    public View getView(int position, View convertView, ViewGroup parent) {


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("user");

        //User layout file to generate View
        View view = mInflater.inflate(R.layout.manage_list_item, null);
        ListItem item = getItem(position);
        final String searchUID = item.uid;
        System.out.println(TAG + searchUID);

        final String currentUID = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();


        //declining request
        ImageButton mCancelRequestBtn = (ImageButton) view.findViewById(R.id.remove);
        mCancelRequestBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.v(TAG, "Canceling request");
                myRef.child(currentUID).child("contact").child("pending_out").child(searchUID).removeValue();
                myRef.child(searchUID).child("contact").child("pending_in").child(currentUID).removeValue();
                notifyDataSetChanged();
            }
        });

        //Set image
        ImageView image;
        image = (ImageView)view.findViewById(R.id.profile);
        image.setImageBitmap(item.image);

        //Set user name
        TextView name;
        name = (TextView)view.findViewById(R.id.name);
        name.setText(item.fname);

        return view;
    }
}

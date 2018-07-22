package edu.wit.mobileapp.eldermonitor;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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

public class ManageItemAdapter extends ArrayAdapter<ListItem> {

    private static final String TAG = "ManageItemAdapter";

    private LayoutInflater mInflater;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("user");

    private String currentUID = mAuth.getCurrentUser().getUid().toString();

    private Fragment myFragment;

    public ManageItemAdapter(Context context, int rid, List<ListItem> list, Fragment fragment) {
        super(context, rid, list);

        myFragment = fragment;
        mInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v(TAG, "Entering getView");

        //Retrieve data
        ListItem item = getItem(position);

        //User layout file to generate View
        View view = mInflater.inflate(R.layout.manage_list_item, null);

        final String searchUID = item.uid;

        //declining request
        ImageButton mDeleteBtn = (ImageButton) view.findViewById(R.id.remove);
        mDeleteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.v(TAG, "Delete button");

                myRef.child(currentUID).child("contact").child("approved").child(searchUID).removeValue();
                myRef.child(searchUID).child("contact").child("approved").child(currentUID).removeValue();

                getFragment(myFragment);
            }
        });

        //Set image
        ImageView image;
        image = (ImageView)view.findViewById(R.id.profile);
        image.setImageBitmap(item.image);

        //Set user name
        TextView name;
        name = (TextView)view.findViewById(R.id.name);
        name.setText(item.fname + " " + item.lname);

        return view;
    }


    public void getFragment(Fragment fragment) {
        Log.v(TAG, "Entering getFragment");

        FragmentTransaction ft = ((AppCompatActivity) getContext()).getSupportFragmentManager()
                .beginTransaction();
        ft.detach(fragment);
        ft.attach(fragment);
        ft.commit();
    }
}

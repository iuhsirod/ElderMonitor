package edu.wit.mobileapp.eldermonitor;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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

public class ManageItemAdapter extends ArrayAdapter<ListItem> {
    private LayoutInflater mInflater;

    public ManageItemAdapter(Context context, int rid, List<ListItem> list) {
        super(context, rid, list);
        mInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);


    }

    public View getView(int position, View convertView, ViewGroup parent) {



        //Retrieve data
        ListItem item = getItem(position);

        //User layout file to generate View
        View view = mInflater.inflate(R.layout.manage_list_item, null);

        final String searchUID = item.uid;

        final String currentUID = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        //declining request
        ImageButton mDeleteBtn = (ImageButton) view.findViewById(R.id.remove);
        mDeleteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                FirebaseDatabase.getInstance().getReference("user").child(currentUID).child("contact").child("approved").child(searchUID).removeValue();
                FirebaseDatabase.getInstance().getReference("user").child(searchUID).child("contact").child("approved").child(currentUID).removeValue();
            }
        });

        //Set image
        ImageView image;
        image = (ImageView)view.findViewById(R.id.profile);
        image.setImageBitmap(item.image);

        //Set user name
        TextView name;
        name = (TextView)view.findViewById(R.id.name);
        name.setText(position + " " + item.name);

        return view;
    }
}

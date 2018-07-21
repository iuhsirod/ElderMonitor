package edu.wit.mobileapp.eldermonitor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsFragment extends Fragment {
    private static final String TAG = "SettingsFragment";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("user");

    private String currentUID = mAuth.getCurrentUser().getUid().toString();

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "Entering onCreateView");

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        final DatabaseReference user = FirebaseDatabase.getInstance().getReference("user").child(currentUID);

        final TextView first = view.findViewById(R.id.first_name);
        final TextView last = view.findViewById(R.id.last_name);
        final TextView birth = view.findViewById(R.id.birthday);
        final TextView phone = view.findViewById(R.id.phone);
        final TextView email = view.findViewById(R.id.email);
        final ImageView profile = view.findViewById(R.id.profile);
        final Switch broadcast = view.findViewById(R.id.broadcast);

        broadcast.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v(TAG, "Broadcaster checked");

                user.child("broadcast").setValue(isChecked);
            }
        });

        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v(TAG, "Current user reference");

                String fname = dataSnapshot.child("first_name").getValue(String.class);
                String lname = dataSnapshot.child("last_name").getValue(String.class);
                String eemail = dataSnapshot.child("email").getValue(String.class);
                Boolean mbroadcast = dataSnapshot.child("broadcast").getValue(Boolean.class);
                //do what you want with the email

                ListItem item = new ListItem(getActivity());
                item.fname = fname;
                item.lname = lname;
                item.email = eemail;
                item.broadcast = mbroadcast;

                first.setText(item.fname);
                last.setText(item.lname);
                email.setText(item.email);
                profile.setImageBitmap(item.image);
                broadcast.setChecked(item.broadcast);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

}

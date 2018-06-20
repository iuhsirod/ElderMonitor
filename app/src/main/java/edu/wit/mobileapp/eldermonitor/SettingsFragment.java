package edu.wit.mobileapp.eldermonitor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("");

    private FirebaseAuth mAuth;


    private static final String TAG = "SettingsFragment";

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mAuth = FirebaseAuth.getInstance();

        String UID = mAuth.getCurrentUser().getUid().toString();
        myRef = FirebaseDatabase.getInstance().getReference("user").child(UID);

        final TextView first = view.findViewById(R.id.first_name);
        final TextView last = view.findViewById(R.id.last_name);
        final TextView birth = view.findViewById(R.id.birthday);
        final TextView phone = view.findViewById(R.id.phone);
        final TextView email = view.findViewById(R.id.email);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String first_name = dataSnapshot.child("first_name").getValue(String.class);
                String last_name = dataSnapshot.child("last_name").getValue(String.class);
                String memail = dataSnapshot.child("email").getValue(String.class);
                //do what you want with the email
                first.setText(first_name);
                last.setText(last_name);
                email.setText(memail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

}

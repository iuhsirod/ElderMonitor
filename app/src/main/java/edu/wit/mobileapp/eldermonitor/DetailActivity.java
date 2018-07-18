package edu.wit.mobileapp.eldermonitor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class DetailActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static final String TAG = "DetailActivity";

    private String currentUID;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "Entering ");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setSupportActionBar((android.support.v7.widget.Toolbar)findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String jsonItem = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonItem = extras.getString("item");
        }
        final ListItem itemObj = new Gson().fromJson(jsonItem, ListItem.class);

        TextView name = findViewById(R.id.name);
        name.setText(itemObj.name);

        myRef = FirebaseDatabase.getInstance().getReference("user").child(itemObj.uid).child("help");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    Log.v(TAG, "there's none....");
                }
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    RadioGroup helpResp = findViewById(R.id.help_response);
                    helpResp.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        mAuth = FirebaseAuth.getInstance();
        currentUID = mAuth.getCurrentUser().getUid().toString();

        RadioButton setOmw = findViewById(R.id.omw);
        setOmw.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myRef.child(currentUID).setValue("omw");
            }
        });

        RadioButton setAvail = findViewById(R.id.available);
        setAvail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myRef.child(currentUID).setValue("available");
            }
        });

        RadioButton setBusy = findViewById(R.id.busy);
        setBusy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myRef.child(currentUID).setValue("busy");
            }
        });


    }
}

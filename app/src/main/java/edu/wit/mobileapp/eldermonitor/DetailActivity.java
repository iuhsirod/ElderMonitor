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

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("");

    private String currentUID = mAuth.getCurrentUser().getUid().toString();

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
        name.setText(itemObj.fname);

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

                    String idName = dataSnapshot.child(currentUID).getValue().toString();
                    int id = getResources().getIdentifier(idName, "id", getPackageName());
                    RadioButton radioButton = findViewById(id);
                    radioButton.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        final RadioGroup group = (RadioGroup) findViewById(R.id.help_response);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = group.getCheckedRadioButtonId();
                System.out.println(id);

                switch (id) {
                    case R.id.omw:
                        myRef.child(currentUID).setValue("omw");
                        break;
                    case R.id.available:
                        myRef.child(currentUID).setValue("available");
                        break;
                    case R.id.busy:
                        myRef.child(currentUID).setValue("busy");
                        break;
                    default:
                        myRef.child(currentUID).setValue("");
                        break;
                }
            }
        });
    }
}

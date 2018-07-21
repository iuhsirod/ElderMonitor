package edu.wit.mobileapp.eldermonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
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

    private static final String TAG = "DetailActivity";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("user");

    private String currentUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "Entering onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (mAuth.getCurrentUser() == null) {
            Log.v(TAG, "no auth");

            Intent intent = new Intent(DetailActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            Log.v(TAG, "Valid");

            currentUID = mAuth.getCurrentUser().getUid().toString();
            setSupportActionBar((android.support.v7.widget.Toolbar) findViewById(R.id.toolbar));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            WebView myWebView = (WebView) findViewById(R.id.webview);
            myWebView.loadUrl("http://192.168.1.189:8080/stream");

            String jsonItem = "";
            Bundle extras = getIntent().getExtras();

            if (extras != null) {
                jsonItem = extras.getString("item");
            }

            final ListItem itemObj = new Gson().fromJson(jsonItem, ListItem.class);

            TextView fname = findViewById(R.id.name);
            fname.setText(itemObj.fname + " " + itemObj.lname);

            myRef = myRef.child(itemObj.uid);

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Boolean help = dataSnapshot.child("help").getValue(Boolean.class);
                    if (help) {

                        RadioGroup helpResp = findViewById(R.id.help_response);
                        helpResp.setVisibility(View.VISIBLE);
                    }

                    DataSnapshot approved = dataSnapshot.child("contact").child("approved");
                    if (approved != null) {

                        String idName = approved.child(currentUID).getValue(String.class);

                        if (idName.length() > 0) {

                            int id = getResources().getIdentifier(idName, "id", getPackageName());
                            RadioButton radioButton = findViewById(id);
                            radioButton.setChecked(true);
                        }
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
                    Log.v(TAG, "Radio");

                    int id = group.getCheckedRadioButtonId();

                    switch (id) {
                        case R.id.omw:
                            Log.v(TAG, "Radio = omw");

                            myRef.child("contact").child("approved").child(currentUID).setValue("omw");
                            break;

                        case R.id.available:
                            Log.v(TAG, "Radio = available");

                            myRef.child("contact").child("approved").child(currentUID).setValue("available");
                            break;

                        case R.id.busy:
                            Log.v(TAG, "Radio = busy");

                            myRef.child("contact").child("approved").child(currentUID).setValue("busy");
                            break;

                        default:
                            Log.v(TAG, "Radio = ");

                            myRef.child("contact").child("approved").child(currentUID).setValue("");
                            break;
                    }
                }
            });

            Button goToQuestions = (Button) findViewById(R.id.viewQuestions);
            goToQuestions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent viewQuestionsIntent = new Intent(DetailActivity.this, QuestionsList.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("uid", itemObj.uid);
                    viewQuestionsIntent.putExtras(bundle);
                    startActivity(viewQuestionsIntent);
                }
            });
        }
    }
}

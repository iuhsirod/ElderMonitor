package edu.wit.mobileapp.eldermonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("user");

    List<String> respList = new ArrayList<String>();
    List<String> keyList = new ArrayList<String>();

    private String currentUID;
    private String fname;
    private String lname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "Entering onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (mAuth.getCurrentUser() == null) {
            Log.v(TAG, "No auth");

            Intent intent = new Intent(DetailActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else {
            Log.v(TAG, "Valid");

            fname = getIntent().getExtras().getString("fname");
            lname = getIntent().getExtras().getString("lname");

            TextView name = findViewById(R.id.name);
            name.setText(fname + " " + lname);

            currentUID = mAuth.getCurrentUser().getUid().toString();
            setSupportActionBar((android.support.v7.widget.Toolbar) findViewById(R.id.toolbar));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            WebView myWebView = (WebView) findViewById(R.id.webview);
            myWebView.loadUrl("http:/172.16.0.4:8080/stream");


            Bundle bundle = this.getIntent().getExtras();
            final String UID = bundle.getString("uid");

            respList.clear();
            final DatabaseReference currUserRef = myRef;

            currUserRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println("1");
                    keyList.clear();
                    respList.clear();

                    Boolean help = dataSnapshot.child(UID).child("help").getValue(Boolean.class);
                    if (help) {
                        findViewById(R.id.help_label).setVisibility(View.VISIBLE);
                        findViewById(R.id.help_response).setVisibility(View.VISIBLE);
                    }

                    DataSnapshot approved = dataSnapshot.child(UID).child("contact").child("approved");
                    Iterable<DataSnapshot> snapshotIterator = approved.getChildren();
                    Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                    while (iterator.hasNext()) {
                        System.out.println("2");
                        try {
                            DataSnapshot curr = iterator.next();
                            keyList.add(curr.getKey() + ";" + curr.getValue(String.class));
                        } catch (DatabaseException e) {
                            Log.v(TAG, "preferences=" + e);
                        }
                    }

                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<String>(DetailActivity.this, android.R.layout.simple_list_item_1);

                    for(String key: keyList) {
                        String split[] = key.split(";");

                        System.out.println("HELP" + split[0]);
                        if (split.length != 2) {
                            adapter.add(dataSnapshot.child(split[0]).child("first_name").getValue(String.class) + ": ");
                        }
                        else {
                            adapter.add(dataSnapshot.child(split[0]).child("first_name").getValue(String.class) + ": " + split[1]);
                        }
                    }

                    ListView listView = (ListView)findViewById(R.id.resp_list);
                    listView.setAdapter(adapter);

                    //Radio
                    if (approved != null) {

                        System.out.println("3");
                        String resp = approved.child(currentUID).getValue(String.class);

                        if (resp.length() > 0) {
                            int id = getResources().getIdentifier(resp, "id", getPackageName());

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

                            myRef.child(UID).child("contact").child("approved").child(currentUID).setValue("omw");
                            break;

                        case R.id.available:
                            Log.v(TAG, "Radio = available");

                            myRef.child(UID).child("contact").child("approved").child(currentUID).setValue("available");
                            break;

                        case R.id.busy:
                            Log.v(TAG, "Radio = busy");

                            myRef.child(UID).child("contact").child("approved").child(currentUID).setValue("busy");
                            break;

                        default:
                            Log.v(TAG, "Radio = ");

                            myRef.child(UID).child("contact").child("approved").child(currentUID).setValue("");
                            break;
                    }
                }
            });

            Button goToQuestions = (Button) findViewById(R.id.viewQuestions);
            goToQuestions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailActivity.this, QuestionsActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("uid", UID);
                    bundle.putString("fname", fname);
                    bundle.putString("lname", lname);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
                }
            });
        }
    }
}

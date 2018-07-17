package edu.wit.mobileapp.eldermonitor;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

public class DetailActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static final String TAG = "DetailActivity";

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
        ListItem itemObj = new Gson().fromJson(jsonItem, ListItem.class);

        TextView name = findViewById(R.id.name);
        name.setText(itemObj.name);



    }

}

package edu.wit.mobileapp.eldermonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class QuestionsActivity extends AppCompatActivity {
    private static final String TAG = "QuestionsActivity";

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("user");

    private ArrayList<String> questionsArray = new ArrayList<String>();
    private ArrayList<String> answersArray = new ArrayList<String>();
    private ArrayList<String> list = new ArrayList<String>();

    private String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "Entering onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);


        Bundle bundle = this.getIntent().getExtras();
        UID = bundle.getString("uid");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button goToQuestions = (Button)findViewById(R.id.add_questions);
        goToQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent viewQuestionsIntent = new Intent(QuestionsActivity.this, EditActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("uid", UID);
                viewQuestionsIntent.putExtras(bundle);

                startActivity(viewQuestionsIntent);
            }
        });

        DatabaseReference questionsRef = myRef.child(UID).child("questions");
        questionsRef.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Get map of users in datasnapshot

                list.clear();
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                while (iterator.hasNext()) {
                    try {
                        DataSnapshot current = iterator.next();
                        list.add(current.getKey() + ";" + current.getValue(String.class));

                    } catch (DatabaseException e) {
                        Log.v(TAG, "preferences=" + e);
                    }
                }

                Log.v(TAG, "Adapter");
                QuestionItemAdapter adapter;
                adapter = new QuestionItemAdapter(QuestionsActivity.this, 0, list);

                //Assign HomeItemAdapter to listview
                ListView listView = findViewById(R.id.questions_list);
                listView.setAdapter(adapter);

                // Go to DetailActivity
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(QuestionsActivity.this, EditActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("uid", UID);
                    bundle.putString("item", list.get(position));
                    bundle.putBoolean("isEdit", true);

                    intent.putExtras(bundle);
                    startActivity(intent);
                    }
                });

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle databaseError
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(TAG, "onOptionsItemSelected");

        Intent intent = new Intent(QuestionsActivity.this, DetailActivity.class);
        Bundle bundle = new Bundle();

        bundle.putString("uid", UID);
        intent.putExtras(bundle);

        startActivity(intent);

        return true;
    }
}

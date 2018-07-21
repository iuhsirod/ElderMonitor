package edu.wit.mobileapp.eldermonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class QuestionsList extends AppCompatActivity{
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("user");
    private ArrayList<String> questionsArray = new ArrayList<String>();
    private ArrayList<String> answersArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions_list);

        Bundle bundle = this.getIntent().getExtras();

        final String uid = bundle.getString("uid");

        myRef = database.getReference("user").child(uid).child("questions");
        myRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        getQuestions((Map<String,Object>) dataSnapshot.getValue(), uid);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

        Button goToQuestions = (Button)findViewById(R.id.add_questions);
        goToQuestions.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewQuestionsIntent = new Intent(QuestionsList.this, QuestionsEdit.class);
                Bundle bundle = new Bundle();
                bundle.putString("uid", uid);
                viewQuestionsIntent.putExtras(bundle);
                startActivity(viewQuestionsIntent);
            }
        }));
    }

    private void getQuestions(Map<String,Object> questions, final String userID) {
        for (Map.Entry<String, Object> entry : questions.entrySet()){
            //Get phone field and append to list
            questionsArray.add(entry.getKey().toString());
            answersArray.add(entry.getValue().toString());
            Log.v("questionsAnswers", entry.getKey().toString() + " answer: " + entry.getValue().toString());

            ArrayAdapter<String> adapter =
                    new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

            for(int i = 0; i<questionsArray.size(); i++){
                adapter.add(questionsArray.get(i) + "\n" + answersArray.get(i));
            }

            ListView listView = (ListView)findViewById(R.id.questions_list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent,
                                        View view,
                                        int position,
                                        long id) {
                    Log.v("test", "item " + position + " is clicked");
                    Intent viewQuestionsIntent = new Intent(QuestionsList.this, QuestionsEdit.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("uid", userID);
                    bundle.putString("question", questionsArray.get(position));
                    bundle.putBoolean("isEdit", true);
                    viewQuestionsIntent.putExtras(bundle);
                    startActivity(viewQuestionsIntent);
                }
            });
        }

    }
}

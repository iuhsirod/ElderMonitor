package edu.wit.mobileapp.eldermonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {
    private static final String TAG = "EditActivity";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("user");

    private String UID;
    private String question;
    private String answer;

    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "Entering onCreateView");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = this.getIntent().getExtras();

        UID = bundle.getString("uid");
        String item = bundle.getString("item");
        Boolean isEdit = bundle.getBoolean("isEdit");

        if(isEdit) {
            String itemValue[] = item.split(";");
            question = itemValue[0];
            answer = itemValue[1];
        }

        final EditText setQuestion = (EditText) findViewById(R.id.question_input);
        final EditText setAnswer = (EditText) findViewById(R.id.answer_input);

        Button deleteQuestion = (Button) findViewById(R.id.delete_question);
        Button editQuestion = (Button) findViewById(R.id.edit_question);

        if (isEdit) {
            setQuestion.setText(question, TextView.BufferType.EDITABLE);
            setAnswer.setText(answer, TextView.BufferType.EDITABLE);
            setQuestion.setEnabled(false);
        }
        else {
            deleteQuestion.setVisibility(View.GONE);
            editQuestion.setText("Add Question");
        }

        deleteQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myRef.child(UID).child("questions").child(question).removeValue();

                onOptionsItemSelected(null);
            }
        });

        editQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef = database.getReference("user").child(UID).child("questions");
                Map<String, Object> question = new HashMap<>();
                question.put(setQuestion.getText().toString(), setAnswer.getText().toString());
                myRef.updateChildren(question);

                onOptionsItemSelected(null);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(TAG, "onOptionsItemSelected");

        Intent intent = new Intent(EditActivity.this, QuestionsActivity.class);
        Bundle bundle = new Bundle();

        bundle.putString("uid", UID);
        intent.putExtras(bundle);

        startActivityForResult(intent, 1);

        return true;
    }
}

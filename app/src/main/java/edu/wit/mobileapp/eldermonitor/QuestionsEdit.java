package edu.wit.mobileapp.eldermonitor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class QuestionsEdit extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions_edit);

        Bundle bundle = this.getIntent().getExtras();

        final String uid = bundle.getString("uid");
        final String question = bundle.getString("question");
        final Boolean isEdit = bundle.getBoolean("isEdit");

        final EditText setQuestion = (EditText)findViewById(R.id.input_question_value);
        final EditText setAnswer = (EditText)findViewById(R.id.input_answer_value);

        Button deleteQuestion = (Button)findViewById(R.id.delete_question);
        Button editQuestion = (Button)findViewById(R.id.edit_question);

        if(isEdit){
            setQuestion.setText(question, TextView.BufferType.EDITABLE);
            setQuestion.setEnabled(false);
        } else {
            deleteQuestion.setVisibility(View.GONE);
            editQuestion.setText("Add Question");
        }

        deleteQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getReference("user").child(uid).child("questions").child(question).removeValue();
                Intent deleteQuestionIntent = new Intent(QuestionsEdit.this, QuestionsList.class);
                Bundle bundle = new Bundle();
                bundle.putString("uid", uid);
                deleteQuestionIntent.putExtras(bundle);
                startActivity(deleteQuestionIntent);
            }
        });

        editQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef = database.getReference("user").child(uid).child("questions");
                Map<String, Object> question = new HashMap<>();
                question.put(setQuestion.getText().toString(), setAnswer.getText().toString());
                myRef.updateChildren(question);

                Intent editQuestionIntent = new Intent(QuestionsEdit.this, QuestionsList.class);
                Bundle bundle = new Bundle();
                bundle.putString("uid", uid);
                editQuestionIntent.putExtras(bundle);
                startActivity(editQuestionIntent);
            }
        });
    }
}

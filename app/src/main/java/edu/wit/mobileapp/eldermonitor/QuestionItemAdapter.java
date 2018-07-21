package edu.wit.mobileapp.eldermonitor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class QuestionItemAdapter extends ArrayAdapter<String> {
    private static final String TAG = "QuestionItemAdapter";

    private LayoutInflater mInflater;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("user");

    private String currentUID = mAuth.getCurrentUser().getUid().toString();

    public QuestionItemAdapter(Context context, int rid, List<String> list) {
        super(context, rid, list);

        mInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v(TAG, "Entering getView");

        //Retrieve data
        String item = getItem(position);
        String itemValue[] = item.split(";");
        String question = itemValue[0];
        String answer = itemValue[1];

        //User layout file to generate View
        View view = mInflater.inflate(R.layout.question_list_item, null);

//        ImageButton mSendRequestBtn = (ImageButton) view.findViewById(R.id.search_add);
//        mSendRequestBtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Log.v(TAG, "Sending request");
//
//                Intent viewQuestionsIntent = new Intent(QuestionsActivity.this, EditActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("uid", userID);
//                bundle.putString("question", questionsArray.get(position));
//                bundle.putBoolean("isEdit", true);
//
//                viewQuestionsIntent.putExtras(bundle);
//                startActivity(viewQuestionsIntent);
//            }
//        });


        //Set user name
        TextView questionView;
        questionView = (TextView)view.findViewById(R.id.question);
        questionView.setText(question);

        //Set user name
        TextView answerView;
        answerView = (TextView)view.findViewById(R.id.answer);
        answerView.setText(answer);

        return view;
    }
}

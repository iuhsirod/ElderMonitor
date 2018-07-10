package edu.wit.mobileapp.eldermonitor;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SearchFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("");
    private FirebaseAuth firebaseAuth;


    private ListView listView;

    private ImageView mProfileImage;
    private TextView mProfileName, mProfileStatus, mProfileFriendsCount;

    private DatabaseReference mFriendReqDatabase;
    private DatabaseReference mFriendDatabase;
    private DatabaseReference mNotificationDatabase;

    private FirebaseUser mCurrent_user;

    private String mCurrent_state;

    private FirebaseAuth mAuth;

    private String searchUID;
    private String currentUID;


    private static final String TAG = "ManageFragment";

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater,
                @Nullable ViewGroup container,
                @Nullable Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_search, container, false);

            final String searchEmail = "yuana@wit.edu";
            mAuth = FirebaseAuth.getInstance();
            currentUID = mAuth.getCurrentUser().getUid().toString();
            myRef = FirebaseDatabase.getInstance().getReference("user");

            Query query = myRef.orderByChild("email").equalTo(searchEmail);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // There may be multiple users with the email address, so we need to loop over the matches
                    for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                        searchUID = userSnapshot.getKey();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "find email address:onCancelled", databaseError.toException());
                    // ...
                }
            });

            //approving request
            Button mSendRequestBtn = (Button) view.findViewById(R.id.send_request);
            mSendRequestBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    FirebaseDatabase.getInstance().getReference("user").child(currentUID).child("contact").child(searchUID).setValue("pending");
                    FirebaseDatabase.getInstance().getReference("user").child(searchUID).child("contact").child(currentUID).setValue("pending");
                }
            });


        return view;
    }
}


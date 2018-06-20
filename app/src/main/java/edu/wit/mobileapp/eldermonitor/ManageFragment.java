package edu.wit.mobileapp.eldermonitor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ManageFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("");
    private FirebaseAuth firebaseAuth;


    private ListView listView;

    private ImageView mProfileImage;
    private TextView mProfileName, mProfileStatus, mProfileFriendsCount;
    private Button mProfileSendReqBtn, mDeclineBtn;

    private DatabaseReference mUsersDatabase;

    private ProgressDialog mProgressDialog;

    private DatabaseReference mFriendReqDatabase;
    private DatabaseReference mFriendDatabase;
    private DatabaseReference mNotificationDatabase;

    private DatabaseReference mRootRef;

    private FirebaseUser mCurrent_user;

    private String mCurrent_state;

    private FirebaseAuth mAuth;

    private static final String TAG = "ManageFragment";

    public static ManageFragment newInstance() {
        return new ManageFragment();
    }

    @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater,
                @Nullable ViewGroup container,
                @Nullable Bundle savedInstanceState) {

            final View view = inflater.inflate(R.layout.fragment_manage, container, false);
//            final String user_id = getActivity().getIntent().getStringExtra("user_id");

            final String user_id = "DE1zHg4VJAdvE6BKoTvtftS2Zhj1";
            mRootRef = FirebaseDatabase.getInstance().getReference();

            mAuth = FirebaseAuth.getInstance();
            mUsersDatabase = FirebaseDatabase.getInstance().getReference();

            String UID = mAuth.getCurrentUser().getUid().toString();
            myRef = FirebaseDatabase.getInstance().getReference("user").child(UID);
            Query tmp = myRef.child("user").orderByChild("email").equalTo("yuana@wit.edu");



            //sending request
            myRef.child("contact").child("current user").setValue("pending");
            //approving request
            myRef.child("contact").child("DE1zHg4VJAdvE6BKoTvtftS2Zhj1").setValue("approved");
            //declining request
//            myRef.child("contact").child("current user").removeValue();


//            mFriendReqDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
//            mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
//            mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("notifications");
//            mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();



            mProfileName = (TextView) view.findViewById(R.id.request_name);
            mProfileSendReqBtn = (Button) view.findViewById(R.id.accept_request);
            mDeclineBtn = (Button) view.findViewById(R.id.decline_request);

        mAuth = FirebaseAuth.getInstance();
        listView = view.findViewById(R.id.newListView);


        myRef = FirebaseDatabase.getInstance().getReference("user").child(UID).child("contact");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<ListItem> list = new ArrayList<ListItem>();

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                while(iterator.hasNext()) {
                    try {
                        String value = iterator.next().getKey().toString();
                        DatabaseReference curr = FirebaseDatabase.getInstance().getReference("user").child(value);


                        final ListItem item = new ListItem();
                        DatabaseReference fName = curr.child("first_name");

                        fName.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String first_name = dataSnapshot.getValue(String.class);
                                //do what you want with the email
                                item.name = first_name;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        list.add(item);
                    }
                    catch (DatabaseException e) {
                        Log.v(TAG, "preferences=" + e);
                    }
                }

                //Create ListItemAdapter
                RequestItemAdapter adapter;
                adapter = new RequestItemAdapter(getActivity(), 0, list);

                //Assign ListItemAdapter to listview
                ListView listView = view.findViewById(R.id.newListView);
                listView.setAdapter(adapter);


                // Go to DetailActivity
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }
}


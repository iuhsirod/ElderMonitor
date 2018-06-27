package edu.wit.mobileapp.eldermonitor;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    private String searchUID;

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

            mAuth = FirebaseAuth.getInstance();

            //Gets current user uid
            String currentUID = mAuth.getCurrentUser().getUid().toString();
            String searchEmail = "yuana@wit.edug";

            Query query = FirebaseDatabase.getInstance().getReference("user").orderByChild("email").equalTo("yuana@wit.edu");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            searchUID = userSnapshot.getKey();
                        }
                    }
                    else {
                        System.out.println("failed to query");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "find email address:onCancelled", databaseError.toException());
                    // ...
                }
            });

            //DATABASE
            //sending request
            FirebaseDatabase.getInstance().getReference("user").child(currentUID).child("contact").child(searchUID).setValue("pending");
            FirebaseDatabase.getInstance().getReference("user").child(searchUID).child("contact").child(currentUID).setValue("pending");

            //approving request
            mProfileSendReqBtn = (Button) view.findViewById(R.id.accept_request);
            FirebaseDatabase.getInstance().getReference("user").child(currentUID).child("contact").child(searchUID).setValue("approve");
            FirebaseDatabase.getInstance().getReference("user").child(searchUID).child("contact").child(currentUID).setValue("approve");

            //declining request
            mDeclineBtn = (Button) view.findViewById(R.id.decline_request);
            FirebaseDatabase.getInstance().getReference("user").child(currentUID).child("contact").child(searchUID).removeValue();
            FirebaseDatabase.getInstance().getReference("user").child(searchUID).child("contact").child(currentUID).removeValue();


            // LISTVIEW
//            mProfileName = (TextView) view.findViewById(R.id.request_name);
//
//            mAuth = FirebaseAuth.getInstance();
//            listView = view.findViewById(R.id.newListView);
//
//            myRef = FirebaseDatabase.getInstance().getReference("user").child(UID).child("contact");
//            myRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//
//                    List<ListItem> list = new ArrayList<ListItem>();
//
//                    Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
//                    Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
//
//                    while(iterator.hasNext()) {
//                        try {
//                            String value = iterator.next().getKey().toString();
//                            DatabaseReference curr = FirebaseDatabase.getInstance().getReference("user").child(value);
//                            if (curr != null){
//                                break;
//                            }
//
//
//                                final ListItem item = new ListItem();
//                            DatabaseReference fName = curr.child("first_name");
//
//                            fName.addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    String first_name = dataSnapshot.getValue(String.class);
//                                    //do what you want with the email
//                                    item.name = first_name;
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                }
//                            });
//
//                            list.add(item);
//                        }
//                        catch (DatabaseException e) {
//                            Log.v(TAG, "preferences=" + e);
//                        }
//                    }
//
//                    //Create ListItemAdapter
//                    if (list.size() > 0) {
//                        RequestItemAdapter adapter;
//                        adapter = new RequestItemAdapter(getActivity(), 0, list);
//
//                        //Assign ListItemAdapter to listview
//                        ListView listView = view.findViewById(R.id.newListView);
//                        listView.setAdapter(adapter);
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });

        return view;
    }
}


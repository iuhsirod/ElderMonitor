package edu.wit.mobileapp.eldermonitor;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ManageFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("");

    private ListView listView;

    private ImageView mProfileImage;
    private TextView mProfileName, mProfileStatus, mProfileFriendsCount;

    private FirebaseUser mCurrent_user;

    private String mCurrent_state;

    private FirebaseAuth mAuth;

    private String searchUID;
    private String currentUID;

    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private ManagerAdapter mAdapter;
    private TabLayout mTabLayout;


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

            final String searchEmail = "yuana@wit.edu";
            mAuth = FirebaseAuth.getInstance();
            currentUID = mAuth.getCurrentUser().getUid().toString();
            myRef = FirebaseDatabase.getInstance().getReference("user");


            //approving request
            Button mApproveRequestBtn = (Button) view.findViewById(R.id.accept_request);
            mApproveRequestBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    FirebaseDatabase.getInstance().getReference("user").child(currentUID).child("contact").child(searchUID).setValue("approve");
                    FirebaseDatabase.getInstance().getReference("user").child(searchUID).child("contact").child(currentUID).setValue("approve");
                }
            });

            //declining request
            Button mDeclineRequestBtn = (Button) view.findViewById(R.id.decline_request);
            mDeclineRequestBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    FirebaseDatabase.getInstance().getReference("user").child(currentUID).child("contact").child(searchUID).removeValue();
                    FirebaseDatabase.getInstance().getReference("user").child(searchUID).child("contact").child(currentUID).removeValue();
                }
            });


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


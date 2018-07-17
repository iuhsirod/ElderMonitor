package edu.wit.mobileapp.eldermonitor;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

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


    protected static List<ListItem> list = new ArrayList<ListItem>();
    protected static Hashtable<String, ListItem> users;
    protected static List<String> keyList = new ArrayList<String>();


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


            // LISTVIEW
            mProfileName = (TextView) view.findViewById(R.id.request_name);

            String UID = mAuth.getCurrentUser().getUid().toString();


            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("user").child(UID).child("contact").child("approved");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    keyList.clear();
                    Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                    while (iterator.hasNext()) {
                        try {
                            //uid
                            DataSnapshot current = iterator.next();
                            keyList.add(current.getKey().toString());

                        } catch (DatabaseException e) {
                            Log.v(TAG, "preferences=" + e);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });


            DatabaseReference curr = FirebaseDatabase.getInstance().getReference("user");

            curr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    list.clear();
                    for (int i=0; i<keyList.size(); i++) {
                        ListItem item = new ListItem();
                        DataSnapshot current = dataSnapshot.child(keyList.get(i));
                        String uid = current.toString();
                        String first_name = current.child("first_name").getValue(String.class);

                        item.uid = keyList.get(i);
                        item.name = first_name;
                        System.out.println(i + ": " + " " + item.uid + " " +item.name);
                        list.add(item);
                    }

                    //Create ListItemAdapter
                    ManageItemAdapter adapter;
                    adapter = new ManageItemAdapter(getActivity(), 0, list);

                    //Assign ListItemAdapter to listview
                    ListView listView = view.findViewById(R.id.friend_list);
                    listView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        return view;
    }
}


package edu.wit.mobileapp.eldermonitor;

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
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
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

public class HomeFragment extends Fragment {

    public static final String TITLE = "Home";
    private static final String TAG = "Home Fragment";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    protected static List<ListItem> list = new ArrayList<ListItem>();
    protected static List<String> keyList = new ArrayList<String>();
    protected static ListItem item = new ListItem();

    public static HomeFragment newInstance() {

        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.v("tmp", "homefrag");
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        String UID = mAuth.getCurrentUser().getUid().toString();



        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("user").child(UID).child("contact").child("approved");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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

                for (int i=0; i<keyList.size(); i++) {
                    DataSnapshot current = dataSnapshot.child(keyList.get(i));
                    String uid = current.toString();
                    String first_name = current.child("first_name").getValue(String.class);

                    boolean broadcaster = current.child("broadcast").getValue(Boolean.class);
                    if (broadcaster) {
                        item.uid = keyList.get(i);
                        item.name = first_name;
                        list.add(item);
                    }
                }

                //Create ListItemAdapter
                ListItemAdapter adapter;
                adapter = new ListItemAdapter(getActivity(), 0, list);

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
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}
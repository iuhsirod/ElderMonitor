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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("user");

    private String currentUID;

    private static List<ListItem> list = new ArrayList<ListItem>();
    private static List<String> keyList = new ArrayList<String>();

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "Entering onCreateView");
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        if (mAuth.getCurrentUser() != null) {
            Log.v(TAG, "Valid user");

            currentUID = mAuth.getCurrentUser().getUid().toString();

            DatabaseReference approvedRef = myRef.child(currentUID).child("contact").child("approved");
            approvedRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.v(TAG, "Contact approved reference");

                    keyList.clear();
                    Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                    while (iterator.hasNext()) {
                        try {
                            Log.v(TAG, "Iterating through keys...");

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

            //
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.v(TAG, "User reference");

                    list.clear();
                    for (int i = 0; i < keyList.size(); i++) {

                        DataSnapshot current = dataSnapshot.child(keyList.get(i));
                        String first_name = current.child("first_name").getValue(String.class);
                        String last_name = current.child("last_name").getValue(String.class);

                        boolean broadcaster = current.child("broadcast").getValue(Boolean.class);
                        if (broadcaster) {
                            Log.v(TAG, "User is broadcaster");

                            ListItem item = new ListItem(getActivity());
                            item.uid = keyList.get(i);
                            item.fname = first_name;
                            item.lname = last_name;
                            list.add(item);
                        }
                    }

                    //Create HomeItemAdapter
                    HomeItemAdapter adapter;
                    adapter = new HomeItemAdapter(getActivity(), 0, list);

                    //Assign HomeItemAdapter to listview
                    ListView listView = view.findViewById(R.id.newListView);
                    listView.setAdapter(adapter);

                    // Go to DetailActivity
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent intent = new Intent(getActivity(), DetailActivity.class);
                            intent.putExtra("item", new Gson().toJson(list.get(position)));

                            startActivity(intent);
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        return view;
    }
}
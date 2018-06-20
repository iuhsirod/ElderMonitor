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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HomeFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("");
    private FirebaseAuth firebaseAuth;

    private ListView listView;

    private FirebaseAuth mAuth;

    private static final String TAG = "ManageFragment";

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater,
                @Nullable ViewGroup container,
                @Nullable Bundle savedInstanceState) {

            final View view = inflater.inflate(R.layout.fragment_home, container, false);

            mAuth = FirebaseAuth.getInstance();
            listView = view.findViewById(R.id.newListView);
            String UID = mAuth.getUid();


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
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            return view;
    }
}
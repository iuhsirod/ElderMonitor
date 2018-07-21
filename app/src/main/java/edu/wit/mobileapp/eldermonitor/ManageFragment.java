package edu.wit.mobileapp.eldermonitor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

public class ManageFragment extends Fragment {
    private static final String TAG = "ManageFragment";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("user");

    private TextView fname, lname;

    protected static List<ListItem> list = new ArrayList<ListItem>();
    protected static List<String> keyList = new ArrayList<String>();

    private String currentUID = mAuth.getCurrentUser().getUid().toString();

    public static ManageFragment newInstance() {
        return new ManageFragment();
    }

    @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater,
                @Nullable ViewGroup container,
                @Nullable Bundle savedInstanceState) {

             Log.v(TAG, "Entering onCreate");

            final View view = inflater.inflate(R.layout.fragment_manage, container, false);

            // LISTVIEW
            fname = (TextView) view.findViewById(R.id.first_name);
            lname = (TextView) view.findViewById(R.id.last_name);

            DatabaseReference approvedRef = myRef.child(currentUID).child("contact").child("approved");
            approvedRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.v(TAG, "Current contact approved reference");

                    keyList.clear();
                    Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                    while (iterator.hasNext()) {
                        Log.v(TAG, "Iterating through approved");
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

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.v(TAG, "User reference");

                    list.clear();
                    for (int i=0; i<keyList.size(); i++) {

                        ListItem item = new ListItem(getContext());
                        DataSnapshot current = dataSnapshot.child(keyList.get(i));
                        String fname = current.child("first_name").getValue(String.class);
                        String lname = current.child("last_name").getValue(String.class);

                        item.uid = keyList.get(i);
                        item.fname = fname;
                        item.lname = lname;
                        list.add(item);
                    }

                    //Create HomeItemAdapter
                    ManageItemAdapter adapter;
                    adapter = new ManageItemAdapter(getActivity(), 0, list);

                    //Assign HomeItemAdapter to listview
                    ListView listView = view.findViewById(R.id.contact_list);
                    listView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        return view;
    }
}


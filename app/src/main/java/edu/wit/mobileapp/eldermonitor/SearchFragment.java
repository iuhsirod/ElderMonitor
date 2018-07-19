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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class SearchFragment extends Fragment {

    private final String TAG = "SearchFragment";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("user");

    private ImageView mProfileImage;
    private TextView mProfileName;


    private String searchUID;
    private String currentUID = mAuth.getCurrentUser().getUid().toString();

    protected static List<ListItem> list = new ArrayList<ListItem>();
    protected static Hashtable<String, ListItem> users;
    protected static List<String> keyList = new ArrayList<String>();


    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "Entering onCreate");

        final View view = inflater.inflate(R.layout.fragment_search, container, false);
        list.clear();
        keyList.clear();

        final EditText emailInput = (EditText) view.findViewById(R.id.search_email_input);
        final TextView searchResults = (TextView) view.findViewById(R.id.num_results);

        //approving request
        ImageButton search = (ImageButton) view.findViewById(R.id.search_email_btn);
        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.v(TAG, "Entering search by email");

                String searchEmail = emailInput.getText().toString();

                Query query = myRef.orderByChild("email").equalTo(searchEmail);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.v(TAG, "Search by email reference");

                        keyList.clear();
                        searchResults.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                        if (dataSnapshot.getChildrenCount() == 0) {
                            Log.v(TAG, "No children found");
                        }
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            Log.v(TAG, dataSnapshot.getChildrenCount() + " children found");
                        }

                        Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                        Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                        while (iterator.hasNext()) {
                            Log.v(TAG, "Iterating and adding to keyList");
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
                        for (int i = 0; i < keyList.size(); i++) {

                            ListItem item = new ListItem(getContext());
                            DataSnapshot current = dataSnapshot.child(keyList.get(i));
                            item.uid = keyList.get(i);
                            String first_name = current.child("first_name").getValue(String.class);

                            item.fname = first_name;
                            list.add(item);
                        }

                        //Create ListItemAdapter
                        SearchItemAdapter adapter;
                        adapter = new SearchItemAdapter(getActivity(), 0, list);

                        //Assign ListItemAdapter to listview
                        ListView listView = view.findViewById(R.id.searched_list);
                        listView.setAdapter(adapter);


                        // Go to DetailActivity
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Log.v(TAG, "Redirecting to DetailedActivity");

                                Intent intent = new Intent(getActivity(), DetailActivity.class);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        return view;
    }
}



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
import android.widget.Button;
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

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("");

    private ImageView mProfileImage;
    private TextView mProfileName, mProfileStatus, mProfileFriendsCount;

    private FirebaseAuth mAuth;

    private String searchUID;
    private String currentUID;

    protected static List<ListItem> list = new ArrayList<ListItem>();
    protected static Hashtable<String, ListItem> users;
    protected static List<String> keyList = new ArrayList<String>();

    private final String TAG = "ManageFragment";

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_search, container, false);
        list.clear();
        keyList.clear();

        final EditText emailInput = (EditText) view.findViewById(R.id.search_email_input);
        final TextView searchResults = (TextView) view.findViewById(R.id.num_results);

        //search_email_input
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("user");

        currentUID = mAuth.getCurrentUser().getUid().toString();

        //approving request
        ImageButton search = (ImageButton) view.findViewById(R.id.search_email_btn);
        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String searchEmail = emailInput.getText().toString();
                Log.v(TAG, "Searching for " + searchEmail);

                Query query = myRef.orderByChild("email").equalTo(searchEmail);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // There may be multiple users with the email address, so we need to loop over the matches
                        keyList.clear();
                        searchResults.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                        if (dataSnapshot.getChildrenCount() == 0) {
                            Log.v(TAG, "there's none....");
                        }
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            searchUID = userSnapshot.getKey();
                            Log.v(TAG, "found " + searchUID);
                        }

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
                        for (int i = 0; i < keyList.size(); i++) {

                            ListItem item = new ListItem(getContext());
                            DataSnapshot current = dataSnapshot.child(keyList.get(i));
                            String uid = current.toString();
                            //TODO fix
                            item.uid = keyList.get(i);
                            String first_name = current.child("first_name").getValue(String.class);

                            item.name = first_name;
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
                                Intent intent = new Intent(getActivity(), DetailActivity.class);
                                startActivity(intent);
                            }
                        });

                    }
//                    FirebaseDatabase.getInstance().getReference("user").child(currentUID).child("contact").child(searchUID).setValue("pending");
//                    FirebaseDatabase.getInstance().getReference("user").child(searchUID).child("contact").child(currentUID).setValue("pending");

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                System.out.println("9" + list.size());

            }
        });

        return view;
    }
}



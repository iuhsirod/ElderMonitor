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
import android.widget.ListAdapter;
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

public class RequestFragment extends Fragment {
    private static final String TAG = "RequestFragment";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("user");

    private String currentUID = mAuth.getCurrentUser().getUid().toString();

    private ImageView mProfileImage;
    private TextView fname, lname;
    private TextView searchResults;

    private String searchUID;

    protected static List<ListItem> inList = new ArrayList<ListItem>();
    protected static List<String> inKeyList = new ArrayList<String>();

    protected static List<ListItem> outList = new ArrayList<ListItem>();
    protected static List<String> outKeyList = new ArrayList<String>();


    public static RequestFragment newInstance() {
        return new RequestFragment();
    }

    @Nullable
        @Override
        public View onCreateView(final LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            Log.v(TAG, "Entering onCreate");

            final View view = inflater.inflate(R.layout.fragment_request, container, false);

            //incoming
            fname = (TextView) view.findViewById(R.id.first_name);
            lname = (TextView) view.findViewById(R.id.last_name);

            DatabaseReference pendingInRef = myRef.child(currentUID).child("contact").child("pending_in");

            pendingInRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.v(TAG, "Current contact pending in");

                    inKeyList.clear();

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
                            inKeyList.add(current.getKey().toString());

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

                    inList.clear();
                    for (int i = 0; i < inKeyList.size(); i++) {

                        DataSnapshot current = dataSnapshot.child(inKeyList.get(i));

                        ListItem item = new ListItem(getContext());
                        String fname = current.child("first_name").getValue(String.class);
                        String lname = current.child("last_name").getValue(String.class);

                        item.uid = inKeyList.get(i);
                        item.fname = fname;
                        item.lname = lname;
                        inList.add(item);
                    }

                    //Create HomeItemAdapter
                    RequestItemAdapter rAdapter;
                    rAdapter = new RequestItemAdapter(getActivity(), 0, inList);

                    //Assign HomeItemAdapter to listview
                    ListView rListView = view.findViewById(R.id.incoming_list);
                    rListView.setAdapter(rAdapter);

                    rAdapter.notifyDataSetChanged();

                    ListUtils.setDynamicHeight(rListView);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            //outgoing
            DatabaseReference pendingOutRef = myRef.child(currentUID).child("contact").child("pending_out");

            pendingOutRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.v(TAG, "Current contact pending out");

                    outKeyList.clear();
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
                            outKeyList.add(current.getKey().toString());

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

                    outList.clear();
                    for (int i = 0; i < outKeyList.size(); i++) {

                        ListItem item = new ListItem(getContext());
                        DataSnapshot current = dataSnapshot.child(outKeyList.get(i));
                        String fname = current.child("first_name").getValue(String.class);
                        String lname = current.child("last_name").getValue(String.class);

                        item.uid = outKeyList.get(i);
                        item.fname = fname;
                        item.lname = lname;
                        outList.add(item);
                    }

                    //Create HomeItemAdapter
                    CancelItemAdapter cAdapter;
                    cAdapter = new CancelItemAdapter(getActivity(), 0, outList);

                    //Assign HomeItemAdapter to listview
                    ListView mListView = view.findViewById(R.id.outgoing_list);
                    mListView.setAdapter(cAdapter);

                    cAdapter.notifyDataSetChanged();

                    ListUtils.setDynamicHeight(mListView);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        return view;
    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }


}


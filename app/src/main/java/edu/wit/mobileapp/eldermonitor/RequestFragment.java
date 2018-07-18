package edu.wit.mobileapp.eldermonitor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
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
import java.util.Iterator;
import java.util.List;

public class RequestFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("");

    private ListView listView;

    private ImageView mProfileImage;
    private TextView mProfileName, mProfileStatus, mProfileFriendsCount;
    private TextView searchResults;

    private FirebaseUser mCurrent_user;

    private String mCurrent_state;

    private FirebaseAuth mAuth;

    private String searchUID;
    private String currentUID;

    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private ManagerAdapter mAdapter;
    private TabLayout mTabLayout;


    protected static List<ListItem> inList = new ArrayList<ListItem>();
    protected static List<String> inKeyList = new ArrayList<String>();

    protected static List<ListItem> outList = new ArrayList<ListItem>();
    protected static List<String> outKeyList = new ArrayList<String>();

    private static final String TAG = "ManageFragment";

    public static RequestFragment newInstance() {
        return new RequestFragment();
    }

    @Nullable
        @Override
        public View onCreateView(final LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {

            final View view = inflater.inflate(R.layout.fragment_request, container, false);

            mAuth = FirebaseAuth.getInstance();
            currentUID = mAuth.getCurrentUser().getUid().toString();
            myRef = FirebaseDatabase.getInstance().getReference("user");

            //incoming
            mProfileName = (TextView) view.findViewById(R.id.request_name);

            mAuth = FirebaseAuth.getInstance();

            DatabaseReference curr = FirebaseDatabase.getInstance().getReference("user");
            myRef = FirebaseDatabase.getInstance().getReference("user").child(currentUID).child("contact").child("pending_in");

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    inKeyList.clear();

                    // There may be multiple users with the email address, so we need to loop over the matches

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


            curr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    inList.clear();
                    for (int i = 0; i < inKeyList.size(); i++) {
                        DataSnapshot current = dataSnapshot.child(inKeyList.get(i));

                        ListItem item = new ListItem(getContext());
                        String uid = current.toString();
                        String first_name = current.child("first_name").getValue(String.class);

                        item.uid = inKeyList.get(i);
                        item.fname = first_name;
                        inList.add(item);
                    }

                    //Create ListItemAdapter
                    RequestItemAdapter rAdapter;
                    rAdapter = new RequestItemAdapter(getActivity(), 0, inList);

                    //Assign ListItemAdapter to listview
                    ListView rListView = view.findViewById(R.id.incoming_list);
                    rListView.setAdapter(rAdapter);

                    ListUtils.setDynamicHeight(rListView);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




            //outgoing
            mProfileName = (TextView) view.findViewById(R.id.request_name);

            mAuth = FirebaseAuth.getInstance();

            myRef = FirebaseDatabase.getInstance().getReference("user").child(currentUID).child("contact").child("pending_out");

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // There may be multiple users with the email address, so we need to loop over the matches
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

            curr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    outList.clear();
                    for (int i = 0; i < outKeyList.size(); i++) {
                        ListItem item = new ListItem(getContext());
                        DataSnapshot current = dataSnapshot.child(outKeyList.get(i));
                        String uid = current.toString();
                        String first_name = current.child("first_name").getValue(String.class);

                        item.uid = outKeyList.get(i);
                        item.fname = first_name;
                        outList.add(item);
                    }

                    //Create ListItemAdapter
                    CancelItemAdapter cAdapter;
                    cAdapter = new CancelItemAdapter(getActivity(), 0, outList);

                    //Assign ListItemAdapter to listview
                    ListView mListView = view.findViewById(R.id.outgoing_list);
                    mListView.setAdapter(cAdapter);

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


package edu.wit.mobileapp.eldermonitor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private RelativeLayout mDrawerRelativeLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    String[] mDrawerOptionLabels;

    //
    private ImageView mProfileImage;
    private TextView mProfileName, mProfileStatus, mProfileFriendsCount;
    private Button mProfileSendReqBtn, mDeclineBtn;

    private DatabaseReference mUsersDatabase;

    private ProgressDialog mProgressDialog;

    private DatabaseReference mFriendReqDatabase;
    private DatabaseReference mFriendDatabase;
    private DatabaseReference mNotificationDatabase;

    private DatabaseReference mRootRef;

    private FirebaseUser mCurrent_user;

    private String mCurrent_state;
//

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        mDrawerToggle.syncState();
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            Log.v(TAG, "home");
            // Handle the camera action
        }
        else if (id == R.id.nav_manage) {
            Log.v(TAG, "manage");
        }
        else if (id == R.id.nav_settings) {
            Log.v(TAG, "settings");
        }
        else if (id == R.id.nav_logout) {
            Log.v(TAG, "logout");

            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, Login.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}



//        //
//        final String user_id = getIntent().getStringExtra("user_id");
//
//        mRootRef = FirebaseDatabase.getInstance().getReference();
//
//        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
//        mFriendReqDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
//        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
//        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("notifications");
//        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();
//
////        mProfileImage = (ImageView) findViewById(R.id.profile_image);
////        mProfileName = (TextView) findViewById(R.id.profile_displayName);
////        mProfileStatus = (TextView) findViewById(R.id.profile_status);
////        mProfileFriendsCount = (TextView) findViewById(R.id.profile_totalFriends);
//        mProfileSendReqBtn = (Button) findViewById(R.id.request);
////        mDeclineBtn = (Button) findViewById(R.id.profile_decline_btn);
//
//
//        mCurrent_state = "not_friends";
//
//        mDeclineBtn.setVisibility(View.INVISIBLE);
//        mDeclineBtn.setEnabled(false);
//
//
//        mProgressDialog = new ProgressDialog(this);
//        mProgressDialog.setTitle("Loading User Data");
//        mProgressDialog.setMessage("Please wait while we load the user data.");
//        mProgressDialog.setCanceledOnTouchOutside(false);
//        mProgressDialog.show();
//
////        mUsersDatabase.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(DataSnapshot dataSnapshot) {
////
////                String display_name = dataSnapshot.child("name").getValue().toString();
////                String status = dataSnapshot.child("status").getValue().toString();
////                String image = dataSnapshot.child("image").getValue().toString();
////
////                mProfileName.setText(display_name);
////                mProfileStatus.setText(status);
////
////                if(mCurrent_user.getUid().equals(user_id)) {
////
////                    mDeclineBtn.setEnabled(false);
////                    mDeclineBtn.setVisibility(View.INVISIBLE);
////
////                    mProfileSendReqBtn.setEnabled(false);
////                    mProfileSendReqBtn.setVisibility(View.INVISIBLE);
////
////                }
////
////
////                //--------------- FRIENDS LIST / REQUEST FEATURE -----
////
////                mFriendReqDatabase.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
////                    @Override
////                    public void onDataChange(DataSnapshot dataSnapshot) {
////
////                        if(dataSnapshot.hasChild(user_id)) {
////
////                            String req_type = dataSnapshot.child(user_id).child("request_type").getValue().toString();
////
////                            if(req_type.equals("received")) {
////
////                                mCurrent_state = "req_received";
////                                mProfileSendReqBtn.setText("Accept Friend Request");
////
////                                mDeclineBtn.setVisibility(View.VISIBLE);
////                                mDeclineBtn.setEnabled(true);
////
////
////                            }
////                            else if(req_type.equals("sent")) {
////
////                                mCurrent_state = "req_sent";
////                                mProfileSendReqBtn.setText("Cancel Friend Request");
////
////                                mDeclineBtn.setVisibility(View.INVISIBLE);
////                                mDeclineBtn.setEnabled(false);
////
////                            }
////
////                            mProgressDialog.dismiss();
////
////                        }
////                        else {
////                            mFriendDatabase.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
////                                @Override
////                                public void onDataChange(DataSnapshot dataSnapshot) {
////
////                                    if(dataSnapshot.hasChild(user_id)) {
////
////                                        mCurrent_state = "friends";
////                                        mProfileSendReqBtn.setText("Unfriend this Person");
////
////                                        mDeclineBtn.setVisibility(View.INVISIBLE);
////                                        mDeclineBtn.setEnabled(false);
////
////                                    }
////
////                                    mProgressDialog.dismiss();
////                                }
////
////                                @Override
////                                public void onCancelled(DatabaseError databaseError) {
////                                    mProgressDialog.dismiss();
////
////                                }
////                            });
////                        }
////                    }
////
////                    @Override
////                    public void onCancelled(DatabaseError databaseError) {
////
////                    }
////                });
////            }
////
////            @Override
////            public void onCancelled(DatabaseError databaseError) {
////
////            }
////        });
////
////
////        mProfileSendReqBtn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////
////                mProfileSendReqBtn.setEnabled(false);
////
////                // --------------- NOT FRIENDS STATE ------------
////
////                if(mCurrent_state.equals("not_friends")) {
////
////
////                    DatabaseReference newNotificationref = mRootRef.child("notifications").child(user_id).push();
////                    String newNotificationId = newNotificationref.getKey();
////
////                    HashMap<String, String> notificationData = new HashMap<>();
////                    notificationData.put("from", mCurrent_user.getUid());
////                    notificationData.put("type", "request");
////
////                    Map requestMap = new HashMap();
////                    requestMap.put("Friend_req/" + mCurrent_user.getUid() + "/" + user_id + "/request_type", "sent");
////                    requestMap.put("Friend_req/" + user_id + "/" + mCurrent_user.getUid() + "/request_type", "received");
////                    requestMap.put("notifications/" + user_id + "/" + newNotificationId, notificationData);
////
////                    mRootRef.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
////                        @Override
////                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
////
////                            if(databaseError != null) {
////                                Toast.makeText(MainActivity.this, "There was some error in sending request", Toast.LENGTH_SHORT).show();
////                            }
////                            else {
////                                mCurrent_state = "req_sent";
////                                mProfileSendReqBtn.setText("Cancel Friend Request");
////                            }
////
////                            mProfileSendReqBtn.setEnabled(true);
////                        }
////                    });
////                }
////
////
////                // - -------------- CANCEL REQUEST STATE ------------
////
////                if(mCurrent_state.equals("req_sent")) {
////
////                    mFriendReqDatabase.child(mCurrent_user.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
////                        @Override
////                        public void onSuccess(Void aVoid) {
////
////                            mFriendReqDatabase.child(user_id).child(mCurrent_user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
////                                @Override
////                                public void onSuccess(Void aVoid) {
////
////
////                                    mProfileSendReqBtn.setEnabled(true);
////                                    mCurrent_state = "not_friends";
////                                    mProfileSendReqBtn.setText("Send Friend Request");
////
////                                    mDeclineBtn.setVisibility(View.INVISIBLE);
////                                    mDeclineBtn.setEnabled(false);
////                                }
////                            });
////                        }
////                    });
////                }
////
////
////                // ------------ REQ RECEIVED STATE ----------
////
////                if(mCurrent_state.equals("req_received")) {
////
////                    final String currentDate = DateFormat.getDateTimeInstance().format(new Date());
////
////                    Map friendsMap = new HashMap();
////                    friendsMap.put("Friends/" + mCurrent_user.getUid() + "/" + user_id + "/date", currentDate);
////                    friendsMap.put("Friends/" + user_id + "/"  + mCurrent_user.getUid() + "/date", currentDate);
////
////
////                    friendsMap.put("Friend_req/" + mCurrent_user.getUid() + "/" + user_id, null);
////                    friendsMap.put("Friend_req/" + user_id + "/" + mCurrent_user.getUid(), null);
////
////
////                    mRootRef.updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
////                        @Override
////                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
////
////
////                            if(databaseError == null) {
////
////                                mProfileSendReqBtn.setEnabled(true);
////                                mCurrent_state = "friends";
////                                mProfileSendReqBtn.setText("Unfriend this Person");
////
////                                mDeclineBtn.setVisibility(View.INVISIBLE);
////                                mDeclineBtn.setEnabled(false);
////
////                            }
////                            else {
////
////                                String error = databaseError.getMessage();
////
////                                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
////
////
////                            }
////
////                        }
////                    });
////
////                }
////
////
////                // ------------ UNFRIENDS ---------
////
////                if(mCurrent_state.equals("friends")) {
////
////                    Map unfriendMap = new HashMap();
////                    unfriendMap.put("Friends/" + mCurrent_user.getUid() + "/" + user_id, null);
////                    unfriendMap.put("Friends/" + user_id + "/" + mCurrent_user.getUid(), null);
////
////                    mRootRef.updateChildren(unfriendMap, new DatabaseReference.CompletionListener() {
////                        @Override
////                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
////
////
////                            if(databaseError == null) {
////
////                                mCurrent_state = "not_friends";
////                                mProfileSendReqBtn.setText("Send Friend Request");
////
////                                mDeclineBtn.setVisibility(View.INVISIBLE);
////                                mDeclineBtn.setEnabled(false);
////
////                            }
////                            else {
////
////                                String error = databaseError.getMessage();
////
////                                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
////
////
////                            }
////
////                            mProfileSendReqBtn.setEnabled(true);
////
////                        }
////                    });
////
////                }
////
////
////            }
////        });

//    }




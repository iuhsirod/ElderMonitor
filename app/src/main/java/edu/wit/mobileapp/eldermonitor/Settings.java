package edu.wit.mobileapp.eldermonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Settings extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("");
    private FirebaseAuth firebaseAuth;

    private EditText mFirst_input;
    private EditText mLast_input;
    private EditText mEmail_input;
    private EditText mBirth_input;
    private EditText mPhone_input;
    private EditText mPassword_input;
    private EditText mVerify_input;

    private TextView password_req;

    private Button mSubmit_button;

    private static final String TAG = "Register";

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        firebaseAuth = FirebaseAuth.getInstance();

        mFirst_input = findViewById(R.id.first_name_input);
        mLast_input = findViewById(R.id.last_name_input);
        mEmail_input = findViewById(R.id.email_input);
        mBirth_input = findViewById(R.id.birthday_input);
        mPhone_input = findViewById(R.id.phone_input);
        mPassword_input = findViewById(R.id.password_input);
        mVerify_input = findViewById(R.id.verify_password_input);

        mSubmit_button = findViewById(R.id.submit_registration);
        mSubmit_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                userRegister();
            }
        });

        mPassword_input.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                password_req.setVisibility(View.VISIBLE);

                return false;
            }
        });
    }

    /**
     *
     * @param view
     */
    public void backToLoginClicked(View view) {
        Intent intent = new Intent(Settings.this,Login.class);

        startActivity(intent);
    }

    /**
     *
     */
    private void userRegister() {
        final String first = mFirst_input.getText().toString();
        final String last = mLast_input.getText().toString();
        final String email = mEmail_input.getText().toString();
        final String birth = mBirth_input.getText().toString();
        final String phone = mPhone_input.getText().toString();
        String password = mPassword_input.getText().toString();
        String verify = mVerify_input.getText().toString();


        // Verify valid input
        if(TextUtils.isEmpty(first)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();

            return;
        } if(TextUtils.isEmpty(last)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();

            return;
        }
        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();

            return;
        }
        if(TextUtils.isEmpty(birth)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();

            return;
        }
        if(TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();

            return;
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();

            return;
        }
        if(TextUtils.isEmpty(verify)) {
            Toast.makeText(this, "Please enter password verification", Toast.LENGTH_LONG).show();

            return;
        }


        // Verify passwords match
        if(TextUtils.equals(password, verify)) {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {

                        Toast.makeText(Settings.this, "Successful Registration", Toast.LENGTH_LONG).show();

                        String user = firebaseAuth.getCurrentUser().getUid();

                        myRef = database.getReference("user").child(user);
                        myRef.child("first_name").setValue(first);
                        myRef.child("last_name").setValue(last);
                        myRef.child("email").setValue(email);
                        myRef.child("birthday").setValue(birth);
                        myRef.child("phone").setValue(phone);

                        Intent signedup = new Intent(Settings.this, Login.class);
                        startActivity(signedup);
                    }

                    if( !task.isSuccessful() ) {
                        Toast.makeText(Settings.this, "Failed Registration", Toast.LENGTH_LONG).show();

                        return;
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();

            return;
        }

    }
}

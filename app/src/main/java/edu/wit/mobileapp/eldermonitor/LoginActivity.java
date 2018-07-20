package edu.wit.mobileapp.eldermonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener authStateListener;

    private EditText mUser_Email;
    private EditText mUser_Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "Entering onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUser_Email = findViewById(R.id.user_email);
        mUser_Password = findViewById(R.id.user_password);

        authStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.v(TAG, "Authenticating");

                if (firebaseAuth.getCurrentUser()!= null) {
                    Log.v(TAG, "Valid authentication");

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(authStateListener);
    }

    public void loginButtonClicked(View view) {
        String email =  mUser_Email.getText().toString();
        String pass = mUser_Password.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
            Toast.makeText(this,"Enter both Values", Toast.LENGTH_LONG).show();
        }
        else {
            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if( !task.isSuccessful() ) {
                        Toast.makeText(LoginActivity.this,"Incorrect username and password",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }


    public void registerButtonClicked(View view) {
        Intent intent = new Intent(LoginActivity.this,Register.class);

        startActivity(intent);
    }
}

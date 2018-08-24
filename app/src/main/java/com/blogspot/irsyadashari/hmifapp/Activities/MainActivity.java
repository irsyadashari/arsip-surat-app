package com.blogspot.irsyadashari.hmifapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.blogspot.irsyadashari.hmifapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button signInBtn,signUpBtn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private DatabaseReference mDatabaseReference;

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInBtn = findViewById(R.id.signInBtn);
        signUpBtn= findViewById(R.id.signUpBtn);

        signInBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);

        //Firebase Auth Init
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
                    Toast.makeText(MainActivity.this, "Signed In", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                }else {
                    Toast.makeText(MainActivity.this, "Welcome User", Toast.LENGTH_LONG).show();
                }
            }
        };

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.signInBtn:
                //go to Sign In Activity
                Intent goToSignIn = new Intent(MainActivity.this,SignInActivity.class);
                startActivity(goToSignIn);
                break;

            case R.id.signUpBtn:
                //go to signup activity
                Intent goToSignUp = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(goToSignUp);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}

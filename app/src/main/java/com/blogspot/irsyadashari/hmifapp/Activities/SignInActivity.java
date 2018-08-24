package com.blogspot.irsyadashari.hmifapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.irsyadashari.hmifapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordUserEditText;
    private Button loginBtn;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private DatabaseReference mDatabaseReference;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordUserEditText = findViewById(R.id.passUserEditText);
        loginBtn = findViewById(R.id.loginBtn);
        mProgressDialog = new ProgressDialog(this);


        //Firebase Auth Init
        mAuth = FirebaseAuth.getInstance();

        //fungsi loginBtn ketika di klik
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 //TODO: signInWithEmailAndPassword(email, password)

                if (!TextUtils.isEmpty(usernameEditText.getText().toString())
                        && !TextUtils.isEmpty(passwordUserEditText.getText().toString())) {

                    String usernameEditTextValue = usernameEditText.getText().toString();
                    String passwordEditTextValue = passwordUserEditText.getText().toString();

                    SignIn(usernameEditTextValue,passwordEditTextValue);
                }
                else {
                    Toast.makeText(SignInActivity.this, "Signed In failed\nFill the field", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
                    Toast.makeText(SignInActivity.this, "Signed In", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                    finish();
                }else {
                    Toast.makeText(SignInActivity.this, "Welcome User", Toast.LENGTH_LONG).show();
                }
            }
        };

    }

    private void SignIn(String username, String password)
    {
        mProgressDialog.setMessage("Signing In...");
        mProgressDialog.show();

        mAuth.signInWithEmailAndPassword(username,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            //Yay!! We're in!
                            Toast.makeText(SignInActivity.this, "Signed in", Toast.LENGTH_LONG)
                                    .show();
                            mProgressDialog.dismiss();
                            startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                            finish();
                        }else {
                            // if the login is failed
                            mProgressDialog.dismiss();
                            Toast.makeText(SignInActivity.this,
                                    "Signed in failed\n username or password doesn't match",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }

                    }
                });
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

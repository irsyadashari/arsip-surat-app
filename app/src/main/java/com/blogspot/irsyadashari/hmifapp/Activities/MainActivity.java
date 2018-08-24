package com.blogspot.irsyadashari.hmifapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.blogspot.irsyadashari.hmifapp.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button signInBtn,signUpBtn;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInBtn = findViewById(R.id.signInBtn);
        signUpBtn= findViewById(R.id.signUpBtn);

        signInBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);

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
}

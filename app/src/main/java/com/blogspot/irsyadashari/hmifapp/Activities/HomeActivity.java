package com.blogspot.irsyadashari.hmifapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.blogspot.irsyadashari.hmifapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    private Button signOutBtn;
    private Button inputArsipBtn;
    private Button lihatArsipBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //firebase variable init
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //widget init
        signOutBtn = findViewById(R.id.signOutBtn);
        inputArsipBtn = findViewById(R.id.inputArsipBtn);
        lihatArsipBtn = findViewById(R.id.lihatArsipBtn);

        signOutBtn.setOnClickListener(this);
        inputArsipBtn.setOnClickListener(this);
        lihatArsipBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.signOutBtn:
                mAuth.signOut();
                startActivity(new Intent(HomeActivity.this,MainActivity.class));
                finish();
                break;

            case R.id.lihatArsipBtn:
                //GOTO lihat arsip Activity
                startActivity(new Intent(HomeActivity.this,LihatArsip.class));
                break;

            case R.id.inputArsipBtn:
                //GOTO Input Arsip Activity
                startActivity(new Intent(HomeActivity.this,InputArsip.class));
                break;
        }
    }
}
